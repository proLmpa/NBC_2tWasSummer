package com.example.itwassummer.user.service;

import com.example.itwassummer.common.jwt.JwtUtil;
import com.example.itwassummer.common.security.UserDetailsServiceImpl;
import com.example.itwassummer.user.dto.LoginRequestDto;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import com.example.itwassummer.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext) {
//        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // 필터 추가
//                .addFilters(new JwtAuthorizationFilter(jwtUtil, userDetailsService), new JwtAuthorizationFilter(jwtUtil, userDetailsService))
//                .build();
//    }

    @BeforeAll
    void signUp() {
        // given
        String email = "user2023@email.com";
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
        MvcResult result = mvc.perform(post("/api/users/login")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals("Bearer", Objects.requireNonNull(result.getResponse().getHeader("Authorization")).substring(0, 6));
    }

}
