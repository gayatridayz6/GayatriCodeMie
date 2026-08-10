package com.library.controller;

import com.library.dto.ErrorResponse;
import com.library.exception.CurrencyMetadataUnavailableException;
import com.library.exception.FxRateUnavailableException;
import com.library.exception.UnsupportedCurrencyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UnsupportedCurrencyException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedCurrency(UnsupportedCurrencyException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("UNSUPPORTED_CURRENCY", exception.getMessage()));
    }

    @ExceptionHandler(CurrencyMetadataUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyMetadataUnavailable(CurrencyMetadataUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("CURRENCY_METADATA_UNAVAILABLE", exception.getMessage()));
    }

    @ExceptionHandler(FxRateUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleFxRateUnavailable(FxRateUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse("FX_RATE_UNAVAILABLE", exception.getMessage(), true));
    }
}
