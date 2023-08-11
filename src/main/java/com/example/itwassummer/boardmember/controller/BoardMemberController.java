package com.example.itwassummer.boardmember.controller;

import com.example.itwassummer.boardmember.service.BoardMemberService;
import com.example.itwassummer.common.dto.ApiResponseDto;
import com.example.itwassummer.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "BoardMember API", description = "사용자를 보드에 초대, 보드에서 삭제하는 기능과 관련된 API 정보를 담고 있습니다.")
public class BoardMemberController {
    private final BoardMemberService boardMemberService;

    public BoardMemberController(BoardMemberService boardMemberService) {
        this.boardMemberService = boardMemberService;
    }

    @Operation(summary = "보드에 사용자 초대", description = "전달된 Bearer 토큰을 통해 보드 작성자 여부 확인 및 보드와 초대할 사람의 존재 유무 확인 후 전부 충족 시 대상 사용자를 보드에 추가합니다.")
    @PostMapping("/boards/{boardId}/invite")
    public ResponseEntity<ApiResponseDto> inviteBoardMember(@PathVariable Long boardId, @RequestParam("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardMemberService.inviteBoardMember(boardId, userId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "새로운 사용자를 보드에 등록했습니다."));
    }

    @Operation(summary = "보드에서 사용자 삭제", description = "전달된 Bearer 토큰을 통해 보드 작성자 여부 확인 및 보드와 삭제할 사람의 존재 유무 확인 후 전부 충족 시 대상 사용자를 보드에서 제거합니다.")
    @DeleteMapping("/boards/{boardId}/invite")
    public ResponseEntity<ApiResponseDto> deleteBoardMember(@PathVariable Long boardId, @RequestParam("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardMemberService.deleteBoardMember(boardId, userId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), "기존 사용자를 보드에서 삭제했습니다."));
    }
}
