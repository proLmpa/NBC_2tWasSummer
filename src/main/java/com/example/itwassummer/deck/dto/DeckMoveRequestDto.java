package com.example.itwassummer.deck.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckMoveRequestDto {
	@NotNull
	Long boardId;
	@NotNull
	Long parentId;
}
