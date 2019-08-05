package com.example.dell.demo2.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class CommentEntity implements Serializable{

    private String pid;
    private String mid;
    private float rating;
    private String comment;
    private String tag;


    public CommentEntity(String pid, String mid, float rating, String comment, String tag) {
        this.pid = pid;
        this.mid = mid;
        this.rating = rating;
        this.comment = comment;
        this.tag = tag;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
