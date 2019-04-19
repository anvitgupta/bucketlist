package com.cs4279.buckit;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class BuckItUser {
    private String email, username;

    public BuckItUser() {}

    public BuckItUser(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("username", username);

        return result;
    }
}
