package com.example.cheersfor.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cheersfor.R;
import com.example.cheersfor.models.Question;
import com.example.cheersfor.models.Vote;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class VotingActivity extends AppCompatActivity {

    private TextView questionTextView;
    private Button optionButton1, optionButton2, optionButton3, optionButton4;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        questionTextView = findViewById(R.id.questionTextView);
        optionButton1 = findViewById(R.id.optionButton1);
        optionButton2 = findViewById(R.id.optionButton2);
        optionButton3 = findViewById(R.id.optionButton3);
        optionButton4 = findViewById(R.id.optionButton4);

        db = FirebaseFirestore.getInstance();
        questions = new ArrayList<>();
        loadQuestions();

        optionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVote(questions.get(currentQuestionIndex).getId(), 1);
            }
        });
        optionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVote(questions.get(currentQuestionIndex).getId(), 2);
            }
        });
        optionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVote(questions.get(currentQuestionIndex).getId(), 3);
            }
        });
        optionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVote(questions.get(currentQuestionIndex).getId(), 4);
            }
        });
    }

    private void loadQuestions() {
        db.collection("questions").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Question question = document.toObject(Question.class);
                    questions.add(question);
                }
                if (!questions.isEmpty()) {
                    displayQuestion(questions.get(currentQuestionIndex));
                }
            }
        });
    }

    private void displayQuestion(Question question) {
        questionTextView.setText(question.getText());
        optionButton1.setText(question.getOptions().get(0).getText());
        optionButton2.setText(question.getOptions().get(1).getText());
        optionButton3.setText(question.getOptions().get(2).getText());
        optionButton4.setText(question.getOptions().get(3).getText());
    }

    private void submitVote(int questionId, int selectedOptionId) {
        Vote vote = new Vote();
        vote.setUserId(getCurrentUserId());
        vote.setQuestionId(questionId);
        vote.setSelectedOptionId(selectedOptionId);

        db.collection("votes").add(vote).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    displayQuestion(questions.get(currentQuestionIndex));
                } else {
                    // Handle end of questions
                }
            }
        });
    }

    private int getCurrentUserId() {
        // Return the currently logged-in user's ID
        return 1; // Replace with actual user ID retrieval logic
    }
}
