package com.example.itwassummer.label.controller;

import com.example.itwassummer.common.dto.ApiResponseDto;
import com.example.itwassummer.label.dto.LabelRequestDto;
import com.example.itwassummer.label.dto.LabelResponseDto;
import com.example.itwassummer.label.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "라벨 API", description = "카드에 태그형태로 붙는 라벨 API 정보들을 담고 있습니다.")
public class LabelController {

    private final LabelService labelService;

    @PostMapping("/labels")
    @Operation(summary = "새로운 라벨 생성", description = "라벨의 이름과 색상 정보를 받아 새로운 라벨을 생성합니다.")
    public ResponseEntity<LabelResponseDto> createLabel(@RequestBody LabelRequestDto requestDto, @RequestParam("boardId") Long boardId) {
        LabelResponseDto responseDto = labelService.createLabel(requestDto, boardId);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/labels")
    @Operation(summary = "라벨 리스트 조회", description = "라벨의 이름과 색상 정보를 받아 새로운 라벨을 생성합니다.")
    public ResponseEntity<List<LabelResponseDto>> getLabels(@RequestParam Long boardId) {
        List<LabelResponseDto> labelResponseDtos = labelService.getLabels(boardId);
        return ResponseEntity.ok().body(labelResponseDtos);
    }

    @PutMapping("/labels/{labelId}")
    @Operation(summary = "라벨 정보 수정", description = "보드 내에 생성되어 있는 라벨의 정보를 변경합니다.")
    public ResponseEntity<LabelResponseDto> editLabel(@PathVariable Long labelId, @RequestBody LabelRequestDto requestDto) {
        LabelResponseDto responseDto = labelService.editLabel(labelId, requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/labels/{labelId}")
    @Operation(summary = "라벨 삭제", description = "보드 내에 생성되어 있는 라벨을 삭제합니다.")
    public ResponseEntity<ApiResponseDto> deleteLabel(@PathVariable Long labelId) {
        labelService.deleteLabel(labelId);
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "라벨 삭제 완료."));
    }

}
