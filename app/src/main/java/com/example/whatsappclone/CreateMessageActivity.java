package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsappclone.Utils.MessageChannel;
import com.example.whatsappclone.Utils.MessageChannelDB;
import com.example.whatsappclone.Utils.User;
import com.example.whatsappclone.Utils.UserDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateMessageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private MessageChannelDB messageChannelDB;
    private UserDB userDB;

    private EditText groupNameField, friendUsernameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        messageChannelDB =  new MessageChannelDB();
        userDB = new UserDB();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Button btnCreateChannel = findViewById(R.id.btnCreateChannel);
        groupNameField = findViewById(R.id.editTxtChannelName);
        friendUsernameField = findViewById(R.id.editTxtReceiverUsername);

        btnCreateChannel.setOnClickListener(view -> createGroupRequest());
    }

    private void createGroupRequest() {
        String groupName = groupNameField.getText().toString();
        String friendUsername = friendUsernameField.getText().toString();

        if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(friendUsername)) {
            Toast.makeText(this, "Please write Group Name & friend name", Toast.LENGTH_SHORT).show();
            return;
        }

        userDB.findUserByUsername(friendUsername).addOnSuccessListener(findUserTask -> {
            if (findUserTask.exists()) {
                Log.d("CreateMessageActivity", "findUser: " + findUserTask.getData());

                User friendUser = new User(findUserTask.getString("uid"), friendUsername);
                User currentUser = new User(mUser.getUid(), mUser.getDisplayName());

                ArrayList<User> users = new ArrayList<>(Arrays.asList(currentUser, friendUser));
                MessageChannel newChannel = new MessageChannel(groupName, friendUser.getProfileImage(), users);
                messageChannelDB.create(newChannel).addOnFailureListener(createChannelTask ->
                        Toast.makeText(this, groupName + " failed to create", Toast.LENGTH_SHORT).show());
                finish();
            } else {
                Toast.makeText(this, "username don't exist", Toast.LENGTH_SHORT).show();
            }

        });

    }

}