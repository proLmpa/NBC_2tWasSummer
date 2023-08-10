package com.example.itwassummer.deck.service;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.deck.dto.DeckMoveRequestDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;
import com.example.itwassummer.deck.entity.Deck;
import com.example.itwassummer.deck.repository.DeckRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

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
			deck.updateParent(connectDecks(deckList).getLast());
		}
		return new DeckResponseDto(deck);
	}

	@Transactional(readOnly = true)
	@Override
	public List<DeckResponseDto> getAllDecks(Long boardId) {
		Board board = findBoard(boardId);
		List<Deck> deckList = deckRepository.findAllDecksByBoardId(boardId);
		LinkedList<Deck> deckLinkedList = connectDecks(deckList);
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
	public void moveDeck(Long deckId, DeckMoveRequestDto requestDto) {
		Board board = findBoard(requestDto.getBoardId());
		List<Deck> deckList = deckRepository.findAllDecksByBoardId(requestDto.getBoardId());
		LinkedList<Deck> deckLinkedList = connectDecks(deckList);
		Deck lastDeck = deckLinkedList.getLast();
		Deck firstDeck = deckLinkedList.getFirst();
		Deck deck = findDeck(deckId);
		Deck parentDeck = null;
		if (requestDto.getParentId() != 0) {
			parentDeck = findDeck(requestDto.getParentId());
		}

		// Deck이 끝에 있을 때
		if (lastDeck.getId().equals(deckId)) {
			if (requestDto.getParentId() == 0) {    // 처음으로 옮길 때
				deck.updateParent(null);
				firstDeck.updateParent(deck);
			} else { // 중간으로 옮길 때
				Deck childDeck = deckRepository.findByParentAndIsDeletedFalse(parentDeck);
				childDeck.updateParent(deck);
				deck.updateParent(parentDeck);
			}
			return;
		}

		// Deck이 처음에 있을 때 or Deck이 중간에 있을 때

		Deck myChildDeck = deckRepository.findByParentAndIsDeletedFalse(deck);
		myChildDeck.updateParent(null);

		// Deck이 처음에 있을 때
		if (firstDeck.getId().equals(deckId)) {
			if (requestDto.getParentId().equals(lastDeck.getId())) { // 끝으로 옮길 때
				deck.updateParent(lastDeck);
			} else {
				Deck otherChildDeck = deckRepository.findByParentAndIsDeletedFalse(parentDeck);
				otherChildDeck.updateParent(deck);
				deck.updateParent(parentDeck);
			}
			return;
		}

		// Deck이 중간에 있을 때
		myChildDeck.updateParent(deck.getParent());

		if (requestDto.getParentId() == 0) { // 처음으로 옮길 때
			firstDeck.updateParent(deck);
		} else if (requestDto.getParentId().equals(lastDeck.getId())) { // 끝으로 옮길 때

		} else { // 중간으로 옮길 때
			Deck otherChildDeck = deckRepository.findByParentAndIsDeletedFalse(parentDeck);
			otherChildDeck.updateParent(deck);
		}
		deck.updateParent(parentDeck);
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

	private LinkedList<Deck> connectDecks(List<Deck> deckList) {
		LinkedList<Deck> deckLinkedList = new LinkedList<>();
		deckLinkedList.add(deckList.get(0));
		if (deckList.size() == 1) {
			return deckLinkedList;
		}

		Map<Long, Deck> deckMap = new HashMap<>();
		for (Deck deck : deckList) {
			Long parentId = deck.getParent() == null ? null : deck.getParent().getId();
			deckMap.put(parentId, deck);
		}

		for (int i = 1; i <= deckList.size() - 1; i++) {
			Long lastId = deckLinkedList.getLast().getId();
			if (deckMap.containsKey(lastId)) {
				deckLinkedList.add(deckMap.get(lastId));
			}
		}

		return deckLinkedList;
	}

}
