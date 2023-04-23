package com.example.sigma;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private ArrayList<Exercise> exercises;



    public ExerciseAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout for a single exercise item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_layout, parent, false);
        int id = View.generateViewId();
        view.setId(id);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.textViewExerciseName.setText(exercise.getName());

        // create a new SetAdapter for the current exercise
        SetAdapter setAdapter = new SetAdapter(exercise.getSets());
        holder.recyclerViewSets.setAdapter(setAdapter);
        holder.recyclerViewSets.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        // find the addSetButton within the exercise item and set an OnClickListener
        Button addSetButton = holder.itemView.findViewById(R.id.addSetButton);
        addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new Set object and add it to the current exercise's list
                Set newSet = new Set(exercise.getSets().size() + 1, 0, 0);
                exercise.addSet(newSet);

                // notify the adapter that a new item has been added to the current exercise's list of sets
                setAdapter.notifyItemInserted(exercise.getSets().size());
            }
        });



    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewExerciseName;
        public RecyclerView recyclerViewSets;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            textViewExerciseName = itemView.findViewById(R.id.text_view_exercise_name);
            recyclerViewSets = itemView.findViewById(R.id.SetsRecyclerView);
        }
    }

    public void updateExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }
}
