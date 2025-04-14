package com.example.currencyconverter.service;

import com.example.currencyconverter.model.BillDetails;

import java.math.BigDecimal;

public interface CurrencyConverterService {
    BigDecimal calculate(final BillDetails bill);
}
