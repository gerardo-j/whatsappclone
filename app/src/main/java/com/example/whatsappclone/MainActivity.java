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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SharedPreferences authPref;
    private FirebaseAuth mAuth;
    private Button btnSignOut;
    private TextView txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        btnSignOut = findViewById(R.id.btnSignOut);
        txtDescription = findViewById(R.id.txtSignInDescription);
        addClickListener();
    }

    private void addClickListener() {
        btnSignOut.setOnClickListener(view -> signOut());
    }

    private void signOut() {
        SharedPreferences.Editor editAuthPref = authPref.edit();
        editAuthPref.putBoolean("isAuth", false);
        editAuthPref.apply();
        mAuth.signOut();
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuItemSettings) {
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else {
            Log.d(TAG, "Unexpected item clicked");
        }
        return super.onOptionsItemSelected(item);
    }

}