package com.example.harsh.androlearner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        Intent intent = getIntent();
        String user_name = intent.getStringExtra("user_name");
        String user_id = intent.getStringExtra("user_id");
        String url = "https://graph.facebook.com/"+user_id+"/picture?type=large";

        CircleImageView profile_picture = (CircleImageView) findViewById(R.id.profile_user_image);

        Picasso.with(getApplicationContext()).load(url).into(profile_picture);

        TextView name = (TextView) findViewById(R.id.profile_user_name);
        name.setText(user_name);

        FeedFragment feedFragment = FeedFragment.newInstance("u","1203922519636129",user_id);

        getSupportFragmentManager().beginTransaction().add(R.id.profile_fragment_container,feedFragment).commit();

    }
}
