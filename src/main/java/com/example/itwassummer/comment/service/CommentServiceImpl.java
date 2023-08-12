package com.example.itwassummer.comment.service;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.comment.dto.CommentCreateRequestDto;
import com.example.itwassummer.comment.dto.CommentEditRequestDto;
import com.example.itwassummer.comment.dto.CommentResponseDto;
import com.example.itwassummer.comment.entity.Comment;
import com.example.itwassummer.comment.repository.CommentRepository;
import com.example.itwassummer.common.error.CustomErrorCode;
import com.example.itwassummer.common.exception.CustomException;
import com.example.itwassummer.user.entity.User;
import com.example.itwassummer.user.entity.UserRoleEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentList(Long cardId, int page, int size, String sortBy,
        boolean isAsc) {
        Card card = findCard(cardId);
        Direction direction = isAsc ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        List<CommentResponseDto> commentList = commentRepository.findAllByCard(card, pageable).stream()
            .map(CommentResponseDto::new).toList();
        return commentList;
    }

    @Override
    @Transactional
    public String createComment(CommentCreateRequestDto requestDto, User user) {

        Comment comment = new Comment(requestDto);
        Card card = findCard(requestDto.getCardId());
        comment.addCard(card);
        comment.addUser(user);
        commentRepository.save(comment);

        return "코멘트 생성 완료";
    }


    @Override
    @Transactional
    public String editComment(Long commentId, CommentEditRequestDto requestDto, User user) {

        Comment comment = findComment(commentId);

        checkUser(comment.getUser(), user);
        comment.editComment(requestDto);

        return "코멘트 수정 완료";
    }

    @Override
    @Transactional
    public String deleteComment(Long commentId, User user) {

        Comment comment = findComment(commentId);

        checkUser(comment.getUser(), user);
        commentRepository.delete(comment);

        return "코멘트 삭제 완료";
    }

    // 사용자 정보 확인
    private void checkUser(User commentUser, User loginUser) {
        if (!commentUser.getId().equals(loginUser.getId())
            && !loginUser.getRole().equals(UserRoleEnum.ADMIN))
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()
                -> new CustomException(CustomErrorCode.COMMENT_NOT_FOUND, null));
    }

    private Card findCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(()
                -> new CustomException(CustomErrorCode.CARD_NOT_FOUND, null));
    }


}
