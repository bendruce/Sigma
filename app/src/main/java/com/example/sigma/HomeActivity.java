package com.example.sigma;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // find the addWorkoutFolderBtn button by its ID
        Button addWorkoutFolderBtn = findViewById(R.id.addWrkoutFolderBtn);

        // set the click listener for the addWorkoutFolderBtn button
        addWorkoutFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an AlertDialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.CustomAlertDialogTheme);


                // set the title and message
                builder.setTitle("Enter Folder Name:");
                builder.setMessage("Please enter the name of the workout folder:");

                // create a EditText view to get user input
                final EditText input = new EditText(HomeActivity.this);
                builder.setView(input);

                // set the positive button action
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get the user input text
                        String folderName = input.getText().toString();
                        // do something with the folderName
                        Toast.makeText(HomeActivity.this, "Folder name: " + folderName, Toast.LENGTH_SHORT).show();

                    }
                });

                // set the negative button action
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user cancelled the dialog
                    }
                });

                // create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public void launchPRs(View v){
        //launch new activity
        Intent i = new Intent(this, PersonalRecordsActivity.class);
        //i.putExtra("Message", "This is Settings");
        startActivity(i);

    }
    public void launchNewExercise(View v){
        //launch new activity
        Intent i = new Intent(this, MainActivity.class);
        //i.putExtra("Message", "This is Settings");
        startActivity(i);

    }
}