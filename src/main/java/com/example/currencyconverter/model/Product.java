package com.example.currencyconverter.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String name;
    private int quantity;
    private ProductCategoryEnum category;
    private double amount;

    public Product(final String name, final ProductCategoryEnum category, final int quantity, final double amount) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.amount = amount;
    }
}
