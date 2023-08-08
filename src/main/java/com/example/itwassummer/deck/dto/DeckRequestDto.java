package com.example.itwassummer.deck.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeckRequestDto {
	String name;
	Long parentId;
}
