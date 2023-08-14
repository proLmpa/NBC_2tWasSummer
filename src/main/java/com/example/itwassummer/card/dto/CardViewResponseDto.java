package com.example.itwassummer.card.dto;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.cardlabel.dto.CardLabelResponseDto;
import com.example.itwassummer.checklist.dto.CheckListResponseDto;
import com.example.itwassummer.comment.dto.CommentResponseDto;
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

  // 등록일
  private String createdAt;

  // 수정일
  private String modifiedAt;

  //체크리스트 표시하는 리스트
  private List<CheckListResponseDto> checkLists = null;

  //댓글 표시하는 리스트
  private List<CommentResponseDto> comments = null;

  //카드라벨 표시하는 리스트
  private List<CardLabelResponseDto> cardLabels = null;

  // 생성자
  public CardViewResponseDto(Card card) {
    this.cardId = card.getId();
    this.name = card.getName();
    this.dueDate = String.valueOf(card.getDueDate());
    this.description = card.getDescription();
    this.attachment = card.getAttachment();
    this.createdAt = String.valueOf(card.getCreatedAt());
    this.modifiedAt = String.valueOf(card.getModifiedAt());
    this.parentId = card.getParentId();
    this.checkLists = card.getCheckLists().stream().map(
        CheckListResponseDto::new
    ).toList();
    this.comments = card.getComments().stream().map(
        CommentResponseDto::new
    ).toList();
    this.cardLabels = card.getCardLabels().stream().map(
        CardLabelResponseDto::new
    ).toList();
  }
}
