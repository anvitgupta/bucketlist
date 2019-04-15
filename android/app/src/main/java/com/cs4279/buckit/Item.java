package com.cs4279.buckit;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Item {

    private String key;  // Autogenerated key from "push"
    private String title;  // Title of the item card
    private String description;  // Description of the bucket list item
    private String original_creator;  // The username of the creator of the item, should be the original
    private String post_creator;  // The username of the creator of this particular item, different from original if "reposted"
    // creator, even if a different user adds it to their personal lsit
    private String date;  // The date string of post
    private boolean completed;  // Boolean flag representing whether item has been completed
    private int timestamp;  // Unix timestamp for creation of item
    private int timeCompleted;  // Unix timestamp for completion of item, -1 if not complete yet
    private double score; // Something to sort the items by in the activity feed later perhaps?
    // Increased by some amount if added by someone else, another amount (lower) if "liked"?

    private boolean isInPersonalList;
    private boolean isActivity;

    public Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Item.class)
    }

    public Item(String key, String title, String description, String original_creator, String post_creator, String date, boolean completed, int timestamp, int timeCompleted, double score) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.original_creator = original_creator;
        this.post_creator = post_creator;
        this.date = date;
        this.completed = completed;
        this.timestamp = timestamp;
        this.timeCompleted = timeCompleted;
        this.score = score;

        isInPersonalList = false;
        isActivity = false;
    }

    // Copy constructor
    public Item(Item i) {
        this.key = i.key;
        this.title = i.title;
        this.description = i.description;
        this.original_creator = i.original_creator;
        this.post_creator = i.post_creator;
        this.date = i.date;
        this.completed = i.completed;
        this.timestamp = i.timestamp;
        this.timeCompleted = i.timeCompleted;
        this.score = i.score;

        this.isInPersonalList = i.isInPersonalList;
        this.isActivity = i.isActivity;
    }

    // @Exclude
    public String getKey() { return key; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getOriginalCreator() { return original_creator; }

    public String getPostCreator() { return post_creator; }

    public String getDate() { return date; }

    public boolean getCompleted() { return completed; }

    public int getTimestamp() { return timestamp; }

    public int getTimeCompleted() { return timeCompleted; }

    public double getScore() { return score; }

    @Exclude
    public boolean isInPersonalList() { return isInPersonalList; }

    @Exclude
    public boolean isActivity() { return isActivity; }

    public void setIsInPersonalList(boolean isInPersonalList) {
        this.isInPersonalList = isInPersonalList;
    }

    public void markCompleted() {
        completed = true;
    }

    public void markAsActivity() { isActivity = true; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("original_creator", original_creator);
        result.put("post_creator", post_creator);
        result.put("date", date);
        result.put("completed", completed);
        result.put("timestamp", timestamp);
        result.put("timeCompleted", timeCompleted);
        result.put("score", score);

        return result;
    }

}