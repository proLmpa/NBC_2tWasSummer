package com.example.itwassummer.checklist.dto;

import com.example.itwassummer.check.dto.ChecksResponseDto;
import com.example.itwassummer.checklist.entity.CheckList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckListResponseDto {

  // 제목
  private String title;

  // 체크 목록
  private List<ChecksResponseDto> checks = null;

  // 카드 이름
  private String cardName;

  // 등록일
  private String createdAt;

  // 수정일
  private String modifiedAt;

  public CheckListResponseDto(CheckList checkList){
    this.title = checkList.getTitle();
    this.checks= checkList.getChecks().stream().map(ChecksResponseDto::new).toList();
    this.cardName=  checkList.getCard().getName();
    this.createdAt= String.valueOf(checkList.getCreatedAt());
    this.modifiedAt= String.valueOf(checkList.getModifiedAt());
  }
}
