package com.example.cheersfor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cheersfor.R;
import com.example.cheersfor.models.Story;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ModerateStoriesAdapter extends RecyclerView.Adapter<ModerateStoriesAdapter.StoryViewHolder>{

    private List<Story> stories;
    private FirebaseFirestore db;

    public ModerateStoriesAdapter(List<Story> stories, FirebaseFirestore db) {
        this.stories = stories;
        this.db = db;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.storyTextView.setText(story.getText());
        // Removed companyTextView and buttons
        // If you still need to approve or delete stories, you can do it from Firestore directly
//        holder.companyTextView.setText(String.valueOf(story.getCompanyId()));
//
//        holder.approveButton.setOnClickListener(v -> approveStory(story));
//        holder.deleteButton.setOnClickListener(v -> deleteStory(story));
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        public TextView storyTextView;
//        public TextView companyTextView;
//        public Button approveButton;
//        public Button deleteButton;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyTextView = itemView.findViewById(R.id.storyTextView);
//            companyTextView = itemView.findViewById(R.id.companyTextView);
//            approveButton = itemView.findViewById(R.id.approveButton);
//            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

//    private void approveStory(Story story) {
//        db.collection("stories").document(String.valueOf(story.getId()))
//                .update("isModerated", true)
//                .addOnSuccessListener(aVoid -> {
//                    stories.remove(story);
//                    notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure
//                });
//    }
//
//    private void deleteStory(Story story) {
//        db.collection("stories").document(String.valueOf(story.getId()))
//                .delete()
//                .addOnSuccessListener(aVoid -> {
//                    stories.remove(story);
//                    notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure
//                });
//    }
}
