package com.example.sigma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<WorkoutItem> workoutItems;

    public WorkoutAdapter(List<WorkoutItem> workoutItems) {
        this.workoutItems = workoutItems;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prev_workout_home_layout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutItem workoutItem = workoutItems.get(position);
        holder.nameTextView.setText(workoutItem.getName());
        holder.dateTextView.setText(workoutItem.getDate());
        holder.lengthTextView.setText(workoutItem.getLength());
    }

    @Override
    public int getItemCount() {
        return workoutItems.size();
    }
    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView dateTextView;
        private TextView lengthTextView;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.workoutTitle);
            dateTextView = itemView.findViewById(R.id.workoutDate);
            lengthTextView = itemView.findViewById(R.id.workoutLength);
        }

        public void bind(WorkoutItem workoutItem) {
            nameTextView.setText(workoutItem.getName());
            dateTextView.setText(workoutItem.getDate());
            lengthTextView.setText(workoutItem.getLength());
        }
    }
}
