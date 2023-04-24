package com.example.sigma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class OpenFolderActivity extends AppCompatActivity {

    private TextView folderTitleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_folder);
        String folderTitle = getIntent().getStringExtra("folderTitle");
        folderTitleView = findViewById(R.id.openFolderNameTextView);
        folderTitleView.setText(folderTitle);
    }
}