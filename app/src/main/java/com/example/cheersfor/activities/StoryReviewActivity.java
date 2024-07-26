package com.example.cheersfor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cheersfor.R;
import com.example.cheersfor.interfaces.BooleanCallback;
import com.example.cheersfor.managers.StoryManager;

public class StoryReviewActivity extends AppCompatActivity{

    private EditText storyEditText;
    private Button saveButton;
    private String analyzedText;
    private String companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_review);

        storyEditText = findViewById(R.id.storyEditText);
        saveButton = findViewById(R.id.saveButton);

        // Preluarea datelor transmise prin Intent
        Intent intent = getIntent();
        analyzedText = intent.getStringExtra("analyzedText");
        companyName = intent.getStringExtra("companyName");

        // Afișarea textului analizat în EditText
        storyEditText.setText(analyzedText);

        // Setarea acțiunii pentru butonul de salvare
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalStoryText = storyEditText.getText().toString();
                saveStory(finalStoryText, companyName);
            }
        });
    }

    private void saveStory(String storyText, String companyName) {
        StoryManager storyManager = new StoryManager();
        storyManager.addStoryToDatabase(storyText, companyName, new BooleanCallback() {
            @Override
            public void onResponse(boolean result) {
                if (result) {
                    finish(); // Închide activitatea după ce povestea a fost salvată cu succes
                } else {
                    // Gestionare eroare la salvare
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Gestionare eroare la salvare
            }
        });
    }
}
