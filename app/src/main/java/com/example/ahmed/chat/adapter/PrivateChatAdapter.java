package com.example.ahmed.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.model.ChatMessage;
import com.example.ahmed.chat.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivateChatAdapter extends RecyclerView.Adapter<PrivateChatAdapter.PrivateChatHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    FirebaseUser fUser;

    private Context context;
    private List<ChatMessage> chatMessageList;
    private String imageurl;

    public PrivateChatAdapter(Context context, List<ChatMessage> messageList) {
        this.context = context;
        this.chatMessageList = messageList;
    }

    @Override
    public PrivateChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View row = LayoutInflater.from(context).inflate(R.layout.row_chat_message, parent, false); // row right
            PrivateChatHolder holder = new PrivateChatHolder(row);
            return holder;
        }else {
            View row = LayoutInflater.from(context).inflate(R.layout.row_chat_message, parent, false); // row left
            PrivateChatHolder holder = new PrivateChatHolder(row);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(PrivateChatHolder holder, int position) {

        ChatMessage chatMessage = chatMessageList.get(position);
    }

    @Override
    public int getItemCount() {
       return chatMessageList.size();
    }

    public class PrivateChatHolder extends RecyclerView.ViewHolder {

        public PrivateChatHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
       fUser = FirebaseAuth.getInstance().getCurrentUser();
       if (chatMessageList.get(position).getSender().equals(fUser.getUid())){
           return MSG_TYPE_RIGHT;
       }else {
           return MSG_TYPE_LEFT;
       }
    }
}
