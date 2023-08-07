package com.example.itwassummer.deck.repository;

import com.example.itwassummer.deck.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
}
