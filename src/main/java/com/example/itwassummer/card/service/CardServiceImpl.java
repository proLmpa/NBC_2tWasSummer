package com.example.itwassummer.card.service;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.dto.CardViewResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.cardmember.dto.CardMemberResponseDto;
import com.example.itwassummer.cardmember.entity.CardMember;
import com.example.itwassummer.cardmember.repository.CardMemberRepository;
import com.example.itwassummer.checklist.service.CheckListService;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.common.file.FileUploader;
import com.example.itwassummer.common.file.S3FileDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

  private final CheckListService checkListService;

  @Override
  @Transactional(readOnly = true)
  public CardViewResponseDto getCard(Long cardId) {
    Card card = this.findCard(cardId);
    CardViewResponseDto responseDto = new CardViewResponseDto(card);
    return responseDto;
  }

  @Override
  @Transactional
  public CardResponseDto save(CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException {

    // 파일 등록
    List<S3FileDto> fileDtoList = null;
    if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
      fileDtoList = fileUploader.uploadFiles(files, "files");
      requestDto.setAttachment(fileDtoList);
    }

    Card card = Card.builder()
        .requestDto(requestDto)
        .build();
    Card returnCard = cardRepository.save(card);
    CardResponseDto responseDto = new CardResponseDto(returnCard);
    return responseDto;
  }

  @Override
  @Transactional
  public CardResponseDto update(Long cardId, CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException {
    // 파일첨부
    List<S3FileDto> fileDtoList = null;
    Card card = findCard(cardId);

    // 파일정보 불러오기
    List<S3FileDto> attachment = card.getAttachment();

    // 기존 파일 삭제
    if (attachment != null && attachment.size() > 0) {
      for (int i = 0; i < attachment.size(); i++) {
        fileUploader.deleteFile(attachment.get(i).getUploadFilePath(),
            attachment.get(i).getUploadFileName());
      }
    }

    // 파일 등록 
    if (!(files == null || (files.size() == 1 && files.get(0).isEmpty()))) {
      fileDtoList = fileUploader.uploadFiles(files, "files");
      requestDto.setAttachment(fileDtoList);
    }

    // card 내용 수정
    card.update(requestDto);
    CardResponseDto responseDto = new CardResponseDto(card);

    return responseDto;
  }

  @Override
  @Transactional
  public void delete(Long cardId) {
    Card card = findCard(cardId);

    // 파일정보 불러오기
    List<S3FileDto> attachment = card.getAttachment();

    // 기존 파일 삭제
    if (attachment != null && attachment.size() > 0) {
      for (int i = 0; i < attachment.size(); i++) {
        fileUploader.deleteFile(attachment.get(i).getUploadFilePath(),
            attachment.get(i).getUploadFileName());
      }
    }
    //기존 체크리스트 삭제
    for (int i = 0; i < card.getCheckLists().size(); i++) {
      checkListService.delete(card.getCheckLists().get(i).getId());
    }

    cardRepository.deleteById(card.getId());
  }

  public Card findCard(Long cardId) {
    return cardRepository.findById(cardId).orElseThrow(() ->
        new CustomException(CustomErrorCode.CARD_NOT_FOUND, null));
  }

  @Override
  @Transactional
  public List<CardMemberResponseDto> changeCardMembers(Long cardId, String emailList) {
    List<CardMemberResponseDto> result = new ArrayList<>();
    // 전체삭제
    // cardMemberRepository.deleteByCardId(cardId);
    Card nowCard = findCard(cardId);
    nowCard.getCardMembers().clear();
    if (!emailList.isEmpty() && emailList != null) {
      List<String> emailArrays = Arrays.stream(emailList.split(",")).toList();

      for (int i = 0; i < emailArrays.size(); i++) {
        Optional<User> optionalUser = userRepository.findByEmail(emailArrays.get(i));
        if (optionalUser.isPresent()) {
          CardMember cardMember = new CardMember(nowCard, optionalUser.get());
          CardMember returnCardMember = cardMemberRepository.save(cardMember);
          result.add(new CardMemberResponseDto(returnCardMember));
        }
      }
    }
    return result;
  }

  @Override
  @Transactional
  public CardResponseDto changeDueDate(Long cardId, String dueDate) {
    Card card = findCard(cardId);
    LocalDateTime parseDueDate = LocalDateTime.parse(dueDate);
    card.updateDueDate(parseDueDate);
    CardResponseDto responseDto = new CardResponseDto(card);
    return responseDto;
  }

  @Override
  @Transactional
  public CardResponseDto moveCard(Long cardId, Long order) {
    Card card = findCard(cardId);
    cardRepository.changeOrder(card, order);
    card = findCard(cardId);
    card.updateParentId(order);
    CardResponseDto responseDto = new CardResponseDto(card);
    return responseDto;
  }
}
