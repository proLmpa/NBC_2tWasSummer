package com.example.itwassummer.cardmember.dto;

import com.example.itwassummer.cardmember.entity.CardMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 카드별 맴버
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardMemberResponseDto {
  // 사용자 이메일
  private String userEmail;

  // 사용자 닉네임
  private String nickName;

  // 카드 이름
  private String cardName;

  public CardMemberResponseDto(CardMember cardMember) {
    this.userEmail = cardMember.getUser().getEmail();
    this.nickName = cardMember.getUser().getNickname();
    this.cardName = cardMember.getCard().getName();
  }
}
