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

        imageProfile = findViewById(R.id.imageProfile);
        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
        Glide.with(this)
            .load(mUser.getPhotoUrl())
            .error(R.drawable.default_profile_icon)
            .into(imageProfile);
        txtProfileName.setText(mUser.getDisplayName());
        txtProfileEmail.setText(mUser.getEmail());
        btnDeleteUser.setOnClickListener(view -> deleteUser());
    }

    private void deleteUser() {
        Toast.makeText(this, "Clicked Delete", Toast.LENGTH_LONG).show();
        mUser.delete()
            .addOnCompleteListener(deleteUserTask -> {
                if (deleteUserTask.isSuccessful()) {
                    editAuthPref.putBoolean("isAuth", false);
                    editAuthPref.apply();
                    Toast.makeText(this, "Deleted user", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, SplashActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            });
    }

}