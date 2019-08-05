package com.example.dell.demo2.loginsucce_participantpart;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ChildMeetingBean implements Serializable{
    private Integer childMeetingId, mainMeetingId;
    private String childMeetingName, childMeetingTime, childMeetingPlace,
            childMeetingContent, childMeetingCompere;

    public ChildMeetingBean() {
    }

    public ChildMeetingBean(Integer childMeetingId, String childMeetingName,
                            String childMeetingTime, String childMeetingPlace,
                            String childMeetingContent, String childMeetingCompere) {
        super();
        this.childMeetingId = childMeetingId;
        this.childMeetingName = childMeetingName;
        this.childMeetingTime = childMeetingTime;
        this.childMeetingPlace = childMeetingPlace;
        this.childMeetingContent = childMeetingContent;
        this.childMeetingCompere = childMeetingCompere;

    }

    public ChildMeetingBean(String childMeetingName, String childMeetingTime, String childMeetingPlace, String childMeetingContent, String childMeetingCompere) {
        this.childMeetingName = childMeetingName;
        this.childMeetingTime = childMeetingTime;
        this.childMeetingPlace = childMeetingPlace;
        this.childMeetingContent = childMeetingContent;
        this.childMeetingCompere = childMeetingCompere;
    }

    public Integer getChildMeetingId() {
        return childMeetingId;
    }

    public void setChildMeetingId(Integer childMeetingId) {
        this.childMeetingId = childMeetingId;
    }

    public Integer getMainMeetingId() {
        return mainMeetingId;
    }

    public void setMainMeetingId(Integer mainMeetingId) {
        this.mainMeetingId = mainMeetingId;
    }

    public String getChildMeetingName() {
        return childMeetingName;
    }

    public void setChildMeetingName(String childMeetingName) {
        this.childMeetingName = childMeetingName;
    }

    public String getChildMeetingTime() {
        return childMeetingTime;
    }

    public void setChildMeetingTime(String childMeetingTime) {
        this.childMeetingTime = childMeetingTime;
    }

    public String getChildMeetingPlace() {
        return childMeetingPlace;
    }

    public void setChildMeetingPlace(String childMeetingPlace) {
        this.childMeetingPlace = childMeetingPlace;
    }

    public String getChildMeetingContent() {
        return childMeetingContent;
    }

    public void setChildMeetingContent(String childMeetingContent) {
        this.childMeetingContent = childMeetingContent;
    }

    public String getChildMeetingCompere() {
        return childMeetingCompere;
    }

    public void setChildMeetingCompere(String childMeetingCompere) {
        this.childMeetingCompere = childMeetingCompere;
    }
}
