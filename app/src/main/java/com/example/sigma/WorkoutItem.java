package com.example.sigma;

public class WorkoutItem {
    private String name;
    private String date;
    private String length;

    public WorkoutItem(String name, String date, String length) {
        this.name = name;
        this.date = date;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getLength() {
        return length;
    }
}
