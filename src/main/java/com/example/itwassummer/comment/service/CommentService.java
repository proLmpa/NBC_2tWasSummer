package com.example.itwassummer.comment.service;

import com.example.itwassummer.comment.dto.CommentCreateRequestDto;
import com.example.itwassummer.comment.dto.CommentEditRequestDto;
import com.example.itwassummer.comment.dto.CommentResponseDto;
import com.example.itwassummer.user.entity.User;
import java.util.List;


public interface CommentService {

    /**
     * 카드별 댓글 목록 조회
     *
     * @param cardId 카드 데이터가 있는지 조회
     * @param page   페이지
     * @param size   페이지별 사이즈
     * @param sortBy 정렬순서
     * @param isAsc  정렬기준 (오름차순, 내림차순)
     * @return List<CommentResponseDto>
     */
    List<CommentResponseDto> getCommentList(Long cardId, int page, int size, String sortBy,
        boolean isAsc);

    /**
     * 댓글 등록
     * @param requestDto 요청정보
     * @param user 사용자정보
     * @return String 댓글 등록 정보
     */
    String createComment(CommentCreateRequestDto requestDto, User user);

    /**
     * 댓글 수정
     * @param commentId 댓글 id
     * @param requestDto 요청정보
     * @param user 사용자정보
     * @return String 댓글 수정 정보
     */
    String editComment(Long commentId, CommentEditRequestDto requestDto, User user);

    /**
     * 댓글 삭제
     * @param commentId 댓글 id
     * @param user 사용자정보
     * @return String 삭제정보
     */
    String deleteComment(Long commentId, User user);
}
