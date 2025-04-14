package com.example.currencyconverter.client;

import com.example.currencyconverter.dto.ExchangeRatesResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
public class OpenExchangeRatesClientApi {
    private static final String BASE_URL =
            "https://openexchangerates.org/api/latest.json?app_id=1c5e28878e8f4550a358e59b778bbb11&base=%s";

    @Cacheable(value = "exchangeRates", key = "#originalCurrency")
    public Map<String, Double> getExchangeRates(final String originalCurrency) {
        final String url = String.format(BASE_URL, originalCurrency);
        final RestTemplate restTemplate = new RestTemplate();
        return Objects.requireNonNull(restTemplate.getForObject(url, ExchangeRatesResponse.class)).getRates();
    }
}
