package com.example.itwassummer.deck.service;

import com.example.itwassummer.deck.dto.DeckMoveRequestDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;

import java.util.List;

public interface DeckService {
	/*
		@param boardId, name(덱 등록 요청 정보)
		@return DeckResponseDto(덱 정보)
	 */
	DeckResponseDto createDeck(Long boardId, String name);

	/*
		@param boardId(덱 조회 요청 정보)
		@return List<DeckResponseDto>(모든 덱 조회)
	 */
	List<DeckResponseDto> getAllDecks(Long boardId);

	/*
		@param deckId(덱 조회 요청 정보)
		@return DeckResponseDto(개별 덱 조회)
	 */
	DeckResponseDto getDeck(Long deckId);

	/*
		@param deckId, name(덱 이름 수정 요청 정보)
	 */
	void updateDeckName(Long deckId, String name);

	/*
		@param deckId, requestDto (덱 위치 수정 요청 정보)
	 */
	void moveDeck(Long deckId, DeckMoveRequestDto requestDto);

	/*
		@param deckId (덱 삭제 요청 정보)
	 */
	void deleteDeck(Long deckId);
}
