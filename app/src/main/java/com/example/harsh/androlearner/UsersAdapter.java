package com.example.harsh.androlearner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harsh on 26/10/16.
 * Explore view user adapter.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private final List<User> userList;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView user_user_name;
        public final de.hdodenhof.circleimageview.CircleImageView user_user_image;

        public ViewHolder(View view) {
            super(view);
            user_user_name = (TextView) view.findViewById(R.id.user_user_name);
            user_user_image = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.user_user_image);
        }
    }

    public UsersAdapter(Context context, List<User> myDataset) {

        this.context = context;
        userList = myDataset;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, int position) {
        final User c = userList.get(position);
        holder.user_user_name.setText(c.getUser_name());


        String profile_picture_uri = "https://graph.facebook.com/"+c.getUser_id()+ "/picture?width=100&height=100";
        Log.d("AmazeLogs",profile_picture_uri);
        Picasso.with(context).load(profile_picture_uri).placeholder(R.drawable.doge).fit().centerCrop().into(holder.user_user_image);

        //Listeners
        holder.user_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_name", c.getUser_name());
                intent.putExtra("user_id",c.getUser_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
