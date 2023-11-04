package com.example.groceryapplication.models;

public class Order {

    String date;
    String time;
    String address;
    double orderValue;
    String status;
    private String docummentId;

    public Order() {
    }

    public Order(String date, String time, String address, double orderValue, String status) {

        this.date = date;
        this.time = time;
        this.address = address;
        this.orderValue = orderValue;
        this.status = status;
    }

    public String getDocummentId() {
        return docummentId;
    }

    public void setDocummentId(String docummentId) {
        this.docummentId = docummentId;
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
