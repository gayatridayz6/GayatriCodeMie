package com.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "currency_codes")
public class CurrencyCode {

    @Id
    @Column(length = 3, nullable = false)
    private String code;

    @Column(name = "numeric_code", nullable = false)
    private Integer numericCode;

    @Column(nullable = false)
    private String name;

    @Column(name = "source_last_updated")
    private LocalDateTime sourceLastUpdated;

    protected CurrencyCode() {
    }

    public CurrencyCode(String code, Integer numericCode, String name, LocalDateTime sourceLastUpdated) {
        this.code = code;
        this.numericCode = numericCode;
        this.name = name;
        this.sourceLastUpdated = sourceLastUpdated;
    }

    public String getCode() {
        return code;
    }

    public Integer getNumericCode() {
        return numericCode;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getSourceLastUpdated() {
        return sourceLastUpdated;
    }

    public String getLabel() {
        return code + " " + numericCode + " " + name;
    }
}
