package com.example.harsh.androlearner;

/**
 * Created by harsh on 20/09/16.
 * Explore section
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ExploreFragment extends Fragment {
//    private List<String> lastSearches;

    private RecyclerView mRecyclerView;
    private UsersAdapter mAdapter;
    private List<User> usersList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("AmazeLogs", this.getClass().getSimpleName() + " view created");

        // Inflate the layout for this fragment

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.explore_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.search_recycler_view);
        mAdapter = new UsersAdapter(getContext(),usersList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        MaterialSearchBar searchBar = (MaterialSearchBar) getActivity().findViewById(R.id.searchBar);

        searchBar.setHint("Search");
        TextWatcher searchWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                updateSearch(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        EditText searchEdit = (EditText) searchBar.findViewById(com.mancj.materialsearchbar.R.id.mt_editText);
        searchEdit.addTextChangedListener(searchWatcher);

        Log.d("AmazeLogs", this.getClass().getSimpleName() + " activity created");
    }

    private void updateSearch(String s) {
        Log.d("AmazeLogs", s);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://spanshots.com/search";
        RequestParams params = new RequestParams();
        params.add("query", s);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("AmazeLogs", "Search Failed!");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                usersList.clear();
                Log.d("AmazeLogs", "Search Success!");
                JSONArray users = null;

                try {
                    users = new JSONArray(res);
                    Log.d("AmazeLogs", users.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < users.length(); i++) {
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = users.getJSONObject(i);
                        String from_id = jsonobject.getString("user_id");
                        String from_name = jsonobject.getString("name");
                        User c = new User(from_name, from_id);
                        usersList.add(c);
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}