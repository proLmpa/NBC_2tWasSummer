package com.example.itwassummer.card.controller;


import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.service.CardService;
import com.example.itwassummer.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "카드 API", description = "카드 기능과 관련된 API 정보를 담고 있습니다.")
public class CardController {

  public CardController (CardService cardService) {
    this.cardService = cardService;
  }
  private final CardService cardService;

  @Operation(summary = "카드 등록", description = "CardRequestDto를 통해 카드정보를 전달 받은 후 DB에 저장하고 성공 메시지를 반환합니다.")
  @PostMapping("/cards")
  public ResponseEntity addCards(@Valid @RequestBody CardRequestDto requestDto) throws IllegalAccessException {
    CardResponseDto returnDto = cardService.save(requestDto);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

  @Operation(summary = "카드 수정", description = "CardRequestDto를 통해 카드정보를 전달 받은 후 DB에 저장하고 성공 메시지를 반환합니다.")
  @PutMapping("/cards/{cardId}")
  public ResponseEntity updateCards(@PathVariable("cardId") Long cardId, @Valid @RequestBody CardRequestDto requestDto) throws IllegalAccessException {
    CardResponseDto returnDto = cardService.update(cardId, requestDto);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

  @Operation(summary = "카드 삭제", description = "id 값을 통해 삭제")
  @DeleteMapping("/cards/{cardId}")
  public ResponseEntity<ApiResponseDto> deleteCards(@PathVariable("cardId") Long cardId) throws IllegalAccessException {
    cardService.delete(cardId);
    return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "삭제 성공"));
  }


  /**
   * 댓글 목록 조회
   * */
  @GetMapping("/cards/{cardId}/comments")
  @ResponseBody
  public ResponseEntity list(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc
  ) {
    return null;
  }


}
