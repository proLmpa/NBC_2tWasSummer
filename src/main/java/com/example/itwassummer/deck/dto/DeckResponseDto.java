package com.example.itwassummer.deck.dto;

import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.deck.entity.Deck;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DeckResponseDto {
	private Long id;
	private Long parentId;
	private String name;
	private List<CardResponseDto> cards = new ArrayList<>();

	public DeckResponseDto(Deck deck) {
		this.id = deck.getId();
		this.parentId = deck.getParent() == null ? 0 : deck.getParent().getId();
		this.name = deck.getName();
		// this.card = deck.getCards()..... 필요
	}

}
