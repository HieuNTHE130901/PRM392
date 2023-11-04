package com.example.groceryapplication.models;

public class Order {

    String orderId;
    String date;
    String time;
    String address;
    double orderValue;
    String status;

    public Order() {
    }

    public Order(String orderId, String date, String time, String address, double orderValue, String status) {
        this.orderId = orderId;
        this.date = date;
        this.time = time;
        this.address = address;
        this.orderValue = orderValue;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
