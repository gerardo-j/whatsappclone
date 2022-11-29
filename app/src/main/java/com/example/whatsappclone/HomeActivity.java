package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsappclone.Utils.MessageChannel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private SharedPreferences authPref;
    private SharedPreferences.Editor editAuthPref;
    private FirebaseAuth mAuth;
    private DatabaseReference MessageChannelRef;

    private TabLayout tabLayout;
    private ViewPager2 pagerHome;
    private FragmentStateAdapter pagerAdapter;

    private boolean isAllFABVisible;
    private FloatingActionButton fabCreateMessage;
    private FloatingActionButton fabCreateConvo;
    private FloatingActionButton fabPostStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initAuth();
        initViews();

        pagerAdapter = new HomePagerAdapter(this);
        pagerHome.setAdapter(pagerAdapter);
        MessageChannelRef = FirebaseDatabase.getInstance().getReference(MessageChannel.class.getSimpleName());
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

        //fabCreateMessage.setOnClickListener(view -> startActivity(new Intent(this, CreateMessageActivity.class)));
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

    private void initViews() {
        fabCreateMessage = findViewById(R.id.fabCreateMessage);
        fabCreateConvo = findViewById(R.id.fabCreateConvo);
        fabPostStory = findViewById(R.id.fabPostStory);

        fabCreateConvo.setVisibility(View.GONE);
        fabPostStory.setVisibility(View.GONE);

        isAllFABVisible = false;

        Resources res = this.getResources();
        Drawable minusImage = ResourcesCompat.getDrawable(res, R.drawable.minus_sign, null);
        fabCreateMessage.setImageDrawable(minusImage);
        fabCreateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAllFABVisible){
                    fabCreateConvo.show();
                    fabPostStory.show();
                    isAllFABVisible = true;
                    //fabCreateMessage.setImageDrawable(minusImage);
                }
                else{
                    fabPostStory.setVisibility(View.GONE);
                    fabCreateConvo.setVisibility(View.GONE);
                    isAllFABVisible = false;
                    //Drawable minusImage = ResourcesCompat.getDrawable(res, R.drawable., null);
                }
            }
        });

        fabCreateConvo.setOnClickListener(view -> startActivity(new Intent(this, CreateMessageActivity.class)));

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
        if (item.getItemId() == R.id.createGroup) {
            createGroupRequest();
            return true;
        }
        // add create chat
        return super.onOptionsItemSelected(item);
    }

    private void createGroupRequest() {
        final EditText groupNameField = new EditText(HomeActivity.this);
        groupNameField.setHint("e.g Group Name");

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, com.google.android.material.R.style.Theme_Material3_DynamicColors_DayNight);
        builder.setTitle("Enter Group Name :");
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", (dialogInterface, i) -> {
            String groupName = groupNameField.getText().toString();

            if (TextUtils.isEmpty(groupName)) {
                Toast.makeText(HomeActivity.this, "Please write Group Name...", Toast.LENGTH_SHORT).show();
            } else {
                createGroupRequest(groupName);
            }
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
}

    private void createGroupRequest(final String groupName) {
        MessageChannel temp = new MessageChannel();
        temp.setName(groupName);
        MessageChannelRef.child(groupName).setValue(temp).addOnSuccessListener(taskResult ->
                Toast.makeText(HomeActivity.this, groupName + " group is Created Successfully...", Toast.LENGTH_SHORT).show()
        );
    }
}