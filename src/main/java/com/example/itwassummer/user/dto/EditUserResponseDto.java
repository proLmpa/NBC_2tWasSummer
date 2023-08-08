package com.example.itwassummer.user.dto;

import lombok.Getter;

@Getter
public class EditUserResponseDto {
    String nickname;
    String introduction;

    public EditUserResponseDto(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
