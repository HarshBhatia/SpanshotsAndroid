package com.example.harsh.androlearner;

/**
 * Created by harsh on 21/09/16.
 */

public class Like {

    private String like_user_name, like_user_id;

    public Like(String like_user_name, String like_user_id) {


        this.like_user_id = like_user_id;
        this.like_user_name = like_user_name;
    }

    public String get_like_user_name() {
        return like_user_name;
    }

    public String get_like_user_id() {
        return like_user_id;
    }

}
