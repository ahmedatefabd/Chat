package com.example.ahmed.chat.activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.adapter.AllUsersAdapter;
import com.example.ahmed.chat.model.AllUsers;
import com.example.ahmed.chat.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private AllUsersAdapter adapter;
    private DatabaseReference storeUser;
    private FirebaseAuth mAuth;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        ButterKnife.bind(this);
        ToolbarView();
        ReturnData();

    }

    private void ToolbarView() {
        toolbar = findViewById(R.id.AllUsersActivity_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AllUsersFragment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ReturnData() {
        FirebaseDatabase.getInstance().getReference().child("ShowAllUser")
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
                    }
                });
    }
    private void recyclerView(List<AllUsers> allUsersArrayList) {
        adapter = new AllUsersAdapter(AllUsersActivity.this , allUsersArrayList );
        allUserList.setLayoutManager(new LinearLayoutManager(this));
        allUserList.setAdapter(adapter);
    }
}