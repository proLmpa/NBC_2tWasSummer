package com.example.itwassummer.comment.dto;

import com.example.itwassummer.comment.entity.Comment;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private Long id;
//    private String nickname;
    private String content;


    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
//        this.nickname = comment.getUser().getnickname(); todo User에 nickname 추가 여부 이후 수정
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
