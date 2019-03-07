package com.example.ahmed.chat.fragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.adapter.AllUsersAdapter;
import com.example.ahmed.chat.model.AllUsers;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CHATS extends Fragment {
    private RecyclerView recyclerChat;
    private AllUsersAdapter usersAdapter;
    private List<AllUsers> mUsers;
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

//        recyclerChat = view.findViewById(R.id.recyclerChat);
//        recyclerChat.setHasFixedSize(true);
//        recyclerChat.setLayoutManager(new LinearLayoutManager(getContext()));
//        fUser = FirebaseAuth.getInstance().getCurrentUser();
//        usersList = new ArrayList<>();
//        reference = FirebaseDatabase.getInstance().getReference("Message");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                usersList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    ChatMessage chat = snapshot.getValue(ChatMessage.class);
//                    if (chat.getSender().equals(fUser.getUid())){
//                        usersList.add(chat.getReceiver());
//                    }
//                    if (chat.getReceiver().equals(fUser.getUid())){
//                       usersList.add(chat.getSender());
//                    }
//                }
//                readChats();
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
        return view;
    }

    private void readChats() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("ShowAllUser");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AllUsers users = snapshot.getValue(AllUsers.class);
                    for (String id : usersList){
                        if (users.getUser_ID().equals(id)){
                            if (mUsers.size() !=0 ){
                                for (AllUsers users1 : mUsers){
                                    if (!users.getUser_ID().equals(users1.getUser_ID())){
                                        mUsers.add(users);
                                    }
                                }
                            }else{
                                mUsers.add(users);
                            }
                        }
                    }
                }
                usersAdapter = new AllUsersAdapter(getContext(), mUsers);
                recyclerChat.setAdapter(usersAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}