package com.example.itwassummer.user.service;

import com.example.itwassummer.user.dto.EditUserRequestDto;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import com.example.itwassummer.user.repository.UserRepository;
import com.example.itwassummer.userpassword.dto.EditPasswordRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Test
    User signUp() {
        // given
        String email = "user2024@email.com";
        String password = "user123!@#";
        boolean admin = false;
        String adminToken = "";

        encoder = new BCryptPasswordEncoder();
        SignupRequestDto request = new SignupRequestDto(email, password, admin, adminToken);

        // when
        User user = userService.signup(request);

        // then
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertTrue(encoder.matches(password, user.getPassword()));
        Assertions.assertEquals(UserRoleEnum.USER, user.getRole());

        return user;
    }

    @Test
    @DisplayName("사용자 정보 수정")
    void editUserInfo() {
        // given
        String nickname = "I'm Owl";
        String introduction = "This is my child owo";

        EditUserRequestDto requestDto = new EditUserRequestDto(nickname, introduction);

        // when
        User edited = userService.editUserInfo(requestDto, signUp());

        // then
        Assertions.assertEquals(nickname, edited.getNickname());
        Assertions.assertEquals(introduction, edited.getIntroduction());
    }

    @Test
    @DisplayName("사용자 정보 삭제")
    void deleteUserInfo() {
        // given
        User user = signUp();

        // when
        userService.deleteUserInfo(user.getId(), user);

        // then
        Assertions.assertNull(userRepository.findById(user.getId()).orElse(null));
    }

    @Test
    @DisplayName("사용자 비밀번호 수정")
    void editUserPassword() {
        // given
        String password = "user123!@#";
        String newPassword1 = "user234@#$";
        String newPassword2 = "user234@#$";

        EditPasswordRequestDto correctdto = new EditPasswordRequestDto(password, newPassword1, newPassword2);

        // when - 정상 비밀번호 수정
        User user = userService.editUserPassword(correctdto, signUp());

        // then
        Assertions.assertFalse(encoder.matches(password, user.getPassword()));
        Assertions.assertTrue(encoder.matches(newPassword1, user.getPassword()));
    }
}
