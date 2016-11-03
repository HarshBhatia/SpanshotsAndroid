package com.example.harsh.androlearner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by harsh on 21/09/16.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private List<Comment> commentList;
    private Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView comment_text, comment_user_name;
        public CircleImageView comment_user_image;
        public LinearLayout comment;

        public ViewHolder(View view) {
            super(view);
            comment = (LinearLayout) view.findViewById(R.id.comment);
            comment_text = (TextView) view.findViewById(R.id.comment_text);
            comment_user_name = (TextView) view.findViewById(R.id.comment_user_name);
            comment_user_image = (CircleImageView) view.findViewById(R.id.comment_user_image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentsAdapter(Context context, List<Comment> myDataset) {
        this.context = context;
        commentList = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Comment c = commentList.get(position);
        holder.comment_user_name.setText(c.get_comment_user_name());
        holder.comment_text.setText(c.get_comment_text());

        String profile_picture_uri = "https://graph.facebook.com/" + c.get_comment_user_id() + "/picture?width=100&height=100";
        Log.d("AmazeLogs", profile_picture_uri);
        Picasso.with(context).load(profile_picture_uri).into(holder.comment_user_image);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
