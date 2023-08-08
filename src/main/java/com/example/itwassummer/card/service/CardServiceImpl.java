package com.example.itwassummer.card.service;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{

  private final CardRepository cardRepository;

  @Override
  @Transactional
  public CardResponseDto save(CardRequestDto requestDto) throws IllegalAccessException {
    Card card = Card.builder()
        .name(requestDto.getName())
        .description(requestDto.getDescription())
        .dueDate(requestDto.getDueDate())
        .parentId(requestDto.getParentId())
        .build();
    Card returnCard = cardRepository.save(card);
    CardResponseDto responseDto = CardResponseDto
        .builder()
        .name(returnCard.getName())
        .dueDate(String.valueOf(returnCard.getDueDate()))
        .description(returnCard.getDescription())
        .build();
    return responseDto;
  }

  @Override
  @Transactional
  public CardResponseDto update(Long cardId, CardRequestDto requestDto) {
    Card card = findCard(cardId);
    // card 내용 수정
    card.update(requestDto);
    CardResponseDto responseDto = CardResponseDto
        .builder()
        .name(card.getName())
        .dueDate(String.valueOf(card.getDueDate()))
        .description(card.getDescription())
        .build();

    return responseDto;
  }

  @Override
  @Transactional
  public void delete(Long cardId) {
    Card card = findCard(cardId);
    cardRepository.delete(card);
  }

  // 해당 게시글이 DB에 존재하는지 확인
  // 공통 에러메시지는 추후에 커밋된 내용 받아서 수정 예정
  public Card findCard(Long cardId) {
    return cardRepository.findById(cardId).orElseThrow(() ->
        new RuntimeException("게시글이없습니다.", null));
  }
}
