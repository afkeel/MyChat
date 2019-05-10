package com.example.mychat;

import java.util.HashMap;
import java.util.Map;

public class Post {

    public String uid;
    public String msg;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String msg) {

        this.uid = uid;
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

    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("msg", msg);
        return result;
    }
}