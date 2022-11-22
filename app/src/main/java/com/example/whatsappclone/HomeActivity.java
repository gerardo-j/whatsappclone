package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private DatabaseReference RootRef;

    private TabLayout tabLayout;
    private ViewPager2 pagerHome;
    private FragmentStateAdapter pagerAdapter;

    private FloatingActionButton fabCreateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initAuth();
        initViews();

        pagerAdapter = new HomePagerAdapter(this);
        pagerHome.setAdapter(pagerAdapter);
        RootRef = FirebaseDatabase.getInstance().getReference();
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

        fabCreateMessage.setOnClickListener(view -> startActivity(new Intent(this, CreateMessageActivity.class)));
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

    private void createGroupRequest()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name :");

        final EditText groupNameField = new EditText(HomeActivity.this);
        groupNameField.setHint("e.g Group Name");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String groupName = groupNameField.getText().toString();

                if (TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(HomeActivity.this, "Please write Group Name...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    createGroupRequest(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        builder.show();

}

    private void createGroupRequest(final String groupName) {
        RootRef.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(HomeActivity.this, groupName + " group is Created Successfully...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}