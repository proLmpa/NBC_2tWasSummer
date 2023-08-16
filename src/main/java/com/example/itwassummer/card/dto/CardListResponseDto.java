package com.example.itwassummer.card.dto;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.common.file.S3FileDto;
import java.util.List;
import lombok.Getter;

@Getter
public class CardListResponseDto {
  // 카드 이름
  private String name;

  // 정렬순서
  private Long parentId;

  // 덱 이름
  private String deckName;

  // 생성자
  public CardListResponseDto(Card card) {
    this.name = card.getName();
    this.parentId = card.getParentId();
    this.deckName = card.getDeck().getName();
  }
}
