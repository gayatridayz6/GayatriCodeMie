package com.library.dto;

import java.util.List;

public class CurrencyMetadataResponse {

    private final List<CurrencyMetadataDto> data;

    public CurrencyMetadataResponse(List<CurrencyMetadataDto> data) {
        this.data = data;
    }

    public List<CurrencyMetadataDto> getData() {
        return data;
    }
}
