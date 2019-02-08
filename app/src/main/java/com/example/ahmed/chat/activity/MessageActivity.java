package com.example.ahmed.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.adapter.PrivateChatAdapter;
import com.example.ahmed.chat.model.AllUsers;
import com.example.ahmed.chat.model.ChatMessage;
import com.example.ahmed.chat.model.Messagess;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    TextView profileName;
    TextView profileLastSeen;
    CircleImageView profileImage;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.message_ET)
    EditText messageET;
    @BindView(R.id.send_btn)
    ImageView sendBtn;
    private Toolbar toolbar;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private PrivateChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private String userID;
    private String mes;
    private String currentUserid1;
    private String currentUserid2;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.Chat_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        final Bundle intent = getIntent().getExtras();

        if (!mAuth.getCurrentUser().getEmail().equals("atef0755@gmail.com")){
            Toast.makeText(this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
            id = intent.getString("id");
            currentUserid1 = mAuth.getCurrentUser().getUid();
            ReadMessages(currentUserid1, id);

        }else {
            Toast.makeText(this, "atef0755@gmail.com", Toast.LENGTH_LONG).show();
            final AllUsers users = intent.getParcelable("users");
            userID = users.getUser_ID();
            currentUserid2 = mAuth.getCurrentUser().getUid();
            ReadMessages(currentUserid2, userID);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mes = messageET.getText().toString().trim();
                if (!mes.equals("")){
                    if (!mAuth.getCurrentUser().getEmail().equals("atef0755@gmail.com")){
                        Toast.makeText(MessageActivity.this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                        id = intent.getString("id");
                        currentUserid1 = mAuth.getCurrentUser().getUid();
                        sendMessage(currentUserid1, id, mes);
                    }else {
                        Toast.makeText(MessageActivity.this, "atef0755@gmail.com", Toast.LENGTH_LONG).show();
                        final AllUsers users = intent.getParcelable("users");
                        userID = users.getUser_ID();
                        currentUserid2 = mAuth.getCurrentUser().getUid();
                        sendMessage(currentUserid2, users.getUser_ID(), mes);
                    }
                }else {
                    Toast.makeText(MessageActivity.this, "you can not send empty message", Toast.LENGTH_SHORT).show();
                }
                messageET.setText("");
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Message").push().setValue(hashMap);
    }

    private void ReadMessages(final String myID, final String userId){
        messageList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Message");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);

                    if (chatMessage.getReceiver().equals(myID) && chatMessage.getSender().equals(userId) ||
                            chatMessage.getReceiver().equals(userId) && chatMessage.getSender().equals(myID)){

                        messageList.add(chatMessage);
                    }

                    chatAdapter = new PrivateChatAdapter(MessageActivity.this, messageList);
                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
