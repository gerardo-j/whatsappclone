package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int NUM_PAGES = 2;

    private SharedPreferences authPref;
    private SharedPreferences.Editor editAuthPref;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    private FloatingActionButton fabCreateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ;
        initAuth();
        initViews();

        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Status"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currPos = tab.getPosition();
                Log.d(TAG, "inside onTabSelection: tab position = "+currPos);
                viewPager.setCurrentItem(currPos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pagerAdapter = new HomePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        fabCreateMessage.setOnClickListener(view -> startActivity(new Intent(this, CreateMessageActivity.class)));
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

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private void initAuth() {
        authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);
        editAuthPref = authPref.edit();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mAuth.addAuthStateListener(this::onAuthStateChanged);
    }

    private void initViews() {
        fabCreateMessage = findViewById(R.id.fabCreateMessage);
        viewPager = findViewById(R.id.pagerHome);
        tabLayout = findViewById(R.id.tabLayoutHome);

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
            Log.d(TAG, "AUTH changed, authed = true");
        }
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