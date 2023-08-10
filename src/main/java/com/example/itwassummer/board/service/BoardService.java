package com.example.itwassummer.board.service;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.board.dto.BoardResponseDto;
import com.example.itwassummer.user.entity.User;

import java.util.List;

public interface BoardService {
    /**
     * 보드 단일 조회
     * @param boardId 조회할 보드 ID
     * @return 조회한 보드 정보
     */
    BoardResponseDto showBoard(Long boardId);

    /**
     * 전체 보드 조회
     * @param page 조회할 페이지 순번
     * @param size 조회할 페이지에 담길 보드 크기
     * @param sortBy 정렬 기준
     * @param isAsc 오름차순/내림차순 정렬 여부
     * @return 조회한 전체 보드 정보
     */
    List<BoardResponseDto> showBoards(int page, int size, String sortBy, boolean isAsc);

    /**
     * 보드 생성
     * @param requestDto 생성할 보드에 담길 내용 정보
     * @param user 보드 생성하는 사용자 정보
     * @return 생성된 보드 정보
     */
    BoardResponseDto createBoard(BoardRequestDto requestDto, User user);

    /**
     * 보드 내용 수정
     * @param id 수정할 보드 ID
     * @param requestDto 수정할 보드에 담길 내용 정보
     * @param user 보드 수정하는 사용자 정보
     * @return 수정된 보드 정보
     */
    BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user);

    /**
     * 보드 삭제
     * @param id 삭제할 보드 ID
     * @param user 보드를 삭제할 사용자 정보
     */
    void deleteBoard(Long id, User user);
}
