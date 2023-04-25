package com.example.sigma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalRecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_records);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference workoutsRef = database.getReference().child("workouts");

// Loop through all the workouts
        workoutsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("LOOK HERE"+"/n");
                for (DataSnapshot workoutSnapshot : snapshot.getChildren()) {
                    DataSnapshot exercisesSnapshot = workoutSnapshot.child("exercises");

                    // Loop through all the exercises in the workout
                    for (DataSnapshot exerciseSnapshot : exercisesSnapshot.getChildren()) {
                        String exerciseName = exerciseSnapshot.child("exercise name").getValue(String.class);

                        // Only consider Squat, Bench, and Deadlift exercises
                        if (exerciseName.equals("Squat") || exerciseName.equals("Bench") || exerciseName.equals("Deadlift")) {
                            System.out.println("LOOK HERE");
                            DataSnapshot setsSnapshot = exerciseSnapshot.child("sets");
                            int highestReps = 0;
                            double highestWeight = 0.0;

                            // Loop through all the sets for the exercise
                            for (DataSnapshot setSnapshot : setsSnapshot.getChildren()) {
                                int reps = setSnapshot.child("reps").getValue(Integer.class);
                                int setNumber = setSnapshot.child("setNumber").getValue(Integer.class);
                                double weight = setSnapshot.child("weight").getValue(Double.class);

                                // Check if this set has a higher reps and weight combo than the previous highest
                                if (reps * weight > highestReps * highestWeight) {
                                    highestReps = reps;
                                    highestWeight = weight;
                                }
                            }

                            // Print the highest reps and weight combo for the exercise
                            if ("Squat".equals(exerciseName)){
                                TextView squatRepBest = findViewById(R.id.squatCurrentReps);
                                System.out.println(String.valueOf(highestReps));
                                squatRepBest.setText(String.valueOf(highestReps));
                                TextView squatWeightBest = findViewById(R.id.squatCurrentWeight);
                                squatWeightBest.setText(String.valueOf(highestWeight));
                            }

                            System.out.println("Highest combo for " + exerciseName + ": " + highestReps + " reps x " + highestWeight + " Kg");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


    }
}