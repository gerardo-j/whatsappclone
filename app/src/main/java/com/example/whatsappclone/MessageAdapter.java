package com.example.whatsappclone;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Utils.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final String TAG = MessageAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Message> messages;
    private String currentUserId;

    public MessageAdapter(Context context, ArrayList<Message> messages, String currentUserId) {
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        holder.bindItem(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout messageLayout;
        private final TextView txtSenderId, txtMessage, txtDate, txtTime;
        private String senderId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageLayout = itemView.findViewById(R.id.messageLayout);
            txtSenderId = itemView.findViewById(R.id.txtSenderId);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
        }

        public void bindItem(Message currentMessage) {
            senderId = currentMessage.getSenderId();
            txtSenderId.setText(senderId);
            txtMessage.setText(currentMessage.getMessage());
            txtDate.setText(currentMessage.getDate());
            txtTime.setText(currentMessage.getTime());

            if (senderId.equals(currentUserId)) {
                txtSenderId.setText("You");
                txtSenderId.setTextColor(context.getResources().getColor(R.color.black));
                txtMessage.setTextColor(context.getResources().getColor(R.color.black));
                txtDate.setTextColor(context.getResources().getColor(R.color.black));
                txtTime.setTextColor(context.getResources().getColor(R.color.black));
                messageLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

            } else {
                txtSenderId.setTextColor(context.getResources().getColor(R.color.white));
                txtMessage.setTextColor(context.getResources().getColor(R.color.white));
                txtDate.setTextColor(context.getResources().getColor(R.color.white));
                txtTime.setTextColor(context.getResources().getColor(R.color.white));

                messageLayout.setBackgroundColor(context.getResources().getColor(R.color.blue_200));
            }
        }

    }
}