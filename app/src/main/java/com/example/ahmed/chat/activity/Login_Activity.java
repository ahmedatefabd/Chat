package com.example.ahmed.chat.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ahmed.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Login_Activity extends AppCompatActivity {
    @BindView(R.id.email_ET)
    EditText emailET;
    @BindView(R.id.password_ET)
    EditText passwordET;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.sign_up_btn)
    Button signUpBtn;
    @BindView(R.id.forget_pass_TV)
    TextView forgetPassTV;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       if (FirebaseAuth.getInstance().getCurrentUser() != null) {
           Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
           startActivity(intent);
           finish();
       }
        setContentView(R.layout.activity_login_);

        ButterKnife.bind(Login_Activity.this);
        toolbar = findViewById(R.id.Login_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, SignUp_Activity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailET.getText().toString().trim();
                String passward = passwordET.getText().toString().trim();
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email,passward)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login_Activity.this, "Success Login Account", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                    if (email.equals("atef0755@gmail.com")){
                                        Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Intent intent = new Intent(Login_Activity.this, ClintUserActivity.class);
                                        intent.putExtra("userID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                else {
                                    Toast.makeText(Login_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        forgetPassTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Activity.this, Reset_Password_Activity.class));
            }
        });
    }
}
