package com.library.dto;

import com.library.model.CurrencyCode;

public class CurrencyMetadataDto {

    private final String code;
    private final Integer numeric;
    private final String name;
    private final String label;

    public CurrencyMetadataDto(String code, Integer numeric, String name, String label) {
        this.code = code;
        this.numeric = numeric;
        this.name = name;
        this.label = label;
    }

    public static CurrencyMetadataDto from(CurrencyCode currencyCode) {
        return new CurrencyMetadataDto(
                currencyCode.getCode(),
                currencyCode.getNumericCode(),
                currencyCode.getName(),
                currencyCode.getLabel());
    }

    public String getCode() {
        return code;
    }

    public Integer getNumeric() {
        return numeric;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }
}
