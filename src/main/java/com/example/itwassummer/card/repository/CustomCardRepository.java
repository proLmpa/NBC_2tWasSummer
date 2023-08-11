package com.example.itwassummer.card.repository;

import com.example.itwassummer.card.dto.CardListResponseDto;
import com.example.itwassummer.card.dto.CardSearchResponseDto;
import com.example.itwassummer.card.entity.Card;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CustomCardRepository {

  // 정렬순서 변경
  int changeOrder(Card card, long order);

  // 보드 id 기준으로 카드목록 표시
  List<CardListResponseDto> findAllByBoardId(Long boardId, Pageable pageable);

  // 라벨 id 기준으로 카드목록 표시
  List<CardSearchResponseDto> findAllByLabelId(Long labelId, Pageable pageable);
}
