package com.example.cheersfor.services;

import com.example.cheersfor.repositories.FirebaseRepository;
import com.example.cheersfor.models.User;
import java.util.List;

public class FirebaseService {

    private FirebaseRepository firebaseRepository;

    public FirebaseService() {
        firebaseRepository = new FirebaseRepository();
    }

    // Add methods to process Firebase data
    public void generateQuestionsForUser(User user, List<User> colleagues) {
        // Implement logic to create questions based on colleagues
        for (User colleague : colleagues) {
            // Create question and store in Firebase
        }
    }
}
