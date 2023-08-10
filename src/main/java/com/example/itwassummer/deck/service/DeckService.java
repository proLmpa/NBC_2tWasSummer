package com.example.itwassummer.deck.service;

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
}
