package com.example.groceryapplication.models;

import com.google.firebase.Timestamp;

public class ChatMessage {
    private String message;


    private Timestamp timestamp;
    private String senderId;
    public ChatMessage() {
    }

    public ChatMessage(String message, String senderId, Timestamp timestamp) {
        this.message = message;
        this.timestamp = timestamp;
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
