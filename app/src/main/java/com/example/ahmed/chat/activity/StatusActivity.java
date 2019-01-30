package com.example.ahmed.chat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmed.chat.R;
import com.example.ahmed.chat.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusActivity extends AppCompatActivity {
    @BindView(R.id.statuseChange_ET)
    EditText statuseChange_ET;
    @BindView(R.id.SaveChange)
    Button SaveChange_btn;
    private Toolbar toolbar;
    private DatabaseReference changeStatusRes;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.StatusActivity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadingBar = new ProgressDialog(this);

        String old_status = getIntent().getExtras().get(Constant.ExtraBranch.USER_STATUSE).toString();
        statuseChange_ET.setText(old_status);

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        changeStatusRes = FirebaseDatabase.getInstance().getReference().child(Constant.Extra.CHILD_USERS).child(user_id);

        SaveChange_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_statuse = statuseChange_ET.getText().toString().trim();
                changeprofileStatus(new_statuse);
            }
        });
    }

    private void changeprofileStatus(String new_statuse) {
        if (TextUtils.isEmpty(new_statuse))
            Toast.makeText(this, "please write your status.", Toast.LENGTH_SHORT).show();
        else {
            loadingBar.setTitle("Change profile Statuse");
            loadingBar.setMessage("please wait, while we are updating your profile status");
            loadingBar.show();
            changeStatusRes.child(Constant.ExtraBranch.USER_STATUSE).setValue(new_statuse)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadingBar.dismiss();
                                startActivity(new Intent(StatusActivity.this, AccountSettingsActivity.class));
                                Toast.makeText(StatusActivity.this, "profile status updated Successfuly...", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(StatusActivity.this, "Error Occurred...", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
