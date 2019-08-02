package com.android.priyanka.pushnotificationandroid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewholder>{
    Context context;
    List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.users_list, viewGroup, false);
        return new UserViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewholder userViewholder, int i) {
             User user = userList.get(i);
             userViewholder.textEmail.setText(user.email);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewholder extends RecyclerView.ViewHolder{
        TextView textEmail;

        public UserViewholder(@NonNull View itemView) {
            super(itemView);

            textEmail = itemView.findViewById(R.id.textEmail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = userList.get(getAdapterPosition());
                    Intent intent = new Intent(context,SendNotificationActivity.class);
                    intent.putExtra("user",user);
                    context.startActivity(intent);
                }
            });
        }
    }
}
