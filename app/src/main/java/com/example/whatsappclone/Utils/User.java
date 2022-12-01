package com.example.whatsappclone.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String uid, firstName, lastName, email, profileImage;
    ArrayList<MessageChannel> channels;

    public User() {}

    public User(String uid, String firstName, String lastName, String email) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String uid, String firstName, String lastName, String email, ArrayList<MessageChannel> channels) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.channels = channels;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUid() { return uid; }

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
        result.put("id", uid);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);
        return result;
    }
}
