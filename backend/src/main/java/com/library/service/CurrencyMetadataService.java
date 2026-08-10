package com.library.service;

import com.library.dto.CurrencyMetadataDto;
import com.library.exception.CurrencyMetadataUnavailableException;
import com.library.exception.UnsupportedCurrencyException;
import com.library.model.CurrencyCode;
import com.library.repository.CurrencyCodeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CurrencyMetadataService {

    private static final List<String> SUPPORTED_CURRENCIES = Collections.unmodifiableList(Arrays.asList("USD", "EUR", "GBP"));

    private final CurrencyCodeRepository currencyCodeRepository;

    public CurrencyMetadataService(CurrencyCodeRepository currencyCodeRepository) {
        this.currencyCodeRepository = currencyCodeRepository;
    }

    public List<CurrencyMetadataDto> getSupportedCurrencyMetadata(String codesParam) {
        List<String> requestedCodes = parseCodes(codesParam);
        List<CurrencyMetadataDto> metadata = new ArrayList<>();

        for (String code : requestedCodes) {
            CurrencyCode currencyCode = currencyCodeRepository.findById(code)
                    .orElseThrow(() -> new CurrencyMetadataUnavailableException("Currency information is temporarily unavailable."));
            metadata.add(CurrencyMetadataDto.from(currencyCode));
        }

        return metadata;
    }

    public CurrencyMetadataDto getRequiredMetadata(String currencyCode) {
        String normalizedCode = normalizeAndValidate(currencyCode);
        CurrencyCode metadata = currencyCodeRepository.findById(normalizedCode)
                .orElseThrow(() -> new CurrencyMetadataUnavailableException("Currency information is temporarily unavailable."));
        return CurrencyMetadataDto.from(metadata);
    }

    public String normalizeAndValidate(String currencyCode) {
        String normalized = currencyCode == null || currencyCode.trim().isEmpty()
                ? "USD"
                : currencyCode.trim().toUpperCase(Locale.ROOT);

        if (!SUPPORTED_CURRENCIES.contains(normalized)) {
            throw new UnsupportedCurrencyException("Supported currencies are USD, EUR, GBP.");
        }

        return normalized;
    }

    public List<String> getSupportedCurrencies() {
        return SUPPORTED_CURRENCIES;
    }

    private List<String> parseCodes(String codesParam) {
        if (codesParam == null || codesParam.trim().isEmpty()) {
            return SUPPORTED_CURRENCIES;
        }

        Set<String> uniqueCodes = Arrays.stream(codesParam.split(","))
                .map(code -> code.trim().toUpperCase(Locale.ROOT))
                .filter(code -> !code.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (uniqueCodes.isEmpty()) {
            return SUPPORTED_CURRENCIES;
        }

        uniqueCodes.forEach(this::normalizeAndValidate);
        return new ArrayList<>(uniqueCodes);
    }
}
