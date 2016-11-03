package com.example.harsh.androlearner;

import org.json.JSONArray;

/**
 * Created by harsh on 16/09/16.
 */
public class SpanshotTag {
    String ph, vid, mp4, views, user_name, user_id;
    Boolean is_liked;
    JSONArray comments, likes;

    public SpanshotTag(String ph, Boolean is_liked, String mp4, String vid, String views, String user_name, String user_id, JSONArray comments, JSONArray likes
    ) {
        this.is_liked = is_liked;
        this.ph = ph;
        this.mp4 = mp4;
        this.vid = vid;
        this.views = views;
        if(this.views==null){
            this.views = "0";
        }
        this.user_name = user_name;
        this.comments = comments;
        this.likes = likes;
        this.user_id = user_id;

    }
}

