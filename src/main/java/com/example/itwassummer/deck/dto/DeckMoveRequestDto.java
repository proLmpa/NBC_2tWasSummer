package com.example.itwassummer.deck.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeckMoveRequestDto {
	@NotNull
	Long boardId;
	@NotNull
	Long parentId;
}
