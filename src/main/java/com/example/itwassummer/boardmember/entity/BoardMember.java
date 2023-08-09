package com.example.itwassummer.boardmember.entity;

import com.example.itwassummer.board.entity.Board;
import com.example.itwassummer.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "board")
public class BoardMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardMember(User user, Board board) {
        this.user = user;
        this.board = board;
        user.getBoardMembers().add(this);
        // board.getBoardMembers().add(this);
    }

    public void deleteBoardMember(User user, Board board) {
        user.getBoardMembers().remove(this);
//        board.getBoardMembers().remove(this);
    }
}