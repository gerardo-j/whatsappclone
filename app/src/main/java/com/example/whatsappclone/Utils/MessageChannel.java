package com.example.whatsappclone.Utils;

import java.util.ArrayList;

public class MessageChannel {

    private String id, name, imageUrl;
    private ArrayList<User> users;
    private ArrayList<Message> messages;

    public MessageChannel() {}

    public MessageChannel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public MessageChannel(String name, String imageUrl, ArrayList<User> users, ArrayList<Message> messages) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.users = users;
        this.messages = messages;
    }

    public String getId() { return id; }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<User> getUsers() { return users; }

    public ArrayList<Message> getMessages() { return messages; }

    public void setId(String id) { this.id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

}
