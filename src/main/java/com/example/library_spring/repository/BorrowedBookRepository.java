package com.example.library_spring.repository;

import com.example.library_spring.entity.BorrowedBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    Optional<BorrowedBook> findByAppUserIdAndBookId(Long userId, Long bookId);

    List<BorrowedBook> findByAppUserId(Long userId);

    List<BorrowedBook> findByReturnDateIsNull();
}
