package com.library.dto;

import java.math.BigDecimal;
import java.util.List;

public class AccommodationSearchResponse {

    private final Meta meta;
    private final List<AccommodationResult> data;

    public AccommodationSearchResponse(Meta meta, List<AccommodationResult> data) {
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public List<AccommodationResult> getData() {
        return data;
    }

    public static class Meta {
        private final CurrencyMetadataDto appliedCurrency;
        private final String fxAsOf;
        private final boolean conversionUsed;

        public Meta(CurrencyMetadataDto appliedCurrency, String fxAsOf, boolean conversionUsed) {
            this.appliedCurrency = appliedCurrency;
            this.fxAsOf = fxAsOf;
            this.conversionUsed = conversionUsed;
        }

        public CurrencyMetadataDto getAppliedCurrency() {
            return appliedCurrency;
        }

        public String getFxAsOf() {
            return fxAsOf;
        }

        public boolean isConversionUsed() {
            return conversionUsed;
        }
    }

    public static class AccommodationResult {
        private final String id;
        private final String name;
        private final String propertyType;
        private final BigDecimal rating;
        private final Price price;

        public AccommodationResult(String id, String name, String propertyType, BigDecimal rating, Price price) {
            this.id = id;
            this.name = name;
            this.propertyType = propertyType;
            this.rating = rating;
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPropertyType() {
            return propertyType;
        }

        public BigDecimal getRating() {
            return rating;
        }

        public Price getPrice() {
            return price;
        }
    }

    public static class Price {
        private final Money nightly;
        private final Money total;

        public Price(Money nightly, Money total) {
            this.nightly = nightly;
            this.total = total;
        }

        public Money getNightly() {
            return nightly;
        }

        public Money getTotal() {
            return total;
        }
    }

    public static class Money {
        private final BigDecimal amount;
        private final String currencyCode;

        public Money(BigDecimal amount, String currencyCode) {
            this.amount = amount;
            this.currencyCode = currencyCode;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }
    }
}
