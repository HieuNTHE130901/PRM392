package com.example.groceryapplication.models;

import java.io.Serializable;

public class NavCategory implements Serializable {
    private String product_img_url;
    private String productName;
    private String productPrice;
    private String totalQuantity;
    private String totalPrice;
    private String docummentId;

    private String currentDate;
    private String currentTime;
    public NavCategory() {
    }

    public NavCategory(String product_img_url, String productName, String productPrice, String totalQuantity, String totalPrice, String currentDate, String currentTime) {
        this.product_img_url = product_img_url;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

    public String getDocummentId() {
        return docummentId;
    }

    public void setDocummentId(String docummentId) {
        this.docummentId = docummentId;
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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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
