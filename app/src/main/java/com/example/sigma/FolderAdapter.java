package com.example.sigma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private List<FolderItem> folderItems;

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
