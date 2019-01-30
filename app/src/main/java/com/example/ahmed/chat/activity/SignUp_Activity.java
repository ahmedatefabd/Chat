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
import com.example.ahmed.chat.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private DatabaseReference storeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);

        ButterKnife.bind(SignUp_Activity.this);
        toolbar = findViewById(R.id.sign_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SignUp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                                    storeUser = FirebaseDatabase.getInstance().getReference().child(Constant.Extra.CHILD_USERS).child(current_user_id);
                                    storeUser.child(Constant.ExtraBranch.USER_NAME).setValue(Name);
                                    storeUser.child(Constant.ExtraBranch.USER_STATUSE).setValue("i am android developer");
                                    storeUser.child(Constant.ExtraBranch.USER_IMAGE).setValue("default_profile");
                                    storeUser.child(Constant.ExtraBranch.USER_THUMB_IMAGE).setValue("default_profile")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(SignUp_Activity.this, "Success Save Account in Database", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SignUp_Activity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
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
}