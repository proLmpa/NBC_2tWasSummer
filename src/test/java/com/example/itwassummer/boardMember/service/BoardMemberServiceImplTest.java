package com.example.itwassummer.boardMember.service;

import com.example.itwassummer.boardmember.service.BoardMemberServiceImpl;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.repository.UserRepository;
import com.example.itwassummer.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardMemberServiceImplTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardMemberServiceImpl boardMemberService;

    private static final String BASE_URL = "/api";

    @BeforeEach
    void registerInviterAndInvitee() {
        // given
        String inviterEmail = "user2023@email.com";
        String inviterPassword = "user123!@#";
        boolean admin = false;
        String adminToken = "";

        String inviteeEmail = "invitee2023@email.com";
        String inviteePassword = "invitee123!@#";

        // when
        SignupRequestDto inviterRequestDto =
                SignupRequestDto.builder()
                        .email(inviterEmail).password(inviterPassword)
                        .admin(admin).adminToken(adminToken)
                        .build();

        SignupRequestDto inviteeRequestDto =
                SignupRequestDto.builder()
                        .email(inviteeEmail).password(inviteePassword)
                        .admin(admin).adminToken(adminToken)
                        .build();

        // then
        userService.signup(inviterRequestDto);
        userService.signup(inviteeRequestDto);
    }

    @BeforeEach
    void registerBoard() {
        // given

        // when

        // then

    }

    User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Test
    @DisplayName("보드에 사용자 추가")
    void inviteBoardMember() {
        // given
        String inviterEmail = "user2023@email.com";
        String inviteeEmail = "invitee2023@email.com";

        User inviter = findUserByEmail("invitee2023@email.com");
        User invitee = findUserByEmail("user2023@email.com");

    }


    @Test
    @DisplayName("보드에서 사용자 제거")
    void delteBoardMember() {
        // given
        String inviterEmail = "user2023@email.com";
        String inviteeEmail = "invitee2023@email.com";

        User inviter = findUserByEmail("invitee2023@email.com");
        User invitee = findUserByEmail("user2023@email.com");

    }
}
