package com.example.whatsappclone;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.whatsappclone.Utils.MessageChannel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeChatsFragment extends Fragment {
    private static final String TAG = HomeChatsFragment.class.getSimpleName();

    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    private DatabaseReference MessageChannelRef;

    public HomeChatsFragment() {} // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        groupFragmentView = inflater.inflate(R.layout.fragment_home_chats, container, false);
        MessageChannelRef = FirebaseDatabase.getInstance().getReference(MessageChannel.class.getSimpleName());

        InitializeFields();
        RetrieveAndDisplayGroups();

        list_view.setOnItemClickListener((adapterView, view, position, id) -> {
            String currentGroupName = adapterView.getItemAtPosition(position).toString();

            Intent groupChatIntent = new Intent(getContext(), MessageChannelActivity.class);
            groupChatIntent.putExtra("groupName" , currentGroupName);
            startActivity(groupChatIntent);
        });


        return groupFragmentView;
    }



    private void InitializeFields() {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);
    }

    private void RetrieveAndDisplayGroups() {
        Log.d(TAG, "RetrieveAndDisplayGroups()");
        MessageChannelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Set<String> set = new HashSet<>();
                list_of_groups.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    list_of_groups.add(Objects.requireNonNull(snapshot.getValue(MessageChannel.class)).getName());
                }

//                list_of_groups.clear();
//                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

}