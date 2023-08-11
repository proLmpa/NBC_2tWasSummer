package com.example.itwassummer.comment.service;

import com.example.itwassummer.comment.dto.CommentCreateRequestDto;
import com.example.itwassummer.comment.dto.CommentEditRequestDto;
import com.example.itwassummer.user.entity.User;


public interface CommentService {

    /**
     * 카드 상세 조회
     * @param requestDto 요청정보
     * @return CardViewResponseDto 카드 상세 정보
     */
    String createComment(CommentCreateRequestDto requestDto, User user);

    /**
     * 카드 상세 조회
     * @param commentId 댓글 id
     * @return CardViewResponseDto 카드 상세 정보
     */
    String editComment(Long commentId, CommentEditRequestDto requestDto, User user);

    /**
     * 카드 상세 조회
     * @param commentId 댓글 id
     * @return CardViewResponseDto 카드 상세 정보
     */
    String deleteComment(Long commentId, User user);
}
