package com.example.harsh.androlearner;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.support.design.widget.Snackbar;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ncapdevi.fragnav.FragNavController;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;


import org.json.JSONArray;

import cz.msebera.android.httpclient.util.TextUtils;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private BottomBar bottomBar;


    private MaterialSearchView searchView;

    private String STATIC_FILES_URL = "http://spanshots.com/static/files_uploaded/";

    private final int TAB_FEED = FragNavController.TAB1;
    private final int TAB_EXPLORE = FragNavController.TAB2;
    private final int TAB_NOTIFICATIONS = FragNavController.TAB3;
    private final int TAB_GALLERY = FragNavController.TAB4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //App Intro
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            startActivity(new Intent(MainActivity.this, SpanshotsIntroActivity.class));
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG)
                    .show();
        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();


        //FragNav Init
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
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_gallery) {
                    fragNavController.switchTab(TAB_GALLERY);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                }
                if (tabId == R.id.tab_notification) {
                    fragNavController.switchTab(TAB_NOTIFICATIONS);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                }
                if (tabId == R.id.tab_explore) {
                    fragNavController.switchTab(TAB_EXPLORE);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                }
                if (tabId == R.id.tab_trending) {
                    fragNavController.switchTab(TAB_FEED);
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                }
            }
        });

    }


    //Material Search
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
//
//        return true;
//    }

    //Double Back Press to Exit
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
//        if (searchView.isSearchOpen()) {
//            searchView.closeSearch();
//        } else {
//            super.onBackPressed();
//        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Click on Video
    public void videoPlayFromVideo(View view) {
        Log.d("AmazeLogs", "Click from video");

    }

    //Click on Image
    public void videoPlayFromImage(final View view) {

        SpanshotTag tag = (SpanshotTag) view.getTag();
        final ViewGroup row = (ViewGroup) view.getParent();
        final ViewGroup row_parent = (ViewGroup) row.getParent();
        final VideoView video = (VideoView) row.findViewById(R.id.files_mp4);
        final ProgressBar progress = (ProgressBar) row.findViewById(R.id.spanshotProgressBar);

        final int views = Integer.parseInt(tag.views);
        //Make progress Bar visible
        progress.setVisibility(View.VISIBLE);

        Log.d("AmazeLogs", "Click from Image");

        //Set video URI from tag
        Uri vidUri = Uri.parse(tag.mp4);
        video.setVideoURI(vidUri);
        video.setVisibility(View.VISIBLE);

        //Hide media Controls
        MediaController mc = new MediaController(this);
        mc.setVisibility(View.GONE);
        video.setMediaController(mc);

        //Video on prepared listener
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                progress.setVisibility(View.GONE);
            }
        });

        //Video on end listener
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("AmazeLogs", "End video");

                //Increment Views
                final com.robinhood.ticker.TickerView tickerView = (com.robinhood.ticker.TickerView)row_parent.findViewById(R.id.views);
                tickerView.setCharacterList(TickerUtils.getDefaultNumberList());
                tickerView.setText(Integer.toString(views+1));


                //Fade In last frame
                Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                view.startAnimation(fadeInAnimation);
                view.setVisibility(View.VISIBLE);
                fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Hide Video When Ended
                        video.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        //Hide Image
        view.setVisibility(View.INVISIBLE);

        //Start Video
        video.start();

    }

    //Click on Like Button
    public void likeSpanshot(View view) {
        ImageView iv = (ImageView) view;

        //Play Like Sound
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();

        //Get post Hash from tag
        SpanshotTag tag = (SpanshotTag) view.getTag();
        String post_hash = tag.ph;
        Boolean is_liked = tag.is_liked;

        if (is_liked) {
            tag.is_liked = false;
            iv.setImageResource(R.drawable.ic_favorite_border_pink_a400_24dp);
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(view);
        } else {
            is_liked = true;
            iv.setImageResource(R.drawable.ic_favorite_pink_a400_24dp);
            YoYo.with(Techniques.RubberBand)
                    .duration(700)
                    .playOn(view);
        }
    }


    //Click on Share Button
    public void shareSpanshot(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    // Click on More Options button
    public void showMoreOptions(View view) {
        //Use material Dialogs
    }

    //Check if connected
    boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    //Click on comments button
    public void viewComments(View view){
        SpanshotTag tag = (SpanshotTag) view.getTag();
        JSONArray comments = (JSONArray) tag.comments;

        Log.d("AmazeLogs",comments.toString());

        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("comments", comments.toString());

        startActivity(intent);
    }

    //Click on likes button
    public void viewLikes(View view){
        SpanshotTag tag = (SpanshotTag) view.getTag();
        JSONArray likes = (JSONArray) tag.likes;

        Log.d("AmazeLogs",likes.toString());

        Intent intent = new Intent(this, LikesActivity.class);
        intent.putExtra("likes", likes.toString());

        startActivity(intent);
    }

    //Click on username
    public void viewUser(View view){
        SpanshotTag tag = (SpanshotTag) view.getTag();
        String user_name = tag.user_name;
        String user_id = tag.user_id;
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("user_name", user_name.toString());
        intent.putExtra("user_id", user_id.toString());
        startActivity(intent);
    }
}
