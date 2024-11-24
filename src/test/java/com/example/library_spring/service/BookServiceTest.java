package com.example.library_spring.service;

import com.example.library_spring.entity.Book;
import com.example.library_spring.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllBooks_ReturnsAllBooks() {
        // Arrange
        List<Book> mockBooks = Arrays.asList(
                new Book(1L, "Title1", "Author1", "isbn1", 1, 1),
                new Book(2L, "Title2", "Author2", "isbn2", 2, 2)
        );
        when(bookRepository.findAll()).thenReturn(mockBooks);

        // Act
        List<Book> books = bookService.getAllBooks();

        // Assert
        assertEquals(2, books.size());
        assertEquals("Title1", books.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getBookById_ReturnsBook_WhenBookExists() {
        // Arrange
        Book mockBook = new Book(1L, "Title1", "Author1", "isbn1", 1, 1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        // Act
        Optional<Book> book = bookService.getBookById(1L);

        // Assert
        assertTrue(book.isPresent());
        assertEquals("Title1", book.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void getBookById_ReturnsBook_WhenDoesNotBookExist() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Book> book = bookService.getBookById(1L);

        // Assert
        assertFalse(book.isPresent());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void saveBook_SavesAndReturnsBook() {
        // Arrange
        Book mockBook = new Book(1L, "Title1", "Author1", "isbn1", 1, 1);
        when(bookRepository.save(mockBook)).thenReturn(mockBook);

        // Act
        Book savedBook = bookService.saveBook(mockBook);

        // Assert
        assertNotNull(savedBook);
        assertEquals("Title1", savedBook.getTitle());
        verify(bookRepository, times(1)).save(mockBook);
    }

    @Test
    public void deleteBook_DeletesBookById_WhenBookExists() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // Act
        bookService.deleteBook(bookId);

        // Assert
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void deleteBook_ThrowsException_WhenBookDoesNotExist() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act and Assert
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.deleteBook(bookId),
                "Expected EntityNotFoundException to be thrown"
        );
        assertEquals("Book with id 1 not found", thrown.getMessage());
    }

    @Test
    public void updateBook_UpdatesBook_WhenBookExists() {
        // Arrange
        Book existingBook = new Book(1L, "Old Title", "Old Author", "Old isbn", 1, 1);
        Book updatedBook = new Book(1L, "New Title", "New Author", "New isbn", 2, 2);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        // Act
        Book result = bookService.updateBook(1L, updatedBook);

        // Assert
        assertEquals("New Title", result.getTitle());
        assertEquals("New Author", result.getAuthor());
        assertEquals("New isbn", result.getIsbn());
        assertEquals(2, result.getAvailableCopies());
        assertEquals(2, result.getTotalCopies());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    public void updateBook_ThrowsException_WhenBookDoesNotExist() {
        // Arrange
        Book updatedBook = new Book(1L, "New Title", "New Author", "New isbn", 1, 1);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateBook(1L, updatedBook),
                "Expected RuntimeException to be thrown"
        );

        assertEquals("Book with id 1 not found", thrown.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(any());
    }
}
