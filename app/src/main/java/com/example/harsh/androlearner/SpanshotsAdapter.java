package com.example.harsh.androlearner;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.danikula.videocache.HttpProxyCacheServer;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.robinhood.ticker.TickerUtils;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.ByteArrayBuffer;

/**
 * Created by harsh on 15/09/16.
 * The feed adapter
 * Uses get_post on the server to generate requested spanshots
 */

public class SpanshotsAdapter extends RecyclerView.Adapter<SpanshotsAdapter.MyViewHolder> {
    private final List<Spanshot> spanshotList;
    private final Context context;

    private class VIEW_TYPES {
        public static final int Loading = 1;
        public static final int Normal = 2;
        public static final int Disconnected = 3;
    }


    @Override
    public int getItemViewType(int position) {
        if (spanshotList.get(position).isLoading())
            return VIEW_TYPES.Loading;
        else if (spanshotList.get(position).isDisconnected())
            return VIEW_TYPES.Disconnected;
        else
            return VIEW_TYPES.Normal;
    }


    public SpanshotsAdapter(Context context, List<Spanshot> spanshotList) {
        this.context = context;
        this.spanshotList = spanshotList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView caption, username, likes_count, comments_count;
        public ImageView like_button, comment_button, share_button;
        public VideoView files_mp4;
        public ImageView files_jpg;
        public com.robinhood.ticker.TickerView views;
        public ProgressBar progress;

        public MyViewHolder(View view, int viewType) {
            super(view);
            if (viewType == VIEW_TYPES.Normal) {
                caption = (TextView) view.findViewById(R.id.caption);
                username = (TextView) view.findViewById(R.id.username);
                comments_count = (TextView) view.findViewById(R.id.comments_count);
                likes_count = (TextView) view.findViewById(R.id.likes_count);
                like_button = (ImageView) view.findViewById(R.id.like_button);
                comment_button = (ImageView) view.findViewById(R.id.comment_button);
                share_button = (ImageView) view.findViewById(R.id.share_button);
                files_jpg = (ImageView) view.findViewById(R.id.files_jpg);
                files_mp4 = (VideoView) view.findViewById(R.id.files_mp4);
                views = (com.robinhood.ticker.TickerView) view.findViewById(R.id.views);
                progress = (ProgressBar) view.findViewById(R.id.spanshotProgressBar);
                views.setCharacterList(TickerUtils.getDefaultNumberList());
            }
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch (viewType) {
            case VIEW_TYPES.Normal:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.spanshot_layout, parent, false);
                break;
            case VIEW_TYPES.Loading:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.loading, parent, false);
                break;
            case VIEW_TYPES.Disconnected:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.disonnected, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.spanshot_layout, parent, false);
                break;
        }

        return new MyViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPES.Loading) {
            Log.d("Amaze Logs", "Is loading");
        } else if (getItemViewType(position) == VIEW_TYPES.Disconnected) {
            Log.d("Amaze Logs", "Is disconnected");
        } else {

            final Spanshot s = spanshotList.get(position);

            //Inflate Data
            if (!s.get_is_liked())
                holder.like_button.setImageResource(R.drawable.ic_favorite_border_grey_500_24dp);
            else
                holder.like_button.setImageResource(R.drawable.ic_favorite_pink_a400_24dp);
            holder.caption.setText(s.get_caption());
            holder.views.setText(s.get_views());
            holder.comments_count.setText(context.getResources().getQuantityString(R.plurals.numberOfComments, s.get_comments().length(), s.get_comments().length()));
            holder.likes_count.setText(context.getResources().getQuantityString(R.plurals.numberOfLikes, s.get_likes().length(), s.get_likes().length()));
            holder.username.setText(s.get_user_name());
            Picasso.with(context).load(s.get_files_jpg()).resize(0, 480).into(holder.files_jpg);

            Log.d("AmazeLogs", "From Fragment: The view count of" + s.caption + " is " + s.views);

            //LISTENERS

            holder.username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("user_name", s.user_name);
                    intent.putExtra("user_id", s.user_id);
                    context.startActivity(intent);
                }
            });

            //Like Button Click Listener
            holder.like_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (s.get_is_liked()) {
                        YoYo.with(Techniques.Shake).duration(700).playOn(v);
                        LikeSpanshot(s.ph);
                        s.set_is_liked(false);
                        holder.like_button.setImageResource(R.drawable.ic_favorite_border_grey_500_24dp);
                    } else {
                        YoYo.with(Techniques.RubberBand).duration(700).playOn(v);
                        s.set_is_liked(true);
                        LikeSpanshot(s.ph);
                        holder.like_button.setImageResource(R.drawable.ic_favorite_pink_a400_24dp);

                    }
                    Log.d("AmazeLogs", s.get_ph() + " Liked!");

                }
            });

            //Image on click listener
            holder.files_jpg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!s.get_is_playing()) {
                        holder.progress.setVisibility(View.VISIBLE);

                        v.setVisibility(View.INVISIBLE);
                        //Set Proxy video URI
                        String proxyUrl = getVideoProxy().getProxyUrl(s.files_mp4);
                        holder.files_mp4.setVideoPath(proxyUrl);
                        holder.files_mp4.setVisibility(View.VISIBLE);

                        holder.files_mp4.start();
                        s.set_is_playing(true);
                    } else {
                    }

                }
            });
            //On hold listener
            holder.files_jpg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d("AmazeLogs", "Long Hold");
                    openBottomSheet(s.files_vid,s.ph);
                    return false;
                }
            });
            //Video on prepared listener
            holder.files_mp4.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    final int interval = 4000;
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        public void run() {
                            //Fade in last frame
                            if (!holder.files_mp4.isPlaying() && holder.files_jpg.getVisibility() != View.VISIBLE) {
                                Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fadein);
                                holder.files_jpg.startAnimation(fadeInAnimation);
                                holder.files_jpg.setVisibility(View.VISIBLE);
                                s.set_is_playing(false);
                            }
                        }
                    };
                    handler.postDelayed(runnable, interval);
                    holder.progress.setVisibility(View.INVISIBLE);

                }
            });

            //Video on end listener
            holder.files_mp4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d("AmazeLogs", "End holder.files_mp4");

                    //Increment Views
                    s.set_views(Integer.toString(Integer.parseInt(s.get_views()) + 1));
                    holder.views.setCharacterList(TickerUtils.getDefaultNumberList());
                    holder.views.setText(s.get_views());
                    ViewPlus(s.ph);


                    //Fade In last frame
                    Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fadein);
                    holder.files_jpg.startAnimation(fadeInAnimation);
                    holder.files_jpg.setVisibility(View.VISIBLE);
                    fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {


                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            //Hide Video When Ended
                            s.set_is_playing(false);
                            holder.files_mp4.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            });

            //Like count on click listener
            holder.likes_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, LikesActivity.class);
                    intent.putExtra("likes", s.likes.toString());

                    context.startActivity(intent);
                }
            });

            //Comment count on click listener
            holder.comments_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("comments", s.comments.toString());
                    intent.putExtra("post_hash", s.ph);

                    context.startActivity(intent);
                }
            });

            //Comment button listener
            holder.comment_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("comments", s.comments.toString());
                    intent.putExtra("post_hash", s.ph);

                    context.startActivity(intent);
                }
            });

            //Share button Onclick listener
            holder.share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBottomSheet(s.files_vid,s.ph);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return spanshotList.size();
    }

    private void LikeSpanshot(String post_hash) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://spanshots.com/put_like";
        RequestParams params = new RequestParams();
        params.add("hash", post_hash);
        params.add("user_id", "1203922519636129");
        params.add("user_name", "Harsh Bhatia");
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("AmazeLogs", "Like Failed!");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                Log.d("AmazeLogs", "Like Success!");
            }
        });
    }

    private void ViewPlus(String post_hash) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://spanshots.com/viewplus";
        RequestParams params = new RequestParams();
        params.add("post_hash", post_hash);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("AmazeLogs", "ViewPlus Failed!");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                Log.d("AmazeLogs", "ViewPlus Success!");
            }
        });
    }

    private HttpProxyCacheServer getVideoProxy() {
        return ProxyFactory.getProxy(context);
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, Long> {
        final ProgressDialog pd = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show progress dialog

            pd.setMessage("Downloading Spanshot...");
            pd.show();
        }


        @Override
        protected Long doInBackground(String... urls) {
            for (String DownloadUrl : urls) {
                String fileName = "video.mp4";
                try {
                    File root = Environment.getExternalStorageDirectory();

                    File dir = new File(root.getAbsolutePath() + "/xmls");
                    if (!dir.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        dir.mkdirs();
                    }

                    URL url = new URL(DownloadUrl); //you can write here any link
                    File file = new File(dir, fileName);

                    long startTime = System.currentTimeMillis();
                    Log.d("AmazeDownloadManager", "download beginning");
                    Log.d("AmazeDownloadManager", "download url:" + url);
                    Log.d("AmazeDownloadManager", "downloaded file name:" + fileName);

                     /* Open a connection to that URL. */
                    URLConnection ucon = url.openConnection();

                   /*
                    * Define InputStreams to read from the URLConnection.
                    */
                    InputStream is = ucon.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);

                   /*
                    * Read bytes to the Buffer until there is nothing more to read(-1).
                    */
                    ByteArrayBuffer baf = new ByteArrayBuffer(5000);
                    int current;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }


                    /* Convert the Bytes read to a String. */
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(baf.toByteArray());
                    fos.flush();
                    fos.close();
                    Log.d("AmazeDownloadManager", "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
                    Log.d("AmazeLogs", root.getAbsolutePath());
                } catch (IOException e) {
                    Log.d("DownloadManager", "Error: " + e);
                }

            }
            return null;
        }

        protected void onPostExecute(Long result) {
            Toast.makeText(context, "Download complete!", Toast.LENGTH_LONG)
                    .show();
            //Create share intent
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setPackage("com.whatsapp");

            // Create the URI from the media
            File media = new File(Environment.getExternalStorageDirectory() + "/xmls/video.mp4");
            Uri uri = Uri.fromFile(media);


            // Add the URI to the Intent.
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.setType("video/*");
            context.startActivity(sendIntent);
            pd.dismiss();
        }
    }

    public void openBottomSheet(final String mp4_url, String post_hash) {
        final String link = "http://spanshots.com/p/" +post_hash;

        //Bottom Sheet
        new BottomSheet.Builder(context)
                .setSheet(R.menu.share_bottom_sheet)
                .setTitle("Share")
                .setListener(new BottomSheetListener() {
                    @Override
                    public void onSheetShown(@NonNull BottomSheet bottomSheet) {

                    }

                    @Override
                    public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.share_on_whatsapp:
                                //Download video
                                new DownloadFilesTask().execute(mp4_url);
                                break;
                            case R.id.share_link:
                                Intent shareLinkIntent = new Intent();
                                shareLinkIntent.setAction(Intent.ACTION_SEND);
                                shareLinkIntent.putExtra(Intent.EXTRA_TEXT, link);
                                shareLinkIntent.setType("text/plain");
                                context.startActivity(shareLinkIntent);
                                break;
                            case R.id.copy_link:
                                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Spanshots", link);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(context, "Link copied!", Toast.LENGTH_LONG)
                                        .show();
                                break;
                            case R.id.repost:
                                new MaterialDialog.Builder(context)
                                        .title("Repost")
                                        .content("Are you sure you want to repost this Spanshot on your profile?")
                                        .positiveText("Yes")
                                        .negativeText("Nope")
                                        .show();
                                break;
                        }
                    }

                    @Override
                    public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @DismissEvent int i) {

                    }
                })
                .show();

    }

}
