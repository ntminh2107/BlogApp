package com.mad.g1.nguyentuanminh.blogapp.model;

public class Post {
    String username;
    String userid;
    String title;
    String content;
    String img;

    public Post(String username, String userid, String title, String content, String img) {
        this.username = username;
        this.userid = userid;
        this.title = title;
        this.content = content;
        this.img = img;
    }

    public Post(String title, String content, String img) {
        this.title = title;
        this.content = content;
        this.img = img;
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
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
}
