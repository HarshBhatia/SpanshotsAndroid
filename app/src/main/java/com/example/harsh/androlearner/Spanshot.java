package com.example.harsh.androlearner;

import org.json.JSONArray;

/**
 * Created by harsh on 15/09/16.
 */

public class Spanshot {
    public String caption, files_jpg, user_name, files_mp4, files_vid, ph, views, user_id;
    public Boolean is_liked;
    public Boolean is_loading;
    public Boolean is_disconnected;

    public Boolean get_is_playing() {
        return is_playing;
    }

    public void set_is_playing(Boolean is_playing) {
        this.is_playing = is_playing;
    }

    public Boolean is_playing;
    public JSONArray likes, comments;


    public Spanshot(String caption, String user_name, String files_jpg, String files_mp4, String files_vid, String ph, Boolean is_liked, String views, Boolean is_loading, Boolean is_disconnected, String user_id, JSONArray comments, JSONArray likes) {
        this.caption = caption;
        this.user_name = user_name;
        this.files_jpg = files_jpg;
        this.files_mp4 = files_mp4;
        this.files_vid = files_vid;
        this.is_liked = is_liked;
        this.ph = ph;
        this.views = views;
        this.is_loading = is_loading;
        this.is_disconnected = is_disconnected;
        this.user_id = user_id;
        this.likes = likes;
        this.comments = comments;
        this.is_playing = false;

    }

    public String get_user_name() {
        return user_name;
    }

    public String get_user_id() {
        return user_id;
    }

    public String get_caption() {
        return caption;
    }

    public String get_files_jpg() {

        return files_jpg;
    }

    public String get_files_mp4() {
        return files_mp4;
    }

    public String get_files_vid() {
        return files_vid;
    }

    public String get_ph() {
        return ph;
    }

    public Boolean get_is_liked() {
        return is_liked;
    }

    public void set_is_liked(Boolean v) {
        is_liked = v;
    }

    public void set_views(String v) {
        views = v;
    }

    public String get_views() {

        if (views == null || views.isEmpty() || views == "" || views == "null") {
            views = "0";
        }
        return views;
    }

    public JSONArray get_likes() {
        return likes;
    }

    public JSONArray get_comments() {
        return comments;
    }

    public Boolean isLoading() {
        return is_loading;
    }

    public Boolean isDisconnected() {
        return is_disconnected;
    }
}
