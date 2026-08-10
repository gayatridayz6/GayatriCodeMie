package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookRepository);
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book("Clean Code", "Robert Martin", "ISBN-1", 3);
        Book book2 = new Book("Design Patterns", "Gang of Four", "ISBN-2", 2);

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testCreateBook_Success() {
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-123", 3);

        when(bookRepository.findByIsbn("ISBN-123")).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        Book createdBook = bookService.createBook(book);

        assertNotNull(createdBook);
        assertEquals("Clean Code", createdBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testCreateBook_DuplicateISBN() {
        Book existingBook = new Book("Old Book", "Author", "ISBN-123", 1);
        Book newBook = new Book("New Book", "Author", "ISBN-123", 1);

        when(bookRepository.findByIsbn("ISBN-123")).thenReturn(Optional.of(existingBook));

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.createBook(newBook);
        });
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            bookService.deleteBook(999L);
        });
    }
}
