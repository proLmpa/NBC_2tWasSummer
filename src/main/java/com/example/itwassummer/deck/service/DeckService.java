package com.example.itwassummer.deck.service;

import com.example.itwassummer.deck.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeckService {
	private final DeckRepository deckRepository;

}
