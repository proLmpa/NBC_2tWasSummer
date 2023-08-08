package com.example.itwassummer.cardnotifications.entity;

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

// 카드별 알림
@Entity
@NoArgsConstructor
public class CardNotifications {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  boolean isRead;

  boolean isWatched;

  @ManyToOne(fetch = FetchType.LAZY)
  private User users;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private Card cards;

  public CardNotifications(Card cards, User users) {
    this.cards = cards;
    this.users = users;
  }

}
