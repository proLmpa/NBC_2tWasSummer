package com.example.itwassummer.check.entity;

import com.example.itwassummer.check.dto.ChecksRequestDto;
import com.example.itwassummer.checklist.entity.CheckList;
import com.example.itwassummer.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "checks") // 예약어라서 check 라는 이름으로 테이블을 생성하면 안됨
public class Checks extends Timestamped {

  // 컬럼
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100, nullable = false)
  private String name;

  @Column(nullable = false)
  private boolean checked;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "check_list_id", nullable = false)
  private CheckList checkList;

  // 생성자 초기에는 체크박스에 체크하지 않은상태로 생성
  @Builder
  public Checks(ChecksRequestDto requestDto) {
    this.name = requestDto.getName();
  }

  // 수정
  public void update(ChecksRequestDto requestDto) {
    this.name = requestDto.getName();
  }

  // 체크 이름 수정
  public void updateName(String name) {
    this.name = name;
  }

  // 체크여부 수정
  public void updateCheck(boolean checked) {
    this.checked = checked;
  }

  // 체크리스트 추가
  public void addCheckList(CheckList checkList) {
    this.checkList = checkList;
  }
}