package com.example.itwassummer.card.entity;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.cardlabel.entity.CardLabel;
import com.example.itwassummer.comment.entity.Comment;
import com.example.itwassummer.common.file.S3FileDto;
import com.example.itwassummer.deck.entity.Deck;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Type;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "card")
public class Card {

  //// 컬럼
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "card_id")
  private Long id;

  @Column(length = 10, nullable = false)
  private String name;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime dueDate;

  @Column(length = 100)
  private String description;

  @Column(nullable = false)
  private Long parentId;
/*
  @Column
  private String attachment;*/

  @Convert(converter = S3FileDto.S3FileDtoConverter.class)
  @Type(JsonType.class)
  @Column(name="attachment",columnDefinition = "json")
  private List<S3FileDto> attachment;

  //// 연관관계
  @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();
 
  @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CardLabel> cardLabels = new ArrayList<>();
  

  //// 생성자
  @Builder
  public Card(CardRequestDto requestDto) {
    this.name = requestDto.getName();
    this.dueDate = requestDto.getDueDate();
    this.description = requestDto.getDescription();
    this.attachment = requestDto.getAttachment();
    this.parentId = requestDto.getParentId();
  }

  // 수정
  public void update(CardRequestDto requestDto) {
    this.name = requestDto.getName();
    this.dueDate = requestDto.getDueDate();
    this.description = requestDto.getDescription();
    this.attachment = requestDto.getAttachment();
  }

  // 마감일 수정
  public void updateDueDate(LocalDateTime dueDate) {
    this.dueDate = dueDate;
  }
}