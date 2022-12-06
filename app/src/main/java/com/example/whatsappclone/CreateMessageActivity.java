package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsappclone.Utils.MessageChannel;
import com.example.whatsappclone.Utils.MessageChannelDB;
import com.example.whatsappclone.Utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateMessageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private MessageChannelDB messageChannelDB;

    private EditText groupNameField, friendNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        messageChannelDB =  new MessageChannelDB();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Button btnCreateChannel = findViewById(R.id.btnCreateChannel);
        groupNameField = findViewById(R.id.editTxtReceiverUsername);
        friendNameField = findViewById(R.id.editTxtChannelName);

        btnCreateChannel.setOnClickListener(view -> createGroupRequest());
    }

    private void createGroupRequest() {
        String groupName = groupNameField.getText().toString();
        String friendName = friendNameField.getText().toString();

        User currentUser = new User(mUser.getUid(), mUser.getDisplayName());
        User receiverUser = findUser(friendName);

        if (TextUtils.isEmpty(groupName)) {
            Toast.makeText(this, "Please write Group Name...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (receiverUser.getUid() == null) {
            Toast.makeText(this, friendName + " does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<User> users = new ArrayList<>(Arrays.asList(currentUser, receiverUser));
        MessageChannel newChannel = new MessageChannel(groupName, receiverUser.getProfileImage(), users);
        messageChannelDB.create(newChannel).addOnFailureListener(createChannelTask ->
                Toast.makeText(this, groupName + " failed to create", Toast.LENGTH_SHORT).show());
    }

    private User findUser(String username) {
        return new User();
    }
}