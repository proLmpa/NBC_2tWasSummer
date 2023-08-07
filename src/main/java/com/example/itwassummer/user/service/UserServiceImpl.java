package com.example.itwassummer.user.service;

import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.user.dto.EditUserRequestDto;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import com.example.itwassummer.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.token}")
    private String adminToken;

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
        if(requestDto.isAdmin() && requestDto.getAdminToken().equals(adminToken)) {
            role = UserRoleEnum.ADMIN;
        }

        userRepository.save(new User(email, password, role));
    }

    @Transactional
    @Override
    public User editUserInfo(EditUserRequestDto requestDto, User user) {
        User found = findUserByEmail(user.getEmail());
        if(found == null)
            throw new CustomException(CustomErrorCode.USER_NOT_FOUND, null);

        found.editUserInfo(requestDto.getNickname(), requestDto.getIntroduction());
        return found;
    }

    @Override
    public void deleteUserInfo(Long userId, User user) {
        User found = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));

        if(!matchOwner(found, user) && !isAdmin(user)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
        }

        userRepository.deleteById(found.getId());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private boolean matchOwner(User user1, User user2) {
        return user1.getId().equals(user2.getId());
    }

    private boolean isAdmin(User user) {
        return user.getRole() == UserRoleEnum.ADMIN;
    }
}
