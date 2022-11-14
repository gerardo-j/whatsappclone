package com.example.whatsappclone.Utils;

import java.util.ArrayList;

public class MessageChannel {

    private ArrayList<User> users;
    private ArrayList<Message> messages;

    public MessageChannel() {}

    public MessageChannel(ArrayList<User> users, ArrayList<Message> messages) {
        this.users = users;
        this.messages = messages;
    }


    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Message> getMessage() {
        return messages;
    }

}
