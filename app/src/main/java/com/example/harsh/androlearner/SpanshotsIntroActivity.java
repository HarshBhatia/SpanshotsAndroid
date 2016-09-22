package com.example.harsh.androlearner;

import android.Manifest;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

/**
 * Created by harsh on 19/09/16.
 */
public class SpanshotsIntroActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /* Enable/disable skip button */
        setSkipEnabled(true);

        /* Enable/disable finish button */
        setFinishEnabled(true);

        /* Add a navigation policy to define when users can go forward/backward */
        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int position) {
                return true;
            }

            @Override
            public boolean canGoBackward(int position) {
                return true;
            }
        });


        addSlide(new SimpleSlide.Builder()
                .title("Hello World")
                .description("This is the first slide description")
                .image(R.drawable.doge)
                .background(R.color.purple_500)
                .backgroundDark(R.color.purple_700)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Hello World 2")
                .description("This is the second slide description")
                .image(R.drawable.doge2)
                .background(R.color.blue_500)
                .backgroundDark(R.color.blue_700)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Hello World 3")
                .description("This is the third slide description")
                .image(R.drawable.doge)
                .background(R.color.pink_500)
                .backgroundDark(R.color.pink_700)
                .build());

    }
}