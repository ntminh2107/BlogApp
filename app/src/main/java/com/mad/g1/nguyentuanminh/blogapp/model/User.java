package com.mad.g1.nguyentuanminh.blogapp.model;

import java.util.Date;

public class User {
    int id;
    String email;
    String password;
    String role;
    String fullname;
    Date dob;
    String pob;
    String gender;

    public User(int id, String email, String password, String role, String fullname, Date dob, String pob, String gender) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
        this.dob = dob;
        this.pob = pob;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
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
}
