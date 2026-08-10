package com.library.service;

import com.library.dto.PagedResponse;
import com.library.exception.BadRequestException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pagable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BookQueryService {
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "title", "author", "isbn", "copies");

    private final BookRepository bookRepository;

    public BookQueryService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public PagedResponse<Book> getBooks(String search, Integer page, Integer size, String sort) {
        int safePage = (page == null ? 0 : page);
        int safeSize = (size == null ? DEFAULT_SIZE : size);

        if (safePage < 0) {
            throw new BadRequestException("page must be >= 0");
        }
        if (safeSize < 1 || safeSize > MAX_SIZE) {
            throw new BadRequestException(" size must be between 1 and " + MAX_SIZE);
        }

        Sort sortObj = parseSort(sort);
        Pageable pageable = PageRequest.of(safePage, safeSize, sortObj);

        Page<Book> result;
        if (search != null && !search.trim().isEmpty()) {
            result = bookRepository.searchBooks(search.trim(), pageable);
        } else {
            result = bookRepository.findAll(pageable);
        }

        return new PagedResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return Sort.by(Sort.Direction.ASC, "id");
        }

        String[] parts = sort.split(",");
        if (parts.length != 2) {
            throw new BadRequestException("sort must be in format 'field,asc' or 'field,desc'");
        }
        String field = parts[0].trim();
        String dir = parts[1].trim();

        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            throw new BadRequestException("sort field not allowed: " + field);
        }

        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(dir);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("sort direction must be 'asc' or 'desc'");
        }

        return Sort.by(direction, field);
    }
}
