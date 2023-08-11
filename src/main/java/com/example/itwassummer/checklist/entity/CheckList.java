package com.example.itwassummer.checklist.entity;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.check.entity.Checks;
import com.example.itwassummer.checklist.dto.CheckListRequestDto;
import com.example.itwassummer.common.entity.Timestamped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "check_list")
public class CheckList extends Timestamped {

  // 컬럼
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "card_id", nullable = false)
  private Card card;

  @OneToMany(mappedBy = "checkList", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Checks> checks = new ArrayList<>();

  //// 생성자
  @Builder
  public CheckList(CheckListRequestDto requestDto) {
    this.title = requestDto.getTitle();
  }

  // 수정
  public void update(CheckListRequestDto requestDto) {
    this.title = requestDto.getTitle();
  }
  
  // 카드 추가
  public void addCard(Card card) {
    this.card = card;
  }
}