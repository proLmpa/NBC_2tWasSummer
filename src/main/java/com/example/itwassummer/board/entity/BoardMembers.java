package com.example.itwassummer.board.entity;

import com.example.itwassummer.user.entity.User;
import jakarta.persistence.*;

@Entity
public class BoardMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardmembers_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
