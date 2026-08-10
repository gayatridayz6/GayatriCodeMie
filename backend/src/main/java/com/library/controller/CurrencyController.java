package com.library.controller;

import com.library.dto.CurrencyMetadataResponse;
import com.library.service.CurrencyMetadataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/currencies")
@CrossOrigin(origins = "http://localhost:3000")
public class CurrencyController {

    private final CurrencyMetadataService currencyMetadataService;

    public CurrencyController(CurrencyMetadataService currencyMetadataService) {
        this.currencyMetadataService = currencyMetadataService;
    }

    @GetMapping
    public ResponseEntity<CurrencyMetadataResponse> getSupportedCurrencies(@RequestParam(required = false) String codes) {
        return ResponseEntity.ok(new CurrencyMetadataResponse(currencyMetadataService.getSupportedCurrencyMetadata(codes)));
    }
}
