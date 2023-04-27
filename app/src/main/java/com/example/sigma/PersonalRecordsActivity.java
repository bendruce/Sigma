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
                int highestRepsSquat = 0;
                double highestWeightSquat = 0.0;
                int previousRepsSquat = 0;
                double previousWeightSquat = 0.0;
                int highestRepsBench = 0;
                double highestWeightBench = 0.0;
                int previousRepsBench = 0;
                double previousWeightBench = 0.0;
                int highestRepsDead = 0;
                double highestWeightDead = 0.0;
                int previousRepsDead = 0;
                double previousWeightDead = 0.0;
                for (DataSnapshot workoutSnapshot : snapshot.getChildren()) {
                    DataSnapshot exercisesSnapshot = workoutSnapshot.child("exercises");

                    // Loop through all the exercises in the workout
                    for (DataSnapshot exerciseSnapshot : exercisesSnapshot.getChildren()) {
                        String exerciseName = exerciseSnapshot.child("exercise name").getValue(String.class);

                        // Only consider Squat, Bench, and Deadlift exercises
                        if (exerciseName.equals("Squat") || exerciseName.equals("Bench") || exerciseName.equals("Deadlift")) {
                            DataSnapshot setsSnapshot = exerciseSnapshot.child("sets");


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
                                    //|| (weight==highestWeight && reps>highestReps)

                                    if ((weight > highestWeightSquat|| (weight==highestWeightSquat && reps>highestRepsSquat))&&"Squat".equals(exerciseName)) {
                                        System.out.println("----------------");
                                        System.out.println(exerciseName);
                                        System.out.println(weight);
                                        System.out.println(highestWeightSquat);
                                        System.out.println("----------------");
                                        previousRepsSquat = highestRepsSquat;
                                        previousWeightSquat = highestWeightSquat;
                                        highestRepsSquat = reps;
                                        highestWeightSquat = weight;
                                    } else if ((weight > previousWeightSquat || (weight==previousWeightSquat && reps>previousRepsSquat)) &&"Squat".equals(exerciseName)) {
                                        previousRepsSquat = reps;
                                        previousWeightSquat = weight;
                                    }
                                    if ((weight > highestWeightBench || (weight==highestWeightBench && reps>highestRepsBench) )&&"Bench".equals(exerciseName) ) {
                                        /*
                                        System.out.println("----------------");
                                        System.out.println(exerciseName);
                                        System.out.println(weight);
                                        System.out.println(highestWeightBench);
                                        System.out.println("----------------");
                                        */

                                        previousRepsBench = highestRepsBench;
                                        previousWeightBench = highestWeightBench;
                                        highestRepsBench = reps;
                                        highestWeightBench = weight;
                                    } else if ((weight > previousWeightBench || (weight==previousWeightBench && reps>previousRepsBench)) &&"Bench".equals(exerciseName)) {
                                        previousRepsBench = reps;
                                        previousWeightBench = weight;
                                    }
                                    if ((weight > highestWeightDead || (weight==highestWeightDead && reps>highestRepsDead))&&"Deadlift".equals(exerciseName) ) {
                                        /*
                                        System.out.println("----------------");
                                        System.out.println(exerciseName);
                                        System.out.println(weight);
                                        System.out.println(highestWeightDead);
                                        System.out.println("----------------");

                                         */
                                        previousRepsDead = highestRepsDead;
                                        previousWeightDead = highestWeightDead;
                                        highestRepsDead = reps;
                                        highestWeightDead = weight;
                                    } else if ((weight > previousWeightDead || (weight==previousWeightDead && reps>previousRepsDead)) &&"Deadlift".equals(exerciseName)) {
                                        previousRepsDead = reps;
                                        previousWeightDead = weight;
                                    }
                                } else {
                                    Log.d("TAG", "Either reps or weight is null");
                                }
                            }

                            // Print the highest and previous personal records for the exercise
                            if ("Squat".equals(exerciseName)){
                                System.out.println("******************");
                                System.out.println(exerciseName);
                                System.out.println(highestRepsSquat);
                                System.out.println(highestWeightSquat);
                                System.out.println("******************");
                                TextView squatRepBest = findViewById(R.id.squatCurrentReps);
                                TextView squatWeightBest = findViewById(R.id.squatCurrentWeight);
                                TextView squatRepPrevious = findViewById(R.id.squatPrevReps2);
                                TextView squatWeightPrevious = findViewById(R.id.squatPrevWeight2);

                                squatRepBest.setText(String.valueOf(highestRepsSquat));
                                squatWeightBest.setText(String.valueOf(highestWeightSquat));
                                squatRepPrevious.setText(String.valueOf(previousRepsSquat));
                                squatWeightPrevious.setText(String.valueOf(previousWeightSquat));
                            }
                            if ("Bench".equals(exerciseName)){
                                System.out.println("******************");
                                System.out.println(exerciseName);
                                System.out.println(highestRepsBench);
                                System.out.println(highestWeightBench);
                                System.out.println("******************");
                                TextView benchRepBest = findViewById(R.id.benchCurrentReps2);
                                TextView benchWeightBest = findViewById(R.id.benchCurrentWeight2);
                                TextView benchRepPrevious = findViewById(R.id.benchPrevReps2);
                                TextView benchWeightPrevious = findViewById(R.id.benchPrevWeight2);

                                benchRepBest.setText(String.valueOf(highestRepsBench));
                                benchWeightBest.setText(String.valueOf(highestWeightBench));
                                benchRepPrevious.setText(String.valueOf(previousRepsBench));
                                benchWeightPrevious.setText(String.valueOf(previousWeightBench));
                            }
                            if ("Deadlift".equals(exerciseName)){
                                TextView deadRepBest = findViewById(R.id.deadCurrentReps);
                                TextView deadWeightBest = findViewById(R.id.deadCurrentWeight2);
                                TextView deadRepPrevious = findViewById(R.id.deadPrevReps);
                                TextView deadWeightPrevious = findViewById(R.id.deadPrevWeight2);

                                deadRepBest.setText(String.valueOf(highestRepsDead));
                                deadWeightBest.setText(String.valueOf(highestWeightDead));
                                deadRepPrevious.setText(String.valueOf(previousRepsDead));
                                deadWeightPrevious.setText(String.valueOf(previousWeightDead));
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