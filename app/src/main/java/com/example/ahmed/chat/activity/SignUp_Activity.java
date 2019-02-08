package com.example.ahmed.chat.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.model.AllUsers;
import com.example.ahmed.chat.model.Chat_Call_Center;
import com.example.ahmed.chat.model.ClintData;
import com.example.ahmed.chat.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp_Activity extends AppCompatActivity {
    @BindView(R.id.email_ET)
    EditText emailET;
    @BindView(R.id.password_ET)
    EditText passwordET;
    @BindView(R.id.sign_up_btn)
    Button signUpBtn;
    @BindView(R.id.name)
    EditText name;
    private Toolbar toolbar;
    public Chat_Call_Center call_center;
    private DatabaseReference storeUser;
    private DatabaseReference callCenterReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);

        ButterKnife.bind(SignUp_Activity.this);
        toolbar = findViewById(R.id.sign_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SignUp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        call_center = new Chat_Call_Center();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString();
                final String Name = name.getText().toString().trim();
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    storeUser = FirebaseDatabase.getInstance().getReference()
                                            .child(Constant.Extra.CHILD_USERS).child(current_user_id);
                                    storeUser.child(Constant.ExtraBranch.USER_NAME).setValue(Name);
                                    storeUser.child(Constant.ExtraBranch.USER_STATUSE).setValue("default_status");
                                    storeUser.child(Constant.ExtraBranch.USER_IMAGE).setValue("default_profile");
                                    storeUser.child("user_ID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    storeUser.child(Constant.ExtraBranch.USER_THUMB_IMAGE).setValue("default_profile")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(SignUp_Activity.this, "Success Save Account in Database", Toast.LENGTH_SHORT).show();
                                                        saveData();
                                                        callCenterBranch();
                                                        if (email.equals("atef0755@gmail.com")){
                                                            Intent intent = new Intent(SignUp_Activity.this, HomeActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }else {
                                                            Intent intent = new Intent(SignUp_Activity.this, ClintUserActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(SignUp_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void callCenterBranch() {
        final ClintData clintData = new ClintData();
        final AllUsers users = new AllUsers();
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        String email = mAuth.getCurrentUser().getEmail();
        clintData.setUser_ID(user_id);
        users.setUser_ID(user_id);
        clintData.setUser_Email(email);
        clintData.setUser_Name(this.name.getText().toString().trim());


        callCenterReference = FirebaseDatabase.getInstance().getReference().child(Constant.Extra.DATA_CLINT);

            callCenterReference.child(user_id).setValue(clintData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
    }

    public void saveData() {
        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        storeUser = FirebaseDatabase.getInstance().getReference().child(Constant.Extra.CHILD_USERS).child(user_id);
        storeUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(Constant.ExtraBranch.USER_NAME).getValue().toString();
                String statuse = dataSnapshot.child(Constant.ExtraBranch.USER_STATUSE).getValue().toString();
                String image = dataSnapshot.child(Constant.ExtraBranch.USER_IMAGE).getValue().toString();
                String id = dataSnapshot.child("user_ID").getValue().toString();
                AllUsers users = new AllUsers();
                users.setUser_name(name);
                users.setUser_status(statuse);
                users.setUser_image(image);
                users.setUser_ID(id);
                updateData(users);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void updateData(AllUsers users) {
        String key= FirebaseDatabase.getInstance().getReference().child("ShowAllUser").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("ShowAllUser")
                .child(key)
                .setValue(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                        }else {
                        }
                    }
                });
    }
}