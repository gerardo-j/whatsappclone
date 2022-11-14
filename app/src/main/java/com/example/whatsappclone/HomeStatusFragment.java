package com.example.whatsappclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeStatusFragment extends Fragment {

    private ArrayList<StoryPost> postArrayList;
    private String[] postDetail, postUser;
    private int[] imageResourceID;
    private RecyclerView recyclerview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataInitialize();

        recyclerview = view.findViewById(R.id.storyRecycler);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        HomeStatusAdapter myAdapter = new HomeStatusAdapter(getContext(),postArrayList);
        recyclerview.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

    }

    private void dataInitialize() {
        postArrayList = new ArrayList<>();

        postUser = new String[]{
                "User1",
                "User2",
                "User3",
                "User4",
                "User5",
        };

        postDetail = new String[]{
                "Post Detail 1",
                "Post Detail 2",
                "Post Detail 3",
                "Post Detail 4",
                "Post Detail 5",
        };

        imageResourceID = new int[]{
                R.drawable.default_profile_icon,
                R.drawable.default_profile_icon,
                R.drawable.default_profile_icon,
                R.drawable.default_profile_icon,
                R.drawable.default_profile_icon,
        };

        for (int i = 0; i < postDetail.length; i++){
            StoryPost story = new StoryPost(postUser[i],postDetail[i],imageResourceID[i]);
            postArrayList.add(story);
        }

    }
}