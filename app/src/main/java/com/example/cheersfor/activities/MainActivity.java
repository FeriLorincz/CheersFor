package com.example.cheersfor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cheersfor.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button addStoryButton;
    private Button moderateStoriesButton;
    private Button logoutButton;
    private Button openStoryActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            addStoryButton = findViewById(R.id.addStoryButton);
            moderateStoriesButton = findViewById(R.id.moderateStoriesButton);
            logoutButton = findViewById(R.id.logoutButton);
            openStoryActivityButton = findViewById(R.id.open_story_activity_button);

            addStoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddStoryActivity.class);
                    startActivity(intent);
                }
            });


            //pentru verificare AddStoryActivity de aici:
        openStoryActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStoryActivity.class);
                startActivity(intent);
            }
        });
        //pana aici



            moderateStoriesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ModerateStoriesActivity.class);
                    startActivity(intent);
                }
            });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        }
    }