package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.internal.InternalTokenResult;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private SharedPreferences authPref;
    private SharedPreferences.Editor editAuthPref;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FloatingActionButton fabCreateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ;
        authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);
        editAuthPref = authPref.edit();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mAuth.addAuthStateListener(this::onAuthStateChanged);

        fabCreateMessage = findViewById(R.id.fabCreateMessage);
        addClickListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mUser == null){
            Toast.makeText(this, "User is null - Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(this::onAuthStateChanged);
    }

    private void onAuthStateChanged(FirebaseAuth auth) {
        if (auth.getCurrentUser() == null) {
            Log.d(TAG, "AUTH changed, authed = false");
            editAuthPref.putBoolean("isAuth", false);
            editAuthPref.apply();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else {
            auth.getCurrentUser().reload();
            Log.d(TAG, "onAuthStateChanged reload()");
            Log.d(TAG, "AUTH changed, authed = true");
        }
    }

    private void addClickListener() {
        fabCreateMessage.setOnClickListener(view -> startActivity(new Intent(this, CreateMessageActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuItemSettings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.menuItemSignOut) {
            mAuth.signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}