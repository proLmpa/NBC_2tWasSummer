package com.example.itwassummer.deck.controller;

import com.example.itwassummer.common.dto.ApiResponseDto;
import com.example.itwassummer.deck.dto.DeckRequestDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;
import com.example.itwassummer.deck.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/decks")
public class DeckController {
	private final DeckService deckService;

	// 덱 생성
	@PostMapping("/{boardId}")
	public ResponseEntity<DeckResponseDto> createDeck(@PathVariable Long boardId,
													  @RequestBody DeckRequestDto requestDto) {
		return null;
	}

	// 덱 단일 조회
	@GetMapping("/{deckId}")
	public ResponseEntity<DeckResponseDto> getDeck() {
		return null;
		// isDeleted 아닌 애들, 순서대로 정렬해서.
	}


	@PatchMapping("/{deckId}/name")
	public ResponseEntity<ApiResponseDto> updateDeckName(@PathVariable Long deckId,
														 @RequestParam String name) {
		return null;
	}


	@PatchMapping("/{deckId}/move")
	public ResponseEntity<ApiResponseDto> moveDeck(@PathVariable Long deckId,
												   @RequestParam Long parentId) {
		return null;
	}


	@PatchMapping("/{deckId}/delete")
	public ResponseEntity<ApiResponseDto> deleteDeck(@PathVariable Long deckId,
													 @RequestParam Boolean isDeleted) {
		return null;
	}

}
