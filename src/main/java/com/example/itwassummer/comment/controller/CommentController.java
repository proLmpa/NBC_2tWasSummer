package com.example.itwassummer.comment.controller;

import com.example.itwassummer.comment.dto.CommentCreateRequestDto;
import com.example.itwassummer.comment.dto.CommentEditRequestDto;
import com.example.itwassummer.comment.service.CommentService;
import com.example.itwassummer.common.dto.ApiResponseDto;
import com.example.itwassummer.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/comments/{cardId}")
    public ResponseEntity<ApiResponseDto> createComment(
            @PathVariable Long cardId,
            @ModelAttribute CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        String result = commentService.createComment(cardId, requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), result));
        //issue ResponseEntity 반환을 어떻게 하는 게 베스트일지?
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> editComment(
            @PathVariable Long commentId,
            @ModelAttribute CommentEditRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String result = commentService.editComment(commentId, requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), result));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String result = commentService.deleteComment(commentId, userDetails.getUser());

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(), result));
    }

}
