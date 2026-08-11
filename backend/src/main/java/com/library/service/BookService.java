package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Page<Book> getBooksPaged(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return bookRepository.findAll(pageable);
        }
        return bookRepository.searchBooks(search.trim(), pageable);
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.searchBooks(query.trim());
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        if (book.getCopies() == null) {
            book.setCopies(1);
        }

        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalArgumentException("ISBN already exists: " + book.getIsbn());
        }

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        if (bookDetails.getTitle() != null) {
            book.setTitle(bookDetails.getTitle());
        }
        if (bookDetails.getAuthor() != null) {
            book.setAuthor(bookDetails.getAuthor());
        }
        if (bookDetails.getIsbn() != null && !bookDetails.getIsbn().equals(book.getIsbn())) {
            if (bookRepository.findByIsbn(bookDetails.getIsbn()).isPresent()) {
                throw new IllegalArgumentException("ISBN already exists: " + bookDetails.getIsbn());
            }
            book.setIsbn(bookDetails.getIsbn());
        }
        if (bookDetails.getCopies() != null) {
            book.setCopies(bookDetails.getCopies());
        }

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}
