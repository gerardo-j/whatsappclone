package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {
    public static final String AUTH_PREF_NAME = "AuthPref";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences authPref = getSharedPreferences(AUTH_PREF_NAME, MODE_PRIVATE);
        boolean isAuth = authPref.getBoolean("isAuth", false);

        Log.d(TAG, "Is authed = " + isAuth);

        if (isAuth) { // Start main activity
            startActivity(new Intent(this, MainActivity.class));
        } else { // Start login activity
            startActivity(new Intent(this, SignInActivity.class));
        }

        finish();
    }

}