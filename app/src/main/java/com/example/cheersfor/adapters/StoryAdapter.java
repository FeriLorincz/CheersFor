package com.example.cheersfor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cheersfor.R;
import com.example.cheersfor.models.Story;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder>{

    private List<Story> stories;

    public StoryAdapter(List<Story> stories) {
        this.stories = stories;
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
        //holder.companyTextView.setText(String.valueOf(story.getCompanyId()));
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public void updateStories(List<Story> newStories) {
        this.stories = newStories;
        notifyDataSetChanged();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        public TextView storyTextView;
        //public TextView companyTextView;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            storyTextView = itemView.findViewById(R.id.storyTextView);
            //companyTextView = itemView.findViewById(R.id.companyTextView);
        }
    }
}
