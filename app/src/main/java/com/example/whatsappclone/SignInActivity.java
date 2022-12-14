package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private SharedPreferences authPref;
    private FirebaseAuth mAuth;
    private SharedPreferences.Editor editAuthPref;

    private ProgressBar progressBar;
    private Button btnSignIn;
    private TextView txtForgotPassword, txtRegister;
    private EditText editTxtEmail, editTxtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);
        editAuthPref = authPref.edit();

        initViews();
        setClickListener();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        btnSignIn = findViewById(R.id.btnSignOut);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtRegister = findViewById(R.id.txtSignIn);
        editTxtEmail = findViewById(R.id.editTxtEmailAddress);
        editTxtPassword = findViewById(R.id.editTxtPassword);
    }

    private void setClickListener() {
        btnSignIn.setOnClickListener(view -> signIn());
        txtForgotPassword.setOnClickListener(view -> startActivity(new Intent(this, ResetPasswordActivity.class)));
        txtRegister.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void signIn() {
        String email = editTxtEmail.getText().toString().trim();
        String password = editTxtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTxtEmail.setError("Email is required");
            editTxtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTxtEmail.setError("Provide valid email");
            editTxtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTxtPassword.setError("Password is required");
            editTxtPassword.requestFocus();
            return;
        }
        if (password.length() < 7) {
            editTxtPassword.setError("Minimum length is 7");
            editTxtPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(signInTask -> {
                if (signInTask.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    editAuthPref.putBoolean("isAuth", true);
                    editAuthPref.apply();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", signInTask.getException());
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            });
    }


}