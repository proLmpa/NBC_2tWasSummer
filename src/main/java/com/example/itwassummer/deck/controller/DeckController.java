package com.example.itwassummer.deck.controller;

import com.example.itwassummer.common.dto.ApiResponseDto;
import com.example.itwassummer.deck.dto.DeckRequestDto;
import com.example.itwassummer.deck.dto.DeckResponseDto;
import com.example.itwassummer.deck.service.DeckServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Deck API", description = "Deck CRUD를 위한 API 정보를 담고 있습니다.")
public class DeckController {
	private final DeckServiceImpl deckService;

	// 덱 생성
	@Operation(summary = "Deck 생성", description = "boardId에 맞는 Board에 Deck을 생성하고 DeckResponseDto를 반환합니다.")
	@PostMapping("/decks")
	public ResponseEntity<DeckResponseDto> createDeck(@RequestParam Long boardId,
													  @RequestBody DeckRequestDto requestDto) {
		DeckResponseDto responseDto = deckService.createDeck(boardId, requestDto);
		return ResponseEntity.ok().body(responseDto);
	}

	// 덱 단일 조회
	@Operation(summary = "Deck 단일 조회", description = "deckId에 맞는 Deck을 조회하여 DeckResponseDto로 반환합니다.")
	@GetMapping("/decks/{deckId}")
	public ResponseEntity<DeckResponseDto> getDeck() {
		return null;
		// isDeleted 아닌 애들, 순서대로 정렬해서.
	}


	// 덱 이름 변경
	@Operation(summary = "Deck 이름 수정", description = "deckId에 맞는 Deck을 조회하여 이름을 수정합니다.")
	@PatchMapping("/decks/{deckId}/name")
	public ResponseEntity<ApiResponseDto> updateDeckName(@PathVariable Long deckId,
														 @RequestParam String name) {
		return null;
	}

	// 덱 위치 변경
	@Operation(summary = "Deck 위치 변경", description = "deckId에 맞는 Deck을 조회하여 위치를 수정합니다.")
	@PatchMapping("/decks/{deckId}/move")
	public ResponseEntity<ApiResponseDto> moveDeck(@PathVariable Long deckId,
												   @RequestParam Long parentId) {
		return null;
	}

	// 덱 삭제(논리적 삭제)
	@Operation(summary = "Deck 보관", description = "deckId에 맞는 Deck을 조회하여 논리적 삭제를 진행합니다.")
	@PatchMapping("/decks/{deckId}/delete")
	public ResponseEntity<ApiResponseDto> deleteDeck(@PathVariable Long deckId,
													 @RequestParam Boolean isDeleted) {
		return null;
	}

	// 복구 목록에서 덱 조회(삭제된 덱 조회)
	@Operation(summary = "Deck 조회", description = "boardId에 맞는 보드에서 삭제된 덱들을 조회합니다.")
	@GetMapping("/decks/deleted")
	public ResponseEntity<DeckResponseDto> getDeletedDecks(@RequestParam Long boardId) {
		return null;
	}

}
