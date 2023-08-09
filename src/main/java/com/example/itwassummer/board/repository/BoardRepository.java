package com.example.itwassummer.board.repository;

import com.example.itwassummer.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
