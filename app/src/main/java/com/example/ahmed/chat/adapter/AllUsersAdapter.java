package com.example.ahmed.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.model.AllUsers;
import com.example.ahmed.chat.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.AllUsersHolder> {
    private Context context;
    private List<AllUsers> allUsersList;
    private DatabaseReference getDatabaseReference;
    private FirebaseAuth mAuth;

    public AllUsersAdapter(Context context, List<AllUsers> allUsers) {
        this.context = context;
        this.allUsersList = allUsers;
    }
    @Override
    public AllUsersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.all_users_display_layout, parent, false);
        AllUsersHolder holder = new AllUsersHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AllUsersHolder holder, int position) {
        final AllUsers users = allUsersList.get(position);
        mAuth = FirebaseAuth.getInstance();
        String online_user_id = mAuth.getCurrentUser().getUid();
        getDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.Extra.CHILD_USERS).child(online_user_id);
        getDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child(Constant.ExtraBranch.USER_NAME).getValue().toString();
                String statuse = dataSnapshot.child(Constant.ExtraBranch.USER_STATUSE).getValue().toString();
                String image = dataSnapshot.child(Constant.ExtraBranch.USER_IMAGE).getValue().toString();
                users.setUser_name(name);
                users.setUser_status(statuse);
                users.setUser_image(image);
                holder.usernameAllUserTV.setText(users.getUser_name());
                holder.userStatusAllUserTV.setText(users.getUser_status());
                Picasso.get().load(users.getUser_image()).placeholder(R.drawable.user).into(holder.imageAllUser);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return allUsersList.size();
    }

    public class AllUsersHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageAllUser)
        CircleImageView imageAllUser;
        @BindView(R.id.username_AllUser_TV)
        TextView usernameAllUserTV;
        @BindView(R.id.userStatus_AllUser_TV)
        TextView userStatusAllUserTV;

        public AllUsersHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}