package com.example.cheersfor.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cheersfor.R;

//import com.linkedin.platform.LISessionManager;
//import com.linkedin.platform.listeners.AuthListener;
//import com.linkedin.platform.errors.LIAuthError;
//import com.linkedin.platform.APIHelper;
//import com.linkedin.platform.listeners.ApiListener;
//import com.linkedin.platform.listeners.ApiResponse;

import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cheersfor.repositories.FirebaseRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

import com.example.cheersfor.interfaces.Callback;



public class LoginActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "776vsjtosz2k9e";
    private static final String CLIENT_SECRET = "WXXReyZKrtLDIYgw";
    private static final String REDIRECT_URI = "https://www.example.com/auth/linkedin/callback";
    private static final String STATE = "random_generated_state";
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/oauth/v2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/oauth/v2/accessToken";
    private static final String PROFILE_URL = "https://api.linkedin.com/v2/me";

    private WebView webView;
    private FirebaseRepository firebaseRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseRepository = new FirebaseRepository();
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri.toString().startsWith(REDIRECT_URI)) {
                    handleRedirect(uri);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        String authUrl = AUTHORIZATION_URL + "?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&state=" + STATE + "&scope=r_liteprofile%20r_emailaddress";
        webView.loadUrl(authUrl);
    }

    private void handleRedirect(Uri uri) {
        String code = uri.getQueryParameter("code");
        if (code != null) {
            getAccessToken(code);
        }
    }

    private void getAccessToken(String code) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ACCESS_TOKEN_URL + "?grant_type=authorization_code&code=" + code + "&redirect_uri=" + REDIRECT_URI + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET)
                .post(RequestBody.create(null, new byte[0]))
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Gson gson = new Gson();
                    AccessTokenResponse accessTokenResponse = gson.fromJson(responseBody, AccessTokenResponse.class);
                    getUserProfile(accessTokenResponse.access_token);
                }
            }
        });
    }

    private void getUserProfile(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(PROFILE_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Gson gson = new Gson();
                    LinkedInProfile profile = gson.fromJson(responseBody, LinkedInProfile.class);
                    storeUserData(profile);
                }
            }
        });
    }

    private void storeUserData(LinkedInProfile profile) {
        runOnUiThread(() -> {
            String profileImageUrl = profile.profilePicture.displayImage.toString();
            List<Integer> previousCompanyIds = convertPositionsToList(profile.positions);

            firebaseRepository.saveUserData(
                    profile.id,
                    profile.localizedFirstName,
                    profile.localizedLastName,
                    profile.emailAddress,
                    profileImageUrl,
                    previousCompanyIds,
                    new Callback<Boolean>() {
                        @Override
                        public void onResponse(Boolean result) {
                            // Redirect to main activity or show success message
                        }

                        @Override
                        public void onFailure(Exception e) {
                            // Handle error
                        }
                    });
        });
    }

    private List<Integer> convertPositionsToList(LinkedInProfile.Positions positions) {
        // Implementați logica de conversie a obiectului Positions în List<Integer>
        return new ArrayList<>();
    }

    private static class AccessTokenResponse {
        String access_token;
        int expires_in;
    }

    private static class LinkedInProfile {
        String id;
        String localizedFirstName;
        String localizedLastName;
        String emailAddress;
        ProfilePicture profilePicture;
        Positions positions;

        static class ProfilePicture {
            DisplayImage displayImage;

            static class DisplayImage {
                String elements;
            }
        }

        static class Positions {
            // Define fields
        }
    }
}