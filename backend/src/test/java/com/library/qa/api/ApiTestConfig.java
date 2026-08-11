package com.library.qa.api;

public final class ApiTestConfig {
    private ApiTestConfig() { }

    /**
     * Base URL for local testing. Override with -DAPI_BASE_URL=http://host:8080
     */
    public static final String BASE_URL = System.getProperty("API_BASE_URL", "http://localhost:8080");

    /**
     * Librarian credentials. Override via -DLIBRARIAN_USER/-DLIBRARIAN_PASS
     */
    public static final String LIBRARIAN_USER = System.getProperty("LIBRARIAN_USER", "librarian");
    public static final String LIBRARIAN_PASS = System.getProperty("LIBRARIAN_PASS", "librarianPass");

    /**
     * Viewer credentials. Override via -DVIEWER_USER/-DVIEWER_PASS
     */
    public static final String VIEWER_USER = System.getProperty("VIEWER_USER", "viewer");
    public static final String VIEWER_PASS = System.getProperty("VIEWER_PASS", "viewerPass");
}
