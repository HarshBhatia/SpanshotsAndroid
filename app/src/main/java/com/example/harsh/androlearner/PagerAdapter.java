package com.example.harsh.androlearner;

/**
 * Created by harsh on 21/09/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

//import com.github.stephanenicolas.loglifecycle.LogLifeCycle;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
//                ExploreFragment tab1 = new ExploreFragment();
                FeedFragment tab1 = FeedFragment.newInstance("f","1203922519636129",null);

                return tab1;
            case 1:
                FeedFragment tab2 = FeedFragment.newInstance("fresh","1203922519636129",null);
                return tab2;
            case 2:
//                GalleryFragment tab3 = new GalleryFragment();
                FeedFragment tab3 = FeedFragment.newInstance("t","1203922519636129",null);

                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}