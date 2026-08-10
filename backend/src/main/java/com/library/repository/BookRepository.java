package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find book by ISBN (unique constraint)
    Optional<Book> findByIsbn(String isbn);

    // Search books by title, author, or ISBN
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Book> searchBooks(@Param("query") String query);

    // ... existing code ...

    // Find books with available copies (copies > 0)
    @Query("SELECT b FROM Book b WHERE b.copies > 0 ORDER BY b.title ASC")
    List<Book> findAvailableBooks();

    // Find books by author name
    List<Book> findByAuthorIgnoreCase(String author);

    // ... existing code ...
}
