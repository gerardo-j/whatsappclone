package com.example.whatsappclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Utils.StoryPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeStatusFragment extends Fragment {

    private ArrayList<StoryPost> postArrayList;
    private String[] postDetail, postUser;
    private int[] imageResourceID;
    private RecyclerView recyclerview;
    HomeStatusAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postArrayList = new ArrayList<>();
        dataInitialize();

        recyclerview = view.findViewById(R.id.storyRecycler);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setHasFixedSize(true);
        myAdapter = new HomeStatusAdapter(getContext(),postArrayList);
        recyclerview.setAdapter(myAdapter);
        dataInitialize();
    }

    private void dataInitialize() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(StoryPost.class.getSimpleName());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(StoryPost.class) == null) return;
                Log.d("HomeStatusFragment", "onDataChange: " + dataSnapshot);
                postArrayList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StoryPost storyPost = postSnapshot.getValue(StoryPost.class);
                    Log.d("HomeStatusFragment", "onDataChange: " + storyPost.getImageURL());
                    postArrayList.add(storyPost);
                }
                myAdapter.notifyItemInserted(postArrayList.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("homestatus", "loadPost:onCancelled", databaseError.toException());
            }

        };
        databaseReference.addValueEventListener(postListener);

//        postUser = new String[]{
//                "User1",
//                "User2",
//                "User3",
//                "User4",
//                "User5",
//        };
//
//        postDetail = new String[]{
//                "Post Detail 1",
//                "Post Detail 2",
//                "Post Detail 3",
//                "Post Detail 4",
//                "Post Detail 5",
//        };
//
//        imageResourceID = new int[]{
//                R.drawable.default_profile_icon,
//                R.drawable.default_profile_icon,
//                R.drawable.default_profile_icon,
//                R.drawable.default_profile_icon,
//                R.drawable.default_profile_icon,
//        };

//        for (int i = 0; i < postDetail.length; i++){
//            StoryPost story = new StoryPost(postUser[i],postDetail[i],imageResourceID[i]);
//            postArrayList.add(story);
//        }

    }
}