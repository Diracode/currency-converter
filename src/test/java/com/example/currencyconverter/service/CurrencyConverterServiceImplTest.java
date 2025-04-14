package com.example.currencyconverter.service;

import com.example.currencyconverter.client.OpenExchangeRatesClientApi;
import com.example.currencyconverter.exception.RateNotFoundException;
import com.example.currencyconverter.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CurrencyConverterServiceImplTest {

    @Mock
    private OpenExchangeRatesClientApi apiClient;

    @InjectMocks
    private CurrencyConverterServiceImpl currencyConverterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateWithValidConversionAndDiscount() {
        // Setup mock behavior
        Map<String, Double> exchangeRates = Map.of("USD", 1.2);
        when(apiClient.getExchangeRates("EUR")).thenReturn(exchangeRates);

        // Create test data
        Customer customer = new Customer(UserTypeEnum.EMPLOYEE, 0);
        Product product = new Product("Pencil", ProductCategoryEnum.OTHER, 10, 200.0);
        BillDetails bill = new BillDetails();
        bill.setCustomer(customer);
        bill.setOriginalCurrency("EUR");
        bill.setItems(List.of(product));
        bill.setTotalAmount(200.0);
        bill.setTargetCurrency("USD");

        // Call method
        BigDecimal result = currencyConverterService.calculate(bill);

        // Verify
        double expectedDiscount = 200 * 1.2 * 0.3; // 30% discount for employees
        int perHundredDiscount = (int) (200 * 1.2) / 100;
        double expectedPerHundredDiscount = perHundredDiscount * 5;
        BigDecimal payableAmount = toBigDecimal((200 * 1.2) - expectedDiscount - expectedPerHundredDiscount);
        assertEquals(payableAmount, result);
    }

    private BigDecimal toBigDecimal(final double amount) {
        return new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
    }

    @Test
    void testCalculateWithRateNotFound() {
        // Setup mock behavior
        when(apiClient.getExchangeRates("USD")).thenReturn(Collections.emptyMap());

        // Create test data
        Customer customer = new Customer(UserTypeEnum.AFFILIATE, 0);
        Product product = new Product("Bag", ProductCategoryEnum.OTHER, 10, 100.0);;
        BillDetails bill = new BillDetails();
        bill.setCustomer(customer);
        bill.setOriginalCurrency("EUR");
        bill.setItems(List.of(product));
        bill.setTotalAmount(200.0);
        bill.setTargetCurrency("USD");

        // Expect exception
        assertThrows(RateNotFoundException.class, () -> currencyConverterService.calculate(bill));
    }

    @Test
    void testCalculateSameCurrencyNoConversion() {
        // Create test data
        Customer customer = new Customer(UserTypeEnum.OTHER, 3); // Customer with tenure > 2 years
        Product product = new Product("Pole", ProductCategoryEnum.OTHER, 10, 100.0);
        BillDetails bill = new BillDetails();
        bill.setCustomer(customer);
        bill.setOriginalCurrency("USD");
        bill.setItems(List.of(product));
        bill.setTotalAmount(200.0);
        bill.setTargetCurrency("USD");

        // Call method
        BigDecimal result = currencyConverterService.calculate(bill);

        // Verify
        double expectedDiscount = 200 * 0.05; // 5% discount for tenure > 2 years
        double expectedPerHundredDiscount = (200 / 100) * 5;
        assertEquals(toBigDecimal(200 - expectedDiscount - expectedPerHundredDiscount), result);
    }

    @Test
    void testCalculateBillWithOnlyGroceries() {
        // Setup mock behavior
        Map<String, Double> exchangeRates = Map.of("USD", 1.2);
        when(apiClient.getExchangeRates("EUR")).thenReturn(exchangeRates);

        // Create test data
        Customer customer = new Customer(UserTypeEnum.EMPLOYEE, 0); // Employee would normally get 30%
        Product product = new Product("Food", ProductCategoryEnum.GROCERIES, 10, 200.0);
        BillDetails bill = new BillDetails();
        bill.setCustomer(customer);
        bill.setOriginalCurrency("EUR");
        bill.setItems(List.of(product));
        bill.setTotalAmount(200.0);
        bill.setTargetCurrency("USD");

        // Call method
        BigDecimal result = currencyConverterService.calculate(bill);

        // Verify
        int perHundredDiscount = (int) (200 * 1.2) / 100;
        double expectedPerHundredDiscount = perHundredDiscount * 5;
        assertEquals( toBigDecimal((200 * 1.2) - expectedPerHundredDiscount), result); // No percentage-based discount
    }
}
