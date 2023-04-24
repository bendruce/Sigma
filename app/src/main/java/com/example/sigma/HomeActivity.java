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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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


        folders = new ArrayList<>();
        folderRecyclerView = findViewById(R.id.FoldersRecyclerView);
        folderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        folderAdapter = new FolderAdapter(folders);
        folderRecyclerView.setAdapter(folderAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference foldersRef = database.getReference("folders");


        // attach a listener to the folders reference to detect when the data changes
        foldersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clear the existing list of folders
                folders.clear();

                // iterate over each child of the folders reference
                for (DataSnapshot folderSnapshot : dataSnapshot.getChildren()) {
                    String folderName = folderSnapshot.getValue(String.class);
                    FolderItem folderItem = new FolderItem(folderName, 0);

                    //Toast.makeText(HomeActivity.this, "Folder: " + folderItem, Toast.LENGTH_SHORT).show();

                    folders.add(folderItem);

                }

                // notify the folder adapter that the data has changed
                folderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // handle any errors
            }
        });

        // create a reference to the Firebase database location where the workouts are stored
        workouts = new ArrayList<>();
        // set up the workout RecyclerView and adapter
        workoutRecyclerView = findViewById(R.id.WorkoutsRecyclerView);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutAdapter = new WorkoutAdapter(workouts);
        workoutRecyclerView.setAdapter(workoutAdapter);
        DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("workouts");

        // attach a listener to the workouts reference to detect when the data changes
        workoutsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clear the existing list of workouts
                workouts.clear();

                // iterate over each child of the workouts reference
                for (DataSnapshot workoutSnapshot : dataSnapshot.getChildren()) {
                    String workoutName = workoutSnapshot.getKey();
                    DataSnapshot dateSnapshot = workoutSnapshot.child("date");
                    DataSnapshot lengthSnapshot = workoutSnapshot.child("length");
                    DataSnapshot folderSnapshot = workoutSnapshot.child("folder");
                    String workoutDate = dateSnapshot.getValue(String.class);
                    String workoutLength = lengthSnapshot.getValue(String.class);
                    String workoutFolder = folderSnapshot.getValue(String.class);

                    if ("Home".equals(workoutFolder)) {
                        WorkoutItem workoutItem = new WorkoutItem(workoutName, workoutDate, workoutLength);
                        workouts.add(workoutItem);
                    }

                }

                // notify the workout adapter that the data has changed
                workoutAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // handle any errors
            }
        });


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
                        final String folderName = input.getText().toString();

                        // retrieve the list of folder names from the Firebase Realtime Database
                        DatabaseReference foldersRef = databaseRef.child("folders");
                        foldersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean folderExists = false;
                                for (DataSnapshot folderSnapshot : dataSnapshot.getChildren()) {
                                    String existingFolderName = folderSnapshot.getValue(String.class);
                                    if (existingFolderName.equals(folderName)) {
                                        folderExists = true;
                                        break;
                                    }
                                }

                                if (folderExists) {
                                    // if the folder name already exists, show an error message
                                    Toast.makeText(HomeActivity.this, "Folder name already exists!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // if the folder name does not exist, add it to the Firebase Realtime Database
                                    String key = databaseRef.child("folders").push().getKey();
                                    databaseRef.child("folders").child(key).setValue(folderName);
                                    Toast.makeText(HomeActivity.this, "Folder added successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // handle any errors
                            }
                        });
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
