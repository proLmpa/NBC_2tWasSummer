package com.example.itwassummer.cardlabel.controller;

import com.example.itwassummer.cardlabel.service.CardLabelService;
import com.example.itwassummer.common.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "CardLabel API", description = "카드에 라벨을 지정해주고 지정 해제해주는 API입니다. (라벨 생성 X)")
public class CardLabelController {
    private final CardLabelService cardLabelService;

    @PostMapping("/cards/labels")
    @Operation(summary = "카드에 라벨 등록", description = "생성되어있는 라벨을 해당 카드에 등록해준다.")
    public ResponseEntity<ApiResponseDto> createCardLabel(
            @RequestParam("cardId") Long cardId, @RequestParam("labelId") Long labelId) {

        String result = cardLabelService.createCardLabel(cardId, labelId);

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), result));
    }

    @DeleteMapping("/cards/labels")
    @Operation(summary = "카드에 라벨 삭제", description = "카드에 지정되어 있는 라벨을 해제해준다. DB상에서는 삭제")
    public ResponseEntity<ApiResponseDto> deleteCardLabel(
            @RequestParam("cardId") Long cardId, @RequestParam("labelId") Long labelId) {

        String result = cardLabelService.deleteCardLabel(cardId, labelId);

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), result));
    }
}
