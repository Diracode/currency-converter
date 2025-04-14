package com.example.currencyconverter.service;

import com.example.currencyconverter.client.OpenExchangeRatesClientApi;
import com.example.currencyconverter.exception.RateNotFoundException;
import com.example.currencyconverter.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CurrencyConverterServiceImpl implements CurrencyConverterService {
    @Autowired
    private OpenExchangeRatesClientApi apiClient;

    @Override
    public BigDecimal calculate(final BillDetails bill) {
        final String targetCurrency = bill.getTargetCurrency();
        final String originalCurrency = bill.getOriginalCurrency();
        double discountableAmount;
        double initialAmount;

        if (StringUtils.equals(targetCurrency, originalCurrency)) {
            initialAmount = bill.getTotalAmount();
            discountableAmount = getDiscountableAmount(bill, bill.getTotalAmount(), null);

        } else {
            final Map<String, Double> exchangeRates = apiClient.getExchangeRates(originalCurrency);
            if (exchangeRates == null || exchangeRates.isEmpty()) {
                throw new RateNotFoundException("No applicable rates found for currency: " + originalCurrency);
            }

            final double rate = exchangeRates.get(bill.getTargetCurrency());
            log.info("rate: {}", rate);
            final double convertedAmount = bill.getTotalAmount() * rate;
            initialAmount = convertedAmount;
            discountableAmount = getDiscountableAmount(bill, convertedAmount, rate);
        }

        final double percentageDiscountableAmount = getDiscountAmount(bill, discountableAmount);
        final double perHundredBillDiscountAmount = getPerHundredDiscount(initialAmount);

        return new BigDecimal(initialAmount - percentageDiscountableAmount - perHundredBillDiscountAmount)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private double getPerHundredDiscount(final double initialAmount) {
        int perHundredDiscount = (int) (initialAmount / 100);
        return perHundredDiscount * 5;
    }

    private double getDiscountAmount(final BillDetails bill, final double discountableAmount) {
        final Customer user = bill.getCustomer();

        if (UserTypeEnum.EMPLOYEE.equals(user.getUserType())) {
            return discountableAmount * (30 / 100.0);
        } else if(UserTypeEnum.AFFILIATE.equals(user.getUserType())) {
            return discountableAmount * (10 / 100.0);
        } else if (user.getTenure() >= 2) {
            return discountableAmount * (5 / 100.0);
        }
        return 0;
    }

    private double getDiscountableAmount(final BillDetails bill, double convertedAmount, final Double rate) {
        double amountToBeDiscounted = convertedAmount;
        final List<Product> productList = bill.getItems();

        for (final Product product : productList) {
            if (ProductCategoryEnum.GROCERIES.equals(product.getCategory())) {
                if (rate != null) {
                    amountToBeDiscounted -= (product.getAmount() * rate);
                } else {
                    amountToBeDiscounted -= product.getAmount();
                }
            }
        }
        return amountToBeDiscounted;
    }
}
