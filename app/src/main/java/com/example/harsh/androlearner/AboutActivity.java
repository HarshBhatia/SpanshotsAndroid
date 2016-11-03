package com.example.harsh.androlearner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNavBar);
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Spanshots helps you to capture moments in a new amazing way and makes life more beautiful by adding motion in every moment.")
                .setImage(R.drawable.logo)
                .addItem(new Element().setTitle("Version 3.2"))
                .addGroup("Connect with us")
                .addEmail("spanshots@gmail.com")
                .addWebsite("http://spanshots.com/")
                .addFacebook("spanshots")
                .addTwitter("spanshots_official")
                .addYoutube("UCxDLtjpY7sIlIk4OuQdSF1Q")
                .addPlayStore("com.spanshots.success.in")
                .addInstagram("spanshots_official")
                .create();

        setContentView(aboutPage);
    }
}
