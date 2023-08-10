package com.example.itwassummer.check.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecksResponseDto {

  // 작업명
  private String name;

  // 체크 여부
  private boolean checked;

  // 등록일
  private String createdAt;

  // 수정일
  private String modifiedAt;
}
