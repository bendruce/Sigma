package com.example.sigma;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editTextExerciseName;
    private EditText editTextWorkoutTitle;
    private Button buttonAddExercise;
    private RecyclerView recyclerViewExercises;
    private RecyclerView recyclerViewSets;
    private Workout workout;
    private List<Set> sets = new ArrayList<>();
    private SetAdapter setAdapter;

    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();



    @SuppressLint({"MissingInflateParams", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find the UI components by their IDs
        editTextWorkoutTitle = findViewById(R.id.WorkoutTitle);
        editTextExerciseName = findViewById(R.id.edit_text_exercise_name);
        buttonAddExercise = findViewById(R.id.button_add_exercise);
        recyclerViewExercises = findViewById(R.id.recycler_view_exercises);

        // create a new Workout object
        workout = new Workout();

        // set up the RecyclerView adapter for exercises
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(workout.getExercises());
        recyclerViewExercises.setAdapter(exerciseAdapter);
        recyclerViewExercises.setLayoutManager(new LinearLayoutManager(this));

        // create an instance of InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // set an OnFocusChangeListener to hide the keyboard when the user clicks off the editTextExerciseName
        editTextExerciseName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        // set up the TouchListener to hide the keyboard when the user clicks elsewhere on the screen
        findViewById(R.id.WorkoutScreen).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        // set up the button click listener
        buttonAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextExerciseName.getText().toString();
                Exercise exercise = new Exercise(name);
                workout.addExercise(exercise);
                exerciseAdapter.notifyDataSetChanged();
            }
        });







        // find the finishWorkoutButton and set an OnClickListener
        Button finishWorkoutButton = findViewById(R.id.finishWorkoutButton);
        finishWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    StringBuilder sb = new StringBuilder();
// iterate over all the exercises in the workout
                    databaseRef.child("workouts").child("date").setValue((LocalDate.now()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

                    for (Exercise exercise : workout.getExercises()) {
                        DatabaseReference exerciseRef = databaseRef.child("workouts").child(editTextWorkoutTitle.getText().toString()).child("exercises").push();
                        exerciseRef.child("exercise name").setValue(exercise.getName());

                        // add the exercise name to the StringBuilder
                        sb.append(exercise.getName()).append(":").append("\n");
                        // iterate over all the sets in the exercise
                        for (Set set : exercise.getSets()) {
                            // create a new Set node in Firebase
                            DatabaseReference setRef = exerciseRef.child("sets").push();
                            setRef.child("setNumber").setValue(set.getSetNumber());
                            setRef.child("reps").setValue(set.getReps());
                            setRef.child("weight").setValue(set.getWeight());
                            // add the set information to the StringBuilder
                            sb.append(" Set ").append(set.getSetNumber()).append(": ").append(set.getReps()).append(" x ")
                                    .append(set.getWeight()).append("Kg").append("\n");
                        }
                        // add a new line after each exercise
                        sb.append("\n");

                    }

                    // convert the StringBuilder to a String
                    String workoutText = sb.toString();
                    Log.i("LOOK HERE",workoutText);
                    // display the workout text, e.g. in a Toast message
                    Toast.makeText(MainActivity.this, workoutText, Toast.LENGTH_LONG).show();
                    // create an intent to start the PrevWorkoutActivity
                    Intent intent = new Intent(MainActivity.this, PrevWorkoutActivity.class);
                    // add the workout text and title as extras
                    intent.putExtra("workoutText", workoutText);
                    intent.putExtra("workoutTitle", editTextWorkoutTitle.getText().toString());
                    // start the activity
                    //databaseRef.child("workouts").child(editTextWorkoutTitle.getText().toString()).push().setValue(workoutText);
                    startActivity(intent);
                } catch (Exception e) {
                    // handle any exceptions that occur during the workout data creation
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });








    }
}
/*
recyclerViewSets = findViewById(R.id.recycler_view_sets);

                if (setAdapter != null) {
                setAdapter = new SetAdapter(sets);
                recyclerViewSets.setAdapter(setAdapter);
                recyclerViewSets.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }

            // find the addSetButton and set an OnClickListener
            Button addSetButton = findViewById(R.id.addSetButton);
                addSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // create a new Set object and add it to the list
                    Set newSet = new Set(sets.size() + 1, 0, 0); // you can set the reps and weight to default values
                    sets.add(newSet);

                    // notify the adapter that a new item has been added
                    setAdapter.notifyItemInserted(sets.size() - 1);
                }
            });
 */
/*
buttonAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the data from the EditText fields
                String name = editTextExerciseName.getText().toString();


                // create a new Exercise object and add it to the Workout object's ArrayList
                Exercise exercise = new Exercise(name);
                workout.addExercise(exercise);

                // update the adapter with the new list of exercises
                exerciseAdapter.updateExercises(workout.getExercises());

                // notify the RecyclerView adapter that the data has changed
                exerciseAdapter.notifyDataSetChanged();

                // set up the RecyclerView adapter for sets if it has not been initialized

            }

        });
 */