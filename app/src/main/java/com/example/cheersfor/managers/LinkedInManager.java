package com.example.cheersfor.managers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.cheersfor.activities.AddStoryActivity;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LinkedInManager {

    private static final String CLIENT_ID = "776vsjtosz2k9e";
    private static final String REDIRECT_URI = "com.example.cheersfor:/oauth2redirect";
    private static final String SCOPE = "r_liteprofile r_emailaddress w_member_social";

    private static final String API_BASE_URL = "https://api.linkedin.com/v2/";
    private static final String INVITE_ENDPOINT = "people/~/connections";
    private static final String SHARED_PREFS_NAME = "linkedin_prefs";
    private static final String ACCESS_TOKEN_KEY = "access_token_key";

    private SharedPreferences sharedPreferences;
    private AuthorizationService mAuthService;
    private AuthorizationServiceConfiguration mAuthConfig;
    private Context context;

    private AddStoryActivity activity;


    public LinkedInManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        mAuthService = new AuthorizationService(context);
    }



//    public LinkedInManager(AddStoryActivity activity) {
//
//        this.activity = activity;
//    }

    public void startLinkedInAuthFlow(AddStoryActivity activity) {

        this.activity = activity;

        mAuthConfig = new AuthorizationServiceConfiguration(
                Uri.parse("https://www.linkedin.com/oauth/v2/authorize"), // Authorization endpoint
                Uri.parse("https://www.linkedin.com/oauth/v2/accessToken") // Token endpoint
        );

        //mAuthService = new AuthorizationService(activity);

        AuthorizationRequest.Builder authRequestBuilder = new AuthorizationRequest.Builder(
                mAuthConfig,
                CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(REDIRECT_URI)
        );

        authRequestBuilder.setScope(SCOPE);

        AuthorizationRequest authRequest = authRequestBuilder.build();

        ActivityResultLauncher<Intent> launcher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (data != null) {
                        handleRedirect(data.getData());
                    }
                });

        Intent authIntent = mAuthService.getAuthorizationRequestIntent(authRequest);
        launcher.launch(authIntent);
    }

    private void handleRedirect(Uri data) {
        Intent intent = activity.getIntent();
        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException exception = AuthorizationException.fromIntent(intent);

        if (response != null) {
            mAuthService.performTokenRequest(
                    response.createTokenExchangeRequest(),
                    (tokenResponse, ex) -> {
                        if (tokenResponse != null) {
                            // Token exchange successful, fetch LinkedIn profile and colleagues
                            saveAccessToken(tokenResponse.accessToken);
                            activity.fetchLinkedInProfile(tokenResponse.accessToken);
                            activity.fetchLinkedInColleagues(tokenResponse.accessToken);
                        } else {
                            // Token exchange failed, handle error
                            Log.e("LinkedIn Auth", "Token exchange failed: " + ex.toString());
                            Toast.makeText(activity, "LinkedIn authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (exception != null) {
            // Authorization failed, handle error
            Log.e("LinkedIn Auth", "Authorization failed: " + exception.toString());
            Toast.makeText(activity, "Authorization failed", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveAccessToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, token);
        editor.apply();
    }

    private String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null);
    }

    public void inviteColleague(String email) {
        // Implementarea pentru a trimite invitații către colegi prin LinkedIn
        // Aici va trebui să utilizezi API-ul LinkedIn pentru a trimite invitații
        // Într-o implementare reală, vei crea o cerere API pentru a trimite invitații
        // Momentan, vom afișa doar un mesaj Toast pentru exemplificare

       // String apiUrl = API_BASE_URL + "people/~/connections";

        String apiUrl = API_BASE_URL + INVITE_ENDPOINT;

        OkHttpClient client = new OkHttpClient();

        // Construim corpul cererii HTTP
        JSONObject requestBody = new JSONObject();
        try {
            JSONObject recipient = new JSONObject();
            recipient.put("person", new JSONObject().put("emailAddress", email));
            requestBody.put("recipients", new JSONArray().put(recipient));
            requestBody.put("subject", "Join my network on LinkedIn");
            requestBody.put("message", "Hello, I'd like to connect with you on LinkedIn.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody.toString());

        // Construim cererea HTTP POST
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + getAccessToken())
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        // Executăm cererea HTTP asincron
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("LinkedIn API", "Failed to send invite: " + e.getMessage());
                Toast.makeText(context, "Failed to send invite", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("LinkedIn API", "Failed to send invite: " + response.code());
                    Toast.makeText(context, "Failed to send invite", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    // Verificați răspunsul API pentru a confirma trimiterea invitației
                    Log.d("LinkedIn API", "Invite sent successfully: " + jsonResponse.toString());
                    Toast.makeText(context, "Invite sent successfully", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e("LinkedIn API", "Failed to parse JSON response: " + e.getMessage());
                    Toast.makeText(context, "Failed to send invite", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}
