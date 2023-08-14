package com.example.itwassummer.card.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.board.service.BoardServiceImpl;
import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.dto.CardViewResponseDto;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.cardmember.dto.CardMemberResponseDto;
import com.example.itwassummer.check.dto.ChecksResponseDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;
import com.example.itwassummer.deck.entity.Deck;
import com.example.itwassummer.deck.repository.DeckRepository;
import com.example.itwassummer.deck.service.DeckServiceImpl;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.repository.UserRepository;
import com.example.itwassummer.user.service.UserServiceImpl;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CardServiceTest {

  @Autowired
  private UserServiceImpl userService;

  @Autowired
  private BoardServiceImpl boardService;

  @Autowired
  private DeckServiceImpl deckService;

  @Autowired
  private CardServiceImpl cardService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private DeckRepository deckRepository;

  @Autowired
  private CardRepository cardRepository;

  @Value("${admin.token}")
  private String adminToken;


  @BeforeEach
  void setMasterInfo() {
    Optional<User> user1 = userRepository.findByEmail("test01@email.com");
    String email = "test01@email.com";
    String password = "test1234!";
    SignupRequestDto requestDto;
    if (!user1.isPresent()) {
      requestDto = SignupRequestDto.builder()
          .email(email).password(password).build();
      SignUp(requestDto);
    }
    Optional<User> user2 = userRepository.findByEmail("test02@email.com");
    if (!user2.isPresent()) {
      email = "test02@email.com";
      password = "test1234!";
      requestDto = SignupRequestDto.builder()
          .email(email).password(password).adminToken(adminToken)
          .admin(true).
          build();
      SignUp(requestDto);
    }

    User user = userRepository.findByEmail("test01@email.com").orElse(null);
    BoardRequestDto boardRequestDto = BoardRequestDto.builder()
        .name("TestBoard")
        .color("red")
        .description("This is Test Board")
        .build();
    Optional<Board> findBoard = boardRepository.findByName("TestBoard");
    if (!findBoard.isPresent()) {
      boardService.createBoard(boardRequestDto, user);
    }
    createDeckTest();
  }

  @Test
  @Order(1)
  @DisplayName("Card 생성")
  void createCardTest() throws IOException {
    List<Deck> deckList = deckRepository.findAll();
    Long deckId = deckList.get(0).getId();

    String cardName1 = "TESTCARD1";
    Long parentId1 = Long.valueOf(1);
    String description1 = "예시카드입니다.";
    LocalDateTime now = LocalDateTime.now();

    CardRequestDto requestDto1 = CardRequestDto.builder()
        .name(cardName1)
        .dueDate(now)
        .parentId(parentId1)
        .description(description1)
        .deckId(deckId)
        .build();
    MockMultipartFile file1 = new MockMultipartFile("files"
        , "palmtree.png"
        , "multipart/form-data"
        , new FileInputStream(getClass().getResource("/image/palmtree.png").getFile())
    );
    MockMultipartFile file2 = new MockMultipartFile("files"
        , "pororo.png"
        , "multipart/form-data"
        , new FileInputStream(getClass().getResource("/image/pororo.png").getFile())
    );

    List<MultipartFile> files = List.of(
        file1,
        file2
    );

    CardResponseDto responseDto1 = cardService.save(requestDto1, files);

    assertNotNull(responseDto1.getDeckName());
    assertEquals(responseDto1.getName(), cardName1);
    assertEquals(1, responseDto1.getParentId());

  }

  @Test
  @Order(2)
  @DisplayName("Card 수정")
  void UpdateTest() throws IOException {
    List<Deck> deckList = deckRepository.findAll();
    Long deckId = deckList.get(deckList.size() - 1).getId();

    List<Card> cardList = cardRepository.findAll();
    Long cardId = cardList.get(cardList.size() - 1).getId();

    String cardName1 = "UP-CARD1";
    String description1 = "예시카드 수정입니다.";
    LocalDateTime now = LocalDateTime.now();

    CardRequestDto requestDto1 = CardRequestDto.builder()
        .name(cardName1)
        .dueDate(now)
        .description(description1)
        .deckId(deckId)
        .build();
    MockMultipartFile file1 = new MockMultipartFile("files"
        , "palmtree.png"
        , "multipart/form-data"
        , new FileInputStream(getClass().getResource("/image/palmtree.png").getFile())
    );

    List<MultipartFile> files = List.of(
        file1
    );

    CardResponseDto responseDto1 = cardService.update(cardId, requestDto1, files);

    assertNotNull(responseDto1.getDeckName());
    assertEquals(responseDto1.getName(), cardName1);
    assertEquals(description1, responseDto1.getDescription());

  }

  @Test
  @Order(3)
  @DisplayName("Card 마감일 수정")
  void ChangeDueDateTest() {
    List<Deck> deckList = deckRepository.findAll();
    Long deckId = deckList.get(deckList.size() - 1).getId();

    List<Card> cardList = cardRepository.findAll();
    Long cardId = cardList.get(cardList.size() - 1).getId();

    LocalDateTime now = LocalDateTime.now();
    String nowDate = String.valueOf(now);

    CardResponseDto responseDto1 = cardService.changeDueDate(cardId, nowDate);

    assertEquals(responseDto1.getDueDate(), nowDate);
  }

  @Test
  @Order(4)
  @DisplayName("Card 정렬순서 수정")
  void moveCardTest() {
    List<Card> cardList = cardRepository.findAll();
    Long cardId = cardList.get(cardList.size() - 1).getId();

    LocalDateTime now = LocalDateTime.now();
    Long order = 11L;

    CardResponseDto responseDto1 = cardService.moveCard(cardId, order);

    assertEquals(responseDto1.getParentId(), order);
  }

  @Test
  @Order(5)
  @DisplayName("Card 사용자 수정")
  void changeCardMemberTest() {
    List<Card> cardList = cardRepository.findAll();
    Long cardId = cardList.get(cardList.size() - 1).getId();
    String email1 = "test01@email.com";
    String email2 = "test02@email.com";
    String emails = email1 + "," + email2;

    List<CardMemberResponseDto> responseDtoList = cardService.changeCardMembers(cardId, emails);

    assertEquals(responseDtoList.get(0).getUserEmail(), email1);
    assertEquals(responseDtoList.get(1).getUserEmail(), email2);
  }

  @Test
  @Order(6)
  @DisplayName("Card 다른 덱으로 이동")
  void changeCardDeckTest() {
    List<Card> cardList = cardRepository.findAll();
    List<Deck> deckList = deckRepository.findAll();
    Long cardId = cardList.get(cardList.size() - 1).getId();
    Long deckId = deckList.get(deckList.size() - 1).getId();

    CardResponseDto cardResponseDto = cardService.moveCardToOtherDeck(deckId, cardId, 1L);

    assertEquals(cardResponseDto.getDeckName(), deckList.get(deckList.size() - 1).getName());
  }


  @Test
  @Order(7)
  @DisplayName("Card 상세 조회")
  void getCards() throws IOException {
    List<Card> cardList = cardRepository.findAll();
    Long cardId = cardList.get(cardList.size() - 1).getId();
    CardViewResponseDto viewData = cardService.getCard(cardId);
    String cardName1 = "UP-CARD1";
    String description1 = "예시카드 수정입니다.";
    assertEquals(viewData.getDescription(), description1);
    assertEquals(viewData.getName(), cardName1);
  }

  @Test
  @Order(8)
  @DisplayName("카드 삭제")
  void deleteDeckTest() {
    List<Card> cardList = cardRepository.findAll();
    Long lastCardId = cardList.get(cardList.size() - 1).getId();
    cardService.delete(lastCardId);
  }

  @DisplayName("Deck 생성")
  void createDeckTest() {
    List<Board> boardList = boardRepository.findAll();
    Long boardId = boardList.get(0).getId();

    String deckName1 = "Test-Deck-1";
    String deckName2 = "Test-Deck-2";
    String deckName3 = "Test-Deck-3";
    String deckName4 = "Test-Deck-4";

    List<DeckResponseDto> findDeck1 = deckRepository.findByName(deckName1).stream()
        .map(DeckResponseDto::new).toList();
    List<DeckResponseDto> findDeck2 = deckRepository.findByName(deckName2).stream()
        .map(DeckResponseDto::new).toList();
    List<DeckResponseDto> findDeck3 = deckRepository.findByName(deckName3).stream()
        .map(DeckResponseDto::new).toList();
    List<DeckResponseDto> findDeck4 = deckRepository.findByName(deckName4).stream()
        .map(DeckResponseDto::new).toList();

    DeckResponseDto responseDto1;
    DeckResponseDto responseDto2;
    DeckResponseDto responseDto3;
    DeckResponseDto responseDto4;

    if (findDeck1.size() == 0) {
      responseDto1 = deckService.createDeck(boardId, deckName1);
      responseDto2 = deckService.createDeck(boardId, deckName2);
      responseDto3 = deckService.createDeck(boardId, deckName3);
      responseDto4 = deckService.createDeck(boardId, deckName4);
    } else {
      responseDto1 = findDeck1.get(0);
      responseDto2 = findDeck2.get(0);
      responseDto3 = findDeck3.get(0);
      responseDto4 = findDeck4.get(0);
    }
    assertNotNull(responseDto1.getId());
    assertNotNull(responseDto2.getId());
    assertNotNull(responseDto3.getId());
    assertNotNull(responseDto4.getId());
  }

  @DisplayName("회원가입")
  void SignUp(SignupRequestDto signupRequestDto) {
    userService.signup(signupRequestDto);
  }


}
