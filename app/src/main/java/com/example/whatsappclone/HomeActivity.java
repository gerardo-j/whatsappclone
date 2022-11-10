package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private SharedPreferences authPref;
    private SharedPreferences.Editor editAuthPref;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ArrayList<MessageChannelItem> channels;
    private MessageChannelAdapter messageChannelAdapter;

    private FloatingActionButton fabCreateMessage;
    private RecyclerView recyclerMessageChannel;

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
        recyclerMessageChannel = findViewById(R.id.recyclerMessageChannel);

        recyclerMessageChannel.setLayoutManager(new LinearLayoutManager(this));

        channels = new ArrayList<>();
        messageChannelAdapter = new MessageChannelAdapter(this, channels);
        recyclerMessageChannel.setAdapter(messageChannelAdapter);

        loadChannels();
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

    private void loadChannels() {
        channels.clear();

        channels.add(new MessageChannelItem("Test", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 2", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 3", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 4", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 5", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 6", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 7", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        messageChannelAdapter.notifyItemRangeInserted(0, 7);
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