package com.example.whatsappclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.Utils.Message;
import com.example.whatsappclone.Utils.MessageChannel;
import com.example.whatsappclone.Utils.MessageChannelDB;
import com.example.whatsappclone.Utils.User;
import com.example.whatsappclone.Utils.UserDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeChatsFragment extends Fragment {

    private static final String TAG = HomeChatsFragment.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private MessageChannelDB messageChannelDB;
    private UserDB userDB;

    private ArrayList<MessageChannel> channels;
    private MessageChannelAdapter messageChannelAdapter;
    private RecyclerView recyclerMessageChannel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageChannelDB =  new MessageChannelDB();
        userDB = new UserDB();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        channels = new ArrayList<>();
        messageChannelAdapter = new MessageChannelAdapter(getActivity(), channels);
        recyclerMessageChannel = view.findViewById(R.id.recyclerMessageChannel);
        recyclerMessageChannel.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerMessageChannel.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        recyclerMessageChannel.setAdapter(messageChannelAdapter);

        loadChannels();
    }

    private void loadChannels() {
//        channels.clear();
//
//        ArrayList<User> users = new ArrayList<>(Arrays.asList(new User(mUser.getUid(), "username1", "test@test.com"),new User(mUser.getUid(), "username2", "test@test.com")));
//        ArrayList<Message> messages = new ArrayList<>(Arrays.asList(new Message(mUser.getUid(),"Message 1"), new Message(mUser.getUid(),"Message 2")));
//        channels.add(new MessageChannel("Test Channel, Locally created", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png", users, messages));
//
//        messageChannelAdapter.notifyItemInserted(0);

        ValueEventListener messageChannelListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(MessageChannel.class) == null) return;

                channels.clear();
                Log.d(TAG, "onDataChange");
                Log.d(TAG, snapshot.toString());

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageChannel messageChannel = dataSnapshot.getValue(MessageChannel.class);
                    messageChannel.setId(dataSnapshot.getKey());
                    channels.add(messageChannel);
                }
                messageChannelAdapter.notifyItemInserted(channels.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        };

        userDB.addChannelListener(mUser.getUid(), messageChannelListener);
    }
}