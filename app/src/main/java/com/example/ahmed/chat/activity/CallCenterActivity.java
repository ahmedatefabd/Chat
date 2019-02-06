package com.example.ahmed.chat.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.model.Chat_Call_Center;
import com.example.ahmed.chat.model.ClintData;
import com.example.ahmed.chat.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CallCenterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public Chat_Call_Center chatCallCenter;

    private DatabaseReference databaseReference;
    private DatabaseReference referenceChatCenter;
    private DatabaseReference AdmainReferance;
    private FirebaseAuth mAuth;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_center);

        toolbar = findViewById(R.id.Call_Center_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Call Center");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        final String clintID = intent.getStringExtra("id");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.Extra.DATA_CLINT).child(user_id);
        referenceChatCenter = FirebaseDatabase.getInstance().getReference().child(Constant.Extra.CHILD_CALL_CENTER);

        RetriveAdmainData();
        RetriveClintData();
    }

    private void RetriveAdmainData() {

    }

    private void RetriveClintData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClintData clintData = new ClintData();
                clintData.setUser_Name(dataSnapshot.child("user_Name").getValue().toString());
                clintData.setUser_Email(dataSnapshot.child("user_Email").getValue().toString());
                clintData.setUser_ID(dataSnapshot.child("user_ID").getValue().toString());

                referenceChatCenter.child(clintData.getUser_ID()).child(Constant.Extra.CHILD_CALL_CENTER_MESSAGE)
                        .setValue(clintData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
