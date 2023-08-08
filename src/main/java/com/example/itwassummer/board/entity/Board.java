package com.example.itwassummer.board.entity;

import com.example.itwassummer.board.dto.BoardRequestDto;
import com.example.itwassummer.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String board_name;

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

    public Board(BoardRequestDto requestDto, User user) {
        this.board_name = requestDto.getBoard_name();
        this.description = requestDto.getDescription();
        this.color = requestDto.getColor();
        this.user = user;
    }
}