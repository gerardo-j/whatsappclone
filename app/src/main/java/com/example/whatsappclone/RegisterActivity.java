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

import com.example.whatsappclone.Utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String DEFAULT_PROFILE_IMAGE = "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png";

    private SharedPreferences authPref;
    private SharedPreferences.Editor editAuthPref;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private ProgressBar progressBar;
    private Button btnSignIn;
    private TextView txtLogin;
    private EditText editTxtUsername, editTxtEmail, editTxtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);
        editAuthPref = authPref.edit();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        initViews();
        setClickListener();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        btnSignIn = findViewById(R.id.btnSignOut);
        txtLogin = findViewById(R.id.txtSignIn);
        editTxtUsername = findViewById(R.id.editTxtUsername);
        editTxtEmail = findViewById(R.id.editTxtEmailAddress);
        editTxtPassword = findViewById(R.id.editTxtPassword);
    }

    private void setClickListener() {
        btnSignIn.setOnClickListener(view -> registerUser());
        txtLogin.setOnClickListener(view -> startActivity(new Intent(this, SignInActivity.class)));
    }

    private void registerUser() {
        Log.d(TAG, "Register");

        String username = editTxtUsername.getText().toString().trim();
        String email = editTxtEmail.getText().toString().trim();
        String password = editTxtPassword.getText().toString().trim();

        if (username.isEmpty()) {
            editTxtUsername.setError("Username is required");
            editTxtUsername.requestFocus();
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

        mFirestore.collection("usernames").document(username).get()
                .addOnCompleteListener(getUsernamesTasks -> {
                    if (getUsernamesTasks.isSuccessful()) {
                        Log.d(TAG, "getUsernameTasks() successful");
                        Log.d(TAG, getUsernamesTasks.getResult().getId() + " => " + getUsernamesTasks.getResult().getData());

                        if (getUsernamesTasks.getResult().getData() == null) {
                            Log.d(TAG, "username not found... creating new user");
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(createUserTask -> {
                                        if (createUserTask.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            if (currentUser != null) {
                                                User user = new User(currentUser.getUid(), username, email, DEFAULT_PROFILE_IMAGE);
//                                                String displayName = firstName + ' ' + lastName;
//                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                                        .setDisplayName(displayName)
//                                                        .setPhotoUri(Uri.parse(DEFAULT_PROFILE_IMAGE))
//                                                        .build();
//                                                currentUser.updateProfile(profileUpdates);

                                                HashMap<String, Object> result = new HashMap<>();
                                                result.put("uid", user.getUid());
                                                mFirestore.collection("usernames").document(username)
                                                        .set(result);
                                                FirebaseDatabase.getInstance().getReference(User.class.getSimpleName())
                                                        .child(currentUser.getUid())
                                                        .setValue(user)
                                                        .addOnCompleteListener(updateDatabaseTask -> {
                                                            if (updateDatabaseTask.isSuccessful()) {
                                                                editAuthPref.putBoolean("isAuth", true);
                                                                editAuthPref.apply();
                                                                startActivity(new Intent(this, HomeActivity.class));
                                                                finish();
                                                            } else {
                                                                Log.d(TAG, "updateDatabaseTask() failed");
                                                                Toast.makeText(this, "Failed to create user Realtime DB", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.w(TAG, "signInWithEmail:failure", createUserTask.getException());
                                        }
                                        progressBar.setVisibility(View.INVISIBLE);
                                    });
                        } else {
                            Log.d(TAG, "username found");
                            editTxtUsername.setError("Username already exists");
                            editTxtUsername.requestFocus();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Log.d(TAG, "getUsernameTasks() failed");
                    }
                });

    }

}