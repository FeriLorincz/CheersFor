package com.example.cheersfor.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cheersfor.R;
import com.example.cheersfor.repositories.FirebaseRepository;
import com.example.cheersfor.interfaces.Callback;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private Button registerWithLinkedInButton;

    private FirebaseRepository firebaseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        registerWithLinkedInButton = findViewById(R.id.registerWithLinkedInButton);

        firebaseRepository = new FirebaseRepository();

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            registerManually(name, email, password);
        });

        registerWithLinkedInButton.setOnClickListener(v -> registerWithLinkedIn());
    }

    // Metodă pentru înregistrarea manuală a utilizatorului
    private void registerManually(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aici poți apela metoda din FirebaseRepository pentru a salva datele utilizatorului
        firebaseRepository.registerUser(name, email, password, new Callback<Boolean>() {
            @Override
            public void onResponse(Boolean result) {
                if (result) {
                    // Înregistrare reușită
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Eșec la înregistrare
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Eșec la înregistrare
                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metodă pentru înregistrarea utilizatorului prin LinkedIn (nu implementată aici, necesită integrare LinkedIn API)
    private void registerWithLinkedIn() {
        // Implementarea acestei metode ar implica autentificarea utilizatorului cu LinkedIn și salvarea datelor în Firestore.
        // Pentru aceasta, ai nevoie de implementarea similară cu cea din LoginActivity.
        // Poți utiliza clasele AccessTokenResponse și LinkedInProfile din LoginActivity pentru a procesa răspunsurile de la API-ul LinkedIn.
    }
}

