package com.example.itwassummer.board.entity;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.boardmember.entity.BoardMember;
import com.example.itwassummer.deck.entity.Deck;
import com.example.itwassummer.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    // 보드 이름
    @Column(nullable = false)
    private String name;

    // 설명
    @Column
    private String description;

    // 색상 코드 문자열 형태로 저장
    @Column
    private String color;

    // 보드를 만든 사용자, 연관 관계
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 보드 내부에 들어갈 Deck의 값, 연관 관계
    @OneToMany(mappedBy = "board")
    private List<Deck> deckList = new ArrayList<>();

    // 보드 사용자 목록
    @OneToMany(mappedBy = "board")
    private List<BoardMember> boardMembers = new ArrayList<>();

    public Board(BoardRequestDto requestDto, User user) {
        this.name = requestDto.getName();
        this.description = requestDto.getDescription();
        this.color = requestDto.getColor();
        this.user = user;
    }

    public void updateBoard(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }
}