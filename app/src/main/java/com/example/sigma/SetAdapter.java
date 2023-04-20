package com.example.sigma;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> {

    private List<Set> sets;

    public SetAdapter(List<Set> sets) {
        this.sets = sets;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_layout, parent, false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        Set set = sets.get(position);
        holder.setNumber.setText("Set #" + set.getSetNumber());
        holder.reps.setText(String.valueOf(set.getReps()));
        holder.weight.setText(String.valueOf(set.getWeight()));
        EditText setsReps = holder.itemView.findViewById(R.id.setsReps);
        setsReps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // update the reps value in the corresponding Set object
                try {
                    int reps = Integer.parseInt(s.toString());
                    int position = holder.getAdapterPosition();
                    Set set = sets.get(position);
                    set.setReps(reps);
                } catch (NumberFormatException e) {
                    // Handle the error, e.g. show a toast message
                    Toast.makeText(holder.itemView.getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });


            // Add a TextWatcher to the weight EditText
            EditText setsWeight = holder.itemView.findViewById(R.id.setsWeight);
            setsWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // do nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // update the weight value in the corresponding Set object
                    try {
                        double weight = Double.parseDouble(s.toString());
                        int position = holder.getAdapterPosition();
                        Set set = sets.get(position);
                        set.setWeight(weight);
                    } catch (NumberFormatException e) {
                        // Handle the error, e.g. show a toast message
                        Toast.makeText(holder.itemView.getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // do nothing
                }
            });
        }


    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class SetViewHolder extends RecyclerView.ViewHolder {
        public TextView setNumber;
        public TextView reps;
        public TextView weight;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumber = itemView.findViewById(R.id.text_view_set_number);
            reps = itemView.findViewById(R.id.setsReps);
            weight = itemView.findViewById(R.id.setsWeight);
        }
    }
}
