package com.example.itwassummer.common.exception;

import com.example.itwassummer.common.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ApiResponseDto> handlerCustomException(CustomException e) {
        ApiResponseDto restApiException = new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.badRequest().body(restApiException);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponseDto> handlerValicationException(MethodArgumentNotValidException ex) {
        // Validation 예외처리
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : ex.getFieldErrors()) {
            log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            errorMessage.append(fieldError.getField()).append(" 필드 : ").append(fieldError.getDefaultMessage()).append(" ");
        }

        return ResponseEntity.badRequest().body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage.toString()));
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<ApiResponseDto> nullPointerExceptionHandler(NullPointerException ex) {
        ApiResponseDto restApiException = new ApiResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
         return ResponseEntity.badRequest().body(restApiException);
    }
}
