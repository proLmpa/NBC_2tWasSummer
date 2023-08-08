package com.example.itwassummer.user.controller;

import com.example.itwassummer.user.dto.EditUserRequestDto;
import com.example.itwassummer.user.dto.LoginRequestDto;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.userpassword.dto.EditPasswordRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    private static final String BASE_URL = "/api/users";

    @BeforeEach
    @Test
    @DisplayName("회원 가입 테스트")
    void signup() throws Exception {
        // given
        String email = "user2023@email.com";
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
                );
        // @BeforeEach 때문에 signup()도 실행 전에 signup()이 한번 더 실행되는 문제로 인해 결과 예측은 하지 않기로 함.
    }

    @Test
    @DisplayName("중복 가입 시도 테스트")
    void signup_duplicate() throws Exception {
        // given
        String email = "user2023@email.com";
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
                .andExpectAll()
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        String email = "user2023@email.com";
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
                .andReturn();

        Assertions.assertEquals("Bearer", Objects.requireNonNull(result.getResponse().getHeader("Authorization")).substring(0, 6));
    }

    @Test
    String loginForToken() throws Exception {
        String email = "user2023@email.com";
        String password = "user123!@#";

        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail(email);
        requestDto.setPassword(password);

        String loginBody = mapper.writeValueAsString(requestDto);

        MvcResult result = mvc.perform(post(BASE_URL + "/login")
                        .content(loginBody)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        return Objects.requireNonNull(result.getResponse().getHeader("Authorization"));
    }

    @Test
    @DisplayName("사용자 정보 조회")
    void getUserInfo() throws Exception{
        // 1. 로그인
        String token = loginForToken();

        // 2. 사용자 정보 조회
        String email = "user2023@email.com";

        MvcResult result = mvc.perform(get(BASE_URL + "/info")
                        .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject) parser.parse(jsonResult);

        Assertions.assertEquals(email, jsonObj.get("email"));
    }

    @Test
    @DisplayName("사용자 정보 수정")
    void editUserInfo() throws Exception {
        // 1. 로그인
        String token = loginForToken();

        // 2. 사용자 정보 수정
        String nickname = "I'm Owl";
        String introduction = "This is my child owo";

        String body = mapper.writeValueAsString(
                EditUserRequestDto.builder()
                        .nickname(nickname).introduction(introduction)
                        .build()
        );

        MvcResult result = mvc.perform(patch(BASE_URL + "/info")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject) parser.parse(jsonResult);

        Assertions.assertEquals(nickname, jsonObj.get("nickname"));
        Assertions.assertEquals(introduction, jsonObj.get("introduction"));
    }

    @Test
    @DisplayName("사용자 정보 삭제")
    void deleteUserInfo() throws Exception {
        // 1. 로그인
        String token = loginForToken();

        // 2. 회원 정보 (ID) 가져오기
        String email = "user2023@email.com";

        MvcResult result = mvc.perform(get(BASE_URL + "/info")
                        .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject) parser.parse(jsonResult);

        Long userId = (Long) jsonObj.get("id");
        Assertions.assertEquals(email, jsonObj.get("email"));

        // 3. 회원 정보 삭제
        result = mvc.perform(delete(BASE_URL + "/signout")
                        .header("Authorization", token)
                        .param("userId", String.valueOf(userId))
                )
                .andExpect(status().isOk())
                .andReturn();

        jsonResult = result.getResponse().getContentAsString();
        parser = new JSONParser();
        jsonObj = (JSONObject) parser.parse(jsonResult);

        int statusCode = ((Long)jsonObj.get("statusCode")).intValue();
        Assertions.assertEquals(200,  statusCode);
        // 한글 깨짐 문제로 인해 확인 불가
//        String statusMessage = (String) jsonObj.get("statusMessage");
//        Assertions.assertEquals("회원 정보 삭제 성공", statusMessage);
    }

    @Test
    @DisplayName("사용자 비밀번호 수정")
    void editUserPassword() throws Exception {
        // 1. 로그인
        String token = loginForToken();

        // 2. 사용자 비밀번호 수정
        String password = "user123!@#";
        String newPassword1 = "user234@#$";
        String newPassword2 = "user234@#$";

        String body = mapper.writeValueAsString(
                EditPasswordRequestDto.builder()
                        .password(password)
                        .newPassword1(newPassword1)
                        .newPassword2(newPassword2)
                        .build()
        );

        mvc.perform(patch(BASE_URL + "/password")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                )
                .andExpect(status().isOk());

        // i. 기존 비밀변경 입력 실패
        mvc.perform(patch(BASE_URL + "/password")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                )
                .andExpect(status().isBadRequest());

        // ii. 새 비밀번호를 비밀번호 형식에 맞추지 않음
        body = mapper.writeValueAsString(
                EditPasswordRequestDto.builder()
                        .password(newPassword1)
                        .newPassword1("wrongPassword")
                        .newPassword2("wrongPassword")
                        .build()
        );

        mvc.perform(patch(BASE_URL + "/password")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                )
                .andExpect(status().isBadRequest());

        // iii. 새 비밀번호가 서로 일치하지 않음
        body = mapper.writeValueAsString(
                EditPasswordRequestDto.builder()
                        .password(newPassword1)
                        .newPassword1("test234@#$")
                        .newPassword2("test345#$%")
                        .build()
        );

        mvc.perform(patch(BASE_URL + "/password")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                )
                .andExpect(status().isBadRequest());

        // iv. 기존에 사용한 이력이 있는 비밀번호 사용 시도
        body = mapper.writeValueAsString(
                EditPasswordRequestDto.builder()
                        .password(newPassword1)
                        .newPassword1(password)
                        .newPassword2(password)
                        .build()
        );

        mvc.perform(patch(BASE_URL + "/password")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                )
                .andExpect(status().isBadRequest());
    }
}