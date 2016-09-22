package com.example.harsh.androlearner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.klinker.android.sliding.SlidingActivity;

/**
 * Created by harsh on 22/09/16.
 */

public class UserActivity extends SlidingActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("user_name");
        String user_id = intent.getStringExtra("user_id");
        String url = "http://graph.facebook.com/"+user_id+"/picture?type=large";
        setTitle(user_name);
        setPrimaryColors(
                getResources().getColor(R.color.grey_700),
                getResources().getColor(R.color.grey_900)
        );
        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        setImage(resource);
                    }
                });
        setContent(R.layout.user_view);
    }
}
