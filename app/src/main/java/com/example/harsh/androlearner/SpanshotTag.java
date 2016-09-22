package com.example.harsh.androlearner;

import org.json.JSONArray;

/**
 * Created by harsh on 16/09/16.
 */
public class SpanshotTag {
    String ph, mp4,views,user_name, user_id;
    Boolean is_liked;
    JSONArray comments, likes;
    public SpanshotTag(String ph, Boolean is_liked, String mp4,String views, String user_name,String user_id, JSONArray comments,JSONArray likes
    ){
        this.is_liked = is_liked;
        this.ph = ph;
        this.mp4 = mp4;
        this.views = views;
        this.user_name = user_name;
        this.comments = comments;
        this.likes = likes;
        this.user_id = user_id;

    }
}

