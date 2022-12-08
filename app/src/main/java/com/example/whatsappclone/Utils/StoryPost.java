package com.example.whatsappclone.Utils;

public class StoryPost {

    private String userId, imageURL, caption;

    public StoryPost()  {}

    public StoryPost(String userId, String imageURL, String caption) {
        this.userId = userId;
        this.imageURL = imageURL;
        this.caption = caption;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
