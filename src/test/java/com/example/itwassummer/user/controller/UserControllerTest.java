package com.example.itwassummer.user.controller;

import com.example.itwassummer.user.dto.LoginRequestDto;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/api/users";

    @Test
    @DisplayName("회원 가입 테스트")
    void signup() throws Exception {
        // given
        String email = "user@email.com";
        String password = "user123!@#";
        boolean admin = false;
        String adminToken = "";

        // when
        String body = mapper.writeValueAsString(
                SignupRequestDto.builder()
                        .email(email).password(password)
                        .admin(admin).adminToken(adminToken)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("중복 가입 시도 테스트")
    void signup_duplicate() throws Exception {
        // given
        String email = "user@email.com";
        String password = "user123!@#";
        boolean admin = false;
        String adminToken = "";

        // when
        String body = mapper.writeValueAsString(
                SignupRequestDto.builder()
                        .email(email).password(password)
                        .admin(admin).adminToken(adminToken)
                        .build()
        );

        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        String email = "user@email.com";
        String password = "user123!@#";

        // when
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail(email);
        requestDto.setPassword(password);

        String body = mapper.writeValueAsString(requestDto);

        // then
        MvcResult result = mvc.perform(post(BASE_URL + "/login")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Assertions.assertEquals("Bearer", Objects.requireNonNull(result.getResponse().getHeader("Authorization")).substring(0, 6));
    }
}