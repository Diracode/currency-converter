package com.example.currencyconverter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ExchangeRatesResponse {
    private String disclaimer;
    private String license;
    private long timestamp;
    private String base;
    private Map<String, Double> rates;

    public ExchangeRatesResponse(final Map<String, Double> rates) {
        this.rates = rates;
    }
}
