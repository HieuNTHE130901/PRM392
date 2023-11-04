package com.example.groceryapplication.models;

public class Voucher {
    private String code;
    private double value;

    public Voucher(String code, double value) {
        this.code = code;
        this.value = value;
    }

    // Create getters and setters as needed
    public String getCode() {
        return code;
    }

    public double getValue() {
        return value;
    }
}
