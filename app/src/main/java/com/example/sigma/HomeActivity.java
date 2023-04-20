package com.example.sigma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sigma.FolderAdapter;
import com.example.sigma.FolderItem;
import com.example.sigma.MainActivity;
import com.example.sigma.PersonalRecordsActivity;
import com.example.sigma.R;
import com.example.sigma.WorkoutAdapter;
import com.example.sigma.WorkoutItem;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<FolderItem> folders;
    private List<WorkoutItem> workouts;

    private RecyclerView folderRecyclerView;
    private RecyclerView workoutRecyclerView;

    private FolderAdapter folderAdapter;
    private WorkoutAdapter workoutAdapter;

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
