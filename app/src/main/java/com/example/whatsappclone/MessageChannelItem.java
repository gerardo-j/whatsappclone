package com.example.whatsappclone;

public class MessageChannelItem {

    private String name;
    private String imageUrl;

    public MessageChannelItem(String name) {
        this.name = name;
    }

    public MessageChannelItem(String name, String imageUrl) {
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
