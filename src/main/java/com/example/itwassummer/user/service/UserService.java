package com.example.itwassummer.user.service;

import com.example.itwassummer.user.dto.EditUserRequestDto;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.userpassword.dto.EditPasswordRequestDto;

public interface UserService {
    /**
     * 회원 가입
     * @param requestDto 사용자 회원 가입을 위한 요청 정보
     * @return user 생성된 사용자 정보 반환
     */
    User signup(SignupRequestDto requestDto) throws IllegalAccessException;

    /**
     * 유저 정보 수정
     * @param requestDto 사용자 정보 수정을 위한 요청 정보
     * @param user 수정할 정보를 소유하고 있는 사용자 본인에 대한 정보
     * @return user 수정된 사용자 정보 반환
     */
    User editUserInfo(EditUserRequestDto requestDto, User user);

    /**
     * 유저 정보 삭제
     * @param userId 삭제할 사용자의 식별 ID
     * @param user 삭제할 정보를 소유하고 있는 사용자 본인 혹은 관리자에 대한 정보
     */
    void deleteUserInfo(Long userId, User user);

    /**
     * 유저 비밀번호 수정
     * @param requestDto 사용자 비밀번호 수정을 위한 요청 정보
     * @param user 수정할 정보를 소유하고 있는 사용자 본인에 대한 정보
     */
    void editUserPassword(EditPasswordRequestDto requestDto, User user);
}
