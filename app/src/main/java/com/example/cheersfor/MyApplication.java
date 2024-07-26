package com.example.cheersfor;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Inițializează Firebase
        FirebaseApp.initializeApp(this);
    }
}
