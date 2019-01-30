package com.example.ahmed.chat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.ahmed.chat.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllUsersActivity extends AppCompatActivity {
    @BindView(R.id.recyclerAllUser)
    RecyclerView recyclerAllUser;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.AllUsersActivity_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AllUsers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
