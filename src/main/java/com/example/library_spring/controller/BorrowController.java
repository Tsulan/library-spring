package com.example.library_spring.controller;

import com.example.library_spring.entity.AppUser;
import com.example.library_spring.entity.Book;
import com.example.library_spring.entity.BorrowedBook;
import com.example.library_spring.repository.AppUserRepository;
import com.example.library_spring.service.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;
    private final AppUserRepository appUserRepository;

    public BorrowController(BorrowService borrowService, AppUserRepository appUserRepository) {
        this.borrowService = borrowService;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long bookId, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        borrowService.borrowBook(bookId, userId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @PostMapping("/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId, Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        borrowService.returnBook(bookId, userId);
        return ResponseEntity.ok("Book returned successfully");
    }

    @GetMapping("/borrowed")
    public ResponseEntity<List<BorrowedBook>> getBorrowedBooks(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<BorrowedBook> borrowedBooks = borrowService.getActiveBorrowedBooksByUser(userId);
        return ResponseEntity.ok(borrowedBooks);
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

    public Long getUserIdFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        AppUser currentUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        return currentUser.getId();
    }
}
