package com.example.whatsappclone;

import com.example.whatsappclone.Utils.MessageChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String id, firstName, lastName, email;
    ArrayList<MessageChannel> channels;

    public User() {}

    public User(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String id, String firstName, String lastName, String email, ArrayList<MessageChannel> channels) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.channels = channels;
    }

    public String getId() { return id; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);
        return result;
    }
}
