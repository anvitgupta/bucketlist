package com.cs4279.buckit;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Metadata {

    public String demo_count;
    // public Map<String, Boolean> stars = new HashMap<>();

    public Metadata() {
        // Default constructor required for calls to DataSnapshot.getValue(Metadata.class)
    }

    public Metadata(String demo_count) {
        this.demo_count = demo_count;
    }

    /*
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }*/

}