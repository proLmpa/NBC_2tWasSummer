package com.example.itwassummer.comment.service;

import com.example.itwassummer.comment.dto.CommentCreateRequestDto;
import com.example.itwassummer.comment.dto.CommentEditRequestDto;
import com.example.itwassummer.comment.dto.CommentResponseDto;
import com.example.itwassummer.common.security.UserDetailsImpl;
import com.example.itwassummer.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service //issue @Service 어노테이션을 여기에? 혹은 Impl에?
public interface CommentService {

    String createComment(Long cardId, CommentCreateRequestDto requestDto, User user);

    String editComment(Long commentId, CommentEditRequestDto requestDto, User user);

    String deleteComment(Long commentId, User user);
}
