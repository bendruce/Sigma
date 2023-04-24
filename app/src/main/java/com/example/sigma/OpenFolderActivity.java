package com.example.sigma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OpenFolderActivity extends AppCompatActivity {

    private TextView folderTitleView;


    private List<WorkoutItem> workouts;


    private RecyclerView workoutRecyclerView;


    private WorkoutAdapter workoutAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_folder);
        String folderTitle = getIntent().getStringExtra("folderTitle");
        folderTitleView = findViewById(R.id.openFolderNameTextView);
        folderTitleView.setText(folderTitle);

        workouts = new ArrayList<>();
        // set up the workout RecyclerView and adapter
        workoutRecyclerView = findViewById(R.id.openFoldersRecyclerView);
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

                    if (folderTitle.equals(workoutFolder)) {
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
    }
}