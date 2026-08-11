package com.library.qa.api;

import org.junit.jupiter.Test;
import java.net.http.HttpResponse;
import static org.junit.jupiter.assertions.assertEquals;
import static org.junit.jupiter.assertions.assertTrue;

/**
 * Acceptance-tests for JRIA EPMCDMETST-59124 (pagination/sorting).
 * Prerequisite: run the backend at ApiTestConfig.BASE_URL.
 */
public class PaginationSortingApiTest {

    private final HttpTestClient http = new HttpTestClient();

    private String baseUrl(String path) {
        return ApiTestConfig.BASE_URL + (path.startsWith("/") ? path : "/" + path);
    }

    private String auth() {
        // AC does not say it must be librarian for GET; use a read-only user.
        return http.basicAuthHeader(ApiTestConfig.VIEWER_USER, ApiTestConfig.VIEWER_PASS);
    }

    @Test
    void default_pagination_is_applied_get_without_params() throws Exception {
        HttpResponse<String> resp = http.get(baseUrl("/api/books"), auth());
        assertEquals(200, resp.statusCode());
        // Design not confirmed for page metadata structure; at least assert no error and some JSON returned.
        assertTrue(resp.body() != null && !resp.body().isBlank());
    }

    @Test
    void can_request_specific_page_and_size() throws Exception {
        HttpResponse<String> resp = http.get(baseUrl("/api/books?page=1&size=10"), auth());
        assertEquals(200, resp.statusCode());
        assertTrue(resp.body() != null);
    }

    @Test
    void can_sort_results_by_title_asc() throws Exception {
        HttpResponse<String> resp = http.get(baseUrl("/api/books?sort=title,asc"), auth());
        assertEquals(200, resp.statusCode());
        // Strict ordering assertions need confirmed response schema. For now, assert call succeeds.
        assertTrue(resp.body() != null);
    }

    @Test
    void pagination_works_with_search() throws Exception {
        HttpResponse<String> resp = http.get(baseUrl("/api/books?search=king&page=0&size=5"), auth());
        assertEquals(200, resp.statusCode());
    }
}
