package com.cs4279.buckit;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Item {

    private String name;
    private String description;
    private String creator;
    private String date;
    private double score; // Something to sort the items by in the activity feed later perhaps?

    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    public Item(String name, String description, String creator, String date, double score) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.date = date;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreator() { return creator; }

    public String getDate() { return date; }

    public double getScore() { return score; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("creator", creator);
        result.put("date", date);
        result.put("score", score);

        return result;
    }

}