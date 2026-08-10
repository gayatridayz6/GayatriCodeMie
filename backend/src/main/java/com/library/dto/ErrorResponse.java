package com.library.dto;

public class ErrorResponse {

    private final ErrorDetails error;

    public ErrorResponse(String code, String message) {
        this.error = new ErrorDetails(code, message, null);
    }

    public ErrorResponse(String code, String message, Boolean safeFallback) {
        this.error = new ErrorDetails(code, message, safeFallback);
    }

    public ErrorDetails getError() {
        return error;
    }

    public static class ErrorDetails {
        private final String code;
        private final String message;
        private final Boolean safeFallback;

        public ErrorDetails(String code, String message, Boolean safeFallback) {
            this.code = code;
            this.message = message;
            this.safeFallback = safeFallback;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Boolean getSafeFallback() {
            return safeFallback;
        }
    }
}
