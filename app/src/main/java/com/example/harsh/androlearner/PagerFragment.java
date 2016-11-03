package com.example.harsh.androlearner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.stephanenicolas.loglifecycle.LogLifeCycle;

/**
 * Created by harsh on 22/09/16.
 */

@LogLifeCycle
public class PagerFragment extends Fragment {
    private boolean init = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("AmazeLogs", "PagerFragment view created");
        return inflater.inflate(R.layout.pager_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("AmazeLogs", "View Created");


        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Feed"));
        tabLayout.addTab(tabLayout.newTab().setText("Fresh"));
        tabLayout.addTab(tabLayout.newTab().setText("Trending"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) getView().findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(1);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                View view = tab.getCustomView();
                Log.d("AmazeLogs", tab.getText() + " tab selected");

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("AmazeLogs", tab.getText() + " tab unselected");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                try {
                    FeedFragment f = (FeedFragment) adapter.getItem(1);
                    if (f != null) {

                        SwipeRefreshLayout fragmentView =(SwipeRefreshLayout) getView().findViewById(R.id.activity_main_swipe_refresh_layout);
                        com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView mRecyclerView = (com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView) fragmentView.findViewById(R.id.recycler_view);//mine one is RecyclerView
                        if (mRecyclerView != null){
                            mRecyclerView.scrollVerticallyToPosition(0);

                        }
                    }
                } catch (NullPointerException npe) {

                    Log.d("AmazeLogs", npe.toString());
                }
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("AmazeLogs", "PagerFragment activity created");
    }

    @Override
    public void onResume() {
        super.onResume();

//        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tab_layout);
//        Log.d("AmazeLogs", tabLayout.getSelectedTabPosition() + "tab selected on resume");
//        final ViewPager viewPager = (ViewPager) getView().findViewById(R.id.pager);
//        final PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        viewPager.setCurrentItem(1);


    }

}