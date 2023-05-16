// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR THE ADAPTER TO ADD FOLDERS TO THE RECYCLER VIEW IN THE HOME PAGE
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
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
    }//Constructor for the FolderAdapter class

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//Override onCreateViewHolder to inflate the layout and return the ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {//Bind the ViewHolder to the data
        FolderItem folderItem = folderItems.get(position);//Get the current folderItem
        holder.folderNameTextView.setText(folderItem.getFolderName());
        //Find the buttons in the layout
        openFolderBtn = holder.itemView.findViewById(R.id.openFolderButton);
        delFolderBtn = holder.itemView.findViewById(R.id.deleteFolderButton);

        workoutCounter = 0;//Set workoutCounter to 0
        //Get a reference to the workouts and folders nodes in the database
        DatabaseReference workoutsRef = FirebaseDatabase.getInstance().getReference("workouts");
        DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("folders");
        workoutsRef.addValueEventListener(new ValueEventListener() {//Add a ValueEventListener to the workoutsRef
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for (DataSnapshot workoutSnapshot : dataSnapshot.getChildren()) {//Iterate over each child of the workouts reference
                    DataSnapshot folderSnapshot = workoutSnapshot.child("folder");
                    String workoutFolder = folderSnapshot.getValue(String.class);
                    if ((folderItem.getFolderName()).equals(workoutFolder)){//If the folder name matches increment the workoutCounter
                        workoutCounter+=1;
                    }
                }
                holder.numberOfWorkoutsTextView.setText(String.valueOf(workoutCounter));//Set the text of the numberOfWorkoutsTextView
                workoutCounter = 0;//Reset the workoutCounter
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {//Handle any errors
                Toast.makeText(holder.itemView.getContext(), "Error connecting to database, try again later", Toast.LENGTH_SHORT).show();
            }
        });
        openFolderBtn.setOnClickListener(new View.OnClickListener() {//Set an OnClickListener on the openFolderBtn
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OpenFolderActivity.class);//Create an Intent to start the OpenFolderActivity
                intent.putExtra("folderTitle", folderItem.getFolderName());
                v.getContext().startActivity(intent);
            }
        });
        delFolderBtn.setOnClickListener(new View.OnClickListener() {//Set an OnClickListener on the delFolder
            @Override
            public void onClick(View v) {
                //remove the folder from the database
                //
                //Create a Query to find the folder with the given name
                DatabaseReference folderRef = FirebaseDatabase.getInstance().getReference("folders");
                Query query = folderRef.orderByValue().equalTo(folderItem.getFolderName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {//If the folder exists, delete it
                            String folderKey = snapshot.getChildren().iterator().next().getKey();
                            System.out.println(folderKey);//Do something with the folderKey
                            folderRef.child(folderKey).removeValue();
                        } else {//If the folder does not exist, display an error message
                            Toast.makeText(holder.itemView.getContext(), "Not Deleted, Error", Toast.LENGTH_SHORT).show();

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(holder.itemView.getContext(), "Not Deleted, Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return folderItems.size();
    }//Override getItemCount to return the number of items in the list
    public class FolderViewHolder extends RecyclerView.ViewHolder {//Define the ViewHolder class
        private TextView folderNameTextView;
        private TextView numberOfWorkoutsTextView;


        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderNameTextView = itemView.findViewById(R.id.folderName);
            numberOfWorkoutsTextView = itemView.findViewById(R.id.workoutCount);
        }

        public void bind(FolderItem folderItem) {//Bind the FolderItem data to the TextViews
            folderNameTextView.setText(folderItem.getFolderName());
            numberOfWorkoutsTextView.setText(String.valueOf(folderItem.getNumberOfWorkouts()));
        }
    }
}
