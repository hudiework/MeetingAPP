package com.example.dell.demo2.loadutils;

/**
 * Created by huangjinqing on 2017/6/27.
 */
public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}
