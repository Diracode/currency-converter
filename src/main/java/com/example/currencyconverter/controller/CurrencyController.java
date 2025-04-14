package com.example.currencyconverter.controller;

import com.example.currencyconverter.model.BillDetails;
import com.example.currencyconverter.service.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping
public class CurrencyController {
    @Autowired
    private CurrencyConverterService currencyConverterService;

    @PostMapping("/api/calculate")
    public BigDecimal calculate(@RequestBody final BillDetails bill) {
        return currencyConverterService.calculate(bill);
    }

    @GetMapping("/home")
    public String home() {
        return "Welcome to the currency converter application";
    }
}
