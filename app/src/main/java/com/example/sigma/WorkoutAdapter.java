package com.example.sigma;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<WorkoutItem> workoutItems;

    private ImageButton moveFolderBtn;

    public WorkoutAdapter(List<WorkoutItem> workoutItems) {
        this.workoutItems = workoutItems;
    }

    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

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

        // find the addWorkoutFolderBtn button by its ID
        moveFolderBtn = holder.itemView.findViewById(R.id.moveFolderButton);

        // set the click listener for the addWorkoutFolderBtn button
        moveFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an AlertDialog Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext(), R.style.CustomAlertDialogTheme);


                // set the title and message
                builder.setTitle("Move Folder");
                builder.setMessage("Move to Folder:");

                // create a EditText view to get user input
                final EditText input = new EditText(holder.itemView.getContext());
                builder.setView(input);

                // set the positive button action
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get the user input text
                        String folderName = input.getText().toString();
                        // do something with the folderName
                        Toast.makeText(holder.itemView.getContext(), "Folder name: " + folderName, Toast.LENGTH_SHORT).show();
                        databaseRef.child("workouts").child(workoutItem.getName()).child("folder").setValue(folderName);

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
