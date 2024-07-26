package com.example.cheersfor.managers;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.cheersfor.activities.StoryReviewActivity;
import com.example.cheersfor.api.OpenAIChatRequest;
import com.example.cheersfor.api.OpenAIResponse;
import com.example.cheersfor.api.OpenAIService;
import com.example.cheersfor.interfaces.BooleanCallback;
import com.example.cheersfor.interfaces.Callback;
import com.example.cheersfor.models.Company;
import com.example.cheersfor.models.Story;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class StoryManager {


    private FirebaseFirestore db;
    private OpenAIService openAIService;

    public StoryManager() {
        db = FirebaseFirestore.getInstance();
        openAIService = OpenAIService.Factory.create();
    }

    public void addStory(String storyText, String companyName, BooleanCallback callback, Context context) {
        Log.d("StoryManager", "addStory called with storyText: " + storyText + " and companyName: " + companyName);
        analyzeStoryTone(storyText, companyName, callback, context);
    }

    private void analyzeStoryTone(String storyText, String companyName, BooleanCallback callback, Context context) {
        OpenAIChatRequest.Message userMessage = new OpenAIChatRequest.Message("user",
                "Analyze the tone of the following text: \"" + storyText + "\". If the tone is negative, rewrite it in a positive tone. If the tone is positive, leave it unchanged.");
        OpenAIChatRequest request = new OpenAIChatRequest("gpt-3.5-turbo",
                Collections.singletonList(userMessage), 0.7, 150);

        openAIService.getChatCompletion(request).enqueue(new retrofit2.Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                Log.d("StoryManager", "OpenAI API response received");
                if (response.isSuccessful() && response.body() != null) {
                    String analyzedText = response.body().getChoices().get(0).getMessage().getContent();
                    Log.d("StoryManager", "OpenAI API analyzedText: " + analyzedText);

                    // Determinăm dacă tonul este pozitiv sau negativ
                    boolean isPositiveTone = analyzedText.contains("The tone of the text is positive");

                    String finalText;
                    if (isPositiveTone) {
                        finalText = storyText;
                    } else {
                        // Extragem textul rescris în ton pozitiv
                        int rewrittenIndex = analyzedText.indexOf("Rewritten in a positive tone:");
                        if (rewrittenIndex != -1) {
                            finalText = analyzedText.substring(rewrittenIndex + 27).trim();
                        } else {
                            // Dacă răspunsul nu conține clar delimitarea textului rescris, folosim textul întreg rescris
                            finalText = analyzedText;
                        }
                    }

                    // Adăugăm povestirea la baza de date
                    addStoryToDatabase(finalText, companyName, new BooleanCallback() {
                        @Override
                        public void onResponse(boolean success) {
                            Log.d("StoryManager", "addStoryToDatabase success: " + success);
                            if (success) {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    Intent intent = new Intent(context, StoryReviewActivity.class);
                                    intent.putExtra("analyzedText", finalText);
                                    intent.putExtra("companyName", companyName);
                                    context.startActivity(intent);
                                });
                            }
                            callback.onResponse(success);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.e("StoryManager", "addStoryToDatabase failed", e);
                            callback.onFailure(e);
                        }
                    });

                } else {
                    Log.e("OpenAI API", "Failed to analyze story tone: " + response.message());
                    callback.onResponse(false);
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                Log.e("OpenAI API", "Failed to analyze story tone: ", t);
                callback.onFailure(new Exception(t));
            }
        });
    }

    public void getCompanies(Callback<List<Company>> callback) {
        db.collection("companies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Company> companies = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            companies.add(document.toObject(Company.class));
                        }
                        callback.onResponse(companies);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }


    public void getStoriesByCompany(String companyName, Callback<List<Story>> callback) {
        db.collection("stories")
                .whereEqualTo("companyName", companyName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Story> stories = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            stories.add(document.toObject(Story.class));
                        }
                        callback.onResponse(stories);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }


            private void notifyUserForApproval(String newStoryText, String originalStoryText, String companyName, BooleanCallback callback) {
                // Implement a method to notify the user and get their approval
                // This could involve updating the UI and waiting for user interaction
                // For simplicity, let's assume you have a method to show a dialog or notification to the user

                // Pseudo code:
                // showApprovalDialog(newStoryText, originalStoryText, (userAccepted, userEditedText) -> {
                //     if (userAccepted) {
                //         addStoryToDatabase(userEditedText, companyName, callback);
                //     } else {
                //         callback.onResponse(false, null);
                //     }
                // });

                // Assuming showApprovalDialog is implemented in your UI layer
            }


    public void addStoryToDatabase(String storyText, String companyName, BooleanCallback callback) {
        Log.d("StoryManager", "Adding story to database with text: " + storyText + " for company: " + companyName);
        db.collection("companies")
                .whereEqualTo("name", companyName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Company company = document.toObject(Company.class);
                            Log.d("StoryManager", "Company found: " + company.getName());
                            addStoryToDatabaseWithCompanyId(storyText, company.getId(), callback);
                            return;
                        }
                    } else {
                        Company newCompany = new Company();
                        newCompany.setName(companyName);
                        db.collection("companies")
                                .add(newCompany)
                                .addOnSuccessListener(documentReference -> {
                                    newCompany.setId(documentReference.getId());
                                    Log.d("StoryManager", "New company added with ID: " + documentReference.getId());
                                    addStoryToDatabaseWithCompanyId(storyText, documentReference.getId(), callback);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("StoryManager", "Failed to add new company", e);
                                    callback.onFailure(e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("StoryManager", "Failed to find company", e);
                    callback.onFailure(e);
                });
    }

    private void addStoryToDatabaseWithCompanyId(String storyText, String companyId, BooleanCallback callback) {
        Log.d("StoryManager", "Adding story to Firestore with text: " + storyText + " for company ID: " + companyId);
        Story story = new Story();
        story.setUserId(getCurrentUserId());
        story.setText(storyText);
        story.setCompanyId(companyId);
        story.setModerated(false);

        db.collection("stories").add(story)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("StoryManager", "Story added successfully to Firestore");
                        callback.onResponse(true);
                    } else {
                        Log.e("StoryManager", "Failed to add story to Firestore");
                        callback.onResponse(false);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("StoryManager", "Failed to add story to Firestore", e);
                    callback.onFailure(e);
                });
    }


    private String getCurrentUserId() {
                // Return the currently logged-in user's ID
                return "1"; // Replace with actual user ID retrieval logic
            }
        }