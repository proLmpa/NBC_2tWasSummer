package com.example.itwassummer.cardmember.entity;

import com.example.itwassummer.card.entity.Card;
import com.example.itwassummer.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

// 카드별 담당자
@Entity
@NoArgsConstructor
public class CardMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private Card card;

  public CardMember(Card card, User user) {
    this.card = card;
    this.user = user;
  }
}
