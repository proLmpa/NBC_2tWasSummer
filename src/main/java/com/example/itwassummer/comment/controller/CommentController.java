package com.example.itwassummer.comment.controller;

import com.example.itwassummer.comment.dto.CommentCreateRequestDto;
import com.example.itwassummer.comment.dto.CommentEditRequestDto;
import com.example.itwassummer.comment.dto.CommentResponseDto;
import com.example.itwassummer.comment.service.CommentService;
import com.example.itwassummer.common.dto.ApiResponseDto;
import com.example.itwassummer.common.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Comment API", description = "카드 내에서 토론 할 수 있는 코멘트 기능과 관련된 API 정보를 담고 있습니다.")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 목록 조회", description = "id 값을 통해 조회")
    @GetMapping("/cards/comments/{cardId}")
    @ResponseBody
    public ResponseEntity commentList(
        @PathVariable("cardId") Long cardId,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sortBy") String sortBy,
        @RequestParam("isAsc") boolean isAsc
    ) {
        List<CommentResponseDto> commentList = commentService.getCommentList(cardId,
            page - 1, size, sortBy, isAsc);

        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @Operation(summary = "코멘트 등록", description = "토큰에서 유저정보를 가져오고, CommentCreateRequestDto를 통해 코멘트 정보를 받아 코멘트 생성 후 cardId를 통해 해당 card를 찾아 코멘트를 등록합니다.")
    @PostMapping("/comments")
    public ResponseEntity<ApiResponseDto> createComment(
            @RequestBody CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        String result = commentService.createComment(requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), result));
    }

    @Operation(summary = "코멘트 수정", description = "토큰에서 유저정보를 가져와 작성자 확인을 거친 뒤, CommentEditRequestDto를 통해 코멘트 수정합니다.")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> editComment(
            @PathVariable Long commentId,
            @RequestBody CommentEditRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String result = commentService.editComment(commentId, requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), result));
    }

    @Operation(summary = "코멘트 삭제", description = "토큰에서 유저정보를 가져와 작성자 확인을 거친 뒤, 코멘트를 삭제합니다.")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String result = commentService.deleteComment(commentId, userDetails.getUser());

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), result));
    }

}
