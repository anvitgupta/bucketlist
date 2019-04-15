package com.cs4279.buckit;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Item {

    private String key;
    private String title;
    private String description;
    private String creator;
    private String date;
    private boolean completed;
    private int timestamp;
    private double score; // Something to sort the items by in the activity feed later perhaps?

    private boolean isInPersonalList;

    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    public Item(String key, String title, String description, String creator, String date, boolean completed, int timestamp, double score) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.date = date;
        this.completed = completed;
        this.timestamp = timestamp;
        this.score = score;

        isInPersonalList = false;
    }

    // @Exclude
    public String getKey() { return key; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreator() { return creator; }

    public String getDate() { return date; }

    public boolean getCompleted() { return completed; }

    public int getTimestamp() { return timestamp; }

    public double getScore() { return score; }

    @Exclude
    public boolean isInPersonalList() { return isInPersonalList; }

    public void setIsInPersonalList(boolean isInPersonalList) {
        this.isInPersonalList = isInPersonalList;
    }

    public void markCompleted() {
        completed = true;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("creator", creator);
        result.put("date", date);
        result.put("completed", completed);
        result.put("timestamp", timestamp);
        result.put("score", score);

        return result;
    }

}