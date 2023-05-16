// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR THE HOME PAGE
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.Manifest;

public class HomeActivity extends AppCompatActivity {//main activity class
    //defining variables for folders and workouts
    private List<FolderItem> folders;
    private List<WorkoutItem> workouts;
    //defining variables for recyclerview and adapters
    private RecyclerView folderRecyclerView;
    private RecyclerView workoutRecyclerView;
    private FolderAdapter folderAdapter;
    private WorkoutAdapter workoutAdapter;

    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();//defining database reference

    private static final int requestCode = 0;//request code for permissions


    @Override
    protected void onCreate(Bundle savedInstanceState) {//main method for creating the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //requesting permissions
        String[] permissions = {Manifest.permission.ACCESS_NOTIFICATION_POLICY, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, requestCode);
        requestPermissions(permissions, requestCode);
        //setting up calendar for workout reminders
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        //setting up workout reminder
        setWorkoutReminderAlarm reminder = new setWorkoutReminderAlarm(this);
        reminder.setReminder(calendar);
        //initializing folders arraylist and setting up recyclerview and adapter
        folders = new ArrayList<>();
        folderRecyclerView = findViewById(R.id.FoldersRecyclerView);
        folderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        folderAdapter = new FolderAdapter(folders);
        folderRecyclerView.setAdapter(folderAdapter);
        //getting reference to folders in firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference foldersRef = database.getReference("folders");

        foldersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//called when the data is successfully fetched from the database.
                Log.d("TAG", "Data fetched successfully: " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {//called if the data retrieval is cancelled due to some error.
                Toast.makeText(HomeActivity.this, "Data retrieval cancelled with error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        foldersRef.addValueEventListener(new ValueEventListener() {//attach a listener to the folders reference to detect when the data changes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                folders.clear();//clear the existing list of folders
                for (DataSnapshot folderSnapshot : dataSnapshot.getChildren()) {//iterate over each child of the folders reference
                    String folderName = folderSnapshot.getValue(String.class);
                    FolderItem folderItem = new FolderItem(folderName, 0);
                    folders.add(folderItem);
                }
                folderAdapter.notifyDataSetChanged();//notify the folder adapter that the data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Data retrieval cancelled with error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //set up the workout RecyclerView and adapter
        workouts = new ArrayList<>();
        workoutRecyclerView = findViewById(R.id.WorkoutsRecyclerView);
        workoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutAdapter = new WorkoutAdapter(workouts);
        workoutRecyclerView.setAdapter(workoutAdapter);
        DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("workouts");


        workoutsRef.addValueEventListener(new ValueEventListener() {//attach a listener to the workouts reference to detect when the data changes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                workouts.clear();//clear the existing list of workouts
                for (DataSnapshot workoutSnapshot : dataSnapshot.getChildren()) {//iterate over each child of the workouts reference
                    String workoutName = workoutSnapshot.getKey();
                    DataSnapshot dateSnapshot = workoutSnapshot.child("date");
                    DataSnapshot lengthSnapshot = workoutSnapshot.child("length");
                    DataSnapshot folderSnapshot = workoutSnapshot.child("folder");
                    String workoutDate = dateSnapshot.getValue(String.class);
                    String workoutLength = lengthSnapshot.getValue(String.class);
                    String workoutFolder = folderSnapshot.getValue(String.class);
                    if ("Home".equals(workoutFolder)) {
                        WorkoutItem workoutItem = new WorkoutItem(workoutName, workoutDate, workoutLength);
                        workouts.add(workoutItem);//Add workout item to the list if the workout belongs in Home
                    }
                }
                workoutAdapter.notifyDataSetChanged();//notify the workout adapter that the data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Data retrieval cancelled with error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



        Button addWorkoutFolderBtn = findViewById(R.id.addWrkoutFolderBtn);
        addWorkoutFolderBtn.setOnClickListener(new View.OnClickListener() {//set the click listener for the addWorkoutFolderBtn button
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.CustomAlertDialogTheme);//create an AlertDialog Builder
                //set the title and message
                builder.setTitle("Enter Folder Name:");
                builder.setMessage("Please enter the name of the workout folder:");
                final EditText input = new EditText(HomeActivity.this);//create a EditText view to get user input
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {//set the positive button action
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String folderName = input.getText().toString();//get the user input text
                        DatabaseReference foldersRef = databaseRef.child("folders");//retrieve the list of folder names from the Firebase Realtime Database
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
                                    //if the folder name already exists, show an error message
                                    Toast.makeText(HomeActivity.this, "Folder name already exists!", Toast.LENGTH_SHORT).show();
                                } else {
                                    //if the folder name does not exist, add it to the Firebase Realtime Database
                                    String key = databaseRef.child("folders").push().getKey();
                                    databaseRef.child("folders").child(key).setValue(folderName);
                                    Toast.makeText(HomeActivity.this, "Folder added successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(HomeActivity.this, "Data retrieval cancelled with error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {//set the negative button action
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //user cancelled the dialog, nothing goes here
                    }
                });
                //create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //code to open the user guide \/\/\/\/\/\/
        ImageButton guideButton = findViewById(R.id.openGuideButton);
        guideButton.setOnClickListener(new View.OnClickListener() {//set the click listener for the addWorkoutFolderBtn button
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, UserGuideActivity.class);
                startActivity(i);
            }
        });

        //code to use an intent to move to an external app \/\/\/\/\/\/
        ImageButton openLinkButton = findViewById(R.id.openLinkButton);
        openLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.lboro.ac.uk/sport/facilities/gyms/powerbase/";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }

    public void launchPRs(View v){//opens the prs page
        Intent i = new Intent(this, PersonalRecordsActivity.class);
        startActivity(i);
    }

    public void launchNewExercise(View v){//opens the main activity page (a new workout)
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public class setWorkoutReminderAlarm {
        private Context context;
        public setWorkoutReminderAlarm(Context context) {
            this.context = context;
        }
        public void setReminder(Calendar calendar) {
            Intent intent = new Intent(context, WorkoutReminderBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}


