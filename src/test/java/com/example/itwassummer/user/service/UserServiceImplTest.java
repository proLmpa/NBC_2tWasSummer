package com.example.itwassummer.user.service;

import com.example.itwassummer.user.dto.EditUserRequestDto;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import com.example.itwassummer.user.repository.UserRepository;
import com.example.itwassummer.userpassword.dto.EditPasswordRequestDto;
import org.junit.jupiter.api.*;
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

    private User user;

    @BeforeEach
    void signUp() {
        // given
        String email = "user2024@email.com";
        String password = "user123!@#";
        boolean admin = false;
        String adminToken = "";

        encoder = new BCryptPasswordEncoder();
        SignupRequestDto request = new SignupRequestDto(email, password, admin, adminToken);

        // when
        User signed = userService.signup(request);

        // then
        Assertions.assertEquals(email, signed.getEmail());
        Assertions.assertTrue(encoder.matches(password, signed.getPassword()));
        Assertions.assertEquals(UserRoleEnum.USER, signed.getRole());
        user = signed;
    }

    @Test
    @DisplayName("사용자 정보 수정")
    void editUserInfo() {
        // given
        String nickname = "I'm Owl";
        String introduction = "This is my child owo";

        EditUserRequestDto requestDto = new EditUserRequestDto(nickname, introduction);

        // when
        User edited = userService.editUserInfo(requestDto, user);

        // then
        Assertions.assertEquals(nickname, edited.getNickname());
        Assertions.assertEquals(introduction, edited.getIntroduction());
    }

    @Test
    @DisplayName("사용자 정보 삭제")
    void deleteUserInfo() {
        // given

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
        User edited = userService.editUserPassword(correctdto, user);

        // then
        Assertions.assertFalse(encoder.matches(password, edited.getPassword()));
        Assertions.assertTrue(encoder.matches(newPassword1, edited.getPassword()));
    }
}
