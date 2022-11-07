package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btnSignOut;
    private TextView txtDescription;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        btnSignOut = findViewById(R.id.btnRegister);
        txtDescription = findViewById(R.id.txtSignInDescription);
        addClickListener();
    }

    private void addClickListener() {
        btnSignOut.setOnClickListener(view -> signOut());
    }

    private void signOut() {

    }

}