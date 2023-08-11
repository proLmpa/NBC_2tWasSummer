package com.example.itwassummer.card.dto;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.common.file.S3FileDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {

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

  /// 등록일
  private String createdAt;

  // 수정일
  private String modifiedAt;

  // 생성자
  public CardResponseDto(Card card) {
    this.name = card.getName();
    this.dueDate = String.valueOf(card.getDueDate());
    this.description = card.getDescription();
    this.attachment = card.getAttachment();
    this.createdAt = String.valueOf(card.getCreatedAt());
    this.modifiedAt = String.valueOf(card.getModifiedAt());
    this.parentId = card.getParentId();
  }
}
