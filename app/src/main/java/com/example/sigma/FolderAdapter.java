package com.example.sigma;

import static androidx.core.content.ContextCompat.startActivity;

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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private List<FolderItem> folderItems;

    private ImageButton openFolderBtn;

    private TextView folderTitle;

    public FolderAdapter(List<FolderItem> folderItems) {
        this.folderItems = folderItems;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        FolderItem folderItem = folderItems.get(position);
        holder.folderNameTextView.setText(folderItem.getFolderName());
        holder.numberOfWorkoutsTextView.setText(String.valueOf(folderItem.getNumberOfWorkouts()));

        openFolderBtn = holder.itemView.findViewById(R.id.openFolderButton);
        folderTitle = holder.itemView.findViewById(R.id.folderName);

        openFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("workouts");

                // attach a listener to the workouts reference to detect when the data changes
                workoutsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        StringBuilder sb = new StringBuilder();


                        // iterate over each child of the workouts reference
                        for (DataSnapshot workoutSnapshot : dataSnapshot.getChildren()) {
                            String workoutName = workoutSnapshot.getKey();
                            DataSnapshot dateSnapshot = workoutSnapshot.child("date");
                            DataSnapshot lengthSnapshot = workoutSnapshot.child("length");
                            DataSnapshot folderSnapshot = workoutSnapshot.child("folder");
                            String workoutDate = dateSnapshot.getValue(String.class);
                            String workoutLength = lengthSnapshot.getValue(String.class);
                            String workoutFolder = folderSnapshot.getValue(String.class);

                            if (workoutFolder.equals(folderTitle.getText().toString())){
                                sb.append(workoutName).append(":").append("\n");


                            }

                        }

                        // notify the workout adapter that the data has changed

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // handle any errors
                    }
                });



                Intent intent = new Intent(v.getContext(), OpenFolderActivity.class);


                intent.putExtra("folderTitle", folderTitle.getText().toString());
                // start the activity
                //databaseRef.child("workouts").child(editTextWorkoutTitle.getText().toString()).push().setValue(workoutText);
                v.getContext().startActivity(intent);




            }
        });
    }

    @Override
    public int getItemCount() {
        return folderItems.size();
    }
    public class FolderViewHolder extends RecyclerView.ViewHolder {
        private TextView folderNameTextView;
        private TextView numberOfWorkoutsTextView;


        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderNameTextView = itemView.findViewById(R.id.folderName);
            numberOfWorkoutsTextView = itemView.findViewById(R.id.workoutCount);
        }

        public void bind(FolderItem folderItem) {
            folderNameTextView.setText(folderItem.getFolderName());
            numberOfWorkoutsTextView.setText(String.valueOf(folderItem.getNumberOfWorkouts()));
        }
    }
}
