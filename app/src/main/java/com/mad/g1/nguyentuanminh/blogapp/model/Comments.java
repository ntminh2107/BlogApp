package com.mad.g1.nguyentuanminh.blogapp.model;


public class Comments {
    private String commentId;
    private String userId;
    private String username;

    private String userPic;
    private String content;
    private long timestamp;

    // Required default constructor for Firebase
    public Comments() {
    }

    public Comments(String userId, String username, String userPic, String content) {
        this.userId = userId;
        this.username = username;
        this.userPic = userPic;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public Comments(String commentId, String userId, String username, String userPic, String content) {
        this.commentId = commentId;
        this.userId = userId;
        this.username = username;
        this.userPic = userPic;
        this.content = content;
    }
    // Getters and setters for other fields

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
