package com.example.itwassummer.common.exception;

import com.example.itwassummer.common.error.CustomErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {
    private final CustomErrorCode errorCode;

    public CustomException(CustomErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    public CustomException(CustomErrorCode errorCode, Throwable cause) {
        super(errorCode.getErrorMessage(), cause, false, false);
        this.errorCode = errorCode;
    }
}
