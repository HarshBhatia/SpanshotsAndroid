package com.example.harsh.androlearner;

import org.json.JSONArray;

/**
 * Created by harsh on 15/09/16.
 */

public class Spanshot {
    private String caption, files_jpg, user_name, files_mp4, ph, views, user_id;
    private Boolean is_liked, is_header, is_footer;
    private JSONArray likes, comments;

    public Spanshot() {
    }

    public Spanshot(String caption, String user_name, String files_jpg, String files_mp4, String ph, Boolean is_liked, String views, Boolean is_header, Boolean is_footer, String user_id, JSONArray comments, JSONArray likes) {
        this.caption = caption;
        this.user_name = user_name;
        this.files_jpg = files_jpg;
        this.files_mp4 = files_mp4;
        this.is_liked = is_liked;
        this.ph = ph;
        this.views = views;
        this.is_header = is_header;
        this.is_footer = is_footer;
        this.user_id = user_id;
        this.likes = likes;
        this.comments = comments;

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

    public String get_ph() {
        return ph;
    }

    public Boolean get_is_liked() {
        return is_liked;
    }

    public String get_views() {
        return views;
    }

    public JSONArray get_likes() {
        return likes;
    }

    public JSONArray get_comments() {
        return comments;
    }

    public Boolean isHeader() {
        return is_header;
    }

    public Boolean isFooter() {
        return is_footer;
    }
}
