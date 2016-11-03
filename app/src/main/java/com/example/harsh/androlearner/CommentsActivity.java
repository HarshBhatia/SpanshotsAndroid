package com.example.harsh.androlearner;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CommentsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Comment> commentsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.comments_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.comments_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommentsAdapter(this,commentsList);
        mRecyclerView.setAdapter(mAdapter);


        //Parse comments extra to JSONArray
        JSONArray comments = null;
        Intent intent = getIntent();
        String commentsString = intent.getStringExtra("comments");
        final String post_hash  = intent.getStringExtra("post_hash");

        if (commentsString != null) {
            try {
                comments = new JSONArray(commentsString);


                Log.d("AmazeLogs", "From comments activity" + comments.toString());
            } catch (JSONException e) {
                Log.d("AmazeLogs", "Cannot parse comments array");
                e.printStackTrace();
            }
        }

        FloatingActionButton submit_fab = (FloatingActionButton) findViewById(R.id.submit_comment);
        submit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AmazeLogs","FAB Clicked!");

                putComment(v,post_hash);
            }
        });

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
        mAdapter.notifyDataSetChanged();
    }

    public void putComment(View view, String post_hash) {
        ViewGroup row = (ViewGroup) view.getParent();
        EditText textBox = (EditText) row.findViewById(R.id.new_comment_text);
        String comment_text = textBox.getText().toString();
        Comment c = new Comment("Harsh Bhatia", "123", comment_text);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://spanshots.com/put_comment";
        RequestParams params = new RequestParams();
        params.add("hash",post_hash);
        params.add("user_id","1203922519636129");
        params.add("user_name","Harsh Bhatia");
        params.add("text",comment_text);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("AmazeLogs", "Comment Failed!"+responseString+statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {

                Log.d("AmazeLogs", "Comment Success!");
            }
        });
        commentsList.add(c);
        mAdapter.notifyDataSetChanged();
        textBox.setText("");
    }
}
