package com.example.itwassummer.cardmember.repository;

import com.example.itwassummer.cardmember.entity.CardMember;
import org.springframework.data.jpa.repository.JpaRepository;

// 카드별 담당자
public interface CardMemberRepository extends JpaRepository<CardMember, Long> {

  // 카드 별 데이터 삭제
  void deleteByCardId(Long cardId);
}
