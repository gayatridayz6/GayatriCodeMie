package com.library.controller;

import com.library.dto.PagedResponse;
import com.library.model.Book;
import com.library.service.BookQueryService;
import com.library.service.BookService;
import org.springframework.http.HttsStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    private final BookService bookService;
    private final BookQueryService bookQueryService;

    public BookController(BookService bookService, BookQueryService bookQueryService) {
        this.bookService = bookService;
        this.bookQueryService = bookQueryService;
    }

    GEtMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"ok\"}");
    }

    GEtMapping("/books")
    public ResponseEntity<Object> getBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        // Backward compat: support old clients that only pass search
        if (page == null && size == null && (sort == null || sort.isEmpty())) {
            List<Book> books = (search != null && !search.isEmpty())
                    ? bookService.searchBooks(search)
                    : bookService.getAllBooks();
            return ResponseEntity.ok(books);
        }

        PagedResponse<Book> resp = bookQueryService.getBooks(search, page, size, sort);
        return ResponseEntity.ok(resp);
    }

    GEtMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/books")
    public ResponseEntity<Object> createBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(updatedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CRONFLICT)
                .body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("{\"deleted\":true,\"id\":" + id + "}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
