package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private SharedPreferences authPref;
    SharedPreferences.Editor editAuthPref;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private ImageView imageProfile;
    private TextView txtProfileName, txtProfileEmail;
    private Button btnDeleteUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
        }

        authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);
        editAuthPref = authPref.edit();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        initViews();

        Glide.with(this)
            .load(mUser.getPhotoUrl())
            .error(R.drawable.default_profile_icon)
            .into(imageProfile);
        txtProfileName.setText(mUser.getDisplayName());
        txtProfileEmail.setText(mUser.getEmail());
        btnDeleteUser.setOnClickListener(view -> deleteUser());
    }

    private void initViews() {
        imageProfile = findViewById(R.id.imageProfile);
        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
    }

    private void deleteUser() {
        mUser.delete()
            .addOnCompleteListener(deleteUserTask -> {
                if (deleteUserTask.isSuccessful()) {
                    finish();
                } else {
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            });
    }

}