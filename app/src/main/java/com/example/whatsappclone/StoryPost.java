package com.example.whatsappclone;

public class StoryPost {

    private String username, postDetail, userId, imageURL, caption;
    private int postImage;

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

    public StoryPost(String username, String postDetail, int postImage){
        this.username = username;
        this.postDetail = postDetail;
        this.postImage = postImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostDetail() {
        return postDetail;
    }

    public void setPostDetail(String postDetail) {
        this.postDetail = postDetail;
    }

    public int getPostImage() {
        return postImage;
    }

    public void setPostImage(int postImage) {
        this.postImage = postImage;
    }
}
