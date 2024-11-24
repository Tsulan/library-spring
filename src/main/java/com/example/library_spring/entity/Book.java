package com.example.library_spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN must contain only numbers and dashes")
    @Column(unique = true)
    private String isbn;

    @Min(value = 0, message = "Available copies must be at least 0")
    @Max(value = 1000, message = "Available copies cannot exceed 1000")
    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    @Min(value = 1, message = "Total copies must be at least 1")
    @Max(value = 1000, message = "Total copies cannot exceed 1000")
    @Column(name = "total_copies", nullable = false)
    private int totalCopies;
}
