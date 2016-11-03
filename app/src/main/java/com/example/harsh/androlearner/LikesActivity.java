package com.example.harsh.androlearner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class LikesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LikesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Like> likesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.likes_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.likes_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LikesAdapter(getApplicationContext(),likesList);
        mRecyclerView.setAdapter(mAdapter);

        //Parse likes extra to JSONArray
        JSONArray likes = null;
        Intent intent = getIntent();
        String likesString = intent.getStringExtra("likes");
        if (likesString != null) {
            try {
                likes = new JSONArray(likesString);
            } catch (JSONException e) {
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
        mAdapter.notifyDataSetChanged();
    }
}
