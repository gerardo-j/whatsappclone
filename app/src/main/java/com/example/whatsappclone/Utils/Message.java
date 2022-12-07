package com.example.whatsappclone.Utils;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private String senderId, message, date, time;

    public Message() {}

    public Message(String senderId, String message, String date, String time) {
        this.senderId = senderId;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getSenderId() { return senderId; }

    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time;}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("senderId", senderId);
        result.put("message", message);
        result.put("date", date);
        result.put("time", time);
        return result;
    }

}
