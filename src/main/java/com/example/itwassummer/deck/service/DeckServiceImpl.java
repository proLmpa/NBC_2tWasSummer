package com.example.itwassummer.deck.service;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.deck.dto.DeckMoveRequestDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;
import com.example.itwassummer.deck.entity.Deck;
import com.example.itwassummer.deck.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {
	private final DeckRepository deckRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public DeckResponseDto createDeck(Long boardId, String name) {
		Board board = findBoard(boardId);
		List<Deck> deckList = deckRepository.findAllDecksByBoardId(boardId);
		Deck deck = new Deck(name, board);
		deckRepository.save(deck);
		if (deckList.size() != 0) {
			List<Deck> deckSortedList = sortDecks(deckList);
			deck.updateParent(deckSortedList.get(deckSortedList.size() - 1));
		}
		return new DeckResponseDto(deck);
	}

	@Transactional(readOnly = true)
	@Override
	public List<DeckResponseDto> getAllDecks(Long boardId) {
		Board board = findBoard(boardId);
		List<Deck> deckList = deckRepository.findAllDecksByBoardId(boardId);
		List<Deck> deckLinkedList = sortDecks(deckList);
		return deckLinkedList.stream().map(DeckResponseDto::new).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public DeckResponseDto getDeck(Long deckId) {
		Deck deck = findDeck(deckId);
		return new DeckResponseDto(deck);
	}

	@Transactional
	@Override
	public void updateDeckName(Long deckId, String name) {
		Deck deck = findDeck(deckId);
		deck.updateName(name);
	}

	// 수정이 필요합니다.(테스트 통과 X)
	@Transactional
	@Override
	public List<Long> moveDeck(Long deckId, DeckMoveRequestDto requestDto) {
		Board board = findBoard(requestDto.getBoardId());
		Deck deck = findDeck(deckId);

		List<Deck> deckList = deckRepository.findAllDecksByBoardId(requestDto.getBoardId());

		List<Deck> deckSortedList = sortDecks(deckList);

		deckSortedList.remove(deck);

		int parentIndex = 0;
		if (requestDto.getParentId() != 0) {
			for (int i = 0; i < deckSortedList.size(); i++) {
				if (deckSortedList.get(i).getId().equals(requestDto.getParentId())) {
					parentIndex = i;
				}
			}

			deckSortedList.add(parentIndex + 1, deck);
		} else {
			deckSortedList.add(0, deck);
		}

		for (int i = 0; i < deckSortedList.size(); i++) {
			deckSortedList.get(i).updateParent(null);
		}

		List<Long> list = new ArrayList<>();

		for (int i = 0; i < deckSortedList.size(); i++) {
			list.add(deckSortedList.get(i).getId());
		}
		return list;
	}

	@Transactional
	public void updateParent(List<Long> list) {
		for (int i = 1; i < list.size(); i++) {
			Deck deck = findDeck(list.get(i));
			deck.updateParent(findDeck(list.get(i - 1)));
		}
	}

	@Override
	@Transactional
	public void deleteDeck(Long deckId) {
		Deck deck = findDeck(deckId);
		if (deck.getIsDeleted()) {
			throw new CustomException(CustomErrorCode.ALREADY_DELETED_DECK, null);
		}
		Deck myChildDeck = deckRepository.findByParentAndIsDeletedFalse(deck);
		if (myChildDeck != null) {
			if (deck.getParent() != null) {
				myChildDeck.updateParent(deck.getParent());
			} else {
				myChildDeck.updateParent(null);
			}
		}
		deck.updateParent(null);
		deck.deleteDeck();
	}

	@Override
	@Transactional(readOnly = true)
	public List<DeckResponseDto> getDeletedDecks(Long boardId) {
		Board board = findBoard(boardId);
		List<Deck> deletedDeckList = deckRepository.findAllByBoardAndIsDeletedTrue(board);
		if (deletedDeckList.size() == 0) {
			throw new CustomException(CustomErrorCode.DELETED_DECK_NOT_FOUND, null);
		}
		return deletedDeckList.stream().map(DeckResponseDto::new).toList();
	}

	/////////////////////////////////////////////////////////////////

	private Board findBoard(Long id) {
		return boardRepository.findById(id).orElseThrow(() ->
				new CustomException(CustomErrorCode.BOARD_NOT_FOUND, null)
		);
	}

	private Deck findDeck(Long id) {
		return deckRepository.findById(id).orElseThrow(() ->
				new CustomException(CustomErrorCode.DECK_NOT_FOUND, null)
		);

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
