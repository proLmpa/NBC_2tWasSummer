package com.example.itwassummer.board.repository;

import com.example.itwassummer.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByName(String name);

    void deleteAllByUser_Id(Long id);

    List<Board> findAllByUser_Id(Long id);
}
