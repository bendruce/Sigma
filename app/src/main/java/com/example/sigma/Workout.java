package com.example.sigma;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Workout {
    private ArrayList<com.example.sigma.Set> sets;
    private ArrayList<com.example.sigma.Exercise> exercises;

    public Workout() {
        this.exercises = new ArrayList<>();
        this.sets = new ArrayList<>();
    }

    public void addExercise(com.example.sigma.Exercise exercise) {
        exercises.add(exercise);
        //Log.d("Workout", "Added exercise: " + exercise.getName());
        //Log.d("Workout", "Number of exercises: " + exercises.size());
    }


    public ArrayList<com.example.sigma.Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<com.example.sigma.Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Set> getSets() {
        this.sets = sets;
        return null;
    }
}