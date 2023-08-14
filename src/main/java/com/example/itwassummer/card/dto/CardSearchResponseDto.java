package com.example.itwassummer.card.dto;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.cardlabel.dto.CardLabelResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class CardSearchResponseDto {
  // 카드 이름
  private String name;

  // 정렬순서
  private Long parentId;

  // 덱 이름
  private String deckName;

  // 정보 표시하는 리스트
  private List<CardLabelResponseDto> cardLabels = null;

  // 생성자
  public CardSearchResponseDto(Card card) {
    this.name = card.getName();
    this.parentId = card.getParentId();
    this.deckName = card.getDeck().getName();
    this.cardLabels = card.getCardLabels().stream().map(
        CardLabelResponseDto::new
    ).toList();
  }
}
