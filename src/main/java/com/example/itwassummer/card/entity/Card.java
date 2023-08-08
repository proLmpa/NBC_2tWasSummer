package com.example.itwassummer.card.entity;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.comment.entity.Comment;
import com.example.itwassummer.deck.entity.Deck;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "card")
public class Card {

    ////칼럼들

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dueDate;

    @Column(length = 100)
    private String description;

    @Column(length = 10, nullable = false)
    private Long parentId;


    ////연관관계

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

   /*
   @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    Deck 엔티티에 추가
    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    */


    ////생성자

    @Builder
    public Card(String name, LocalDateTime dueDate, String description, Long parentId){
        this.name = name;
        this.dueDate = dueDate;
        this.description = description;
        this.parentId = parentId;
    }

    public void update(CardRequestDto requestDto) {
        this.name = requestDto.getName();
        this.dueDate = requestDto.getDueDate();
        this.description = requestDto.getDescription();
    }

}