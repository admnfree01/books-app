package com.base.booksapp.model;

import com.base.booksapp.AuthProvider;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String name;


    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider; // LOCAL or GOOGLE

    private String googleId; // Optional for Google Auth
}
