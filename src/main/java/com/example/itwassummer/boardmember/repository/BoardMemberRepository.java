package com.example.itwassummer.boardmember.repository;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.boardmember.entity.BoardMember;
import com.example.itwassummer.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
    Optional<BoardMember> findByBoardAndUser(Board board, User invitee);
}
