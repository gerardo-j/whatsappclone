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

import java.util.ArrayList;
import java.util.Objects;

public class HomeChatsFragment extends Fragment {

    private ArrayList<MessageChannelItem> channels;
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
        Log.d("Constructor", "onViewCreated()");
        recyclerMessageChannel = view.findViewById(R.id.recyclerMessageChannel);
        Log.d("Constructor", String.valueOf(recyclerMessageChannel == null));

        recyclerMessageChannel.setLayoutManager(new LinearLayoutManager(getActivity()));

        channels = new ArrayList<>();
        messageChannelAdapter = new MessageChannelAdapter(getActivity(), channels);
        recyclerMessageChannel.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        recyclerMessageChannel.setAdapter(messageChannelAdapter);

        loadChannels();


    }

    private void loadChannels() {
        channels.clear();

        channels.add(new MessageChannelItem("Test", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest 2", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 3", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 4", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 5", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 6", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 7", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 8", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 9", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 10", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        channels.add(new MessageChannelItem("Test 11", "https://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/profile-icon.png"));
        messageChannelAdapter.notifyItemRangeInserted(0, 12);
    }
}