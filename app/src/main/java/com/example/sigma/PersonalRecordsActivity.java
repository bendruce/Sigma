// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR THE PERSONAL RECORDS PAGE
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                    for (DataSnapshot exerciseSnapshot : exercisesSnapshot.getChildren()) {//loop through all the exercises in the workout
                        String exerciseName = exerciseSnapshot.child("exercise name").getValue(String.class);
                        //Only consider Squat, Bench, and Deadlift exercises
                        if (exerciseName.equals("Squat") || exerciseName.equals("Bench") || exerciseName.equals("Deadlift")) {
                            DataSnapshot setsSnapshot = exerciseSnapshot.child("sets");
                            for (DataSnapshot setSnapshot : setsSnapshot.getChildren()) {//Loop through all the sets for the exercise
                                Integer repsObject = setSnapshot.child("reps").getValue(Integer.class);
                                Double weightObject = setSnapshot.child("weight").getValue(Double.class);

                                if (repsObject != null && weightObject != null) {
                                    int reps = repsObject.intValue();
                                    double weight = weightObject.doubleValue();
                                    if ((weight > highestWeightSquat|| (weight==highestWeightSquat && reps>highestRepsSquat))&&"Squat".equals(exerciseName)) {
                                        previousRepsSquat = highestRepsSquat;
                                        previousWeightSquat = highestWeightSquat;
                                        highestRepsSquat = reps;
                                        highestWeightSquat = weight;
                                    } else if ((weight > previousWeightSquat || (weight==previousWeightSquat && reps>previousRepsSquat)) &&"Squat".equals(exerciseName)) {
                                        previousRepsSquat = reps;
                                        previousWeightSquat = weight;
                                    }
                                    if ((weight > highestWeightBench || (weight==highestWeightBench && reps>highestRepsBench) )&&"Bench".equals(exerciseName) ) {
                                        previousRepsBench = highestRepsBench;
                                        previousWeightBench = highestWeightBench;
                                        highestRepsBench = reps;
                                        highestWeightBench = weight;
                                    } else if ((weight > previousWeightBench || (weight==previousWeightBench && reps>previousRepsBench)) &&"Bench".equals(exerciseName)) {
                                        previousRepsBench = reps;
                                        previousWeightBench = weight;
                                    }
                                    if ((weight > highestWeightDead || (weight==highestWeightDead && reps>highestRepsDead))&&"Deadlift".equals(exerciseName) ) {
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
                Toast.makeText(PersonalRecordsActivity.this, "Data retrieval cancelled with error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        Button searchExercise = findViewById(R.id.searchTrackButton);
        searchExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        EditText userTrackExercise = findViewById(R.id.userExercise);
                        String exerciseName = String.valueOf(userTrackExercise.getText());
                        boolean foundExercise = false;
                        for (DataSnapshot workoutSnapshot : snapshot.getChildren()) {//loop through all the workouts
                            DataSnapshot exercisesSnapshot = workoutSnapshot.child("exercises");
                            for (DataSnapshot exerciseSnapshot : exercisesSnapshot.getChildren()) {//loop through all the exercises in the workout
                                String name = exerciseSnapshot.child("exercise name").getValue(String.class);
                                if (name.equals(exerciseName)) {//check if this is the exercise we're looking for
                                    foundExercise = true;
                                    DataSnapshot setsSnapshot = exerciseSnapshot.child("sets");
                                    int highestReps = 0;
                                    double highestWeight = 0.0;
                                    for (DataSnapshot setSnapshot : setsSnapshot.getChildren()) {
                                        Integer repsObject = setSnapshot.child("reps").getValue(Integer.class);
                                        Double weightObject = setSnapshot.child("weight").getValue(Double.class);

                                        if (repsObject != null && weightObject != null) {
                                            int reps = repsObject.intValue();
                                            double weight = weightObject.doubleValue();
                                            //check if this set has a higher reps and weight combo than the previous highest
                                            if (weight > highestWeight || (weight == highestWeight && reps > highestReps)) {
                                                highestReps = reps;
                                                highestWeight = weight;
                                            }
                                        }
                                    }
                                    int previousReps = 0;
                                    double previousWeight = 0.0;
                                    for (DataSnapshot setSnapshot : setsSnapshot.getChildren()) {
                                        Integer repsObject = setSnapshot.child("reps").getValue(Integer.class);
                                        Double weightObject = setSnapshot.child("weight").getValue(Double.class);

                                        if (repsObject != null && weightObject != null) {
                                            int reps = repsObject.intValue();
                                            double weight = weightObject.doubleValue();
                                            //check if this set has a higher reps and weight combo than the previous highest,
                                            //but lower than the current highest
                                            if (weight > previousWeight && weight < highestWeight
                                                    || (weight == previousWeight && reps > previousReps && weight < highestWeight)
                                                    || (weight == highestWeight && reps < highestReps)) {
                                                previousReps = reps;
                                                previousWeight = weight;
                                            }
                                        }
                                    }
                                    System.out.println("Previous personal record for " + exerciseName + ": " + previousWeight + " lbs for " + previousReps + " reps");
                                    TextView userRepBest = findViewById(R.id.userCurrentReps);
                                    TextView userWeightBest = findViewById(R.id.userCurrentWeight);
                                    TextView userRepPrevious = findViewById(R.id.userPrevReps);
                                    TextView userWeightPrevious = findViewById(R.id.userPrevWeight);

                                    userRepBest.setText(String.valueOf(highestReps));
                                    userWeightBest.setText(String.valueOf(highestWeight));
                                    userRepPrevious.setText(String.valueOf(previousReps));
                                    userWeightPrevious.setText(String.valueOf(previousWeight));
                                    break;
                                }
                            }
                            if (foundExercise) {
                                break;
                            }
                        }
                        if (!foundExercise) {
                            System.out.println("Could not find exercise: " + exerciseName);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // This method is called if the data retrieval is cancelled due to some error.
                        Log.d("TAG", "Data retrieval cancelled with error: " + databaseError.getMessage());
                    }
                });
            }
        });
    }
}