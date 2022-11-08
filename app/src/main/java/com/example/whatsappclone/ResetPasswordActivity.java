package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button btnResetPassword;
    private EditText editTxtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();

        initViews();
        btnResetPassword.setOnClickListener(view -> resetPassword());
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        editTxtEmail = findViewById(R.id.editTxtEmailAddress);
        btnResetPassword = findViewById(R.id.btnResetPassword);
    }

    private void resetPassword() {
        String email = editTxtEmail.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Email send.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}