package com.example.groceryapplication.models;

public class Voucher {
    private String code;
    private int value;

    public Voucher(String code, int value) {
        this.code = code;
        this.value = value;
    }

    // Create getters and setters as needed
    public String getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }
}
