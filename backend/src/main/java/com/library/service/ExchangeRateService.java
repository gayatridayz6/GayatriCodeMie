package com.library.service;

import com.library.exception.FxRateUnavailableException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateService {

    private static final String BASE_CURRENCY = "USD";
    private static final Instant RATE_AS_OF = Instant.parse("2026-08-07T00:00:00Z");
    private final Map<String, BigDecimal> usdQuoteRates;

    public ExchangeRateService() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", BigDecimal.ONE);
        rates.put("EUR", new BigDecimal("0.92"));
        rates.put("GBP", new BigDecimal("0.79"));
        this.usdQuoteRates = Collections.unmodifiableMap(rates);
    }

    public BigDecimal convert(BigDecimal amount, String sourceCurrency, String targetCurrency) {
        if (sourceCurrency.equals(targetCurrency)) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal sourceRate = usdQuoteRates.get(sourceCurrency);
        BigDecimal targetRate = usdQuoteRates.get(targetCurrency);

        if (sourceRate == null || targetRate == null) {
            throw new FxRateUnavailableException("Conversion is temporarily unavailable. Showing prices in the last selected currency.");
        }

        BigDecimal amountInBase = BASE_CURRENCY.equals(sourceCurrency)
                ? amount
                : amount.divide(sourceRate, 8, RoundingMode.HALF_UP);

        return amountInBase.multiply(targetRate).setScale(2, RoundingMode.HALF_UP);
    }

    public String getRateAsOf() {
        return RATE_AS_OF.toString();
    }
}
