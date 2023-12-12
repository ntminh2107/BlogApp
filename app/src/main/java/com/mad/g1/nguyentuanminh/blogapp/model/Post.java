package com.mad.g1.nguyentuanminh.blogapp.model;

import com.google.firebase.database.ServerValue;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Post {
    String postID;
    String username;
    String userid;
    String title;
    String content;
    String img;
    private Object timestamp;
    String userProfileImg;
    int likecount;
    boolean isliked;
    private List<Comments> comments;

    // other fields, constructor, and methods

    public Post() {
        // Set timestamp to ServerValue.TIMESTAMP when creating a new post
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public Post(String username, String userid, String title, String content ,String img,String userProfileImg) {
        this.username = username;
        this.userid = userid;
        this.title = title;
        this.content = content;
        this.img = img;
        this.userProfileImg = userProfileImg;
        // Set timestamp to ServerValue.TIMESTAMP when creating a new post
        this.timestamp = ServerValue.TIMESTAMP;
    }
    public Post(String username, String userid, String title, String content,String userProfileImg) {
        this.username = username;
        this.userid = userid;
        this.title = title;
        this.content = content;
        this.userProfileImg = userProfileImg;
        // Set timestamp to ServerValue.TIMESTAMP when creating a new post
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Post(String postID, String username, String userid, String title, String content, String userProfileImg, List<Comments> comments) {
        this.postID = postID;
        this.username = username;
        this.userid = userid;
        this.title = title;
        this.content = content;
        this.timestamp = ServerValue.TIMESTAMP;
        this.userProfileImg = userProfileImg;
        this.comments = comments;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
    public String getFormattedTimestamp() {
        if (timestamp instanceof Long) {
            long timestampLong = (Long) timestamp;
            Date date = new Date(timestampLong);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
            return dateFormat.format(date);
        }
        return "";
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserProfileImg() {
        return userProfileImg;
    }

    public void setUserProfileImg(String userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public boolean isIsliked() {
        return isliked;
    }

    public void setIsliked(boolean isliked) {
        this.isliked = isliked;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
