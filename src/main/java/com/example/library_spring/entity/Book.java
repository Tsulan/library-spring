package com.example.library_spring.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true)
    private String isbn;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    @Column(name = "total_copies", nullable = false)
    private int totalCopies;
}
