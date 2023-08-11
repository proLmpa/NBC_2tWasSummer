package com.example.itwassummer.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 사용자입니다."),
    CARD_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 카드입니다."),
    CHECK_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 체크항목입니다."),
    CHECK_LIST_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 체크리스트입니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 코멘트입니다."),
    LABEL_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 라벨입니다."),
    LABEL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "이미 동명의 라벨이 존재합니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 사용자입니다."),
    BOARD_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 보드입니다."),
    BOARD_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 보드입니다."),
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED.value(), "승인되지 않은 요청입니다."),
    OLD_PASSWORD_MISMATCHED(HttpStatus.BAD_REQUEST.value(), "기존 비밀번호와 일치하지 않습니다."),
    NEW_PASSWORD_MISMATCHED(HttpStatus.BAD_REQUEST.value(), "새 비밀번호가 일치하지 않습니다."),
    PASSWORD_RECENTLY_USED(HttpStatus.BAD_REQUEST.value(), "기존에 사용된 적 있는 비밀번호입니다."),
    BOARD_MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 작업자입니다."),
    BOARD_MEMBER_NOT_EXISTS(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 작업자입니다."),
    ALREADY_DELETED_DECK(HttpStatus.BAD_REQUEST.value(), "이미 삭제된 덱입니다."),
    DECK_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 덱입니다."),
    DELETED_DECK_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "삭제된 덱이 없습니다.");

    private final int errorCode;
    private final String errorMessage;
}
