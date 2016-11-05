package com.example.harsh.androlearner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by harsh on 11/10/16.
 */

public class PreviewActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        final Context context = this;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Uri uri = Uri.parse(Environment.getExternalStorageDirectory() + "/MagicCamera_test.mp4");
                VideoView video_preview = (VideoView) findViewById(R.id.video_preview);
                MediaController controller = new MediaController(context);
                video_preview.setVideoURI(uri);
                video_preview.setMediaController(controller);
                video_preview.start();
                findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.setType("video/mp4");
                        startActivity(shareIntent);
                    }
                });
            }
        }, 1000);


    }
//    @Override
//    public void onBackPressed() {
////        Intent setIntent = new Intent(PreviewActivity.this, SpanshotActivity.class);
////        startActivity(setIntent);
////        finish();
//    }
}
