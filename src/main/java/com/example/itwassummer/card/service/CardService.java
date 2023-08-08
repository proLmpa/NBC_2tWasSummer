package com.example.itwassummer.card.service;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.repository.UserRepository;

public interface CardService {

  /**
   * 카드 등록
   * @param requestDto 카드 등록 요청 정보
   * @return
   */
  CardResponseDto save(CardRequestDto requestDto) throws IllegalAccessException;


  /**
   * 카드 수정
   * @param requestDto 카드 등록 요청 정보
   * @return
   */
  CardResponseDto update(Long cardId, CardRequestDto requestDto);

  /**
   * 카드 삭제
   * @param cardId 카드 등록 요청 정보
   * @return
   */
  void delete(Long cardId);


  /**
   * 카드 조회
   * @param cardId 카드 데이터가 있는지 조회
   * @return
   */
  Card findCard(Long cardId);
}
