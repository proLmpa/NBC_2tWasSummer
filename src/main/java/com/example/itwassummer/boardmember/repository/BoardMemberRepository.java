package com.example.itwassummer.boardmember.repository;

import com.example.itwassummer.boardmember.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
}
