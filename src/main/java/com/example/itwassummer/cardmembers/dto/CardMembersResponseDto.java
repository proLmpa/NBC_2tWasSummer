package com.example.itwassummer.cardmembers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 카드별 맴버
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardMembersResponseDto {
  // 사용자 이메일
  private String UserEmail;

  // 사용자 닉네임
  private String Nickname;

  // 카드 이름
  private String CardName;

}
