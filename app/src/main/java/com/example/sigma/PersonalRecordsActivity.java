package com.example.sigma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

        workoutsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called when the data is successfully fetched from the database.
                // You can perform your operations here.
                Log.d("TAG", "Data fetched successfully: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // This method is called if the data retrieval is cancelled due to some error.
                Log.d("TAG", "Data retrieval cancelled with error: " + databaseError.getMessage());
            }
        });
        workoutsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot workoutSnapshot : snapshot.getChildren()) {
                    DataSnapshot exercisesSnapshot = workoutSnapshot.child("exercises");

                    // Loop through all the exercises in the workout
                    for (DataSnapshot exerciseSnapshot : exercisesSnapshot.getChildren()) {
                        String exerciseName = exerciseSnapshot.child("exercise name").getValue(String.class);

                        // Only consider Squat, Bench, and Deadlift exercises
                        if (exerciseName.equals("Squat") || exerciseName.equals("Bench") || exerciseName.equals("Deadlift")) {
                            DataSnapshot setsSnapshot = exerciseSnapshot.child("sets");
                            int highestReps = 0;
                            double highestWeight = 0.0;
                            int previousReps = 0;
                            double previousWeight = 0.0;

                            // Loop through all the sets for the exercise
                            for (DataSnapshot setSnapshot : setsSnapshot.getChildren()) {
                                Integer repsObject = setSnapshot.child("reps").getValue(Integer.class);
                                Double weightObject = setSnapshot.child("weight").getValue(Double.class);

                                if (repsObject != null && weightObject != null) {
                                    int reps = repsObject.intValue();
                                    double weight = weightObject.doubleValue();

                                    // Check if this set has a higher reps and weight combo than the previous highest
                                    //(weight > highestWeight) || (weight==highestWeight && reps>highestReps)
                                    //reps * weight > highestReps * highestWeight
                                    if ((weight > highestWeight) || (weight==highestWeight && reps>highestReps)) {
                                        previousReps = highestReps;
                                        previousWeight = highestWeight;
                                        highestReps = reps;
                                        highestWeight = weight;
                                    } else if (reps * weight > previousReps * previousWeight) {
                                        previousReps = reps;
                                        previousWeight = weight;
                                    }
                                } else {
                                    Log.d("TAG", "Either reps or weight is null");
                                }
                            }
                            System.out.println(highestReps);
                            System.out.println(exerciseName);
                            System.out.println(highestWeight);
                            // Print the highest and previous personal records for the exercise
                            if ("Squat".equals(exerciseName)){
                                TextView squatRepBest = findViewById(R.id.squatCurrentReps);
                                TextView squatWeightBest = findViewById(R.id.squatCurrentWeight);
                                TextView squatRepPrevious = findViewById(R.id.squatPrevReps2);
                                TextView squatWeightPrevious = findViewById(R.id.squatPrevWeight2);

                                squatRepBest.setText(String.valueOf(highestReps));
                                squatWeightBest.setText(String.valueOf(highestWeight));
                                squatRepPrevious.setText(String.valueOf(previousReps));
                                squatWeightPrevious.setText(String.valueOf(previousWeight));
                            }
                            if ("Bench".equals(exerciseName)){
                                TextView benchRepBest = findViewById(R.id.benchCurrentReps2);
                                TextView benchWeightBest = findViewById(R.id.benchCurrentWeight2);
                                TextView benchRepPrevious = findViewById(R.id.benchPrevReps2);
                                TextView benchWeightPrevious = findViewById(R.id.benchPrevWeight2);

                                benchRepBest.setText(String.valueOf(highestReps));
                                benchWeightBest.setText(String.valueOf(highestWeight));
                                benchRepPrevious.setText(String.valueOf(previousReps));
                                benchWeightPrevious.setText(String.valueOf(previousWeight));
                            }
                            if ("Deadlift".equals(exerciseName)){
                                TextView deadRepBest = findViewById(R.id.deadCurrentReps);
                                TextView deadWeightBest = findViewById(R.id.deadCurrentWeight2);
                                TextView deadRepPrevious = findViewById(R.id.deadPrevReps);
                                TextView deadWeightPrevious = findViewById(R.id.deadPrevWeight2);

                                deadRepBest.setText(String.valueOf(highestReps));
                                deadWeightBest.setText(String.valueOf(highestWeight));
                                deadRepPrevious.setText(String.valueOf(previousReps));
                                deadWeightPrevious.setText(String.valueOf(previousWeight));
                            }



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