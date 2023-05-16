package com.example.sigma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {//adapter for populating a RecyclerView with exercise data
    private ArrayList<Exercise> exercises;
    public ExerciseAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }//constructor takes a list of exercises

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//inflate the layout for a single exercise item when creating a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_layout, parent, false);//inflate the layout for a single exercise item
        int id = View.generateViewId();
        view.setId(id);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {//bind the exercise data to the viewholder
        Exercise exercise = exercises.get(position);
        holder.textViewExerciseName.setText(exercise.getName());
        //create a new SetAdapter for the current exercise
        SetAdapter setAdapter = new SetAdapter(exercise.getSets());
        holder.recyclerViewSets.setAdapter(setAdapter);
        holder.recyclerViewSets.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        //find the addSetButton within the exercise item and set an OnClickListener
        Button addSetButton = holder.itemView.findViewById(R.id.addSetButton);
        addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set newSet = new Set(exercise.getSets().size() + 1, 0, 0);//create a new Set object and add it to the current exercise's list
                exercise.addSet(newSet);
                setAdapter.notifyItemInserted(exercise.getSets().size());//notify the adapter that a new item has been added to the current exercise's list of sets
            }
        });



    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }//return the size of the exercise list

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {//define the ViewHolder for an exercise item
        public TextView textViewExerciseName;
        public RecyclerView recyclerViewSets;
        public ExerciseViewHolder(View itemView) {//find and store the necessary views within the exercise item
            super(itemView);
            textViewExerciseName = itemView.findViewById(R.id.text_view_exercise_name);
            recyclerViewSets = itemView.findViewById(R.id.SetsRecyclerView);
        }
    }

    public void updateExercises(ArrayList<Exercise> exercises) {//update the exercise list and notify the adapter of the change
        this.exercises = exercises;
        notifyDataSetChanged();
    }
}
