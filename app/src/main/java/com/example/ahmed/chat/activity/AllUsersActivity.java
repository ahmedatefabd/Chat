package com.example.ahmed.chat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.adapter.AllUsersAdapter;
import com.example.ahmed.chat.model.AllUsers;
import com.example.ahmed.chat.utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllUsersActivity extends AppCompatActivity {
    @BindView(R.id.recyclerAllUser)
    RecyclerView allUserList;
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
        ReturnData();
    }
    private void ReturnData() {
        FirebaseDatabase.getInstance().getReference().child(Constant.Extra.CHILD_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<AllUsers> allUsersArrayList = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            AllUsers allUsers = child.getValue(AllUsers.class);
                            allUsersArrayList.add(allUsers);
                        }
                        recyclerView(allUsersArrayList);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AllUsersActivity.this, databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void recyclerView(List<AllUsers> allUsersArrayList) {
       AllUsersAdapter adapter = new AllUsersAdapter(AllUsersActivity.this , allUsersArrayList );
        allUserList.setLayoutManager(new LinearLayoutManager(this));
        allUserList.setAdapter(adapter);
    }
}