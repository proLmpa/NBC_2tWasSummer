package com.example.itwassummer.board.dto;

import com.example.itwassummer.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDto {
    // 보드 이름
    @NotBlank
    private String board_name;

    // 설명
    private String description;

    // 색상 코드 문자열 형태로 저장
    private String color; // default 지정?
}