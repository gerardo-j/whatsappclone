package com.example.whatsappclone;

public class MessageChannel {

    private String name;
    private String imageUrl;

    public MessageChannel(String name) {
        this.name = name;
    }

    public MessageChannel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
