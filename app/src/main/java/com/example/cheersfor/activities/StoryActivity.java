package com.example.cheersfor.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cheersfor.R;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cheersfor.repositories.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StoryActivity extends AppCompatActivity {

    private EditText storyEditText;
    private Button submitStoryButton;
    private FirebaseRepository firebaseRepository;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storyEditText = findViewById(R.id.story_text);
        submitStoryButton = findViewById(R.id.submit_story_button);
        firebaseRepository = new FirebaseRepository();
        firebaseAuth = FirebaseAuth.getInstance();

        submitStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitStory();
            }
        });
    }
    // Handle story submission logic here
    private void submitStory() {
        String storyText = storyEditText.getText().toString().trim();
        if (TextUtils.isEmpty(storyText)) {
            Toast.makeText(this, "Please write a story", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            firebaseRepository.saveStory(userId, storyText, false, new FirebaseRepository.OnStorySavedListener() {
                @Override
                public void onStorySaved() {
                    Toast.makeText(StoryActivity.this, "Story submitted successfully", Toast.LENGTH_SHORT).show();
                    storyEditText.setText("");
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(StoryActivity.this, "Error submitting story: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
