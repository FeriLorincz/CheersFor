package com.example.cheersfor.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cheersfor.R;
import com.example.cheersfor.adapters.StoryAdapter;
import com.example.cheersfor.adapters.ModerateStoriesAdapter;
import com.example.cheersfor.models.Story;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ModerateStoriesActivity extends AppCompatActivity {

    private RecyclerView moderateStoriesRecyclerView;
    private ModerateStoriesAdapter adapter;
    private List<Story> stories;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderate_stories);

        moderateStoriesRecyclerView = findViewById(R.id.moderateStoriesRecyclerView);
        moderateStoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        stories = new ArrayList<>();
        adapter = new ModerateStoriesAdapter(stories, db);
        moderateStoriesRecyclerView.setAdapter(adapter);

        loadUnmoderatedStories();
    }

    private void loadUnmoderatedStories() {
        db.collection("stories")
                .whereEqualTo("isModerated", false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        stories.clear();
                        for (QueryDocumentSnapshot document : result) {
                            Story story = document.toObject(Story.class);
                            stories.add(story);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load stories for moderation", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
