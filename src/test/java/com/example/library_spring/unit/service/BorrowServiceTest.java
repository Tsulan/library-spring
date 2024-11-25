package com.example.library_spring.unit.service;

import com.example.library_spring.entity.AppUser;
import com.example.library_spring.entity.Book;
import com.example.library_spring.entity.BorrowedBook;
import com.example.library_spring.repository.AppUserRepository;
import com.example.library_spring.repository.BookRepository;
import com.example.library_spring.repository.BorrowedBookRepository;
import com.example.library_spring.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BorrowServiceTest {

    @Mock
    private BorrowedBookRepository borrowedBookRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BorrowService borrowService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void borrowBook_ShouldSucceed_WhenBookAndUserExistAndCopiesAvailable() {
        Long bookId = 1L;
        Long userId = 1L;

        AppUser mockUser = new AppUser();
        mockUser.setId(userId);
        mockUser.setName("John Doe");

        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setTitle("Effective Java");
        mockBook.setAvailableCopies(3);
        mockBook.setTotalCopies(5);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        borrowService.borrowBook(bookId, userId);

        verify(borrowedBookRepository).save(any(BorrowedBook.class));
        verify(bookRepository).save(argThat(book -> book.getAvailableCopies() == 2));
    }

    @Test
    public void borrowBook_ShouldThrowException_WhenUserNotFound() {
        Long bookId = 1L;
        Long userId = 1L;

        when(appUserRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> borrowService.borrowBook(bookId, userId));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void borrowBook_ShouldThrowException_WhenBookNotFound() {
        Long bookId = 1L;
        Long userId = 1L;

        AppUser mockUser = new AppUser();
        mockUser.setId(userId);
        mockUser.setName("John Doe");

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> borrowService.borrowBook(bookId, userId));

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    public void borrowBook_ShouldThrowException_WhenNoAvailableCopies() {
        Long bookId = 1L;
        Long userId = 1L;

        AppUser mockUser = new AppUser();
        mockUser.setId(userId);
        mockUser.setName("John Doe");

        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setTitle("Effective Java");
        mockBook.setAvailableCopies(0);
        mockBook.setTotalCopies(5);

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> borrowService.borrowBook(bookId, userId));

        assertEquals("No available copies of this book: Effective Java", exception.getMessage());
    }

    @Test
    public void returnBook_ShouldSucceed_WhenBorrowRecordExists() {
        Long bookId = 1L;
        Long userId = 1L;

        Book mockBook = new Book();
        mockBook.setId(bookId);
        mockBook.setTitle("Effective Java");
        mockBook.setAvailableCopies(2);
        mockBook.setTotalCopies(5);

        BorrowedBook mockBorrowedBook = new BorrowedBook();
        mockBorrowedBook.setBook(mockBook);
        mockBorrowedBook.setBorrowDate(LocalDate.now());

        when(borrowedBookRepository.findByAppUserIdAndBookId(userId, bookId)).thenReturn(Optional.of(mockBorrowedBook));

        borrowService.returnBook(bookId, userId);

        verify(bookRepository).save(argThat(book -> book.getAvailableCopies() == 3));
        verify(borrowedBookRepository).save(argThat(borrowedBook -> borrowedBook.getReturnDate() != null));
    }

    @Test
    public void returnBook_ShouldThrowException_WhenBorrowRecordNotFound() {
        Long bookId = 1L;
        Long userId = 1L;

        when(borrowedBookRepository.findByAppUserIdAndBookId(userId, bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> borrowService.returnBook(bookId, userId));

        assertEquals("Borrow record not found", exception.getMessage());
    }
}
