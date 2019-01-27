package com.example.ahmed.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.chat.model.Message;
import com.example.ahmed.chat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private Context context;
    private List<Message> messageList;

    public ChatAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }
    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.row_message, parent, false);
        ChatHolder holder = new ChatHolder(row);
        return holder;
    }
    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        Message message = messageList.get(position);

        if (message.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            holder.username.setTextColor(Color.RED);
            holder.username.setGravity(Gravity.END);
            holder.message.setGravity(Gravity.END);
        } else {
            holder.username.setTextColor(Color.BLUE);
            holder.username.setGravity(Gravity.START);
            holder.message.setGravity(Gravity.START);
        }
        holder.username.setText(message.getEmail());
        holder.message.setText(message.getMessage());
    }
    @Override
    public int getItemCount() {
        return messageList.size();
    }
    public class ChatHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.username_TV)
        TextView username;
        @BindView(R.id.message_TV)
        TextView message;

        public ChatHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}