package com.example.itwassummer.comment.service;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.card.repository.CardRepository;
import com.example.itwassummer.comment.dto.CommentCreateRequestDto;
import com.example.itwassummer.comment.dto.CommentEditRequestDto;
import com.example.itwassummer.comment.entity.Comment;
import com.example.itwassummer.comment.repository.CommentRepository;
import com.example.itwassummer.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service //issue @Service 어노테이션을 여기에?
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    @Override
    public String createComment(Long cardId, CommentCreateRequestDto requestDto, User user) {

        Comment comment = new Comment(requestDto);
        Card card = cardRepository.findById(cardId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 카드입니다."));
        comment.addCard(card);
        comment.addUser(user);
        commentRepository.save(comment);

        return "코멘트 생성 완료";
    }


    @Override
    @Transactional
    public String editComment(Long commentId, CommentEditRequestDto requestDto, User user) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 코멘트입니다."));

        checkUser(comment.getUser(), user);
        comment.editComment(requestDto);

        return "코멘트 수정 완료";
    }

    @Override
    public String deleteComment(Long commentId, User user) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 코멘트입니다."));

        checkUser(comment.getUser(), user);
        commentRepository.delete(comment);

        return "코멘트 삭제 완료";
    }

    private void checkUser(User user1, User user2) {
        if (!user1.equals(user2)) {
            throw new IllegalArgumentException("본인의 코멘트만 삭제할 수 있습니다.");
        }
    }
}
