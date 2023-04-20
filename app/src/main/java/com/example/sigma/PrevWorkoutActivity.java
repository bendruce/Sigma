package com.example.sigma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PrevWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_workout);
        // get the workout text from the Intent
        String workoutText = getIntent().getStringExtra("workoutText");
        String workoutTitle = getIntent().getStringExtra("workoutTitle");
        // display the workout text, e.g. in a TextView
        TextView workoutTextView = findViewById(R.id.PrevWorkoutText);
        workoutTextView.setText(workoutText);
        TextView workoutTitleTextView = findViewById(R.id.PrevWorkoutTitle);
        workoutTitleTextView.setText(workoutTitle);
        Button prevWorkoutHomeButton = findViewById(R.id.PrevWorkoutHome);
        prevWorkoutHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrevWorkoutActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}