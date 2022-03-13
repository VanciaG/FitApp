package com.example.fitapp;

public class User {
    public String userName, email, hash_password;

    public User(){}

    public User(String userName, String email, String hash_password) {
        this.userName = userName;
        this.email = email;
        this.hash_password = hash_password;
    }
}
