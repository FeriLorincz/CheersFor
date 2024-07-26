package com.example.cheersfor.repositories;

import android.util.Log;

import com.example.cheersfor.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.cheersfor.interfaces.Callback;

public class FirebaseRepository {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    public FirebaseRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void registerUser(String name, String email, String password, Callback<Boolean> callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        saveUserData(userId, name, "", email, "", null, callback);
                    } else {
                        callback.onResponse(false);
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    public void saveUserData(String userId, String firstName, String lastName, String email, String profilePicture, List<Integer> previousCompanyIds, Callback<Boolean> callback) {
        User user = new User(0, firstName + " " + lastName, email, "", profilePicture, 0, previousCompanyIds, 0, "");

        db.collection("users").document(userId).set(user)
                .addOnSuccessListener(aVoid -> callback.onResponse(true))
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    public void saveStory(String userId, String storyText, boolean isModerated, OnStorySavedListener listener) {
        Log.d("FirebaseService", "Saving story with text: " + storyText + " for user ID: " + userId);
        DocumentReference storyRef = db.collection("stories").document();
        Map<String, Object> story = new HashMap<>();
        story.put("userId", userId);
        story.put("text", storyText);
        story.put("isModerated", isModerated);

        storyRef.set(story)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirebaseService", "Story saved successfully");
                    listener.onStorySaved();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Failed to save story", e);
                    listener.onError(e);
                });
    }

    public void saveVote(String userId, String vote, OnVoteSavedListener listener) {
        Log.d("FirebaseService", "Saving vote: " + vote + " for user ID: " + userId);
        DocumentReference voteRef = db.collection("votes").document();
        Map<String, Object> voteData = new HashMap<>();
        voteData.put("userId", userId);
        voteData.put("vote", vote);

        voteRef.set(voteData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirebaseService", "Vote saved successfully");
                    listener.onVoteSaved();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseService", "Failed to save vote", e);
                    listener.onError(e);
                });
    }

    public interface OnStorySavedListener {
        void onStorySaved();
        void onError(Exception e);
    }

    public interface OnVoteSavedListener {
        void onVoteSaved();
        void onError(Exception e);
    }
}