package com.example.itwassummer.board.dto;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.user.entity.User;

public class BoardResponseDto {
    // 보드 이름
    private String board_name;

    // 설명
    private String description;

    // 색상 코드 문자열 형태로 저장
    private String color;

    // 보드를 만든 사용자
    private User user;

    public BoardResponseDto(Board board) {
        this.board_name = board.getBoard_name();
        this.description = board.getDescription();
        this.color = board.getColor();
        this.user = board.getUser();
    }
}
