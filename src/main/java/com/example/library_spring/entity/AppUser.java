package com.example.library_spring.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "AppUser")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
