// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR AN OPEN FOLDER
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
        //set up the workout RecyclerView and adapter
        workoutRecyclerView = findViewById(R.id.openFoldersRecyclerView);
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
                    if (folderTitle.equals(workoutFolder)) {
                        WorkoutItem workoutItem = new WorkoutItem(workoutName, workoutDate, workoutLength);
                        workouts.add(workoutItem);
                    }
                }
                workoutAdapter.notifyDataSetChanged();//notify the workout adapter that the data has changed
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OpenFolderActivity.this, "Data retrieval cancelled with error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}