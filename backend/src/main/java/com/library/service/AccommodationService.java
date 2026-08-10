package com.library.service;

import com.library.dto.AccommodationSearchResponse;
import com.library.dto.CurrencyMetadataDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccommodationService {

    private static final String BASE_CURRENCY = "USD";
    private static final BigDecimal DEFAULT_STAY_NIGHTS = new BigDecimal("3");

    private final CurrencyMetadataService currencyMetadataService;
    private final ExchangeRateService exchangeRateService;

    public AccommodationService(CurrencyMetadataService currencyMetadataService, ExchangeRateService exchangeRateService) {
        this.currencyMetadataService = currencyMetadataService;
        this.exchangeRateService = exchangeRateService;
    }

    public AccommodationSearchResponse search(String currency) {
        String normalizedCurrency = currencyMetadataService.normalizeAndValidate(currency);
        CurrencyMetadataDto currencyMetadata = currencyMetadataService.getRequiredMetadata(normalizedCurrency);

        List<AccommodationSearchResponse.AccommodationResult> results = seedAccommodations().stream()
                .map(accommodation -> toResult(accommodation, normalizedCurrency))
                .collect(Collectors.toList());

        AccommodationSearchResponse.Meta meta = new AccommodationSearchResponse.Meta(
                currencyMetadata,
                exchangeRateService.getRateAsOf(),
                !BASE_CURRENCY.equals(normalizedCurrency));

        return new AccommodationSearchResponse(meta, results);
    }

    private AccommodationSearchResponse.AccommodationResult toResult(AccommodationSeed accommodation, String targetCurrency) {
        BigDecimal nightly = exchangeRateService.convert(accommodation.nightlyUsd, BASE_CURRENCY, targetCurrency);
        BigDecimal total = exchangeRateService.convert(accommodation.nightlyUsd.multiply(DEFAULT_STAY_NIGHTS), BASE_CURRENCY, targetCurrency);

        AccommodationSearchResponse.Price price = new AccommodationSearchResponse.Price(
                new AccommodationSearchResponse.Money(nightly, targetCurrency),
                new AccommodationSearchResponse.Money(total, targetCurrency));

        return new AccommodationSearchResponse.AccommodationResult(
                accommodation.id,
                accommodation.name,
                accommodation.propertyType,
                accommodation.rating,
                price);
    }

    private List<AccommodationSeed> seedAccommodations() {
        return Arrays.asList(
                new AccommodationSeed("acc_123", "Central Hotel", "Hotel", new BigDecimal("8.9"), new BigDecimal("199.99")),
                new AccommodationSeed("acc_456", "Riverside Villa", "Villa", new BigDecimal("9.2"), new BigDecimal("250.00")),
                new AccommodationSeed("acc_789", "City Studio", "Apartment", new BigDecimal("8.4"), new BigDecimal("149.50")));
    }

    private static class AccommodationSeed {
        private final String id;
        private final String name;
        private final String propertyType;
        private final BigDecimal rating;
        private final BigDecimal nightlyUsd;

        private AccommodationSeed(String id, String name, String propertyType, BigDecimal rating, BigDecimal nightlyUsd) {
            this.id = id;
            this.name = name;
            this.propertyType = propertyType;
            this.rating = rating;
            this.nightlyUsd = nightlyUsd;
        }
    }
}
