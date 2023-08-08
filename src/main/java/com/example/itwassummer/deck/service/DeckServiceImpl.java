package com.example.itwassummer.deck.service;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.deck.dto.DeckRequestDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;
import com.example.itwassummer.deck.entity.Deck;
import com.example.itwassummer.deck.repository.BoardRepository;
import com.example.itwassummer.deck.repository.DeckRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {
	private final DeckRepository deckRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public DeckResponseDto createDeck(Long boardId, DeckRequestDto requestDto) {
		Board board = findBoard(boardId);
		String name = requestDto.getName();
		Deck deck = new Deck(name, board);

		List<Deck> decks = deckRepository.findAll();
		if(decks.size()==0){
			deckRepository.save(deck);
			return new DeckResponseDto(deck);
		}

		if (requestDto.getParentId() == 0) {

			Deck firstDeck = deckRepository.findByParentNull(); // parent가 null값인 애 찾기
			firstDeck.updateParent(deck);

		} else {

			Deck parentDeck = findDeck(requestDto.getParentId());

			// 마지막 거 넣었을 때 null값 들어오는지 확인
			Deck afterDeck = deckRepository.findByParent(parentDeck);
			deck.updateParent(parentDeck);

			if (afterDeck != null) {
				afterDeck.updateParent(parentDeck);
			}

		}
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
}
