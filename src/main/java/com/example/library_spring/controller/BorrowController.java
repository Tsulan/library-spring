package com.example.library_spring.controller;

import com.example.library_spring.entity.BorrowedBook;
import com.example.library_spring.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/{bookId}/user/{userId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long bookId, @PathVariable Long userId) {
        borrowService.borrowBook(bookId, userId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @PostMapping("/return/{bookId}/user/{userId}")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId, @PathVariable Long userId) {
        borrowService.returnBook(bookId, userId);
        return ResponseEntity.ok("Book returned successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowedBook>> getBorrowedBooksByUser(@PathVariable Long userId) {
        List<BorrowedBook> borrowedBooks = borrowService.getBorrowedBooksByUser(userId);
        return ResponseEntity.ok(borrowedBooks);
    }

    @GetMapping("/active")
    public ResponseEntity<List<BorrowedBook>> getAllActiveBorrowedBooks() {
        List<BorrowedBook> activeBorrowedBooks = borrowService.getAllActiveBorrowedBooks();
        return ResponseEntity.ok(activeBorrowedBooks);
    }
}
