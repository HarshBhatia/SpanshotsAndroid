package com.example.harsh.androlearner;

/**
 * Created by harsh on 21/09/16.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FeedFragment extends Fragment {
    private final List<Spanshot> spanshotList = new ArrayList<>();
    private SpanshotsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean feed_initialized = false;
    private String LT_TIME, PROTO, NOP, REQUEST_USER_ID, PROFILE_USER_ID;
    private LinearLayoutManager linearLayoutManager;

    public static FeedFragment newInstance(String proto, String request_user_id, String profile_user_id) {
        FeedFragment f = new FeedFragment();
        if (request_user_id == null) {
            Log.d("AmazeLogs", "Empty access token, generating fresh feed.");
        }
        Bundle args = new Bundle();
        args.putString("proto", proto);
        args.putString("request_user_id", request_user_id);
        args.putString("profile_user_id", profile_user_id);
        f.setArguments(args);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("AmazeLogs", this.getClass().getSimpleName() + " view created");
        return inflater.inflate(R.layout.feed_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("AmazeLogs", this.getClass().getSimpleName() + " activity created");
        super.onActivityCreated(savedInstanceState);

        //Inits
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.activity_main_swipe_refresh_layout);
        mAdapter = new SpanshotsAdapter(getActivity(), spanshotList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        mHeaderView = getActivity().findViewById(R.id.loading);
        ObservableRecyclerView recyclerView = (ObservableRecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        LT_TIME = Long.toString(System.currentTimeMillis());
        Log.d("AmazeLogs","Init LT TIME");
        
        PROTO = getArguments().getString("proto");
        REQUEST_USER_ID = getArguments().getString("request_user_id");
        PROFILE_USER_ID = getArguments().getString("profile_user_id");
        NOP = "15";

        //Lazy Load
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // Add the scroll listener
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("AmazeLogsLM",LT_TIME);
                long temp = Long.parseLong(LT_TIME);
                temp = temp +1;
                Log.d("AmazeLogsLM",Long.toString(temp));
                
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("AmazeLogs", "Loading more data...");
                prepareSpanshots(PROTO, Long.toString(temp), NOP, REQUEST_USER_ID, PROFILE_USER_ID);
            }
        });

        // Swipe to refresh
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange_500, R.color.green_500, R.color.blue_500);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("AmazeLogs", "Refershed!");
                        Toast.makeText(getActivity().getApplicationContext(), "Refresh Complete", Toast.LENGTH_LONG).show();
                        spanshotList.clear();
                        prepareSpanshots(PROTO, Long.toString(System.currentTimeMillis()), NOP, REQUEST_USER_ID, PROFILE_USER_ID);
                        linearLayoutManager.scrollToPositionWithOffset(0, 0);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });


        //onCreate Calls
        if (!feed_initialized) {
            prepareSpanshots(PROTO, LT_TIME, NOP, REQUEST_USER_ID, PROFILE_USER_ID);
            long temp = (Long.parseLong(LT_TIME)+100);
            temp = temp + 100;
            LT_TIME = Long.toString(temp);
            feed_initialized = true;
        }


    }

    private void prepareSpanshots(String proto, String lt_time, String nop, String request_user_id, String profile_user_id) {
        Log.d("AmazeLogs", "prepareSpanshotsCalled");
        //AJAX Client
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://spanshots.com/get_posts";
        RequestParams params = new RequestParams();
        params.put("proto", proto);
        params.put("lt_time", lt_time);
        params.put("user_id", request_user_id);
        params.put("userId", profile_user_id);
        params.put("nop", nop);

        //Start POST Request
        client.post(url, params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
                        Log.d("AmazeLogs","New results");

                        mAdapter.notifyDataSetChanged();
                        JSONArray jsonarray = null;
                        try {
                            jsonarray = new JSONArray(res);
                            Log.d("AmazeLogs", jsonarray.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = null;
                            try {
                                jsonobject = jsonarray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String caption = null;
                            String user_name = null;
                            String user_id = null;
                            String files_jpg = null;
                            String files_vid = null;
                            String files_mp4 = null;
                            String ph = null;
                            String views = null;
                            String upload_time;
                            JSONArray comments = null;
                            JSONArray likes = null;
                            Boolean is_liked = false;

                            try {
                                caption = jsonobject.getString("caption");
                                user_name = jsonobject.getString("user_name");
                                user_id = jsonobject.getString("user_id");
                                ph = jsonobject.getString("post_hash");
                                JSONObject files = jsonobject.getJSONObject("files");
                                comments = jsonobject.getJSONArray("comments");
                                likes = jsonobject.getJSONArray("likes");
                                files_jpg = files.get("jpg").toString();
                                files_mp4 = files.get("mp4").toString();
                                files_vid = files.get("vid").toString();
                                views = jsonobject.getString("views");
                                Log.d("AmazeLogs", "From Fragment: The view count of" + caption + " is " + views);

                                upload_time = jsonobject.getString("upload_time");
                                Log.d("AmazeLogs", likes.toString());
                                if (likes.toString().contains("\"from_id\":\"" + REQUEST_USER_ID + "\"")) {
                                    is_liked = true;
                                }
                                if (i == (jsonarray.length() - 1)) {
                                    Log.d("AmazeLogsU","Updating lt time!");

                                    LT_TIME = upload_time;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Spanshot s = new Spanshot(caption, user_name, "http://spanshots.com/static/files_uploaded/" + files_jpg, "http://spanshots.com/static/files_uploaded/" + files_mp4, "http://spanshots.com/static/files_uploaded/" + files_vid, ph, is_liked, views, false, false, user_id, comments, likes);
                            spanshotList.add(s);
                            mAdapter.notifyDataSetChanged();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.d("AmazeLogs", "No connection");
                        Spanshot offline = new Spanshot("null", "null", "null", "null", "null", "null", false, "null", false, true, "null", null, null);
                        spanshotList.add(offline);
                        mAdapter.notifyDataSetChanged();
                    }
                }
        );
    }


    //Check if connected
    boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}