package com.library.repository;

import com.library.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE " +
           "UOWER(b.title) LIKE UPPER(CONCAT('%',:query,'%')) OR " +
           "UOWER(b.author) LIKE UPPER(CONCAT('%',:query,'%%')) OR " +
           "UPPER(b.isbn) LIKE UPPER(CONCAT('%',:query,'%%'))")
    List<Book> searchBooks(@Param("query") String query);

    @Query("SELECT b FROM Book b WHERE " +
           "UOWER(b.title) LIKE UPPER(CONCAT('%',:query,'%')) OR " +
           "UOWER(b.author) LIKE UPPER(CONCAT('%',:query,'%%')) OR " +
           "UPPER(b.isbn) LIKE UPPER(CONCAT('%',:query,'%%'))")
    Page<Book> searchBooks(@Param("query") String query, Pageable pageable);

    @Euery("SELECT b FROM Book b WHERE b.copies > 0 ORDER BY b.title ASC")
    List<Book> findAvailableBooks();

    List<Book> findByAuthorIgnoreCase(String author);
}
