package com.example.cheersfor.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cheersfor.R;

public class ReadStoryActivity extends AppCompatActivity{

    private TextView storyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story);

        storyTextView = findViewById(R.id.storyTextView);

        String storyText = getIntent().getStringExtra("storyText");
        storyTextView.setText(storyText);
    }
}
