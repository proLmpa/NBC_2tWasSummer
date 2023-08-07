package com.example.itwassummer.card.service;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
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
  CardResponseDto update(CardRequestDto requestDto);

  /**
   * 카드 삭제
   * @param id 카드 등록 요청 정보
   * @return
   */
  void delete(Long id);
}
