package com.example.whatsappclone;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Utils.MessageChannel;

import java.util.ArrayList;

public class MessageChannelAdapter extends RecyclerView.Adapter<MessageChannelAdapter.ViewHolder> {
    private static final String TAG = "MessageChannelAdapter";
    private Context context;
    private ArrayList<MessageChannel> channels;

    public MessageChannelAdapter(Context context, ArrayList<MessageChannel> channels) {
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
        private String channelId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtChannelName = itemView.findViewById(R.id.txtChannelName);
            imageChannel = itemView.findViewById(R.id.imageChannel);
            itemView.setOnClickListener(this);
        }

        public void bindItem(MessageChannel currentMessageChannel) {
            channelId = currentMessageChannel.getId();
            txtChannelName.setText(currentMessageChannel.getName());
            Glide.with(context).load(currentMessageChannel.getImageUrl()).into(imageChannel);
        }

        @Override
        public void onClick(View view) {
            Intent messageChannelIntent = new Intent(context, MessageChannelActivity.class);
            messageChannelIntent.putExtra("channelName", txtChannelName.getText().toString());
            messageChannelIntent.putExtra("channelId", channelId);
            context.startActivity(messageChannelIntent);
        }
    }
}
