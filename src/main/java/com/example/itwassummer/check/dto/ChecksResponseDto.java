package com.example.itwassummer.check.dto;

import com.example.itwassummer.check.entity.Checks;
import com.example.itwassummer.checklist.entity.CheckList;
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

  public ChecksResponseDto(Checks checks){
    this.name = checks.getName();
    this.checked= checks.isChecked();
    this.createdAt= String.valueOf(checks.getCreatedAt());
    this.modifiedAt= String.valueOf(checks.getModifiedAt());
  }

}
