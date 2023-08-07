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
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER) // 덱은 항상 보드와 같이 조회되므로
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToOne(fetch = FetchType.EAGER) // 덱들은 항상 같이 조회되므로
    @JoinColumn(name="parent_id")
    private Deck deck;

    private Boolean is_deleted;

}
