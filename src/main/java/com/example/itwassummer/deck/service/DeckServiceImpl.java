package com.example.itwassummer.deck.service;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.board.repository.BoardRepository;
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
		List<Deck> deckList = deckRepository.findAllDecks();
		LinkedList<Deck> deckLinkedList = connectDecks(deckList);
		Deck deck = new Deck(name, board);
		deck.updateParent(deckLinkedList.getLast());
		deckRepository.save(deck);

		return new DeckResponseDto(deck);
	}


	/////////////////////////////////////////////////////////////////

	private Board findBoard(Long id) {
		return boardRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("선택한 Board는 존재하지 않습니다.")
		);
	}

	private Deck findDeck(Long id) {
		return deckRepository.findById(id).orElse(null);
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
