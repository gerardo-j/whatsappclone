//wong sherpa
package com.example.whatsappclone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.databinding.SettingsActivityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
//import com.bumptech.glide.Glide;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    ImageView plus;
    ImageView profileImage;
    Button updateBtn;

    SettingsActivityBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.settings_activity);
        binding = DataBindingUtil.setContentView(this, R.layout.settings_activity);
        // setContentView(binding.getRoot());
        getSupportActionBar().hide();

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.aboutMe.getText().toString().equals("")
                        && !binding.userName.getText().toString().equals("")) {
                    String status = binding.aboutMe.getText().toString();
                    String username = binding.userName.getText().toString();

                    HashMap<String, Object> obj = new HashMap<>();
                    obj.put("userName", username);
                    obj.put("status", status);

                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);

                    Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SettingsActivity.this, "Please enter username and bio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Glide.with(this)
        // .load(mUser.getPhotoUrl())
        // .error(R.drawable.img)
        // .into(imageProfile);
        // txtProfileName.setText(mUser.getDisplayName());
        // txtProfileEmail.setText(mUser.getEmail());
        // btnDeleteUser.setOnClickListener(view -> deleteUser());

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get()
                                .load(users.getProfilePic())
                                .placeholder(R.drawable.img)
                                .into(binding.profileImage);

                        binding.aboutMe.setText(users.getStatus());
                        binding.userName.setText(users.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 20);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri sFile = data.getData();
            binding.profileImage.setImageURI(sFile);
            final StorageReference reference = storage.getReference().child("profile_img")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePic").setValue(uri.toString());
                        }
                    });

                }
            });
        }
    }
}
/*
 * import androidx.appcompat.app.AppCompatActivity;
 * 
 * import android.content.SharedPreferences;
 * import android.os.Bundle;
 * import android.widget.Button;
 * import android.widget.ImageView;
 * import android.widget.TextView;
 * import android.widget.Toast;
 * 
 * import com.bumptech.glide.Glide;
 * import com.google.firebase.auth.FirebaseAuth;
 * import com.google.firebase.auth.FirebaseUser;
 * 
 * public class SettingsActivity extends AppCompatActivity {
 * private static final String TAG = "SettingsActivity";
 * 
 * private SharedPreferences authPref;
 * SharedPreferences.Editor editAuthPref;
 * FirebaseAuth mAuth;
 * FirebaseUser mUser;
 * 
 * private ImageView imageProfile;
 * private TextView txtProfileName, txtProfileEmail;
 * private Button btnDeleteUser;
 * 
 * @Override
 * protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * setContentView(R.layout.activity_settings);
 * if (getSupportActionBar() != null) {
 * getSupportActionBar().setTitle("Settings");
 * }
 * 
 * authPref = getSharedPreferences(SplashActivity.AUTH_PREF_NAME, MODE_PRIVATE);
 * editAuthPref = authPref.edit();
 * mAuth = FirebaseAuth.getInstance();
 * mUser = mAuth.getCurrentUser();
 * 
 * initViews();
 * 
 * Glide.with(this)
 * .load(mUser.getPhotoUrl())
 * .error(R.drawable.default_profile_icon)
 * .into(imageProfile);
 * txtProfileName.setText(mUser.getDisplayName());
 * txtProfileEmail.setText(mUser.getEmail());
 * btnDeleteUser.setOnClickListener(view -> deleteUser());
 * }
 * 
 * private void initViews() {
 * imageProfile = findViewById(R.id.imageProfile);
 * txtProfileName = findViewById(R.id.txtProfileName);
 * txtProfileEmail = findViewById(R.id.txtProfileEmail);
 * btnDeleteUser = findViewById(R.id.btnDeleteUser);
 * }
 * 
 * private void deleteUser() {
 * mUser.delete()
 * .addOnCompleteListener(deleteUserTask -> {
 * if (deleteUserTask.isSuccessful()) {
 * finish();
 * } else {
 * Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
 * }
 * });
 * }
 * 
 * }
 */
