package com.example.harsh.androlearner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harsh on 17/10/16.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private List<Spanshot> spanshotList;
    private Context context;

    public ProfileAdapter(Context context, List<Spanshot> spanshotList) {
        this.context = context;
        this.spanshotList = spanshotList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView files_jpg;

        public MyViewHolder(View view) {
            super(view);
            files_jpg = (ImageView) view.findViewById(R.id.files_jpg);

        }
    }


    @Override
    public ProfileAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spanshot_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.MyViewHolder holder, int position) {
        Spanshot s = spanshotList.get(position);
        Picasso.with(context).load(s.get_files_jpg()).resize(0, 240).into(holder.files_jpg);

    }

    @Override
    public int getItemCount() {
        return spanshotList.size();
    }
}

