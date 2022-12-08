package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatsappclone.Utils.User;
import com.example.whatsappclone.databinding.ActivityPostStatusBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PostStatusActivity extends AppCompatActivity {

    ActivityPostStatusBinding binding;
    Uri imageURI;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnSelectStatusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }
        });

        binding.btnMakePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();
            }
        });
    }

    private void uploadImage() {
        DatabaseReference userStoryRef = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File...");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
        storageReference.putFile(imageURI)
                .addOnSuccessListener(taskSnapshot -> {
                    // get user id
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String storyPostId = userStoryRef.child(User.class.getSimpleName() + "/" + userId + "/" + StoryPost.class.getSimpleName()).push().getKey();
                    String userPath = User.class.getSimpleName() + "/" + userId + "/" + StoryPost.class.getSimpleName() + "/" + storyPostId + "/";
                    String storyPostPath = StoryPost.class.getSimpleName() + "/" + storyPostId + "/";

                    HashMap<String, Object> map = new HashMap<>();
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(
                            uri -> {
                                String imageURL = uri.toString();
                                StoryPost storyPost = new StoryPost(userId, imageURL, binding.etStatusDescription.getText().toString());
                                map.put(userPath, storyPost);
                                map.put(storyPostPath, storyPost);
//                                map.put(storyPostPath + "imageURL", imageURL);
//                                map.put(storyPostPath + "timeStamp", System.currentTimeMillis());
//                                map.put(storyPostPath + "userId", userId);
//                                map.put(storyPostPath + "caption", binding.etStatusDescription.getText().toString());
//                                map.put(userPath + "imageURL", imageURL);
//                                map.put(userPath + "timeStamp", System.currentTimeMillis());
//                                map.put(userPath + "userId", userId);
//                                map.put(userPath + "caption", binding.etStatusDescription.getText().toString());

                                userStoryRef.updateChildren(map);
//                                    progressDialog.dismiss();
                                Toast.makeText(PostStatusActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            }
                    );


                    binding.ivPostImage.setImageURI(null);
                    Toast.makeText(PostStatusActivity.this, "Succesfully Uploaded", Toast.LENGTH_LONG).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(PostStatusActivity.this, "Failed to Upload", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
            imageURI = data.getData();
            binding.ivPostImage.setImageURI(imageURI);
        }
    }
}