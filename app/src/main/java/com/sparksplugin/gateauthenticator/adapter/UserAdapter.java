package com.sparksplugin.gateauthenticator.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.UsersActivity;
import com.sparksplugin.gateauthenticator.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private String type;


    public UserAdapter(Context mContext, List<User> mUsers,String type){
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.user_title, parent, false);
        return new UserAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        String cla = user.getSem()+" "+user.getBranch()+" "+user.getBatch();
        holder.userClass.setText(cla);
        holder.userId.setText(user.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UsersActivity.class);
                intent.putExtra("userid", user.getUID());
                intent.putExtra("name", user.getUsername());
                intent.putExtra("val", type);
               mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView userId;
        public TextView userClass;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            userId = itemView.findViewById(R.id.user_id);
            userClass = itemView.findViewById(R.id.user_class);


        }
    }
}