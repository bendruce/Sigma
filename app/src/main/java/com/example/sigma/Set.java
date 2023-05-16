// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR THE SET
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;

public class Set {
    private int setNumber;
    private int reps;
    private double weight;
    private Integer exerciseIndex;

    public Set(int setNumber, int reps, double weight) {
        this.setNumber = setNumber;
        this.reps = reps;
        this.weight = weight;
    }

    //getters and setters
    public int getSetNumber() {
        return setNumber;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setExerciseIndex(Integer exerciseIndex) {
        this.exerciseIndex = exerciseIndex;
    }

    public Integer getExerciseIndex() {
        return exerciseIndex;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}