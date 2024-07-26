package com.example.cheersfor.activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cheersfor.R;
import com.example.cheersfor.adapters.StoryAdapter;
import com.example.cheersfor.models.Story;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class StoryListActivity extends AppCompatActivity{

    private RecyclerView storyRecyclerView;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        storyRecyclerView = findViewById(R.id.storyRecyclerView);
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        storyList = new ArrayList<>();
        storyAdapter = new StoryAdapter(storyList);
        storyRecyclerView.setAdapter(storyAdapter);

        loadStories();
    }

    private void loadStories() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("stories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                storyList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Story story = doc.toObject(Story.class);
                    storyList.add(story);
                }
                storyAdapter.notifyDataSetChanged();
            }
        });
    }
}
