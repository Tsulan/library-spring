package com.example.library_spring.service;

import com.example.library_spring.entity.AppUser;
import com.example.library_spring.entity.Book;
import com.example.library_spring.entity.BorrowedBook;
import com.example.library_spring.repository.AppUserRepository;
import com.example.library_spring.repository.BookRepository;
import com.example.library_spring.repository.BorrowedBookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final AppUserRepository appUserRepository;
    private final BookRepository bookRepository;
    private final AppUserService appUserService;

    public BorrowService(BorrowedBookRepository borrowedBookRepository, AppUserRepository appUserRepository, BookRepository bookRepository, AppUserService appUserService) {
        this.borrowedBookRepository = borrowedBookRepository;
        this.appUserRepository = appUserRepository;
        this.bookRepository = bookRepository;
        this.appUserService = appUserService;
    }

    public void borrowBook(Long bookId, Long userId) {
        // Find user
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Find book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // Check available copies
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException("No available copies of this book: " + book.getTitle());
        }

        // Create a record of borrowed book
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setAppUser(user);
        borrowedBook.setBook(book);
        borrowedBook.setBorrowDate(LocalDate.now());

        borrowedBookRepository.save(borrowedBook);

        // Reduce the number of available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
    }

    public void returnBook(Long bookId, Long userId) {
        // Find the record of borrowed book
        BorrowedBook borrowedBook = borrowedBookRepository.findByAppUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow record not found"));

        // Increase the number of available copies
        Book book = borrowedBook.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        // Mark the date of the book's return
        borrowedBook.setReturnDate(LocalDate.now());
        borrowedBookRepository.save(borrowedBook);
    }

    public List<BorrowedBook> getBorrowedBooksByUser(Long userId) {
        return borrowedBookRepository.findByAppUserId(userId);
    }

    public List<BorrowedBook> getActiveBorrowedBooksByUser(Long userId) {
        AppUser appUser = appUserService.getUserById(userId);
        return borrowedBookRepository.findByReturnDateIsNullAndAppUser(appUser);
    }

    public List<BorrowedBook> getAllActiveBorrowedBooks() {
        return borrowedBookRepository.findByReturnDateIsNull();
    }
}
