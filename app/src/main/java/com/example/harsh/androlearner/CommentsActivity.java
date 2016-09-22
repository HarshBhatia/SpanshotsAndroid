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

public class CommentsActivity extends SlidingActivity {
    private RecyclerView mRecyclerView;
    private CommentsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Comment> commentsList = new ArrayList<>();
    ;

    public CommentsActivity() {
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setContent(R.layout.comments_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.comments_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommentsAdapter(commentsList);
        mRecyclerView.setAdapter(mAdapter);

        //Set SldingaActivity attributes
        setFab(getResources().getColor(R.color.pink_A200), R.drawable.ic_mode_edit_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommentsActivity.this, "FAB Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        setTitle("Comments");
        setPrimaryColors(
                getResources().getColor(R.color.blue_500),
                getResources().getColor(R.color.blue_700)
        );


        //Parse comments extra to JSONArray
        JSONArray comments = null;
        Intent intent = getIntent();
        String commentsString = intent.getStringExtra("comments");
        if (commentsString != null) {
            try {
                comments = new JSONArray(commentsString);


                Log.d("AmazeLogs", "From comments activity" + comments.toString());
            } catch (JSONException e) {
                Log.d("AmazeLogs", "Cannot parse comments array");
                e.printStackTrace();
            }
        } else {
            Log.d("AmazeLogs", "comments array empty");
        }
        for (int i = 0; i < comments.length(); i++) {
            JSONObject jsonobject = null;
            try {
                jsonobject = comments.getJSONObject(i);
                String text = jsonobject.getString("text");
                String from_id = jsonobject.getString("from_id");
                String from_name = jsonobject.getString("from_name");
                Comment c = new Comment(from_name, from_id, text);
                commentsList.add(c);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Comment c = new Comment("Harsh Bhatia", "123", "Nice");
        commentsList.add(c);
        Comment c2 = new Comment("Harsh Bhatia", "123", "Nice");
        commentsList.add(c);
        Comment c3 = new Comment("Harsh Bhatia", "123", "Nice");
        commentsList.add(c);
        Comment c4 = new Comment("Harsh Bhatia", "123", "Nice");
        commentsList.add(c);
        Comment c5 = new Comment("Harsh Bhatia", "123", "Nice");
        commentsList.add(c);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(0.4f);
    }

    public void addComment() {
        //
    }
}
