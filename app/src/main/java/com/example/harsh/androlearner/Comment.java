package com.example.harsh.androlearner;

/**
 * Created by harsh on 21/09/16.
 */

public class Comment {
    private String comment_user_name, comment_user_id, comment_text;

    public Comment(String comment_user_name, String comment_user_id, String comment_text) {

        this.comment_text = comment_text;
        this.comment_user_id = comment_user_id;
        this.comment_user_name = comment_user_name;
    }

    public String get_comment_user_name() {
        return comment_user_name;
    }

    public String get_comment_user_id() {
        return comment_user_id;
    }

    public String get_comment_text() {
        return comment_text;
    }
}

