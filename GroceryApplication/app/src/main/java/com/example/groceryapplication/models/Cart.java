package com.example.groceryapplication.models;

import java.io.Serializable;

public class Cart implements Serializable {
    private String product_img_url;
    private String productName;
    private double productPrice;
    private int totalQuantity;
    private double totalPrice;
    private String docummentId;
    private String currentDate;
    private String currentTime;
    public Cart() {
    }

    public Cart(String product_img_url, String productName, double productPrice, int totalQuantity, double totalPrice, String currentDate, String currentTime) {
        this.product_img_url = product_img_url;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

    public String getProduct_img_url() {
        return product_img_url;
    }

    public void setProduct_img_url(String product_img_url) {
        this.product_img_url = product_img_url;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDocummentId() {
        return docummentId;
    }

    public void setDocummentId(String docummentId) {
        this.docummentId = docummentId;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
