package com.example.whatsappclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeStatusAdapter extends RecyclerView.Adapter<HomeStatusAdapter.ViewHolder> {
    private ArrayList<StoryPost> stories;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView storyUsername,storyDetail;
        ImageView storyPost;


        public ViewHolder(View itemView) {
            super(itemView);
            storyUsername = itemView.findViewById(R.id.storyUser);
            storyDetail = itemView.findViewById(R.id.storyDetail);
            storyPost = itemView.findViewById(R.id.storyPost);
        }
    }

    public HomeStatusAdapter(Context context, ArrayList<StoryPost> dataset){
        this.context = context;
        stories = dataset;
    }


    @NonNull
    @Override
    public HomeStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.status_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeStatusAdapter.ViewHolder holder, int position) {
        StoryPost story = stories.get(position);
        holder.storyUsername.setText(story.username);
        holder.storyPost.setImageResource(story.postImage);
        holder.storyDetail.setText(story.postDetail);
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }
}
