package com.example.whatsappclone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MessageChannelAdapter extends RecyclerView.Adapter<MessageChannelAdapter.ViewHolder> {
    private static final String TAG = "MessageChannelAdapter";
    private Context context;
    private ArrayList<MessageChannelItem> channels;

    public MessageChannelAdapter(Context context, ArrayList<MessageChannelItem> channels) {
        this.context = context;
        this.channels = channels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.channel_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(channels.get(position));
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView txtChannelName;
        private final ImageView imageChannel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChannelName = itemView.findViewById(R.id.txtChannelName);
            imageChannel = itemView.findViewById(R.id.imageChannel);
            itemView.setOnClickListener(this);
        }

        public void bindItem(MessageChannelItem currentMessageChannelItem) {
            txtChannelName.setText(currentMessageChannelItem.getName());
            Glide.with(context).load(currentMessageChannelItem.getImageUrl()).into(imageChannel);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Clicked view");
            context.startActivity(new Intent(context, MessageChannelActivity.class));
        }
    }
}
