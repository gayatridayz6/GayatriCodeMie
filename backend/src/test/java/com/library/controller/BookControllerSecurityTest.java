package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Test
    void unauthenticatedUserCannotModifyBooks() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"t\",\"author\":\"a\",\"isbn\":\"x\",\"copies\":1}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void librarianCanAddBook() throws Exception {
        mockMvc.perform(post("/api/books")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic("librarian", "librarian123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\",\"author\":\"Auth\",\"isbn\":\"isbn-l1\",\"copies\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    void viewerCannotDeleteBook() throws Exception {
        Book created = bookService.createBook(new Book(null, "Del", "A", "isbn-del", 1));

        mockMvc.perform(delete("/api/books/{id}", created.getId())
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic("viewer", "viewer123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void authenticatedUsersCanViewCatalog() throws Exception {
        mockMvc.perform(get("/api/books")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic("viewer", "viewer123")))
                .andExpect(status().isOk());
    }
}
