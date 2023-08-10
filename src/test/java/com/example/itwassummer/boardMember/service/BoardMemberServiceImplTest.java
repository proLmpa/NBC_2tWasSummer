package com.example.itwassummer.boardMember.service;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.board.dto.BoardResponseDto;
import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.board.service.BoardServiceImpl;
import com.example.itwassummer.boardmember.entity.BoardMember;
import com.example.itwassummer.boardmember.repository.BoardMemberRepository;
import com.example.itwassummer.boardmember.service.BoardMemberServiceImpl;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.repository.UserRepository;
import com.example.itwassummer.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
    private BoardServiceImpl boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardMemberServiceImpl boardMemberService;
    @Autowired
    private BoardMemberRepository boardMemberRepository;

    private static final String BASE_URL = "/api";

    @Value("${admin.token}")
    private String adminToken;

    @BeforeEach
    void setMemberAndBoard() {
        // given
        String testEmail = "test@email.com";
        String testPassword = "test123!@#";
        String adminEmail = "admin@email.com";
        String adminPassword = "admin123!@#";

        // when
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .email(testEmail).password(testPassword)
                .admin(false).adminToken(adminToken)
                .build();

        userService.signup(requestDto);

        requestDto = SignupRequestDto.builder()
                .email(adminEmail).password(adminPassword)
                .admin(true).adminToken(adminToken)
                .build();

        userService.signup(requestDto);

        // then
        Assertions.assertNotNull(findUserByEmail(testEmail));
        Assertions.assertNotNull(findUserByEmail(adminEmail));

        // given
        String name = "workspace";
        String description = "workspace description";
        String color = "#ffffff";

        User found = findUserByEmail(adminEmail);

        // when
        BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                .name(name).description(description).color(color).build();

        BoardResponseDto responseDto = boardService.createBoards(boardRequestDto, found);

        // then
        Assertions.assertEquals(responseDto.getName(), name);
        Assertions.assertEquals(responseDto.getDescription(), description);
        Assertions.assertEquals(responseDto.getColor(), color);
        Assertions.assertEquals(responseDto.getBoardCreator(), found.getNickname());
    }

    @Test
    @DisplayName("보드에 사용자 추가")
    void inviteBoardMember() {
        // given
        String testEmail = "test@email.com";
        String adminEmail = "admin@email.com";
        String boardName = "workspace";

        User invitee = findUserByEmail(testEmail);
        User inviter = findUserByEmail(adminEmail);
        Board foundBoard = findBoard(boardName);

        // when
        boardMemberService.inviteBoardMember(foundBoard.getId(), invitee.getId(), inviter);

        // then
        BoardMember member = boardMemberRepository.findByBoardAndUser(foundBoard, invitee).orElse(null);
        Assertions.assertNotNull(member);
    }


    @Test
    @DisplayName("보드에서 사용자 제거")
    void delteBoardMember() {
        // given
        String testEmail = "test@email.com";
        String adminEmail = "admin@email.com";
        String boardName = "workspace";

        User invitee = findUserByEmail(testEmail);
        User inviter = findUserByEmail(adminEmail);
        Board foundBoard = findBoard(boardName);

        // when
        boardMemberService.inviteBoardMember(foundBoard.getId(), invitee.getId(), inviter);
        boardMemberService.deleteBoardMember(foundBoard.getId(), invitee.getId(), inviter);

        // then
        BoardMember member = boardMemberRepository.findByBoardAndUser(foundBoard, invitee).orElse(null);
        Assertions.assertNull(member);
    }

    @Test
    @DisplayName("작업자가 여럿 등록된 보드 제거")
    void deleteBoard() {
        // given
        String testEmail = "test@email.com";
        String adminEmail = "admin@email.com";
        String boardName = "workspace";

        User invitee = findUserByEmail(testEmail);
        User inviter = findUserByEmail(adminEmail);
        Board foundBoard = findBoard(boardName);

        // when
        boardMemberService.inviteBoardMember(foundBoard.getId(), invitee.getId(), inviter);
        boardService.deleteBoard(foundBoard.getId(), inviter);

        // then
        Assertions.assertNotNull(findUserByEmail(testEmail));
        Assertions.assertNotNull(findUserByEmail(adminEmail));
        Assertions.assertEquals(0, boardMemberRepository.findAll().size());
        // -- 외래 키 참조로 인한 삭제 기능 수행 불가
    }

    @Test
    @DisplayName("작업자가 여럿 등록된 보드의 생성자 제거")
    void deleteBoardCreator() {
        // given
        String testEmail = "test@email.com";
        String adminEmail = "admin@email.com";
        String boardName = "workspace";

        User invitee = findUserByEmail(testEmail);
        User inviter = findUserByEmail(adminEmail);
        Board foundBoard = findBoard(boardName);

        // when
        boardMemberService.inviteBoardMember(foundBoard.getId(), invitee.getId(), inviter);
        userService.deleteUserInfo(inviter.getId(), inviter);

        // then
        Assertions.assertNotNull(findUserByEmail(testEmail));
        Assertions.assertNull(findUserByEmail(adminEmail));
        Assertions.assertNull(findBoard(boardName));
        Assertions.assertEquals(0, boardMemberRepository.findAll().size());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private Board findBoard(String name) {
        return boardRepository.findByName(name).orElse(null);
    }
}
