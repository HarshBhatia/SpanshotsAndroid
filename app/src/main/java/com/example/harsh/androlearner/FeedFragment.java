package com.example.harsh.androlearner;

/**
 * Created by harsh on 20/09/16.
 */

import android.content.Context;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FeedFragment extends Fragment implements ObservableScrollViewCallbacks {
    private com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView recyclerView;
    private List<Spanshot> spanshotList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private SpanshotsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean feed_initalized = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Inflate the layout for this fragment
        // View elements declarations
        ObservableRecyclerView recyclerView = (ObservableRecyclerView) getView().findViewById(R.id.recycler_view);

        recyclerView.setScrollViewCallbacks(this);
        if (!isNetworkAvailable()) {
            return;
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.activity_main_swipe_refresh_layout);
        recyclerView = (com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView) getView().findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        mAdapter = new SpanshotsAdapter(getActivity().getApplicationContext(), spanshotList);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // Swipe to refresh
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange_500, R.color.green_500, R.color.blue_500);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(getView(), "Spanhshots Refresh Complete!", Snackbar.LENGTH_SHORT);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });


        //onCreate Calls
        if (!feed_initalized) {
            long time = System.currentTimeMillis();
            prepareSpanshots("fresh", Long.toString(time), "15");
            feed_initalized = true;
        }

    }

    public void prepareSpanshots(String proto, String lt_time, String nop) {
        //Sample Spanshot
//        Spanshot s = new Spanshot("Hello World", "John Wow", "http://slodive.com/wp-content/uploads/2012/10/funny-dog-pictures/stylish-funny-dog.jpg", "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", "posthash", false, "123", true, false, "12345", );
//        Spanshot s2 = new Spanshot("Hello World", "John Wow", "http://slodive.com/wp-content/uploads/2012/10/funny-dog-pictures/stylish-funny-dog.jpg", "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", "posthash", false, "123", false, true);
//        spanshotList.add(s);
//        spanshotList.add(s2);
//        spanshotList.remove(s);
//        mAdapter.notifyDataSetChanged();

        //AJAX Client
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://spanshots.com/get_posts";
        RequestParams params = new RequestParams();
        params.put("proto", proto);
        params.put("lt_time", lt_time);
        params.put("nop", nop);

        //Start POST Request
        client.post(url, params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String res) {
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
                            String files_mp4 = null;
                            String ph = null;
                            String views = null;
                            JSONArray comments = null;
                            JSONArray likes = null;

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
                                views = jsonobject.getString("views");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Spanshot s = new Spanshot(caption, user_name, "http://spanshots.com/static/files_uploaded/" + files_jpg, "http://spanshots.com/static/files_uploaded/" + files_mp4, ph, false, views, false, false,user_id,comments,likes);
                            spanshotList.add(s);
                            mAdapter.notifyDataSetChanged();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                    }
                }
        );


    }

//    public class LinearLayoutManagerWithSmoothScroller extends LinearLayoutManager {
//        public LinearLayoutManagerWithSmoothScroller(Context context) {
//            super(context, VERTICAL, false);
//        }
//
//        public LinearLayoutManagerWithSmoothScroller(Context context, int orientation, boolean reverseLayout) {
//            super(context, orientation, reverseLayout);
//        }
//
//        @Override
//        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
//                                           int position) {
//            RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
//            smoothScroller.setTargetPosition(position);
//            startSmoothScroll(smoothScroller);
//        }
//
//        private class TopSnappedSmoothScroller extends LinearSmoothScroller {
//            public TopSnappedSmoothScroller(Context context) {
//                super(context);
//
//            }
//
//            @Override
//            public PointF computeScrollVectorForPosition(int targetPosition) {
//                return LinearLayoutManagerWithSmoothScroller.this
//                        .computeScrollVectorForPosition(targetPosition);
//            }
//
//            @Override
//            protected int getVerticalSnapPreference() {
//                return SNAP_TO_START;
//            }
//        }
//    }

    //Check if connected
    boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}