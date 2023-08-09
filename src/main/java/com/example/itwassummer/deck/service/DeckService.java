package com.example.itwassummer.deck.service;

import com.example.itwassummer.deck.dto.DeckResponseDto;

public interface DeckService {
	/*
		@param boardId, name(덱 등록 요청 정보)
		@return DeckResponseDto(덱 정보)
	 */
	DeckResponseDto createDeck(Long boardId, String name);
}
