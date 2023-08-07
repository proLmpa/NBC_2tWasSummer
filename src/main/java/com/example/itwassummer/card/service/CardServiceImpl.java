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
  public CardResponseDto update(CardRequestDto requestDto) {
    return null;
  }

  @Override
  public void delete(Long id) {

  }
}
