package com.example.sigma;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private List<FolderItem> folderItems;

    private ImageButton openFolderBtn;

    private ImageButton delFolderBtn;

    private int workoutCounter;

    public FolderAdapter(List<FolderItem> folderItems) {
        this.folderItems = folderItems;
    }

    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

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


        openFolderBtn = holder.itemView.findViewById(R.id.openFolderButton);
        delFolderBtn = holder.itemView.findViewById(R.id.deleteFolderButton);

        workoutCounter = 0;
        DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("workouts");
        DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("folders");
        workoutsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                // iterate over each child of the workouts reference
                for (DataSnapshot workoutSnapshot : dataSnapshot.getChildren()) {

                    DataSnapshot folderSnapshot = workoutSnapshot.child("folder");
                    String workoutFolder = folderSnapshot.getValue(String.class);
                    if ((folderItem.getFolderName()).equals(workoutFolder)){
                        workoutCounter+=1;
                    }



                }

                holder.numberOfWorkoutsTextView.setText(String.valueOf(workoutCounter));
                workoutCounter = 0;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // handle any errors
            }
        });
        openFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OpenFolderActivity.class);
                intent.putExtra("folderTitle", folderItem.getFolderName());
                v.getContext().startActivity(intent);
            }
        });
        delFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove the folder from the database
                //String folderName = "Legs";
                DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("folders");
                Query query = folderRef.orderByValue().equalTo(folderItem.getFolderName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String folderKey = snapshot.getChildren().iterator().next().getKey();
                            System.out.println(folderKey);//Do something with the folderKey
                            folderRef.child(folderKey).removeValue();
                        } else {
                            System.out.println("Handle case where folderName does not exist in database");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any errors or exceptions that occur during retrieval
                    }
                });

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
