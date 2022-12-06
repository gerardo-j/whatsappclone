package com.example.whatsappclone.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String uid, username, email, profileImage;
    private ArrayList<MessageChannel> channels;

    public User() {}

    public User(String uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public User(String uid, String username, String email) {
        this.uid = uid;
        this.email = email;
    }

    public User(String uid, String username, String email, String profileImage) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
    }

    public User(String uid, String username, String email, ArrayList<MessageChannel> channels) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.channels = channels;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setChannels(ArrayList<MessageChannel> channels) {
        this.channels = channels;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getUid() { return uid; }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<MessageChannel> getChannels() {
        return channels;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", uid);
        result.put("email", email);
        return result;
    }
}
