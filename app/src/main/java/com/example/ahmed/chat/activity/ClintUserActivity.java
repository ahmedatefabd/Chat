package com.example.ahmed.chat.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ClintUserActivity extends AppCompatActivity {
    @BindView(R.id.Call_Center_btn)
    Button CallCenterBtn;
    @BindView(R.id.Call_Center_Signup_btn)
    Button CallCenterSignupBtn;
    @BindView(R.id.userid)
    TextView userid;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clint_user);

        ButterKnife.bind(this);
        ToolbarView();
        final String ID = Constant.SHARED.SHARED_ID;

        CallCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClintUserActivity.this, MessageActivity.class);
                intent.putExtra("id", ID);
                startActivity(intent);
            }
        });
        CallCenterSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ClintUserActivity.this, StartActivity.class));
                finish();
            }
        });
    }

    private void ToolbarView() {
        toolbar = findViewById(R.id.clint_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Move to Call Center");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
