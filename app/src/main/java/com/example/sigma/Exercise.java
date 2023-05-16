// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR THE EXERCISES WITHIN A WORKOUT
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    private String name;
    private List<Set> sets;

    public Exercise(String name) {
        this.name = name;
        this.sets = new ArrayList<>();
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Set> getSets() {
        return sets;
    }

    public void addSet(Set newSet) {
        this.sets.add(newSet);
    }
}

