package com.example.cheersfor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.cheersfor.R;

import com.example.cheersfor.interfaces.BooleanCallback;

import com.example.cheersfor.managers.EmailManager;
import com.example.cheersfor.managers.LinkedInManager;
import com.example.cheersfor.managers.StoryManager;
import com.example.cheersfor.managers.UserManager;
import com.google.firebase.FirebaseApp;

import androidx.appcompat.app.AlertDialog;


public class AddStoryActivity extends AppCompatActivity{

    private EditText storyEditText;
    private EditText companyEditText;
    private Button addStoryButton;

    private LinkedInManager linkedInManager;
    private StoryManager storyManager;
    private EmailManager emailManager;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        // Inițializare Firebase
       // FirebaseApp.initializeApp(this);

        storyEditText = findViewById(R.id.storyEditText);
        companyEditText = findViewById(R.id.companyEditText);
        addStoryButton = findViewById(R.id.addStoryButton);

        linkedInManager = new LinkedInManager(this);
        storyManager = new StoryManager();
        emailManager = new EmailManager();
        userManager = new UserManager();

        addStoryButton.setOnClickListener(v -> checkAndAddStory());

        // Adaugă verificarea inițializării Firestore
        if (storyManager != null) {
            Log.d("AddStoryActivity", "StoryManager initialized successfully.");
        } else {
            Log.e("AddStoryActivity", "Failed to initialize StoryManager.");
        }


    }

    private void checkAndAddStory() {
        String storyText = storyEditText.getText().toString().trim();
        String companyName = companyEditText.getText().toString().trim();
        if (storyText.isEmpty()) {
            //Toast.makeText(this, "Story cannot be empty", Toast.LENGTH_SHORT).show();
            showToast("Story cannot be empty");
            return;
        }
        if (companyName.isEmpty()) {
            //Toast.makeText(this, "Company cannot be empty", Toast.LENGTH_SHORT).show();
            showToast("Company cannot be empty");
            return;
        }


        storyManager.addStory(storyText, companyName, new BooleanCallback() {
            @Override
            public void onResponse(boolean success) {
                if (success) {
                    Log.d("AddStoryActivity", "Story added successfully");
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Intent intent = new Intent(AddStoryActivity.this, SearchStoriesActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    Log.e("AddStoryActivity", "Failed to add story");
                    showToast("Failed to add story");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("AddStoryActivity", "Failed to add story", e);
                showToast("Failed to add story");
            }
        }, this); // Pass context to StoryManager
    }


    private void showToast(String message) {
        Toast.makeText(AddStoryActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToSearchStories(String companyName) {
        Intent intent = new Intent(AddStoryActivity.this, SearchStoriesActivity.class);
        intent.putExtra("companyName", companyName);
        startActivity(intent);
        finish();
    }

    public void fetchLinkedInProfile(String accessToken) {
        // Implementarea funcționalității de fetch LinkedIn Profile
    }

    public void fetchLinkedInColleagues(String accessToken) {
        // Implementarea funcționalității de fetch LinkedIn Colleagues
    }

    public void inviteColleague(String email) {
        emailManager.inviteColleague(email);
    }

    public void sendEmailNotification(String email, String message) {
        emailManager.sendEmailNotification(email, message);
    }
}

//    private static final String CLIENT_ID = "776vsjtosz2k9e";
//    private static final String REDIRECT_URI = "com.example.cheersfor:/oauth2redirect";
//    private static final String SCOPE = "r_liteprofile r_emailaddress w_member_social";
//
//    private static final String EMAIL_USERNAME = "ferilorincz12@gmail.com";
//    private static final String EMAIL_PASSWORD = "Bh82AMD+";
//
//    private EditText storyEditText;
//    private EditText companyEditText;
//    private Button addStoryButton;
//    private FirebaseFirestore db;
//    private OpenAIService openAIService;
//
//    private AuthorizationService mAuthService;
//    private AuthorizationServiceConfiguration mAuthConfig;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_story);
//
//        storyEditText = findViewById(R.id.storyEditText);
//        companyEditText = findViewById(R.id.companyEditText);
//        addStoryButton = findViewById(R.id.addStoryButton);
//        db = FirebaseFirestore.getInstance();
//        openAIService = OpenAIService.Factory.create();
//
//        addStoryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkAndAddStory();
//            }
//        });
//    }
//
//    private void checkAndAddStory() {
//        String storyText = storyEditText.getText().toString().trim();
//        String companyName = companyEditText.getText().toString().trim();
//        if (storyText.isEmpty()) {
//            Toast.makeText(this, "Story cannot be empty", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (companyName.isEmpty()) {
//            Toast.makeText(this, "Company cannot be empty", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        analyzeStoryTone(storyText, companyName);
//    }
//
//    private void analyzeStoryTone(String storyText, String companyName) {
//        String prompt = "Analyze the following text and determine if it has a negative tone:\n\n" + storyText + "\n\nIf the text is negative, rewrite it in a positive manner while keeping the original meaning. If the text is positive, just respond with 'The text is positive.'";
//        OpenAIRequest request = new OpenAIRequest("text-davinci-003", prompt, 150, 0.7);
//
//        openAIService.getCompletion(request).enqueue(new Callback<OpenAIResponse>() {
//            @Override
//            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    String aiResponse = response.body().getChoices().get(0).getText().trim();
//                    if (aiResponse.equalsIgnoreCase("The text is positive.")) {
//                        checkAndAddCompany(companyName, storyText);
//                    } else {
//                        showRewriteSuggestion(aiResponse, storyText, companyName);
//                    }
//                } else {
//                    Toast.makeText(AddStoryActivity.this, "Failed to analyze the story", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
//                Toast.makeText(AddStoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void showRewriteSuggestion(String rewrittenText, String originalText, String companyName) {
//        new AlertDialog.Builder(this)
//                .setTitle("Rewrite Suggestion")
//                .setMessage("Here is a suggested positive rewrite of your story:\n\n" + rewrittenText)
//                .setPositiveButton("Use this version", (dialog, which) -> checkAndAddCompany(companyName, rewrittenText))
//                .setNegativeButton("Keep my original story", (dialog, which) -> notifyAdminAndAddStory(originalText, companyName))
//                .show();
//    }
//
//    private void notifyAdminAndAddStory(String originalText, String companyName) {
//        checkAndAddCompany(companyName, originalText);
//    }
//
//    private void checkAndAddCompany(String companyName, String storyText) {
//        db.collection("companies")
//                .whereEqualTo("name", companyName)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Company company = document.toObject(Company.class);
//                            addStoryToDatabase(storyText, String.valueOf(company.getId()));
//                            break;
//                        }
//                    } else {
//                        Company newCompany = new Company();
//                        newCompany.setName(companyName);
//                        db.collection("companies")
//                                .add(newCompany)
//                                .addOnSuccessListener(documentReference -> {
//                                    newCompany.setId(Integer.parseInt(documentReference.getId()));
//                                });
//                    }
//                });
//    }
//
//    private void checkUserInCompany(String companyName, String storyText) {
//        // Implementăm verificarea dacă utilizatorul este deja asociat cu compania
//        // Vom face această verificare în baza de date
//        db.collection("users")
//                .whereEqualTo("companyId", getCurrentUserCompanyId())
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                        // Utilizatorul este deja asociat cu o companie
//                        addStoryToDatabase(storyText, getCurrentUserCompanyId());
//                    } else {
//                        // Utilizatorul nu este încă asociat cu nicio companie, deci verificăm și adăugăm compania
//                        checkAndAddCompany(companyName, storyText);
//                    }
//                });
//    }
//
//
//    private void addStoryToDatabase(String storyText, String companyId) {
//        Story story = new Story();
//        story.setUserId(getCurrentUserId());
//        story.setText(storyText);
//        story.setCompanyId(Integer.parseInt(companyId));
//        story.setModerated(false);
//
//        db.collection("stories").add(story).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "Story added successfully", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Failed to add story", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private int getCurrentUserId() {
//        // Return the currently logged-in user's ID
//        return 1; // Replace with actual user ID retrieval logic
//    }
//
//    private String getCurrentUserCompanyId() {
//        // Return the currently logged-in user's company ID
//        return "1"; // Replace with actual company ID retrieval logic
//    }
//
//    // Extracția colegilor din LinkedIn
//    private void fetchColleaguesFromLinkedIn() {
//        mAuthConfig = new AuthorizationServiceConfiguration(
//                Uri.parse("https://www.linkedin.com/oauth/v2/authorize"), // Authorization endpoint
//                Uri.parse("https://www.linkedin.com/oauth/v2/accessToken") // Token endpoint
//        );
//
//        mAuthService = new AuthorizationService(this);
//
//        AuthorizationRequest.Builder authRequestBuilder = new AuthorizationRequest.Builder(
//                mAuthConfig,
//                CLIENT_ID,
//                ResponseTypeValues.CODE,
//                Uri.parse(REDIRECT_URI)
//        );
//
//        authRequestBuilder.setScope(SCOPE);
//
//        AuthorizationRequest authRequest = authRequestBuilder.build();
//
//        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    Intent data = result.getData();
//                    if (data != null) {
//                        handleRedirect(data.getData());
//                    }
//                });
//
//        Intent authIntent = mAuthService.getAuthorizationRequestIntent(authRequest);
//        launcher.launch(authIntent);
//    }
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1001) {
//            AuthorizationResponse resp = AuthorizationResponse.fromIntent(data);
//            AuthorizationException ex = AuthorizationException.fromIntent(data);
//
//            if (resp != null) {
//                mAuthService.performTokenRequest(
//                        resp.createTokenExchangeRequest(),
//                        (tokenResponse, exception) -> {
//                            if (tokenResponse != null) {
//                                // Token exchange successful, fetch LinkedIn profile and colleagues
//                                fetchLinkedInProfile(tokenResponse.accessToken);
//                                fetchLinkedInColleagues(tokenResponse.accessToken);
//                            } else {
//                                // Token exchange failed, handle error
//                                Log.e("LinkedIn Auth", "Token exchange failed: " + exception.toString());
//                                Toast.makeText(this, "LinkedIn authentication failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                );
//            } else {
//                // Authorization failed, handle error
//                Log.e("LinkedIn Auth", "Authorization failed: " + ex.toString());
//                Toast.makeText(this, "Authorization failed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//
//
//    private void fetchLinkedInProfile(String accessToken) {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://api.linkedin.com/v2/me?projection=(id,localizedFirstName,localizedLastName,profilePicture(displayImage~:playableStreams))")
//                .addHeader("Authorization", "Bearer " + accessToken)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseBody = response.body().string();
//                    Log.d("LinkedIn Profile", "Profile data: " + responseBody);
//                    // Parse and process LinkedIn profile data here
//                } else {
//                    Log.e("LinkedIn Profile", "Failed to fetch profile: " + response.code());
//                }
//            }
//        });
//    }
//
//
//    private void fetchLinkedInColleagues(String accessToken) {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://api.linkedin.com/v2/organizationPage/colleagues")
//                .addHeader("Authorization", "Bearer " + accessToken)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseBody = response.body().string();
//                    Log.d("LinkedIn Colleagues", "Colleagues data: " + responseBody);
//                    // Parse and process LinkedIn colleagues data here
//                } else {
//                    Log.e("LinkedIn Colleagues", "Failed to fetch colleagues: " + response.code());
//                }
//            }
//        });
//    }
//
//
//
//    private void fetchColleaguesFromLinkedIn() {
//        mAuthConfig = new AuthorizationServiceConfiguration(
//                Uri.parse("https://www.linkedin.com/oauth/v2/authorize"), // Authorization endpoint
//                Uri.parse("https://www.linkedin.com/oauth/v2/accessToken") // Token endpoint
//        );
//
//        mAuthService = new AuthorizationService(this);
//
//        AuthorizationRequest.Builder authRequestBuilder = new AuthorizationRequest.Builder(
//                mAuthConfig,
//                CLIENT_ID,
//                ResponseTypeValues.CODE,
//                Uri.parse(REDIRECT_URI)
//        );
//
//        authRequestBuilder.setScope(SCOPE);
//
//        AuthorizationRequest authRequest = authRequestBuilder.build();
//
//        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    Intent data = result.getData();
//                    if (data != null) {
//                        handleRedirect(data.getData());
//                    }
//                });
//
//        Intent authIntent = mAuthService.getAuthorizationRequestIntent(authRequest);
//        launcher.launch(authIntent);
//    }
//
//
//
//
//
//
//
//
//
//    // Adăugarea manuală a colegilor
//    private void addManualColleague(String colleagueName) {
//        // Codul pentru adăugarea unui coleg manual
//    }
//
//    // Trimiterea emailurilor de notificare
//    private void sendEmailNotification(String email, String message) {
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props,
//                new Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
//                    }
//                });
//
//        try {
//            Message mimeMessage = new MimeMessage(session);
//            mimeMessage.setFrom(new InternetAddress(EMAIL_USERNAME));
//            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
//            mimeMessage.setSubject("CheersFor App Notification");
//            mimeMessage.setText(message);
//
//            Transport.send(mimeMessage);
//
//            Log.d("Email", "Email sent successfully to: " + email);
//        } catch (MessagingException e) {
//            Log.e("Email", "Failed to send email", e);
//        }
//    }
//
//    // Invitarea colegilor să se alăture aplicației
//    private void inviteColleague(String email) {
//        String message = "Te invităm să te alături aplicației Cheers For!";
//        sendEmailNotification(email, message);
//    }
//
//    // Formular de înregistrare manuală
//    private void showManualRegistrationForm() {
//        // Codul pentru afișarea formularului de înregistrare manuală
//
//        // Aici poți deschide activitatea sau fragmentul pentru afișarea formularului de înregistrare manuală.
//        // De exemplu, poți utiliza un Intent pentru a deschide RegisterActivity.
//        Intent intent = new Intent(this, RegisterActivity.class);
//        startActivity(intent);
//    }
//
//    // Verificarea și validarea datelor introduse manual
//    private void validateManualData(String name, String email) {
//        // Codul pentru validarea datelor
//
//        // Implementarea logicii pentru validarea datelor introduse manual
//        if (name != null && !name.isEmpty() && email != null && !email.isEmpty()) {
//            // Datele sunt valide, poți proceda cu înregistrarea utilizatorului sau cu altă acțiune necesară.
//            // De exemplu, poți apela metoda din FirebaseRepository pentru a salva datele utilizatorului.
//            // firebaseRepository.saveUserData(...);
//            System.out.println("Data validation successful!");
//        } else {
//            // Datele nu sunt valide, afișează un mesaj de eroare sau gestionează cazul în consecință.
//            System.out.println("Data validation failed. Please enter valid data.");
//        }
//    }
//}