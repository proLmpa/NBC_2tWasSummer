package com.example.itwassummer.label.repository;

import com.example.itwassummer.label.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label, Long> {
    List<Label> findAllByBoard_Id(Long id);
}
