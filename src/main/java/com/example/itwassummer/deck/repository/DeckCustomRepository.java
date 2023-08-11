package com.example.itwassummer.deck.repository;

import com.example.itwassummer.deck.entity.Deck;

import java.util.List;

public interface DeckCustomRepository {

	List<Deck> findAllDecksByBoardId(Long boardId);
}
