package com.example.itwassummer.card.entity;

import com.example.itwassummer.card.dto.CardRequestDto;
import com.example.itwassummer.deck.entity.Deck;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "card")
public class Card {
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

   /*
   @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    Deck 엔티티에 추가
    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    */

    @Column(length = 10, nullable = false)
    private Long parentId;

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