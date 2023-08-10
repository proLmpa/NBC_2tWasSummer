package com.example.itwassummer.deck.repository;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.deck.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long>, DeckCustomRepository{
	Deck findByParentNullAndBoard(Board board);

	Deck findByParentAndIsDeletedFalse(Deck deck);
}
