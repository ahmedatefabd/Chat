package com.example.ahmed.chat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.model.AllUsers;
import com.example.ahmed.chat.model.Messagess;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

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
    private DatabaseReference storeUser;
    private FirebaseAuth mAuth;
    private String currentUserid;

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

        final Messagess messagess = new Messagess();

        Bundle intent = getIntent().getExtras();
//        final String id = intent.getString("id");
        final String ClintID = intent.getString("userID");
//        Toast.makeText(this, ClintID, Toast.LENGTH_LONG).show();
        final AllUsers users = intent.getParcelable("users");
        profileName = findViewById(R.id.profile_Name);
        profileImage = findViewById(R.id.profile_Image);
        profileLastSeen = findViewById(R.id.profile_LastSeen);

        profileName.setText(users.getUser_name());
        profileLastSeen.setText(users.getUser_ID());
        Toast.makeText(this, users.getUser_ID(), Toast.LENGTH_SHORT).show();
        Picasso.get().load(users.getUser_image()).placeholder(R.drawable.user).into(profileImage);

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes = messageET.getText().toString().trim();
                if (!mes.equals("")){
                    sendMessage(currentUserid, users.getUser_ID(), mes);
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
}
