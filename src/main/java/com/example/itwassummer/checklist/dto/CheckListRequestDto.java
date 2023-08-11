package com.example.itwassummer.checklist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckListRequestDto {

  // 제목
  @NotBlank
  private String title;

  // 카드 번호
  private Long cardId;

}
