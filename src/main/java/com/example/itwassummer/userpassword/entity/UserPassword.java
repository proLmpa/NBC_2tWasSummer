package com.example.itwassummer.userpassword.entity;

import com.example.itwassummer.common.entity.Timestamped;
import com.example.itwassummer.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_password")
public class UserPassword extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserPassword(String password, User user) {
        this.password = password;
        this.user = user;
    }
}