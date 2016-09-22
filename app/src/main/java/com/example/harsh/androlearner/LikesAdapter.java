package com.example.harsh.androlearner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by harsh on 21/09/16.
 */

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHolder> {
    private List<Like> likeList;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView like_user_name;
        public de.hdodenhof.circleimageview.CircleImageView like_user_image;

        public ViewHolder(View view) {
            super(view);
            like_user_name = (TextView) view.findViewById(R.id.like_user_name);
            like_user_image = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.like_user_image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LikesAdapter(List<Like> myDataset) {
        likeList = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LikesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Like c = likeList.get(position);
        holder.like_user_name.setText(c.get_like_user_name());

        String profile_picture_uri = "http://graph.facebook.com/"+c.get_like_user_id()+ "/picture?type=square";
        Log.d("AmazeLogs",profile_picture_uri);
        Picasso.with(context).load(profile_picture_uri).placeholder(R.drawable.doge).fit().centerCrop().into(holder.like_user_image);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return likeList.size();
    }
}
