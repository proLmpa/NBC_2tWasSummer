package com.example.itwassummer.userpassword.repository;

import com.example.itwassummer.userpassword.entity.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPasswordRepository extends JpaRepository<UserPassword, Long> {
    @Query("select up from UserPassword up where up.user.id = :id order by up.createdAt desc limit 3")
    List<UserPassword> get3RecentPasswords(@Param("id") Long id);
}
