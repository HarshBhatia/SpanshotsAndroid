package com.example.harsh.androlearner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by harsh on 20/09/16.
 */

public class LikesActivity extends SlidingActivity {
    private RecyclerView mRecyclerView;
    private LikesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Like> likesList = new ArrayList<>();
    ;

    public LikesActivity() {
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setContent(R.layout.likes_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.likes_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LikesAdapter(likesList);
        mRecyclerView.setAdapter(mAdapter);

        //Set SldingaActivity attributes
        setFab(getResources().getColor(R.color.pink_A200), R.drawable.ic_mode_edit_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LikesActivity.this, "FAB Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        setTitle("Likes");
        setPrimaryColors(
                getResources().getColor(R.color.blue_500),
                getResources().getColor(R.color.blue_700)
        );


        //Parse likes extra to JSONArray
        JSONArray likes = null;
        Intent intent = getIntent();
        String likesString = intent.getStringExtra("likes");
        if (likesString != null) {
            try {
                likes = new JSONArray(likesString);


                Log.d("AmazeLogs", "From likes activity" + likes.toString());
            } catch (JSONException e) {
                Log.d("AmazeLogs", "Cannot parse likes array");
                e.printStackTrace();
            }
        } else {
            Log.d("AmazeLogs", "likes array empty");
        }
        for (int i = 0; i < likes.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = likes.getJSONObject(i);
                String from_id = jsonobject.getString("from_id");
                String from_name = jsonobject.getString("from_name");
                Like c = new Like(from_name, from_id);
                likesList.add(c);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Like c = new Like("Harsh Bhatia", "123");
        likesList.add(c);
        Like c2 = new Like("Harsh Bhatia", "123");
        likesList.add(c);
        Like c3 = new Like("Harsh Bhatia", "123");
        likesList.add(c);
        Like c4 = new Like("Harsh Bhatia", "123");
        likesList.add(c);
        Like c5 = new Like("Harsh Bhatia", "123");
        likesList.add(c);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(0.4f);
    }

    public void addLike() {
        //
    }
}
