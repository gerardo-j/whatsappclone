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


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private SharedPreferences authPref;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar ;
    private Button btnSignIn;
    private TextView txtForgotPassword, txtRegister;
    private EditText editTxtEmail, editTxtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);

        progressBar = findViewById(R.id.progressBar);
        btnSignIn = findViewById(R.id.btnSignOut);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtRegister = findViewById(R.id.txtSignIn);
        editTxtEmail = findViewById(R.id.editTxtEmailAddress);
        editTxtPassword = findViewById(R.id.editTxtPassword);

        setClickListener();
    }

    private void setClickListener() {
        btnSignIn.setOnClickListener(view -> signIn());
        txtForgotPassword.setOnClickListener(view -> Log.d(TAG, "Forgot Password"));
        txtRegister.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void signIn() {
        Log.d(TAG, "SignIn");

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
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(this, "Authentication success.", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editAuthPref = authPref.edit();
                        editAuthPref.putBoolean("isAuth", true);
                        editAuthPref.apply();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }


}