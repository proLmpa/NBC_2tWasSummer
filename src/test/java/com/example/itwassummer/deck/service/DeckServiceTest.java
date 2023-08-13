package com.example.itwassummer.deck.service;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.board.service.BoardServiceImpl;
import com.example.itwassummer.deck.dto.DeckMoveRequestDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;
import com.example.itwassummer.deck.entity.Deck;
import com.example.itwassummer.deck.repository.DeckRepository;
import com.example.itwassummer.user.dto.SignupRequestDto;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.repository.UserRepository;
import com.example.itwassummer.user.service.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/*
	spring.jpa.hibernate.ddl-auto=create
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeckServiceTest {

	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BoardServiceImpl boardService;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private DeckServiceImpl deckService;
	@Autowired
	private DeckRepository deckRepository;
	User user = null;
	Long boardId = null;

	@Test
	@Order(1)
	@DisplayName("User 회원가입")
	void signupUserTest() {
		String email = "test-user@summer.com";
		String password = "test1234!";
		SignupRequestDto requestDto = SignupRequestDto.builder()
				.email(email).password(password).build();
		userService.signup(requestDto);
	}

	@Test
	@Order(2)
	@DisplayName("Board 생성")
	void createBoardTest() {
		user = userRepository.findByEmail("test-user@summer.com").orElse(null);
		BoardRequestDto requestDto = BoardRequestDto.builder()
				.name("TestBoard")
				.color("red")
				.description("This is Test Board")
				.build();
		boardService.createBoard(requestDto, user);
	}

	@Test
	@Order(3)
	@DisplayName("Deck 생성")
	void createDeckTest() {

		List<Board> boardList = boardRepository.findAll();
		boardId = boardList.get(0).getId();
		System.out.println("==================================================");
		System.out.println(boardId);

		System.out.println("===================================================");

		String deckName1 = "Test-Deck-1";
		String deckName2 = "Test-Deck-2";
		String deckName3 = "Test-Deck-3";
		String deckName4 = "Test-Deck-4";

		DeckResponseDto responseDto1 = deckService.createDeck(boardId, deckName1);
		DeckResponseDto responseDto2 = deckService.createDeck(boardId, deckName2);
		DeckResponseDto responseDto3 = deckService.createDeck(boardId, deckName3);
		DeckResponseDto responseDto4 = deckService.createDeck(boardId, deckName4);

		assertNotNull(responseDto1.getId());
		assertNotNull(responseDto2.getId());
		assertNotNull(responseDto3.getId());
		assertNotNull(responseDto4.getId());
		assertEquals(deckName1, responseDto1.getName());
		assertEquals(deckName2, responseDto2.getName());
		assertEquals(deckName3, responseDto3.getName());
		assertEquals(deckName4, responseDto4.getName());
		assertEquals(0, responseDto1.getParentId());
		assertEquals(1, responseDto2.getParentId());
		assertEquals(2, responseDto3.getParentId());
		assertEquals(3, responseDto4.getParentId());

	}

	@Test
	@Order(4)
	@DisplayName("Deck 이름 수정")
	void updateDeckNameTest() {
		String updateName = "Test-Deck-1_2";
		deckService.updateDeckName(1L, updateName);
		Deck deck = deckRepository.findById(1L).orElse(null);
		assertEquals(updateName, deck.getName());
	}

	@Test
	@Order(5)
	@DisplayName("Deck 순서 변경 - 처음 -> 중간(1->3)")
		// 1234 -> 2314
	void moveDeckTest1() {
		DeckMoveRequestDto requestDto = DeckMoveRequestDto.builder()
				.boardId(1L).parentId(3L).build();
		List<Long> longList = deckService.moveDeck(1L, requestDto);
		deckService.updateParent(longList);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(2, deckList.get(1).getParent().getId());
		assertEquals(3, deckList.get(2).getParent().getId());
		assertEquals(1, deckList.get(3).getParent().getId());
	}

	@Test
	@Order(6)
	@DisplayName("Deck 순서 변경 - 처음 -> 끝(2->4)")
		// 2314 -> 3142
	void moveDeckTest2() {
		DeckMoveRequestDto requestDto = DeckMoveRequestDto.builder()
				.boardId(1L).parentId(4L).build();
		List<Long> longList = deckService.moveDeck(2L, requestDto);
		deckService.updateParent(longList);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(3, deckList.get(1).getParent().getId());
		assertEquals(1, deckList.get(2).getParent().getId());
		assertEquals(4, deckList.get(3).getParent().getId());
	}

	@Test
	@Order(7)
	@DisplayName("Deck 순서 변경 - 중간 -> 처음(4->0)")
		// 3142 -> 4312
	void moveDeckTest3() {
		DeckMoveRequestDto requestDto = DeckMoveRequestDto.builder()
				.boardId(1L).parentId(0L).build();
		List<Long> longList = deckService.moveDeck(4L, requestDto);
		deckService.updateParent(longList);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(4, deckList.get(1).getParent().getId());
		assertEquals(3, deckList.get(2).getParent().getId());
		assertEquals(1, deckList.get(3).getParent().getId());

	}

	@Test
	@Order(8)
	@DisplayName("Deck 순서 변경 - 중간 -> 중간(3->1)")
		// 4312 -> 4132
	void moveDeckTest4() {
		DeckMoveRequestDto requestDto = DeckMoveRequestDto.builder()
				.boardId(1L).parentId(1L).build();
		List<Long> longList = deckService.moveDeck(3L, requestDto);
		deckService.updateParent(longList);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(4, deckList.get(1).getParent().getId());
		assertEquals(1, deckList.get(2).getParent().getId());
		assertEquals(3, deckList.get(3).getParent().getId());
	}

	@Test
	@Order(9)
	@DisplayName("Deck 순서 변경 - 중간 -> 끝(1->2)")
		// 4132 -> 4321
	void moveDeckTest5() {
		DeckMoveRequestDto requestDto = DeckMoveRequestDto.builder()
				.boardId(1L).parentId(2L).build();
		List<Long> longList = deckService.moveDeck(1L, requestDto);
		deckService.updateParent(longList);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(4, deckList.get(1).getParent().getId());
		assertEquals(3, deckList.get(2).getParent().getId());
		assertEquals(2, deckList.get(3).getParent().getId());
	}

	@Test
	@Order(10)
	@DisplayName("Deck 순서 변경 - 끝 -> 처음(1->0)")
		// 4321 -> 1432
	void moveDeckTest6() {
		DeckMoveRequestDto requestDto = DeckMoveRequestDto.builder()
				.boardId(1L).parentId(0L).build();
		List<Long> longList = deckService.moveDeck(1L, requestDto);
		deckService.updateParent(longList);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(1, deckList.get(1).getParent().getId());
		assertEquals(4, deckList.get(2).getParent().getId());
		assertEquals(3, deckList.get(3).getParent().getId());
	}

	@Test
	@Order(11)
	@DisplayName("Deck 순서 변경 - 끝 -> 중간(2->1)")
		// 1432 -> 1243
	void moveDeckTest7() {
		DeckMoveRequestDto requestDto = DeckMoveRequestDto.builder()
				.boardId(1L).parentId(1L).build();
		List<Long> longList = deckService.moveDeck(2L, requestDto);
		deckService.updateParent(longList);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(1, deckList.get(1).getParent().getId());
		assertEquals(2, deckList.get(2).getParent().getId());
		assertEquals(4, deckList.get(3).getParent().getId());
	}

	@Test
	@Order(12)
	@DisplayName("Deck 순서 변경 후 Deck 3개 생성")
		// 1243 -> 1243567
	void createDeckTest2() {
		String deckName5 = "Test-Deck-5";
		String deckName6 = "Test-Deck-6";
		String deckName7 = "Test-Deck-7";
		DeckResponseDto responseDto5 = deckService.createDeck(1L, deckName5);
		DeckResponseDto responseDto6 = deckService.createDeck(1L, deckName6);
		DeckResponseDto responseDto7 = deckService.createDeck(1L, deckName7);

		assertEquals(5, responseDto5.getId());
		assertEquals(6, responseDto6.getId());
		assertEquals(7, responseDto7.getId());
		assertEquals(deckName5, responseDto5.getName());
		assertEquals(deckName6, responseDto6.getName());
		assertEquals(deckName7, responseDto7.getName());
		assertEquals(3, responseDto5.getParentId());
		assertEquals(5, responseDto6.getParentId());
		assertEquals(6, responseDto7.getParentId());
	}

	@Test
	@Order(13)
	@DisplayName("Deck 삭제 - 처음, 1")
		// 1243567 -> 243567
	void deleteDeckTest1() {
		deckService.deleteDeck(1L);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(2, deckList.get(1).getParent().getId());
		assertEquals(4, deckList.get(2).getParent().getId());
		assertEquals(3, deckList.get(3).getParent().getId());
		assertEquals(5, deckList.get(4).getParent().getId());
		assertEquals(6, deckList.get(5).getParent().getId());
	}

	@Test
	@Order(14)
	@DisplayName("Deck 삭제 - 중간, 5")
		// 243567 -> 24367
	void deleteDeckTest2() {
		deckService.deleteDeck(5L);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(2, deckList.get(1).getParent().getId());
		assertEquals(4, deckList.get(2).getParent().getId());
		assertEquals(3, deckList.get(3).getParent().getId());
		assertEquals(6, deckList.get(4).getParent().getId());
	}

	@Test
	@Order(15)
	@DisplayName("Deck 삭제 - 끝, 7")
		// 24367 -> 2436
	void deleteDeckTest3() {
		deckService.deleteDeck(7L);
		List<Deck> deckList = findAllSortedDecksNotDeleted(1L);
		assertNull(deckList.get(0).getParent());
		assertEquals(2, deckList.get(1).getParent().getId());
		assertEquals(4, deckList.get(2).getParent().getId());
		assertEquals(3, deckList.get(3).getParent().getId());
	}

	@Test
	@Order(16)
	@DisplayName("Deck 전체 조회")
		// 2436
	void getAllDecksTest() {
		List<DeckResponseDto> responseDtoList = deckService.getAllDecks(1L);
		assertEquals(2, responseDtoList.get(0).getId());
		assertEquals(4, responseDtoList.get(1).getId());
		assertEquals(3, responseDtoList.get(2).getId());
		assertEquals(6, responseDtoList.get(3).getId());
	}

	@Test
	@Order(17)
	@DisplayName("Deck 개별 조회")
	void getDeckTest() {
		DeckResponseDto responseDto = deckService.getDeck(3L);
		assertEquals(3L, responseDto.getId());
		assertEquals(4L, responseDto.getParentId());
	}

	@Test
	@Order(18)
	@DisplayName("삭제된 Deck 조회")
		// 751
	void getDeletedDecksTest() {
		List<DeckResponseDto> responseDtoList = deckService.getDeletedDecks(1L);

		assertEquals(0, responseDtoList.get(0).getParentId());
		assertEquals(0, responseDtoList.get(1).getParentId());
		assertEquals(0, responseDtoList.get(2).getParentId());
	}

	@Test
	@Order(19)
	@DisplayName("덱 복구")
		// 5번
	void restoreDeckTest() {
		deckService.restoreDeck(5L);

		Deck deck = deckRepository.findById(5L).orElse(null);
		assertFalse(deck.getIsDeleted());
	}

	@Test
	@Order(20)
	@DisplayName("덱 복구 후 다시 삭제된 Deck 조회")
		// 73
	void getDeletedDecksTest2() {
		List<DeckResponseDto> responseDtoList = deckService.getDeletedDecks(1L);

		assertEquals(7, responseDtoList.get(0).getId());
		assertEquals(1, responseDtoList.get(1).getId());
	}


	////////////////////////////////////////////////////////////////

	private List<Deck> findAllSortedDecksNotDeleted(Long boardId) {
		List<Deck> deckList = deckRepository.findAllDecksByBoardId(boardId);
		List<Deck> deckSortedList = sortDecks(deckList);
		return deckSortedList;
	}

	private List<Deck> sortDecks(List<Deck> deckList) {
		List<Deck> deckSortedList = new ArrayList<>();
		deckSortedList.add(deckList.get(0));
		if (deckList.size() == 1) {
			return deckSortedList;
		}

		Map<Long, Deck> deckMap = new HashMap<>();
		for (Deck deck : deckList) {
			Long parentId = deck.getParent() == null ? null : deck.getParent().getId();
			deckMap.put(parentId, deck);
		}

		for (int i = 1; i <= deckList.size() - 1; i++) {
			Long lastId = deckSortedList.get(deckSortedList.size() - 1).getId();
			if (deckMap.containsKey(lastId)) {
				deckSortedList.add(deckMap.get(lastId));
			}
		}

		return deckSortedList;
	}

}
