package com.example.itwassummer.cardlabel.repository;

import com.example.itwassummer.cardlabel.entity.CardLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardLabelRepository extends JpaRepository<CardLabel, Long> {
    Optional<CardLabel> findByCard_IdAndLabel_Id(Long id, Long id1);
}
