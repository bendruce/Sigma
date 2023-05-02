package com.example.sigma;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private List<WorkoutItem> workoutItems;

    private ImageButton moveFolderBtn;
    private ImageButton openWorkoutBtn;
    private ImageButton delWrkoutBtn;

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
        openWorkoutBtn = holder.itemView.findViewById(R.id.openPrevWorkoutBtn);
        delWrkoutBtn = holder.itemView.findViewById(R.id.deleteWorkoutBtn);
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
                // set the positive button action
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get the user input text
                        String folderName = input.getText().toString();
                        System.out.println(folderName);
                        databaseRef.child("workouts").child(workoutItem.getName()).child("folder").setValue(folderName);
                        DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("folders");
                        Query query = folderRef.orderByValue().equalTo(folderName);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String folderKey = snapshot.getChildren().iterator().next().getKey();
                                    System.out.println(folderKey);//Do something with the folderKey
                                    folderRef.child(folderKey).removeValue();
                                } else {
                                    System.out.println("Handle case where folderName does not exist in database");
                                    String key = databaseRef.child("folders").push().getKey();
                                    databaseRef.child("folders").child(key).setValue(folderName);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle any errors or exceptions that occur during retrieval
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
        openWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CompleteWorkoutActivity.class);
                intent.putExtra("workoutTitle", workoutItem.getName());
                //v.getContext().startActivity(intent);
                databaseRef.child("workouts").child(workoutItem.getName()).child("asString").get().addOnSuccessListener(dataSnapshot -> {
                    String workoutAsString = (String) dataSnapshot.getValue();
                    intent.putExtra("workoutText", workoutAsString);
                    v.getContext().startActivity(intent);
                }).addOnFailureListener(e -> {
                    // Handle the error here
                    Toast.makeText(v.getContext(), "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
        delWrkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove the workout from the database
                databaseRef.child("workouts").child(workoutItem.getName()).removeValue();


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