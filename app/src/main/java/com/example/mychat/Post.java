package com.example.mychat;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String uid;
    public String username;
    public String msg;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String username, String msg) {

        this.uid = uid;
        this.username = username;
        this.msg = msg;
    }

    public String getUid() {

        return this.uid;
    }

    public void setUid(String uid) {

        this.uid = uid;
    }

    public String getMsg() {

        return this.msg;
    }

    public void setMsg(String msg) {

        this.msg = msg;
    }

    public String getUsername() {

        return this.username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("username", username);
        result.put("msg", msg);
        return result;
    }
}