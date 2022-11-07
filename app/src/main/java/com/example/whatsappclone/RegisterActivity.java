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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    SharedPreferences authPref;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar ;
    private Button btnSignIn;
    private TextView txtLogin;
    private EditText editTxtEmail, editTxtPassword, editTxtFirstName, editTxtLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);

        progressBar = findViewById(R.id.progressBar);
        btnSignIn = findViewById(R.id.btnSignOut);
        txtLogin = findViewById(R.id.txtSignIn);
        editTxtFirstName = findViewById(R.id.editTxtFirstName);
        editTxtLastName = findViewById(R.id.editTxtLastName);
        editTxtEmail = findViewById(R.id.editTxtEmailAddress);
        editTxtPassword = findViewById(R.id.editTxtPassword);
        setClickListener();
    }

    private void setClickListener() {
        btnSignIn.setOnClickListener(view -> registerUser());
        txtLogin.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private void registerUser() {
        Log.d(TAG, "Register");

        String firstName = editTxtFirstName.getText().toString().trim();
        String lastName = editTxtLastName.getText().toString().trim();
        String email = editTxtEmail.getText().toString().trim();
        String password = editTxtPassword.getText().toString().trim();

        if (firstName.isEmpty()) {
            editTxtFirstName.setError("First name is required");
            editTxtFirstName.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            editTxtLastName.setError("Last name is required");
            editTxtLastName.requestFocus();
            return;
        }
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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "registerWithEmail:success");
                        User user = new User(firstName, lastName, email);
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            FirebaseDatabase.getInstance().getReference("user")
                                    .child(currentUser.getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d(TAG, "success");
                                            Toast.makeText(this, "DB register success.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(this, "DB register failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        SharedPreferences.Editor editAuthPref = authPref.edit();
                        editAuthPref.putBoolean("isAuth", true);
                        editAuthPref.apply();
                        Toast.makeText(this, "Authentication register success.", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

}