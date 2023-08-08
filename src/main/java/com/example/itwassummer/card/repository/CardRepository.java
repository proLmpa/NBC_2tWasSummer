package com.example.itwassummer.card.repository;

import com.example.itwassummer.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

}
