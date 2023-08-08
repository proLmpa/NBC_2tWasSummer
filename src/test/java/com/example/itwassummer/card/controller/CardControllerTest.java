package com.example.itwassummer.card.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.user.dto.LoginRequestDto;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerTest {
  @Autowired
  ObjectMapper mapper;

  @Autowired
  MockMvc mvc;

  private static final String BASE_URL = "/api/cards";
  private static final String USER_BASE_URL = "/api/users";

  @Test
  @DisplayName("카드 등록 테스트")
  void insert() throws Exception {
    // given
    String name = "예시카드1";
    Long parentId = Long.valueOf(1);
    String description = "예시카드입니다.";
    String header = login();
    LocalDateTime now = LocalDateTime.now();

    // when
    String body = mapper.writeValueAsString(
        CardRequestDto.builder()
            .name(name)
            .dueDate(now)
            .parentId(parentId)
            .description(description)
            .build()
    );

    // then
    mvc.perform(post(BASE_URL + "/")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization",header)
        )
        .andExpect(status().isOk())
        .andDo(print());
  }


  String login() throws Exception {
    // given
    String email = "user@email.com";
    String password = "user123!@#";

    // when
    LoginRequestDto requestDto = new LoginRequestDto();
    requestDto.setEmail(email);
    requestDto.setPassword(password);

    String body = mapper.writeValueAsString(requestDto);

    // then
    MvcResult result = mvc.perform(post(USER_BASE_URL + "/login")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    return Objects.requireNonNull(result.getResponse().getHeader("Authorization")).toString();
  }

}