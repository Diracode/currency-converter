package com.example.currencyconverter.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BillDetails {

    @NotEmpty
    private List<Product> items = new ArrayList<>();
    @NotBlank
    private String targetCurrency;
    @NotBlank
    private String originalCurrency;
    @NotNull
    private Customer customer;
    private double totalAmount;

}
