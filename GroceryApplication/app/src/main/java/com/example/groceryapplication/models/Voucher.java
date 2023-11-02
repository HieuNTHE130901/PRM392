package com.example.groceryapplication.models;

public class Voucher {
    private String name;
    private int discount;

    public Voucher(String name, int discount) {
        this.name = name;
        this.discount = discount;
    }

    // Create getters and setters as needed
    public String getName() {
        return name;
    }

    public int getDiscount() {
        return discount;
    }
}
