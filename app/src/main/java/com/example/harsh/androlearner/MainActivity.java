package com.example.harsh.androlearner;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private final int TAB_FEED = FragNavController.TAB1;
    private final int TAB_EXPLORE = FragNavController.TAB2;
    private final int TAB_NOTIFICATIONS = FragNavController.TAB3;
    private final int TAB_GALLERY = FragNavController.TAB4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //App Intro
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun)
            startActivity(new Intent(MainActivity.this, SpanshotsIntroActivity.class));
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();


        //Bottom Bar FragNav Init
        List<Fragment> fragments = new ArrayList<>(4);
        PagerFragment pagerFragment = new PagerFragment();
        ExploreFragment exploreFragment = new ExploreFragment();
        GalleryFragment galleryFragment = new GalleryFragment();
        NotificationsFragment notificationsFragment = new NotificationsFragment();

        fragments.add(pagerFragment);
        fragments.add(exploreFragment);
        fragments.add(notificationsFragment);
        fragments.add(galleryFragment);

        final FragNavController fragNavController = new FragNavController(getSupportFragmentManager(), R.id.container, fragments);


        //Bottom Bar Tabs Listener
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_gallery) {
                    fragNavController.switchTab(TAB_GALLERY);
                }
                if (tabId == R.id.tab_notification) {
                    fragNavController.switchTab(TAB_NOTIFICATIONS);
                }
                if (tabId == R.id.tab_explore) {
                    fragNavController.switchTab(TAB_EXPLORE);
                }
                if (tabId == R.id.tab_trending) {
                    fragNavController.switchTab(TAB_FEED);
                }
                if (tabId == R.id.tab_trending) {
                    fragNavController.switchTab(TAB_FEED);
                }
                if (tabId == R.id.tab_camera) {
                   Intent intent = new Intent(MainActivity.this,PolaroidSampleActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //Double Back Press to Exit
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    //Spinner popup options
    public void spinner_options(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settings:
                        return false;
                    case R.id.about:
                        Intent intent = new Intent(getApplication(), AboutActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        inflater.inflate(R.menu.main_menu_options, popup.getMenu());
        popup.show();
    }
}
