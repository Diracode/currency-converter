package com.example.currencyconverter.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Customer {
    private int id;
    private UserTypeEnum userType;
    private double tenure;

    public Customer(final UserTypeEnum userTypeEnum, final double tenure) {
        this.userType = userTypeEnum;
        this.tenure = tenure;
        this.id = UUID.randomUUID().hashCode();
    }
}
