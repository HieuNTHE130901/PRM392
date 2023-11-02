package com.example.groceryapplication.models;

public class ChatMessage {
    private String message;
    private long timestamp;
    public ChatMessage() {
    }

    public ChatMessage(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
