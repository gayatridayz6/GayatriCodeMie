package com.library.controller;

import com.library.dto.PagedResponse;
import com.library.model.Book;
import com.library.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"ok\"}");
    }

    @GetMapping("/books")
    public ResponseEntity<?> getBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "id,asc") String sort
    ) {
        // Backward compatible behavior: if no pagination params provided, return the legacy List<Book>
        boolean paginationRequested = (page != null) || (size != null) || (sort != null);

        if (!paginationRequested || (page == null && size == null && (sort == null || sort.isBlank()))) {
            List<Book> books = (search != null && !search.isEmpty())
                    ? bookService.searchBooks(search)
                    : bookService.getAllBooks();
            return ResponseEntity.ok(books);
        }

        int resolvedPage = (page == null || page < 0) ? 0 : page;
        int resolvedSize = (size == null || size <= 0) ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);

        Sort resolvedSort;
        try {
            resolvedSort = parseSort(sort);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Invalid sort format. Use sort=field,asc|desc\"}");
        }

        Pageable pageable = PageRequest.of(resolvedPage, resolvedSize, resolvedSort);
        Page<Book> booksPage = bookService.getBooksPaged(search, pageable);

        return ResponseEntity.ok(PagedResponse.fromSpringPage(booksPage));
    }

    private Sort parseSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "id");
        }
        String[] parts = sortParam.split(",");
        String property = parts[0].trim();
        String direction = (parts.length > 1 ? parts[1].trim() : "asc");

        Sort.Direction dir = Sort.Direction.fromString(direction);
        return Sort.by(dir, property);
    }

    @GetMapping("/books/{id}")
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
            return ResponseEntity.status(HttpStatus.CONFLICT)
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
