package com.example.ahmed.chat.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ahmed.chat.R;
import com.example.ahmed.chat.activity.MessageActivity;
import com.example.ahmed.chat.model.AllUsers;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.AllUsersHolder> {
    private Context mcontext;
    private List<AllUsers> allUsersList;

    public AllUsersAdapter(Context context, List<AllUsers> allUsers) {
        this.mcontext = context;
        this.allUsersList = allUsers;
    }

    @Override
    public AllUsersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mcontext).inflate(R.layout.all_users_display_layout, parent, false);
        AllUsersHolder holder = new AllUsersHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AllUsersHolder holder, int position) {
        final AllUsers users = allUsersList.get(position);
        holder.usernameAllUserTV.setText(users.getUser_name());
//        holder.userStatusAllUserTV.setText(users.getUser_status());
        Picasso.get().load(users.getUser_image()).placeholder(R.drawable.user).into(holder.imageAllUser);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("users", users);
                mcontext.startActivity(intent);
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
//        @BindView(R.id.userStatus_AllUser_TV)
//        TextView userStatusAllUserTV;

        public AllUsersHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}