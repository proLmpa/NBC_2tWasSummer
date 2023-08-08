package com.example.itwassummer.card.service;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.cardmembers.dto.CardMembersResponseDto;
import com.example.itwassummer.cardmembers.entity.CardMembers;
import com.example.itwassummer.cardmembers.repository.CardMembersRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;

  private final FileUploader fileUploader;

  private final UserRepository userRepository;

  private final CardMembersRepository cardMembersRepository;

  /**
   * 카드 수정
   *
   * @param requestDto 카드 등록 요청 정보3
   * @param files      첨부파일 정보
   * @return
   */
  @Override
  @Transactional
  public CardResponseDto save(CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException {

    // 파일첨부
    List<S3FileDto> fileDtoList = null;
    String attachmentList = "";
    if (files != null) {
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

  /**
   * 카드 수정
   *
   * @param cardId     카드 id
   * @param requestDto 카드 등록 요청 정보
   * @param files      첨부파일 정보
   * @return
   */
  @Override
  @Transactional
  public CardResponseDto update(Long cardId, CardRequestDto requestDto, List<MultipartFile> files)
      throws IOException {
    // 파일첨부
    List<S3FileDto> fileDtoList = null;
    String attachmentList = "";
    if (files != null) {
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

    Card card = findCard(cardId);
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

  /**
   * 카드 삭제
   *
   * @param cardId 카드 id
   * @return
   */
  @Override
  @Transactional
  public void delete(Long cardId) {
    Card card = findCard(cardId);

    cardRepository.deleteById(card.getId());

  }

  // 공통 에러메시지는 추후에 커밋된 내용 받아서 수정 예정

  /**
   * 해당 카드가 DB에 존재하는지 확인
   *
   * @param cardId 카드 id
   * @return
   */
  public Card findCard(Long cardId) {
    return cardRepository.findById(cardId).orElseThrow(() ->
        new NullPointerException("카드가 없습니다."));
  }

  /**
   * 카드별 사용자 변경
   *
   * @param cardId 카드 id
   * @param emailList 사용자이메일목록
   * @return
   */
  @Override
  @Transactional
  public List<CardMembersResponseDto> changeCardMembers(Long cardId, String emailList) {
    List<CardMembersResponseDto> result = null;
    // 전체삭제
    cardMembersRepository.deleteByCardsId(cardId);
    Card nowCard = findCard(cardId);
    if (!emailList.isEmpty() && emailList != null) {
      List<String> emailArrays = Arrays.stream(emailList.split(",")).toList();

      for (int i = 0; i < emailArrays.size(); i++) {
        Optional<User> optionalUser = userRepository.findByEmail(emailArrays.get(i));
        if (optionalUser.isPresent()) {
          CardMembers cardMembers = new CardMembers(nowCard, optionalUser.get());
          cardMembersRepository.save(cardMembers);
        }
      }
    }
    return null;


  }

  /**
   * 카드별 마감일 변경
   *
   * @param cardId 카드 id
   * @param dueDate 마감일
   * @return
   */
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

  /**
   * 카드별 정렬순서 변경
   *
   * @param cardId 카드 id
   * @param order 정렬순서
   * @return
   */
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
