package com.example.ahmed.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp_Activity extends AppCompatActivity {

    @BindView(R.id.email_ET)
    EditText emailET;
    @BindView(R.id.password_ET)
    EditText passwordET;
    @BindView(R.id.sign_up_btn)
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);
        ButterKnife.bind(SignUp_Activity.this);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString();
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password) // method is used for Write Email And Password ---> for Save (Email And Password) in Database(Firebase).
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() { // method is used for check SingnUp (Success or fail).
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) { // (lw 3malet el save naga7t) aza 7asl Save LL Email & Passward fe Database(Firebase)--> b4akl sa7eh
                                    // w 5las tam ta5zenhom 5laaas ro7 LL MainActivity
                                    Toast.makeText(SignUp_Activity.this, "Success Save Account in Database", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(SignUp_Activity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUp_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
    }
}
