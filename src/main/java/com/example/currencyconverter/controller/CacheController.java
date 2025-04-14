package com.example.currencyconverter.controller;

import com.example.currencyconverter.service.CacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @DeleteMapping("/exchangeRates/{currency}")
    public ResponseEntity<String> clearCurrency(@PathVariable String currency) {
        cacheService.clearRateForCurrency(currency);
        return ResponseEntity.ok("Cleared cache for: " + currency);
    }

    @DeleteMapping("/exchangeRates")
    public ResponseEntity<String> clearAll() {
        cacheService.clearAllRates();
        return ResponseEntity.ok("Cleared all exchange rates");
    }
}

