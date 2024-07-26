package com.example.cheersfor.managers;

import com.example.cheersfor.interfaces.BooleanCallback;
import com.example.cheersfor.interfaces.Callback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

public class UserManager {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    public UserManager() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }

    public void addBonusPoints(int points) {
        if (currentUser != null) {
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    Long currentPoints = document.getLong("bonusPoints");
                    if (currentPoints == null) {
                        currentPoints = 0L;
                    }
                    userRef.update("bonusPoints", currentPoints + points);
                } else {
                    userRef.set(new HashMap<String, Object>() {{
                        put("bonusPoints", points);
                    }});
                }
            });
        }
    }

    public void getBonusPoints(Callback<Integer> callback) {
        if (currentUser != null) {
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    Long currentPoints = document.getLong("bonusPoints");
                    if (currentPoints == null) {
                        currentPoints = 0L;
                    }
                    callback.onResponse(currentPoints.intValue());
                } else {
                    callback.onResponse(0);
                }
            }).addOnFailureListener(e -> callback.onFailure(e));
        } else {
            callback.onResponse(0);
        }
    }

    public void checkUserInCompany(String companyName, String storyText, BooleanCallback callback) {
        // Implementăm verificarea dacă utilizatorul este deja asociat cu compania
        // Vom face această verificare în baza de date
        db.collection("users")
                .whereEqualTo("companyId", getCurrentUserCompanyId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Utilizatorul este deja asociat cu o companie
                        callback.onResponse(true);
                    } else {
                        // Utilizatorul nu este încă asociat cu nicio companie, deci verificăm și adăugăm compania
                        callback.onResponse(false);
                    }
                })
                .addOnFailureListener(e -> {
                    // Tratare eroare în caz de eșec
                    callback.onFailure(e);
                });
    }

    private String getCurrentUserCompanyId() {
        // Return the currently logged-in user's company ID
        return "1"; // Replace with actual company ID retrieval logic
    }
}

