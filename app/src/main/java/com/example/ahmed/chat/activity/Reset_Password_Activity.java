package com.example.ahmed.chat.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ahmed.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Reset_Password_Activity extends AppCompatActivity {

    @BindView(R.id.email_ET)
    EditText emailET;
    @BindView(R.id.reset_btn)
    Button resetBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password_);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.reset_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ResetPassword");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String email = emailET.getText().toString().trim();
//________________________________________________________________________________________________________________________________________________________

                FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Reset_Password_Activity.this, "Check Your Email!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Reset_Password_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }
}
