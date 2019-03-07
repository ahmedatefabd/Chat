package com.example.ahmed.chat.activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.adapter.PrivateChatAdapter;
import com.example.ahmed.chat.model.AllUsers;
import com.example.ahmed.chat.model.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity {
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
        ToolbarView();
        Recycler();
        mAuth = FirebaseAuth.getInstance();
        final Bundle intent = getIntent().getExtras();
        if (!mAuth.getCurrentUser().getEmail().equals("atef0755@gmail.com"))
            readMessageAnyUsers(intent);
        else
            readMessagemyAccount(intent);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessages(intent);
            }
        });
    }

    private void ToolbarView() {
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
    }

    private void Recycler() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void readMessageAnyUsers(Bundle intent) {
        id = intent.getString("id");
        currentUserid1 = mAuth.getCurrentUser().getUid();
        ReadMessages(currentUserid1, id);
    }

    private void readMessagemyAccount(Bundle intent) {
        final AllUsers users = intent.getParcelable("users");
        userID = users.getUser_ID();
        currentUserid2 = mAuth.getCurrentUser().getUid();
        ReadMessages(currentUserid2, userID);
    }

    private void sendMessages(Bundle intent) {
        mes = messageET.getText().toString().trim();
        if (!mes.equals("")){
            if (!mAuth.getCurrentUser().getEmail().equals("atef0755@gmail.com"))
                sendMessageAnyAccount(intent);
            else
                sendMessageMyAccount(intent);
        }else {
            Toast.makeText(MessageActivity.this, "you can not send empty message", Toast.LENGTH_SHORT).show();
        }
        messageET.setText("");
    }

    private void sendMessageAnyAccount(Bundle intent) {
        id = intent.getString("id");
        currentUserid1 = mAuth.getCurrentUser().getUid();
        sendMessage(currentUserid1, id, mes);
    }

    private void sendMessageMyAccount(Bundle intent) {
        final AllUsers users = intent.getParcelable("users");
        userID = users.getUser_ID();
        currentUserid2 = mAuth.getCurrentUser().getUid();
        sendMessage(currentUserid2, users.getUser_ID(), mes);
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}