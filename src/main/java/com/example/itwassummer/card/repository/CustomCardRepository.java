package com.example.itwassummer.card.repository;

import com.example.itwassummer.card.entity.Card;

public interface CustomCardRepository {

  // 정렬순서 변경
  int changeOrder(Card card, long order);
}
