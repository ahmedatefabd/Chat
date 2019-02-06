package com.example.ahmed.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmed.chat.adapter.ChatAdapter;
import com.example.ahmed.chat.model.Message;
import com.example.ahmed.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ET_message)
    EditText ET_Message;
    @BindView(R.id.FloatingActionButton)
    android.support.design.widget.FloatingActionButton FloatingActionButton;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.MyChat_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MyChat");

        ButterKnife.bind(MainActivity.this);
        FloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveData();
                ReturnData();
            }
        });
    }

    private void SaveData() {
        Message message = new Message();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        message.setEmail(email);
        String mes = ET_Message.getText().toString() ;
        ET_Message.setText("");
        message.setMessage(mes);
        String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

        FirebaseDatabase.getInstance().getReference().child("Chat")
                .child(key).setValue( message )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                        }else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void ReturnData() {
        FirebaseDatabase.getInstance().getReference().child("Chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Message> messageList = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Message message = child.getValue(Message.class);
                            messageList.add(message);
                        }
                        recyclerView(messageList);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void recyclerView(List<Message> messageList) {
        ChatAdapter adapter = new ChatAdapter(MainActivity.this , messageList );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.Account_Settings:
                startActivity(new Intent(this, AccountSettingsActivity.class));

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}