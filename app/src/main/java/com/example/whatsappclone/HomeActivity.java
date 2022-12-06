package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private SharedPreferences authPref;
    private SharedPreferences.Editor editAuthPref;
    private FirebaseAuth mAuth;

    private TabLayout tabLayout;
    private ViewPager2 pagerHome;
    private FragmentStateAdapter pagerAdapter;

    private boolean isAllFABVisible;
    private FloatingActionButton fabQuickActions, fabCreateGroup, fabPostStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initAuth();
        initFab();
        initViews();

        pagerAdapter = new HomePagerAdapter(this);
        pagerHome.setAdapter(pagerAdapter);
        tabLayout.setTabIndicatorFullWidth(true);
        new TabLayoutMediator(tabLayout, pagerHome, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Chats");
                    break;
                case 1:
                    tab.setText("Status");
                    break;
                default:
                    break;
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currPos = tab.getPosition();
                Log.d(TAG, "inside onTabSelection: tab position = "+currPos);
                pagerHome.setCurrentItem(currPos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(this::onAuthStateChanged);
    }

    @Override
    public void onBackPressed() {
        if (pagerHome.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pagerHome.setCurrentItem(pagerHome.getCurrentItem() - 1);
        }
    }

    private void initAuth() {
        authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);
        editAuthPref = authPref.edit();
        mAuth = FirebaseAuth.getInstance();

        mAuth.addAuthStateListener(this::onAuthStateChanged);
    }

    private void initFab() {
        fabQuickActions = findViewById(R.id.fabQuickActions);
        fabCreateGroup = findViewById(R.id.fabCreateGroup);
        fabPostStory = findViewById(R.id.fabPostStory);

        fabCreateGroup.setVisibility(View.GONE);
        fabPostStory.setVisibility(View.GONE);
        isAllFABVisible = false;

        fabCreateGroup.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CreateMessageActivity.class);
            startActivity(intent);
        });

        fabPostStory.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PostStatusActivity.class);
            startActivity(intent);
        });

        fabQuickActions.setOnClickListener(view -> {
            if (!isAllFABVisible) {
                fabCreateGroup.show();
                fabPostStory.show();
                isAllFABVisible = true;
                fabQuickActions.setImageResource(R.drawable.ic_baseline_close_24);
            } else{
                fabPostStory.setVisibility(View.GONE);
                fabCreateGroup.setVisibility(View.GONE);
                isAllFABVisible = false;
                fabQuickActions.setImageResource(R.drawable.ic_baseline_add_24);
            }
        });
    }

    private void initViews() {
        pagerHome = findViewById(R.id.pagerHome);
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