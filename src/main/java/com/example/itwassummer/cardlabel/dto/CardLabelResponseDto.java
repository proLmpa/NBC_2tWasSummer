package com.example.itwassummer.cardlabel.dto;

import com.example.itwassummer.cardlabel.entity.CardLabel;
import com.example.itwassummer.label.entity.Label;
import lombok.Getter;

// 카드별 라벨
@Getter
public class CardLabelResponseDto {

  private String labelColor;

  private String labelTitle;

  public CardLabelResponseDto(CardLabel label) {
    this.labelTitle = label.getLabel().getTitle();
    this.labelColor = label.getLabel().getColor();
  }
}
