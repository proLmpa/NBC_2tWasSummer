package com.example.itwassummer.checklist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.itwassummer.checklist.dto.CheckListRequestDto;
import com.example.itwassummer.checklist.dto.CheckListResponseDto;
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
public class CheckListController implements UserDetailsService {

  @Autowired
  ObjectMapper mapper;

  @Autowired
  MockMvc mvc;

  private static final String BASE_URL = "/api/checklists";

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
  @DisplayName("체크 상세 조회 테스트")
  void getCheckList() throws Exception {
    // given
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    Long listId = 1L;

    // then
    MvcResult response = mvc.perform(get(BASE_URL + "/" + listId)
            .contentType(MediaType.APPLICATION_JSON)
            .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        )
        .andExpect(status().isOk())
        .andDo(print()).andReturn();

    // mock 객체 변환
    String jsonResult = response.getResponse().getContentAsString();
    JSONParser parser = new JSONParser();
    JSONObject listObj = (JSONObject) parser.parse(jsonResult);
    Long returnId = (Long) listObj.get("listId");

    Assertions.assertEquals(listId, returnId);
    CheckListResponseDto returnDto = CheckListResponseDto.builder()
        .title((String) listObj.get("title"))
        .build();
    Assertions.assertEquals("테스트체크박스", returnDto.getTitle());
  }

  @Test
  @DisplayName("체크 등록 테스트")
  void insertCheckLists() throws Exception {
    // given
    String title = "예시체크리스트new";
    boolean checked = false;
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    String body = mapper.writeValueAsString(
        CheckListRequestDto.builder()
            .title(title)
            .build()
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
    CheckListResponseDto responseDto = CheckListResponseDto.builder()
        .title(returnName)
        .build();

    Assertions.assertEquals("예시체크리스트new", responseDto.getTitle());
  }

  @Test
  @DisplayName("체크리스트 수정 테스트")
  void updateCheckLists() throws Exception {
    // given
    Long listId = Long.valueOf("1");
    String title = "예시체크리스트update";
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    String body = mapper.writeValueAsString(
        CheckListRequestDto.builder()
            .title(title).build()
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
    String returnTitle = checkObj.get("title").toString();
    CheckListResponseDto responseDto = CheckListResponseDto.builder()
        .title(returnTitle)
        .build();

    Assertions.assertEquals("예시체크리스트update", responseDto.getTitle());
  }

  @Test
  @DisplayName("체크리스트 삭제 테스트")
  void deleteCheckLists() throws Exception {
    // given
    UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.com");

    // when
    Long listId = 4L;

    // then
    mvc.perform(delete(BASE_URL + "/" + listId)
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
