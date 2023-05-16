// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR THE WORKOUTS
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private EditText editTextExerciseName;
    private EditText editTextWorkoutTitle;
    private Button buttonAddExercise;
    private TextView editTextWorkoutDuration;
    private RecyclerView recyclerViewExercises;
    private RecyclerView recyclerViewSets;
    private Workout workout;
    private List<Set> sets = new ArrayList<>();
    private SetAdapter setAdapter;

    private long timerValue;

    private TextView stopWatchView;
    private ImageButton stopWatchButton;
    private CountDownTimer stopWatch;
    private long startTime;



    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();



    @SuppressLint({"MissingInflateParams", "MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, CountDownService.class);
        startService(intent);//use CountDownService service provider
        //find the UI components
        editTextWorkoutTitle = findViewById(R.id.WorkoutTitle);
        editTextExerciseName = findViewById(R.id.edit_text_exercise_name);
        buttonAddExercise = findViewById(R.id.button_add_exercise);
        editTextWorkoutDuration = findViewById(R.id.workoutDuration);
        recyclerViewExercises = findViewById(R.id.recycler_view_exercises);
        //create a new Workout object
        workout = new Workout();
        //set up the RecyclerView adapter for exercises
        ExerciseAdapter exerciseAdapter = new ExerciseAdapter(workout.getExercises());
        recyclerViewExercises.setAdapter(exerciseAdapter);
        recyclerViewExercises.setLayoutManager(new LinearLayoutManager(this));
        stopWatchView = findViewById(R.id.stopWatchView);
        stopWatchButton = findViewById(R.id.stopWatchButton);
        stopWatch = new CountDownTimer(1000000000, 1000) {//create a new CountDownTimer object with a 1 second duration and 1 second intervals
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsedTime = System.currentTimeMillis() - startTime;//calculate the elapsed time
                //convert milliseconds to hours, minutes, and seconds
                long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes);
                String elapsedTimeString = String.format("%02d:%02d" , minutes, seconds);
                stopWatchView.setText(elapsedTimeString);
            }

            @Override
            public void onFinish() {
                //stopwatch should realistically never reach this stage so nothing needs to be done
            }
        };

        startTime = System.currentTimeMillis();//set the start time to the current time
        stopWatch.start();//start the timer
        stopWatchButton.setOnClickListener(new View.OnClickListener() {//set up the click listener for the stopWatchButton to reset the stopwatch
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();//reset the start time to the current time
            }
        });
        buttonAddExercise.setOnClickListener(new View.OnClickListener() {// set up the button click listener for new exercises
            @Override
            public void onClick(View v) {
                String name = editTextExerciseName.getText().toString();
                Exercise exercise = new Exercise(name);//add a new exercise
                workout.addExercise(exercise);
                exerciseAdapter.notifyDataSetChanged();
            }
        });


        Button finishWorkoutButton = findViewById(R.id.finishWorkoutButton);
        finishWorkoutButton.setOnClickListener(new View.OnClickListener() {//OnClickListener for the finsish workout button
            @Override
            public void onClick(View v) {
                try {
                    long elapsedMillis = 30000000 - timerValue;
                    //convert milliseconds to hours, minutes, and seconds
                    long hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) - TimeUnit.HOURS.toMinutes(hours);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);
                    String elapsedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    StringBuilder sb = new StringBuilder();
                    databaseRef.child("workouts").child(editTextWorkoutTitle.getText().toString()).child("date").setValue((LocalDate.now()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    databaseRef.child("workouts").child(editTextWorkoutTitle.getText().toString()).child("length").setValue(elapsedTime);
                    databaseRef.child("workouts").child(editTextWorkoutTitle.getText().toString()).child("folder").setValue("Home");

                    for (Exercise exercise : workout.getExercises()) {//iterate over all the exercises in the workout
                        DatabaseReference exerciseRef = databaseRef.child("workouts").child(editTextWorkoutTitle.getText().toString()).child("exercises").push();
                        exerciseRef.child("exercise name").setValue((exercise.getName()).substring(0, 1).toUpperCase() + (exercise.getName()).substring(1));
                        sb.append((exercise.getName()).substring(0, 1).toUpperCase() + (exercise.getName()).substring(1)).append(":").append("\n");//add the exercise name to the StringBuilder
                        for (Set set : exercise.getSets()) {//iterate over all the sets in the exercise
                            //create a new Set node in Firebase
                            DatabaseReference setRef = exerciseRef.child("sets").push();
                            setRef.child("setNumber").setValue(set.getSetNumber());
                            setRef.child("reps").setValue(set.getReps());
                            setRef.child("weight").setValue(set.getWeight());
                            //add the set information to the StringBuilder
                            sb.append(" Set ").append(set.getSetNumber()).append(": ").append(set.getReps()).append(" x ")
                                    .append(set.getWeight()).append("Kg").append("\n");
                        }
                        sb.append("\n");//add a new line after each exercise in the string builder
                    }
                    String workoutText = sb.toString();//convert the StringBuilder to a String
                    databaseRef.child("workouts").child(editTextWorkoutTitle.getText().toString()).child("asString").setValue(workoutText);

                    Intent intent = new Intent(MainActivity.this, CompleteWorkoutActivity.class);// create an intent to start the PrevWorkoutActivity
                    //add the workout text and title as extras
                    intent.putExtra("workoutText", workoutText);
                    intent.putExtra("workoutTitle", editTextWorkoutTitle.getText().toString());
                    //start the activity
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    //handle any exceptions that occur during the workout data creation
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(br, new IntentFilter(CountDownService.COUNTDOWN_BR));
        Log.i(getClass().getSimpleName(), "Registered broadcast receiver");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(br);
        Log.i(getClass().getSimpleName(), "Unregistered broadcast receiver");
    }


    private BroadcastReceiver br = new BroadcastReceiver() {//Create a BroadcastReceiver for the timer
        @Override
        public void onReceive(Context context, Intent intent) {
            timerValue = intent.getLongExtra("countdown", 0);//update the timer value
            long elapsedMillis = 30000000 - timerValue;
            //convert milliseconds to hours, minutes, and seconds
            long hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) - TimeUnit.HOURS.toMinutes(hours);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);
            String elapsedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            editTextWorkoutDuration.setText(elapsedTime);
        }
    };

}
