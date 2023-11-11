package com.mad.g1.nguyentuanminh.blogapp.model;

import java.util.Date;

public class User {

    public String fullname;
    public String dob;
    public String pob;
    public String gender;
    public String username;
    public String des;
    public String image;


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPob() {
        return pob;
    }

    public void setPob(String pob) {
        this.pob = pob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User(String fullname, String dob, String pob, String gender, String username, String des, String image) {
        this.fullname = fullname;
        this.dob = dob;
        this.pob = pob;
        this.gender = gender;
        this.username = username;
        this.des = des;
        this.image = image;
    }

    public User() {
    }
}

