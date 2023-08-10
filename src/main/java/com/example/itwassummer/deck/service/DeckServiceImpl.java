package com.example.itwassummer.deck.service;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
import com.example.itwassummer.deck.dto.DeckMoveRequestDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;
import com.example.itwassummer.deck.entity.Deck;
import com.example.itwassummer.deck.repository.DeckRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

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
		deck.updateParent(getLastDeck(deckList));
		deckRepository.save(deck);

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

	@Transactional
	@Override
	public void moveDeck(Long deckId, DeckMoveRequestDto requestDto) {
		Board board = findBoard(requestDto.getBoardId());
		List<Deck> deckList = deckRepository.findAllDecksByBoardId(requestDto.getBoardId());
		Deck lastDeck = getLastDeck(deckList);
		Deck firstDeck = deckRepository.findByParentNullAndBoard(board);
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
				Deck childDeck = deckRepository.findByParent(parentDeck);
				childDeck.updateParent(deck);
				deck.updateParent(parentDeck);
			}
			return;
		}

		// Deck이 처음에 있을 때 or Deck이 중간에 있을 때

		Deck myChildDeck = deckRepository.findByParent(deck);
		myChildDeck.updateParent(null);

		// Deck이 처음에 있을 때
		if (firstDeck.getId().equals(deckId)) {
			if (requestDto.getParentId().equals(lastDeck.getId())) { // 끝으로 옮길 때
				deck.updateParent(lastDeck);
			} else {
				Deck otherChildDeck = deckRepository.findByParent(parentDeck);
				otherChildDeck.updateParent(deck);
				deck.updateParent(parentDeck);
			}
			return;
		}

		// Deck이 중간에 있을 때
		myChildDeck.updateParent(deck.getParent());

		if (requestDto.getParentId() == 0) { // 처음으로 옮길 때
			firstDeck.updateParent(deck);
		} else if(requestDto.getParentId().equals(lastDeck.getId())){ // 끝으로 옮길 때

		} else { // 중간으로 옮길 때
			Deck otherChildDeck = deckRepository.findByParent(parentDeck);
			otherChildDeck.updateParent(deck);
		}
		deck.updateParent(parentDeck);
	}

	/////////////////////////////////////////////////////////////////

	private Board findBoard(Long id) {
		return boardRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("선택한 Board는 존재하지 않습니다.")
		);
	}

	private Deck findDeck(Long id) {
		return deckRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("선택한 Deck은 존재하지 않습니다.")
		);

	}

	private Deck getLastDeck(List<Deck> deckList) {
		return connectDecks(deckList).getLast();
	}

	private LinkedList<Deck> connectDecks(List<Deck> deckList) {
		LinkedList<Deck> deckLinkedList = new LinkedList<>();
		deckLinkedList.add(deckList.get(0));
		if (deckList.size() == 1) {
			return deckLinkedList;
		}

		for (int i = 1; i <= deckList.size() - 1; i++) {
			for (int j = 1; j < deckList.size(); j++) {
				if (deckList.get(j).getParent().getId().equals(deckLinkedList.getLast().getId())) {
					deckLinkedList.add(deckList.get(j));
					break;
				}
			}
		}
		return deckLinkedList;
	}

}
