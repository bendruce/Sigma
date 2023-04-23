package com.example.sigma;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sigma.FolderAdapter;
import com.example.sigma.FolderItem;
import com.example.sigma.MainActivity;
import com.example.sigma.PersonalRecordsActivity;
import com.example.sigma.R;
import com.example.sigma.WorkoutAdapter;
import com.example.sigma.WorkoutItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<FolderItem> folders;
    private List<WorkoutItem> workouts;

    private RecyclerView folderRecyclerView;
    private RecyclerView workoutRecyclerView;

    private FolderAdapter folderAdapter;
    private WorkoutAdapter workoutAdapter;

    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // create some sample data for folders and workouts
        folders = new ArrayList<>();
        folders.add(new FolderItem("Folder 1", 5));
        folders.add(new FolderItem("Folder 2", 3));

        workouts = new ArrayList<>();
        workouts.add(new WorkoutItem("Workout 1", "01/01/2022", "30 mins"));
        workouts.add(new WorkoutItem("Workout 2", "02/01/2022", "45 mins"));

        // set up the folder RecyclerView and adapter
        folderRecyclerView = findViewById(R.id.WorkoutsRecyclerView);
        folderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        folderAdapter = new FolderAdapter(folders);
        folderRecyclerView.setAdapter(folderAdapter);

        // set up the workout RecyclerView and adapter
        workoutRecyclerView = findViewById(R.id.WorkoutsRecyclerView);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutAdapter = new WorkoutAdapter(workouts);
        workoutRecyclerView.setAdapter(workoutAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference usersRef = database.getReference("https://sigma-9eb55-default-rtdb.firebaseio.com/workouts");


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
                        //databaseRef.child("workouts").child(editTextWorkoutTitle.getText().toString()).push().setValue(workoutText);

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
        i.putExtra("Message", "This is Settings");
        startActivity(i);
    }

    public void launchNewExercise(View v){
        //launch new activity
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Message", "This is Settings");
        startActivity(i);
    }
}
