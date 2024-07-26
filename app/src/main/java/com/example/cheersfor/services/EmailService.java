package com.example.cheersfor.services;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cheersfor.R;
import com.example.cheersfor.services.FirebaseService;


public class EmailService extends AppCompatActivity{

    private FirebaseService firebaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        firebaseService = new FirebaseService();

        EditText storyText = findViewById(R.id.story_text);
        Button submitStoryButton = findViewById(R.id.submit_story_button);

        submitStoryButton.setOnClickListener(v -> {
            String story = storyText.getText().toString();
            analyzeAndSubmitStory(story);
        });
    }

    private void analyzeAndSubmitStory(String story) {
        // Use OpenAI API to analyze and suggest positive modifications
        // Submit story to Firebase after analysis
    }
}
