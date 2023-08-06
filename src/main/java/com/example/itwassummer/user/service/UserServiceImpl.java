package com.example.itwassummer.user.service;

import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import com.example.itwassummer.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String ADMINTOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    @Override
    public void signup(SignupRequestDto requestDto){
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 사용자 존재 여부 확인
        if(findUserByEmail(email) != null) {
            throw new CustomException(CustomErrorCode.USER_ALREADY_EXISTS, null);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isAdmin() && requestDto.getAdminToken().equals(ADMINTOKEN)) {
            role = UserRoleEnum.ADMIN;
        }

        userRepository.save(new User(email, password, role));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
