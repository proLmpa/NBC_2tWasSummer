package com.example.itwassummer.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 사용자입니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 사용자입니다."),
    BOARD_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 보드입니다."),
    BOARD_NAME_ISNULL(HttpStatus.BAD_REQUEST.value(),"보드 이름은 비워둘 수 없습니다.");

    private final int errorCode;
    private final String errorMessage;
}
