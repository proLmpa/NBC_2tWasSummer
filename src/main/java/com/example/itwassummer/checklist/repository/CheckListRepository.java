package com.example.itwassummer.checklist.repository;

import com.example.itwassummer.checklist.entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {

}
