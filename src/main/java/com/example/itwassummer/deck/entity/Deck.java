package com.example.itwassummer.deck.entity;

import com.example.itwassummer.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "deck")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
