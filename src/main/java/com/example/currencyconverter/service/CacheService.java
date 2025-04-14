package com.example.currencyconverter.service;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearAllRates() {
        cacheManager.getCache("exchangeRates").clear();
    }

    public void clearRateForCurrency(String originalCurrency) {
        cacheManager.getCache("exchangeRates").evict(originalCurrency);
    }
}

