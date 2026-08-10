package com.library.service;

import com.library.dto.CurrencyMetadataDto;
import com.library.exception.CurrencyMetadataUnavailableException;
import com.library.exception.UnsupportedCurrencyException;
import com.library.model.CurrencyCode;
import com.library.repository.CurrencyCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CurrencyMetadataServiceTest {

    private CurrencyMetadataService currencyMetadataService;

    @Mock
    private CurrencyCodeRepository currencyCodeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyMetadataService = new CurrencyMetadataService(currencyCodeRepository);
    }

    @Test
    void getSupportedCurrencyMetadataReturnsMetadataFromRepository() {
        when(currencyCodeRepository.findById("USD"))
                .thenReturn(Optional.of(new CurrencyCode("USD", 840, "United States dollar", LocalDateTime.now())));
        when(currencyCodeRepository.findById("EUR"))
                .thenReturn(Optional.of(new CurrencyCode("EUR", 978, "Euro", LocalDateTime.now())));

        List<CurrencyMetadataDto> metadata = currencyMetadataService.getSupportedCurrencyMetadata("USD,EUR");

        assertEquals(2, metadata.size());
        assertEquals("USD 840 United States dollar", metadata.get(0).getLabel());
        assertEquals("EUR 978 Euro", metadata.get(1).getLabel());
    }

    @Test
    void normalizeAndValidateRejectsUnsupportedCurrencies() {
        assertThrows(UnsupportedCurrencyException.class, () -> currencyMetadataService.normalizeAndValidate("JPY"));
    }

    @Test
    void getRequiredMetadataFailsWhenCurrencyReferenceDataUnavailable() {
        when(currencyCodeRepository.findById("GBP")).thenReturn(Optional.empty());

        assertThrows(CurrencyMetadataUnavailableException.class, () -> currencyMetadataService.getRequiredMetadata("GBP"));
    }
}
