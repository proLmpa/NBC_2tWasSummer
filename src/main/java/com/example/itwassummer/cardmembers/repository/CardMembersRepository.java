package com.example.itwassummer.cardmembers.repository;

import com.example.itwassummer.cardmembers.entity.CardMembers;
import com.example.itwassummer.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// 카드별 담당자
public interface CardMembersRepository extends JpaRepository<CardMembers, Long> {

  // 카드 별 데이터 삭제
  void deleteByCardsId(Long cardId);
}
