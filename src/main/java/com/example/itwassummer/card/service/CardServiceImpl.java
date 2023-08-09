package com.example.itwassummer.card.service;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.cardmember.dto.CardMemberResponseDto;
import com.example.itwassummer.cardmember.entity.CardMember;
import com.example.itwassummer.cardmember.repository.CardMemberRepository;
import com.example.itwassummer.common.file.FileUploader;
import com.example.itwassummer.common.file.S3FileDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;

  private final FileUploader fileUploader;

  private final UserRepository userRepository;

  private final CardMemberRepository cardMemberRepository;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;


  @Override
  @Transactional
  public CardResponseDto save(CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException {

    // 파일 등록
    List<S3FileDto> fileDtoList = null;
    String attachmentList = "";
    if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
      fileDtoList = fileUploader.uploadFiles(files, "files");
      for (int i = 0; i < fileDtoList.size(); i++) {
        if (i == fileDtoList.size() - 1) {
          attachmentList += fileDtoList.get(i).getUploadFileUrl();
        } else {
          attachmentList += (fileDtoList.get(i).getUploadFileUrl() + "|");
        }
      }
      requestDto.setAttachment(attachmentList);
    }

    Card card = Card.builder()
        .requestDto(requestDto)
        .build();
    Card returnCard = cardRepository.save(card);
    CardResponseDto responseDto = CardResponseDto
        .builder()
        .name(returnCard.getName())
        .dueDate(String.valueOf(returnCard.getDueDate()))
        .description(returnCard.getDescription())
        .attachInfoList(fileDtoList)
        .parentId(card.getParentId())
        .build();
    return responseDto;
  }

  @Override
  @Transactional
  public CardResponseDto update(Long cardId, CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException {
    // 파일첨부
    List<S3FileDto> fileDtoList = null;
    String attachmentList = "";
    Card card = findCard(cardId);

    // 추후에 S3FileDto 객체를 활용해 등록시 text타입으로 데이터를넣어두고
    // db에서 S3FileDto 객체로 parse 해서 삭제기능을 구현하도록 개선 필요
    String attachUrls = card.getAttachment();
    if (attachUrls != null && !attachUrls.isEmpty()) {
      String[] attachUrlArray = attachUrls.split("\\|");
      if (attachUrlArray.length > 0) {
        for (int i = 0; i < attachUrlArray.length; i++) {
          String temp = attachUrlArray[i];
          String temp2 = temp.replace("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/",
              "");
          String[] temp3 = temp2.split("/");
          String uploadPath = "";
          String uploadFileName = "";
          if (temp3.length > 4) {
            uploadPath = temp3[0] + "/" + temp3[1] + "/" + temp3[2] + "/" + temp3[3];
            uploadFileName = temp3[4];
            fileUploader.deleteFile(uploadPath, uploadFileName);
          }
        }
      }
    }

    // 파일 등록 
    if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
      fileDtoList = fileUploader.uploadFiles(files, "files");
      for (int i = 0; i < fileDtoList.size(); i++) {
        if (i == fileDtoList.size() - 1) {
          attachmentList += fileDtoList.get(i).getUploadFileUrl();
        } else {
          attachmentList += (fileDtoList.get(i).getUploadFileUrl() + "|");
        }
      }
      requestDto.setAttachment(attachmentList);
    }

    // card 내용 수정
    card.update(requestDto);
    CardResponseDto responseDto = CardResponseDto
        .builder()
        .name(card.getName())
        .dueDate(String.valueOf(card.getDueDate()))
        .description(card.getDescription())
        .attachInfoList(fileDtoList)
        .parentId(card.getParentId())
        .build();

    return responseDto;
  }

  @Override
  @Transactional
  public void delete(Long cardId) {
    Card card = findCard(cardId);
    cardRepository.deleteById(card.getId());
  }

  public Card findCard(Long cardId) {
    return cardRepository.findById(cardId).orElseThrow(() ->
        new NullPointerException("카드가 없습니다."));
  }

  @Override
  @Transactional
  public List<CardMemberResponseDto> changeCardMembers(Long cardId, String emailList) {
    List<CardMemberResponseDto> result = null;
    // 전체삭제
    cardMemberRepository.deleteByCardId(cardId);
    Card nowCard = findCard(cardId);
    if (!emailList.isEmpty() && emailList != null) {
      List<String> emailArrays = Arrays.stream(emailList.split(",")).toList();

      for (int i = 0; i < emailArrays.size(); i++) {
        Optional<User> optionalUser = userRepository.findByEmail(emailArrays.get(i));
        if (optionalUser.isPresent()) {
          CardMember cardMember = new CardMember(nowCard, optionalUser.get());
          cardMemberRepository.save(cardMember);
        }
      }
    }
    return null;
  }

  @Override
  @Transactional
  public CardResponseDto changeDueDate(Long cardId, String dueDate) {
    Card card = findCard(cardId);
    LocalDateTime parseDueDate = LocalDateTime.parse(dueDate);

    card.updateDueDate(parseDueDate);

    CardResponseDto responseDto = CardResponseDto
        .builder()
        .name(card.getName())
        .dueDate(String.valueOf(card.getDueDate()))
        .description(card.getDescription())
        .parentId(card.getParentId())
        .build();

    return responseDto;
  }

  @Override
  @Transactional
  public CardResponseDto moveCard(Long cardId, Long order) {
    Card card = findCard(cardId);
    cardRepository.changeOrder(card, order);
    card = findCard(cardId);
    CardResponseDto responseDto = CardResponseDto
        .builder()
        .name(card.getName())
        .dueDate(String.valueOf(card.getDueDate()))
        .description(card.getDescription())
        .parentId(card.getParentId())
        .build();
    return responseDto;
  }
}
