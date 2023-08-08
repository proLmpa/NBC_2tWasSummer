package com.example.itwassummer.board.controller;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.board.dto.BoardResponseDto;
import com.example.itwassummer.board.service.BoardServiceImpl;
import com.example.itwassummer.common.dto.ApiResponseDto;
import com.example.itwassummer.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "보드 API", description = "보드 기능과 관련된 API 정보를 담고 있습니다.")
public class BoardController {
    private final BoardServiceImpl boardService;

    @Operation(summary = "보드 조회", description = "현재 선택한 보드의 내부를 조회합니다.")
    @GetMapping("/{boardId}")
    public ResponseEntity showBoards(@PathVariable("boardId") Long boardId) {
        BoardResponseDto returnDto = boardService.showBoards(boardId);
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @Operation(summary = "전체 보드 조회", description = "전체 보드의 목록을 조회합니다.")
    @GetMapping("/")
    public ResponseEntity showAllBoards() {
        List<BoardResponseDto> returnDto = boardService.showAllBoards();
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @Operation(summary = "보드 생성", description = "새로운 보드를 생성합니다.")
    @PostMapping("/")
    public ResponseEntity createBoards(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.createBoards(requestDto,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "보드 생성 성공"));
    }

    @Operation(summary = "보드 수정", description = "배경 색상 / 보드 이름 / 설명을 수정합니다.")
    @PutMapping("/{boardId}")
    public ResponseEntity updateBoards(@PathVariable("boardId") Long id, @RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        BoardResponseDto returnDto = boardService.update(id, requestDto,userDetails.getUser());
        return new ResponseEntity<>(returnDto, HttpStatus.OK);
    }

    @Operation(summary = "보드 삭제", description = "보드를 생성한 사용자만 삭제를 할 수 있습니다.")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponseDto> deleteBoards(@PathVariable("boardId") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.delete(id,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "보드 삭제 성공"));
    }

    // 보드 초대
    // 특정 사용자들을 해당 보드에 초대시켜 협업을 할 수 있어야 합니다.
}
