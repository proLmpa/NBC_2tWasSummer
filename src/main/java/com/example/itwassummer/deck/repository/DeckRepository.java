package com.example.itwassummer.deck.repository;

import com.example.itwassummer.deck.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
	Deck findByParentNull();

	Deck findByParent(Deck parent);
}
