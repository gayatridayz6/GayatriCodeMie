package com.library.qa.api;

import org.junit.jupiter.Test;
import java.net.http.HttpResponse;
import static org.junit.jupiter.assertions.assertEquals;
import static org.junit.jupiter.assertions.assertTrue;

public class AuthRbacApiTest {
    private final HttpTestClient http = new HttpTestClient();

    private String baseUrl(String path) {
        return ApiTestConfig.BASE_URL + (path.startsWith("/") ? path : "/" + path);
    }

    @Test
    void unauthenticated_user_cannot_modify_books_post_returns_401() throws Exception {
        String payload = "{\"title\":\"QUA_Book\",\"author\":\"QAU_Author\",\"isbn\":\"QAU_ISBN-1\",\"quantity\":1}";
        HttpResponse<String> resp = http.postJson(baseUrl("/api/books"), payload, null);
        assertEquals(401, resp.statusCode());
    }

    @Test
    void authenticated_librarian_can_add_book_post_returns_201() throws Exception {
        String librarianAuth = http.basicAuthHeader(ApiTestConfig.LIBRARIAN_USER, ApiTestConfig.LIBRARIAN_PASS);
        String isbn = "QAU_ISBN-" + System.currentTimeMillis();
        String payload = "{\"title\":\"QUA_Book\",\"author\":\"QAU_Author\",\"isbn\":\"" + isbn+ "\",\"quantity\":1}";
        HttpResponse<String> resp = http.postJson(baseUrl("/api/books"), payload, librarianAuth);
        assertEquals(201, resp.statusCode());
        assertTrue(resp.body() != null && resp.body().contains(isbn));
    }

    @Test
    void authenticated_user_can_view_catalog_get_returns_200() throws Exception {
        String viewerAuth = http.basicAuthHeader(ApiTestConfig.VIEWER_USER, ApiTestConfig.VIEWER_PASS);
        HttpResponse<String> resp = http.get(baseUrl("/api/books"), viewerAuth);
        assertEquals(200, resp.statusCode());
    }

    @Test
    void authenticated_viewer_cannot_delete_books_returns_403_or_401() throws Exception {
        String viewerAuth = http.basicAuthHeader(ApiTestConfig.VIEWER_USER, ApiTestConfig.VIEWER_PASS);
        HttpResponse<String> deleteResp = http.delete(baseUrl("/api/books/1"), viewerAuth);
        assertTrue(deleteResp.statusCode() == 403 || deleteResp.statusCode() == 401);
    }
}
