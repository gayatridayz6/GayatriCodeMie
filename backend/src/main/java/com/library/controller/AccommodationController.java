package com.library.controller;

import com.library.dto.AccommodationSearchResponse;
import com.library.service.AccommodationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accommodations")
@CrossOrigin(origins = "http://localhost:3000")
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping
    public ResponseEntity<AccommodationSearchResponse> searchAccommodations(@RequestParam(defaultValue = "USD") String currency) {
        return ResponseEntity.ok(accommodationService.search(currency));
    }
}
