// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR THE WORKOUTS ON THE HOME PAGE OR WITHIN A FOLDER
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
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

    //getters
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
