package com.example.itwassummer.comment.repository;

import com.example.itwassummer.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
