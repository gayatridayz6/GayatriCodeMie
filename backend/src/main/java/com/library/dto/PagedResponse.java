package com.library.dto;

import com.library.model.Book;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        String sort
) {

    public static PagedResponse<Book> fromSpringPage(org.springframework.data.domain.Page<Book> page) {
        String sortStr = page.getSort() == null ? "" : page.getSort().toString();
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                sortStr
        );
    }
}
