package com.example.harsh.androlearner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.ObjectGraph;

public class FfmpegActivity extends AppCompatActivity {
    @Inject
    FFmpeg ffmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg);

                ButterKnife.inject(this);
        ObjectGraph.create(new DaggerDependencyModule(this)).inject(this);
        loadFFMpegBinary();
    }


    public void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d("AmazeLogsFFmpeg", "Ffmpeg processing Failed");
                }

                @Override
                public void onSuccess(String s) {
                    Log.d("AmazeLogsFFmpeg", "Ffmpeg processing success with output:" + s);
                }

                @Override
                public void onProgress(String s) {
                    Log.d("AmazeLogsFFmpeg", "Started command : ffmpeg " + command);
//                    addTextViewToLayout("progress : "+s);
//                    progressDialog.setMessage("Processing\n"+s);
                }

                @Override
                public void onStart() {
                    Log.d("AmazeLogsFFmpeg", "Started command : ffmpeg " + command);
//                    progressDialog.setMessage("Processing...");
//                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    Log.d("AmazeLogsFFmpeg", "Finished command : ffmpeg " + command);
//                    progressDialog.dismiss();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Log.d("AmazeLogs", "Ffmpeg init failure");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            Log.d("AmazeLogs", "Ffmpeg init failure");
        }
    }


    public void makeGif(String video_name, String base_path) {
        String cmd = "-i " + base_path + video_name + " " + base_path + "video.gif";
        Log.d("AmazeLogs", cmd);
//        String cmd = "ffmpeg -version";
        String[] command = cmd.split(" ");
        if (command.length != 0) {
            execFFmpegBinary(command);
        } else {
            Toast.makeText(this, getString(R.string.empty_command_toast), Toast.LENGTH_LONG).show();
        }
    }


}
