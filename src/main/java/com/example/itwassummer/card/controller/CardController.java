package com.example.itwassummer.card.controller;


import com.example.itwassummer.card.dto.CardListResponseDto;
import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.card.dto.CardResponseDto;
import com.example.itwassummer.card.dto.CardSearchResponseDto;
import com.example.itwassummer.card.dto.CardViewResponseDto;
import com.example.itwassummer.card.service.CardService;
import com.example.itwassummer.cardmember.dto.CardMemberResponseDto;
import com.example.itwassummer.comment.dto.CommentResponseDto;
import com.example.itwassummer.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@Tag(name = "카드 API", description = "카드 기능과 관련된 API 정보를 담고 있습니다.")
public class CardController {

  public CardController(CardService cardService) {
    this.cardService = cardService;
  }

  private final CardService cardService;

  @Operation(summary = "카드 전체조회", description = "카드 id를 넘겨 받아 카드의 상세 정보를 표시")
  @GetMapping(value = "/cardLists")
  @ResponseBody
  public ResponseEntity list(@RequestParam Long boardId,
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc) {
    List<CardListResponseDto> responseDto = cardService.getCardList(boardId,
        page - 1, size, sortBy, isAsc);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }


  @Operation(summary = "카드 상세조회", description = "카드 id를 넘겨 받아 카드의 상세 정보를 표시")
  @GetMapping(value = "/cards/{cardId}")
  @ResponseBody
  public ResponseEntity view(@PathVariable("cardId") Long cardId) {
    CardViewResponseDto responseDto = cardService.getCard(cardId);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @Operation(summary = "카드 등록", description = "CardRequestDto를 통해 카드정보를 전달 받은 후 DB에 저장하고 성공 메시지를 반환합니다.")
  @PostMapping(value = "/cards", consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity addCard(@Valid @RequestPart CardRequestDto requestDto,
      @RequestPart(required = false) List<MultipartFile> files
  ) throws IOException {
    CardResponseDto returnDto = cardService.save(requestDto, files);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

  @Operation(summary = "카드 수정", description = "CardRequestDto를 통해 카드정보를 전달 받은 후 DB에 저장하고 성공 메시지를 반환합니다.")
  @PutMapping(value = "/cards/{cardId}", consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity updateCard(@PathVariable("cardId") Long cardId,
      @Valid @RequestPart CardRequestDto requestDto,
      @RequestPart(required = false) List<MultipartFile> files
  ) throws IOException {
    CardResponseDto returnDto = cardService.update(cardId, requestDto, files);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

  @Operation(summary = "카드 삭제", description = "id 값을 통해 삭제")
  @DeleteMapping("/cards/{cardId}")
  public ResponseEntity<ApiResponseDto> deleteCard(@PathVariable("cardId") Long cardId) {
    cardService.delete(cardId);
    return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "삭제 성공"));
  }


  @Operation(summary = "카드 마감일수정", description = "마감일 수정")
  @PatchMapping("/cards/{cardId}/date")
  public ResponseEntity changeCardDueDate(
      @PathVariable("cardId") Long cardId,
      @RequestParam String dueDate
  )
      throws ParseException {

    SimpleDateFormat dateFormatParser = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss"); //검증할 날짜 포맷 설정
    dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
    dateFormatParser.parse(dueDate); //대상 값 포맷에 적용되는지 확인

    CardResponseDto responseDto = cardService.changeDueDate(cardId, dueDate);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @Operation(summary = "카드별 사용자 변경", description = "카드별 사용자 일괄 삭제 후 재등록")
  @PatchMapping("/cards/{cardId}/members")
  public ResponseEntity changeCardMember(
      @PathVariable("cardId") Long cardId,
      @RequestParam String emailList
  )
      throws IllegalAccessException {
    List<CardMemberResponseDto> result = cardService.changeCardMembers(cardId, emailList);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @Operation(summary = "카드 정렬 순서 바꾸기", description = "카드 정렬 순서 바꾸기")
  @PatchMapping("/cards/{cardId}/move")
  public ResponseEntity moveCard(
      @PathVariable("cardId") Long cardId,
      @RequestParam Long order
  )
      throws IllegalAccessException {
    CardResponseDto result = cardService.moveCard(cardId, order);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @Operation(summary = "라벨 필터검색", description = "id 값을 통해 조회")
  @GetMapping("/cards/labels/{labelId}")
  @ResponseBody
  public ResponseEntity searchLabelList(
      @PathVariable("labelId") Long labelId,
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc
  ) {
    List<CardSearchResponseDto> cardList = cardService.searchLabelList(labelId,
        page - 1, size, sortBy, isAsc);

    return new ResponseEntity<>(cardList, HttpStatus.OK);
  }

  @Operation(summary = "카드 위치 수정", description = "덱 id와 카드 id, 정렬순서를 읽어 다른 덱으로 이동")
  @PutMapping(value = "/decks/{deckId}/cards/{cardId}")
  public ResponseEntity moveCardToOtherDeck(@PathVariable("deckId") Long deckId,
      @PathVariable("cardId") Long cardId,
      @RequestParam("order") Long order
  ) {
    CardResponseDto returnDto = cardService.moveCardToOtherDeck(deckId, cardId, order);
    return new ResponseEntity<>(returnDto, HttpStatus.OK);
  }

}
