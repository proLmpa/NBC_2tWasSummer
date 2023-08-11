package com.example.itwassummer.check.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecksRequestDto {

  // 작업명
  private Long listId;

  // 작업명
  private String name;

  // 완료 체크 여부
  private boolean checked;

}
