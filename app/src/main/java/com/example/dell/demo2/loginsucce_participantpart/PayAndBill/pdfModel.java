package com.example.dell.demo2.loginsucce_participantpart.PayAndBill;

/**
 * Created by Administrator on 2018/4/27.
 */

public class pdfModel {
    private String name;
    private String path;
    private long length;
    public pdfModel() {
        super();
    }
    public pdfModel(String name, String path, long length) {
        super();
        this.name = name;
        this.path = path;
        this.length = length;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public long getLength() {
        return length;
    }
    public void setLength(long length) {
        this.length = length;
    }

}
