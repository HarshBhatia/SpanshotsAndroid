package com.example.harsh.androlearner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mUrls;
    public ImageAdapter(Context c, String[] urls) {
        mUrls = urls;
        mContext = c;
    }

    public int getCount() {
        return mUrls.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(mUrls[position]).into(imageView);

//        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.doge, R.drawable.doge2, R.drawable.doge,
            R.drawable.doge2, R.drawable.doge, R.drawable.doge2,
            R.drawable.doge2, R.drawable.doge, R.drawable.doge2,
            R.drawable.doge2, R.drawable.doge, R.drawable.doge2,
            R.drawable.doge2, R.drawable.doge, R.drawable.doge2,
            R.drawable.doge2, R.drawable.doge, R.drawable.doge2,
    };
}