package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button btnResetPassword;
    private EditText editTxtEmail;
    private TextView txtGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();

        initViews();
        btnResetPassword.setOnClickListener(view -> resetPassword());
        txtGoBack.setOnClickListener(view -> finish());
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        editTxtEmail = findViewById(R.id.editTxtEmailAddress);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        txtGoBack = findViewById(R.id.txtGoBack);
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
            .addOnCompleteListener(resetPasswordTask -> {
                progressBar.setVisibility(View.INVISIBLE);
                if (resetPasswordTask.isSuccessful()) {
                    Toast.makeText(this, "Email send.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                }
            });
    }


}