package com.example.itwassummer.comment.repository;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.comment.dto.CommentResponseDto;
import com.example.itwassummer.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  // 카드별 댓글 전체 조회
  Page<Comment> findAllByCard(Card card, Pageable pageable);

}
