package com.example.itwassummer.card.dto;

import com.example.itwassummer.common.file.S3FileDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 카드 상세 조회를 위한 응답 dto
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardViewResponseDto {
  // 카드 이름
  private Long cardId;

  // 카드 이름
  private String name;

  // 만기일
  private String dueDate;

  // 설명
  private String description;

  // 정렬순서
  private Long parentId;

  //첨부파일 정보 표시하는 리스트
  private List<S3FileDto> attachment = null;

}
