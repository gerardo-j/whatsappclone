package com.example.whatsappclone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Utils.StoryPost;

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
        this.stories = dataset;
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
        holder.storyUsername.setText(story.getUserId());
        Glide.with(context).load(story.getImageURL()).into(holder.storyPost);
//        holder.storyPost.setImageResource(story.getImageURL());
        holder.storyDetail.setText(story.getCaption());

        Log.d("HomeStatusAdapter", "onBindViewHolder: " + story.getImageURL());
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }
}
