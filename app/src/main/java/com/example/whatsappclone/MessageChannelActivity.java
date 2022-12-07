package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.whatsappclone.Utils.Message;
import com.example.whatsappclone.Utils.MessageChannel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MessageChannelActivity extends AppCompatActivity {
    private static final SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    private static final SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");

    private ImageButton SendMessageButton;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages, focusView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference MessageChannelRef;

    private String currentChannelId, currentChannelName, currentUserName, currentDate, currentTime, currentUserId;

    private ArrayList<Message> messages;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_channel);

        currentChannelName = getIntent().getExtras().get("channelName").toString();
        currentChannelId = getIntent().getExtras().get("channelId").toString();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        currentUserId = mUser.getUid();
        MessageChannelRef = FirebaseDatabase.getInstance()
                .getReference(MessageChannel.class.getSimpleName())
                .child(currentChannelId);

        initViews();
        getSupportActionBar().setTitle(currentChannelName);
        SendMessageButton.setOnClickListener(view -> SaveMessageInfoToDatabase());


        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messages, currentUserId);
        recyclerMessage.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessage.setAdapter(messageAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MessageChannelRef.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void initViews() {
        focusView = findViewById(R.id.focusView);
        recyclerMessage = findViewById(R.id.recyclerMessage);
        SendMessageButton = findViewById(R.id.send_message_button);
        userMessageInput = findViewById(R.id.input_group_message);
//        displayTextMessages = findViewById(R.id.group_chat_text_display);
//        mScrollView = findViewById(R.id.my_scroll_view);
    }

    private void SaveMessageInfoToDatabase() {
        String message = userMessageInput.getText().toString();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please write message first...", Toast.LENGTH_SHORT).show();
            return;
        }

        String messageKey = MessageChannelRef.child("messages").push().getKey();
        if (messageKey == null) return;

        Calendar calendar = Calendar.getInstance();
        currentDate = currentDateFormat.format(calendar.getTime());
        currentTime = currentTimeFormat.format(calendar.getTime());

        Message messageObj = new Message(currentUserId, message, currentDate, currentTime);
        MessageChannelRef.child("messages").child(messageKey).updateChildren(messageObj.toMap());
        userMessageInput.setText("");
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Message messageObject = dataSnapshot.getValue(Message.class);
        if (messageObject == null) return;
//        String message = messageObject.getMessage();
//        String username = messageObject.getSenderId();
//        String date = messageObject.getDate();
//        String time = messageObject.getTime();
//
//        displayTextMessages.append(username + ":\n" + message + "\n" + date + " " + time);
//        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);


        messages.add(messageObject);
        messageAdapter.notifyItemInserted(messages.size());
        recyclerMessage.scrollToPosition(messages.size() - 1);

    }
}