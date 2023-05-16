// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR ACTIVITY WHICH DISPLAYS COMPLETED WORKOUTS
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.ShareActionProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CompleteWorkoutActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;
    private String workoutText;
    private String workoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workout);

        workoutText = getIntent().getStringExtra("workoutText");//get the workout text from the intent
        workoutTitle = getIntent().getStringExtra("workoutTitle");//get the workout title from the intent

        TextView workoutTextView = findViewById(R.id.PrevWorkoutText);
        workoutTextView.setText(workoutText);//display the workout text
        TextView workoutTitleTextView = findViewById(R.id.PrevWorkoutTitle);
        workoutTitleTextView.setText(workoutTitle);//display the workout title in the title box
        Button prevWorkoutHomeButton = findViewById(R.id.PrevWorkoutHome);
        prevWorkoutHomeButton.setOnClickListener(new View.OnClickListener() {//listener for button to return the user to the home screen
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompleteWorkoutActivity.this, HomeActivity.class);
                startActivity(intent);//starts the home screen
                finish();
            }
        });
    }
    //below is the code for the ShareActionProvider used to share a completed workout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        MenuItem item = menu.findItem(R.id.action_share);//finds the menu item with the id action_share
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);//gets the action provider for the menu item used to handle sharing functionality
        setShareIntent();//sets the sharing intent, which defines the content to be shared
        return true;
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {//checks if the ShareActionProvider has been initialized
            Intent shareIntent = new Intent(Intent.ACTION_SEND);//creates a new sharing intent
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, workoutTitle + "\n" + workoutText);//adds the workout title and text as extra data to the intent(content to share)
            mShareActionProvider.setShareIntent(shareIntent);//makes the content available for sharing
        }
    }
}
