package com.example.itwassummer.board.service;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.board.dto.BoardResponseDto;
import com.example.itwassummer.user.entity.User;

import java.util.List;

public interface BoardService {

    BoardResponseDto showBoards(Long boardId);

    void createBoards(BoardRequestDto requestDto, User user);

    BoardResponseDto update(Long id, BoardRequestDto requestDto, User user);

    void delete(Long id, User user);

    List<BoardResponseDto> showAllBoards();
}
