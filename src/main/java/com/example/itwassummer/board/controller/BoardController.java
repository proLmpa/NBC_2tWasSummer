package com.example.itwassummer.board.controller;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.board.dto.BoardResponseDto;
import com.example.itwassummer.board.service.BoardServiceImpl;
import com.example.itwassummer.common.dto.ApiResponseDto;
import com.example.itwassummer.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "보드 API", description = "보드 기능과 관련된 API 정보를 담고 있습니다.")
public class BoardController {
    private final BoardServiceImpl boardService;

    @Operation(summary = "보드 조회", description = "현재 선택한 보드의 내부를 조회합니다.")
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponseDto> showBoards(@PathVariable("boardId") Long boardId) {
        BoardResponseDto responseDto = boardService.showBoards(boardId);
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "전체 보드 조회", description = "전체 보드의 목록을 조회합니다.")
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> showAllBoards() {
        List<BoardResponseDto> responseDto = boardService.showAllBoards();
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "보드 생성", description = "새로운 보드를 생성합니다.")
    @PostMapping("/boards")
    public ResponseEntity<BoardResponseDto> createBoards(@Valid @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto responseDto = boardService.createBoards(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "보드 수정", description = "배경 색상 / 보드 이름 / 설명을 수정합니다.")
    @PutMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoards(@PathVariable("boardId") Long id, @Valid @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto responseDto = boardService.update(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "보드 삭제", description = "보드를 생성한 사용자만 삭제를 할 수 있습니다.")
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<ApiResponseDto> deleteBoards(@PathVariable("boardId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.delete(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "보드 삭제 성공"));
    }
}
