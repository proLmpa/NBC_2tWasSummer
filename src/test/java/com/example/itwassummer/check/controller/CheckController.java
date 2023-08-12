package com.example.itwassummer.check.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.itwassummer.check.dto.ChecksRequestDto;
import com.example.itwassummer.check.dto.ChecksResponseDto;
import com.example.itwassummer.common.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false)
public class CheckController implements UserDetailsService {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  MockMvc mvc;

  private static final String BASE_URL = "/api/checks";

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setup() throws Exception {

    this.mvc = MockMvcBuilders
        .webAppContextSetup(this.webApplicationContext)
        .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
        .build();
  }

  @Test
  @DisplayName("체크 등록 테스트")
  void insertChecks() throws Exception {
    // given
    String name = "예시체크new";
    boolean checked = false;
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    String body = mapper.writeValueAsString(
        ChecksRequestDto.builder()
            .name(name)
            .checked(checked).build()
    );

    // then
    MvcResult response = mvc.perform(post(BASE_URL)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    String jsonResult = response.getResponse().getContentAsString();
    JSONParser parser = new JSONParser();
    JSONObject checkObj = (JSONObject) parser.parse(jsonResult);
    String returnName = checkObj.get("name").toString();
    ChecksResponseDto responseDto = ChecksResponseDto.builder()
        .name(returnName)
        .build();

    Assertions.assertEquals("예시체크new", responseDto.getName());
  }

  @Test
  @DisplayName("체크 이름 수정 테스트")
  void updateChecksName() throws Exception {
    // given
    Long checkId = Long.valueOf("1");
    boolean checked = false;
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    String name = "예시체크명update";

    // then
    MvcResult response = mvc.perform(patch(BASE_URL + "/" + checkId + "/name")
            .param("checked", String.valueOf(checked))
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    String jsonResult = response.getResponse().getContentAsString();
    JSONParser parser = new JSONParser();
    JSONObject checkObj = (JSONObject) parser.parse(jsonResult);
    String returnName = checkObj.get("name").toString();
    ChecksResponseDto responseDto = ChecksResponseDto.builder()
        .name(returnName)
        .build();

    Assertions.assertEquals("예시체크명update", responseDto.getName());
  }

  @Test
  @DisplayName("체크 여부 수정 테스트")
  void updateChecksChecked() throws Exception {
    // given
    Long checkId = Long.valueOf("1");
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    boolean checked = true;

    // then
    MvcResult response = mvc.perform(patch(BASE_URL + "/" + checkId + "/isChecked")
            .param("checked", String.valueOf(checked))
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    String jsonResult = response.getResponse().getContentAsString();
    JSONParser parser = new JSONParser();
    JSONObject checkObj = (JSONObject) parser.parse(jsonResult);
    boolean returnChecked = (boolean) checkObj.get("checked");
    ChecksResponseDto responseDto = ChecksResponseDto.builder()
        .checked(returnChecked)
        .build();

    Assertions.assertEquals("예시체크여부update", responseDto.getName());
  }

  @Test
  @DisplayName("체크 삭제 테스트")
  void deleteChecks() throws Exception {
    // given
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    Long checkId = 4L;

    // then
    mvc.perform(delete(BASE_URL + "/" + checkId)
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new User("email", "password", Collections.EMPTY_LIST);
  }
}

