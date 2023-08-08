package com.example.itwassummer.deck.service;

import com.example.itwassummer.deck.dto.DeckRequestDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;

public interface DeckService {
	DeckResponseDto createDeck(Long boardId, DeckRequestDto requestDto);
}
