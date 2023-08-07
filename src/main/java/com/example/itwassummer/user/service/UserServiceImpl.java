package com.example.itwassummer.user.service;

import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.user.dto.EditUserRequestDto;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import com.example.itwassummer.user.repository.UserRepository;
import com.example.itwassummer.userpassword.dto.EditPasswordRequestDto;
import com.example.itwassummer.userpassword.entity.UserPassword;
import com.example.itwassummer.userpassword.repository.UserPasswordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserPasswordRepository userPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserPasswordRepository userPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userPasswordRepository = userPasswordRepository;
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

        User user = userRepository.save(new User(email, password, role));
        userPasswordRepository.save(new UserPassword(password, user));
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
        User found = findUser(userId);

        if(!matchOwner(found, user) && !isAdmin(user)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
        }

        userRepository.deleteById(found.getId());
    }

    @Transactional
    @Override
    public void editUserPassword(EditPasswordRequestDto requestDto, User user) {
        User found = findUser(user.getId());

        if(passwordEncoder.matches(requestDto.getPassword(), found.getPassword())) {
            if(requestDto.getNewPassword1().equals(requestDto.getNewPassword2())) {
                List<UserPassword> userPasswords = userPasswordRepository.get3RecentPasswords(found.getId());
                for(UserPassword password : userPasswords) {
                    if(passwordEncoder.matches(requestDto.getNewPassword2(), password.getPassword())) {
                        throw new CustomException(CustomErrorCode.PASSWORD_RECENTLY_USED, null);
                    }
                }

                String newPassword = passwordEncoder.encode(requestDto.getNewPassword1());
                userPasswordRepository.save(new UserPassword(newPassword, found));
                found.editPassword(newPassword);
            } else {
                throw new CustomException(CustomErrorCode.NEW_PASSWORD_MISMATCHED, null);
            }
        } else {
            throw new CustomException(CustomErrorCode.OLD_PASSWORD_MISMATCHED, null);
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));
    }

    private boolean matchOwner(User user1, User user2) {
        return user1.getId().equals(user2.getId());
    }

    private boolean isAdmin(User user) {
        return user.getRole() == UserRoleEnum.ADMIN;
    }
}
