package com.example.harsh.androlearner;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harsh on 15/09/16.
 */

public class SpanshotsAdapter extends RecyclerView.Adapter<SpanshotsAdapter.MyViewHolder> {
    private List<Spanshot> spanshotList;
    private Context context;

    private class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Footer = 3;
    }

    @Override
    public int getItemViewType(int position) {

        if (spanshotList.get(position).isHeader())
            return VIEW_TYPES.Header;
        else if (spanshotList.get(position).isFooter())
            return VIEW_TYPES.Footer;
        else
            return VIEW_TYPES.Normal;

    }

    public SpanshotsAdapter(Context context, List<Spanshot> spanshotList) {
        this.context = context;
        this.spanshotList = spanshotList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView caption, username, likes_count, comments_count;
        public ImageView like_button,comment_button;
        public VideoView files_mp4;
        public ImageView files_jpg;
        public com.robinhood.ticker.TickerView views;
        public int viewType;

        public MyViewHolder(View view, int viewType) {
            super(view);
            if (viewType == VIEW_TYPES.Normal) {
                caption = (TextView) view.findViewById(R.id.caption);
                username = (TextView) view.findViewById(R.id.username);
                comments_count = (TextView) view.findViewById(R.id.comments_count);
                likes_count = (TextView) view.findViewById(R.id.likes_count);
                like_button = (ImageView) view.findViewById(R.id.like_button);
                comment_button = (ImageView) view.findViewById(R.id.comment_button);
                files_jpg = (ImageView) view.findViewById(R.id.files_jpg);
                files_mp4 = (VideoView) view.findViewById(R.id.files_mp4);
                views = (com.robinhood.ticker.TickerView) view.findViewById(R.id.views);

                views.setCharacterList(TickerUtils.getDefaultNumberList());
            } else {
            }

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        switch (viewType) {
            case VIEW_TYPES.Normal:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.spanshot_layout, parent, false);
                break;
            case VIEW_TYPES.Header:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.header, parent, false);
                break;
            case VIEW_TYPES.Footer:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.footer, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.spanshot_layout, parent, false);
                break;
        }

        return new MyViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            Log.d("Amaze Logs", "Is header");
        } else if (getItemViewType(position) == 3) {
            Log.d("Amaze Logs", "Is footer");
        } else {

            Spanshot s = spanshotList.get(position);
            holder.caption.setText(s.get_caption());
            holder.like_button.setImageResource(R.drawable.ic_favorite_border_pink_a400_24dp);
            holder.views.setText(s.get_views());

            SpanshotTag spanshot_tag = new SpanshotTag(s.get_ph(), s.get_is_liked(), s.get_files_mp4(), s.get_views(), s.get_user_name(), s.get_user_id(), s.get_comments(), s.get_likes());

            holder.like_button.setTag(spanshot_tag);
            holder.files_mp4.setTag(spanshot_tag);
            holder.files_jpg.setTag(spanshot_tag);
            holder.comment_button.setTag(spanshot_tag);
            holder.likes_count.setTag(spanshot_tag);
            holder.comments_count.setTag(spanshot_tag);
            holder.username.setTag(spanshot_tag);
            holder.comments_count.setText(s.get_comments().length()+" comments");

            int videoHeight = 360, videoWidth = 240;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            Bitmap bmp = null;

            try {
                retriever.setDataSource(s.get_files_mp4());
                bmp = retriever.getFrameAtTime();
                videoHeight = bmp.getHeight();
                videoWidth = bmp.getWidth();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

            Picasso.with(context).load(s.get_files_jpg()).placeholder(R.drawable.doge).fit().centerCrop().into(holder.files_jpg);
            holder.username.setText(s.get_user_name());
        }

    }

    @Override
    public int getItemCount() {
        return spanshotList.size();
    }
}
