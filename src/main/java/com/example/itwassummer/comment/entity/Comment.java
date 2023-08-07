package com.example.itwassummer.comment.entity;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.comment.dto.CommentCreateRequestDto;
import com.example.itwassummer.comment.dto.CommentEditRequestDto;
import com.example.itwassummer.common.entity.Timestamped;
import com.example.itwassummer.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor //issue 꼭 써야하는 이유는?
@Table(name = "comment")
public class Comment extends Timestamped {

    ////컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content")
    private String content;



    ////생성자 - 약속된 형태로만 생성가능하도록 합니다.
    public Comment(CommentCreateRequestDto requestDto) {
        this.content = requestDto.getContent();
    }


    ////연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;


    ////연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.

    public void addUser(User user) {
        this.user = user;
    }

    public void addCard(Card card) {
        this.card = card;
//        card.comments.add(this); todo card 이후
    }

        //// 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)

    public void editComment(CommentEditRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
