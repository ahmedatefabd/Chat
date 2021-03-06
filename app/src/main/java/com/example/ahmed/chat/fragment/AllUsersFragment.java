package com.example.ahmed.chat.fragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.adapter.AllUsersAdapter;
import com.example.ahmed.chat.model.AllUsers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AllUsersFragment extends Fragment {
    RecyclerView recycler;
    private AllUsersAdapter adapter;
    private List<AllUsers> allUsersArrayList;

    public AllUsersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        recycler = view.findViewById(R.id.recyclerUser);
        ReturnData();
        return view;
    }

    private void ReturnData() {
        FirebaseDatabase.getInstance().getReference().child("ShowAllUser")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        allUsersArrayList = new ArrayList<>();
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
        adapter = new AllUsersAdapter(getContext(), allUsersArrayList);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);
    }
}