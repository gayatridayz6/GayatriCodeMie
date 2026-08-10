package com.library.service;

import com.library.dto.AccommodationSearchResponse;
import com.library.dto.CurrencyMetadataDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccommodationServiceTest {

    @Test
    void searchConvertsPricesAndAppliesSelectedCurrencyMetadata() {
        CurrencyMetadataService currencyMetadataService = mock(CurrencyMetadataService.class);
        when(currencyMetadataService.normalizeAndValidate("EUR")).thenReturn("EUR");
        when(currencyMetadataService.getRequiredMetadata("EUR"))
                .thenReturn(new CurrencyMetadataDto("EUR", 978, "Euro", "EUR 978 Euro"));

        AccommodationService accommodationService = new AccommodationService(currencyMetadataService, new ExchangeRateService());

        AccommodationSearchResponse response = accommodationService.search("EUR");

        assertEquals("EUR", response.getMeta().getAppliedCurrency().getCode());
        assertTrue(response.getMeta().isConversionUsed());
        assertEquals("EUR", response.getData().get(0).getPrice().getNightly().getCurrencyCode());
        assertEquals("184.00", response.getData().get(0).getPrice().getNightly().getAmount().toPlainString());
    }
}
