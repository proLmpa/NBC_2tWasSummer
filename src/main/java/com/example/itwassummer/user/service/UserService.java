package com.example.itwassummer.user.service;

import com.example.itwassummer.user.dto.SignupRequestDto;

public interface UserService {
    /*
     * 회원 가입
     * @param requestDto 사용자 회원 가입을 위한 요청 정보
    */
    void signup(SignupRequestDto requestDto) throws IllegalAccessException;
}
