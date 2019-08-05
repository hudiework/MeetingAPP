package com.example.dell.demo2.loginsucce_meetingpart;

import java.io.Serializable;

public class MeetingBean implements Serializable {
    /**
     * Created by Administrator on 2018/3/30.
     */

    private int meetingId;//会议id
    private String meetingName;//会议名称
    private String meetingDate;//会议开始日期
    private String meetingPlace;//会议地点
    private String meetingContent;//会议内容
    //    private String meetingAgenda;//会议议程
//    private String meetingGuide;//参会指南
    private String meetingPeople;//会议邀请对象
    private String meetingSpecial;//会议特邀嘉宾
    private String meetingOverDate;//会议结束时间
    private float meetingCost;//会议费用
    private String myRole;//我的角色
    //会议号在服务器端产生
    private String workName;


    public MeetingBean() {
    }

    public MeetingBean(int meetingId, String meetingName, String meetingDate, String meetingPlace, String meetingContent, String meetingPeople, String meetingSpecial, String meetingOverDate, float meetingCost, String myRole) {
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.meetingDate = meetingDate;
        this.meetingPlace = meetingPlace;
        this.meetingContent = meetingContent;
        this.meetingPeople = meetingPeople;
        this.meetingSpecial = meetingSpecial;
        this.meetingOverDate = meetingOverDate;
        this.meetingCost = meetingCost;
        this.myRole = myRole;
    }

    public MeetingBean(String meetingName, String meetingDate, String meetingPlace, String meetingContent, String meetingPeople, String meetingSpecial, String meetingOverDate, float meetingCost) {
        this.meetingName = meetingName;
        this.meetingDate = meetingDate;
        this.meetingPlace = meetingPlace;
        this.meetingContent = meetingContent;
        this.meetingPeople = meetingPeople;
        this.meetingSpecial = meetingSpecial;
        this.meetingOverDate = meetingOverDate;
        this.meetingCost = meetingCost;
    }
    //没有cost时的


    public MeetingBean( String meetingName, String meetingDate, String meetingPlace, String meetingContent, String meetingPeople, String meetingSpecial, String meetingOverDate) {

        this.meetingName = meetingName;
        this.meetingDate = meetingDate;
        this.meetingPlace = meetingPlace;
        this.meetingContent = meetingContent;
        this.meetingPeople = meetingPeople;
        this.meetingSpecial = meetingSpecial;
        this.meetingOverDate = meetingOverDate;
    }

    public MeetingBean(String meetingName, String meetingDate, String meetingPlace, String meetingContent, String meetingPeople, String meetingSpecial, String meetingOverDate, float meetingCost, String myRole) {
        this.meetingName = meetingName;
        this.meetingDate = meetingDate;
        this.meetingPlace = meetingPlace;
        this.meetingContent = meetingContent;
        this.meetingPeople = meetingPeople;
        this.meetingSpecial = meetingSpecial;
        this.meetingOverDate = meetingOverDate;
        this.meetingCost = meetingCost;
        this.myRole = myRole;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public String getMeetingContent() {
        return meetingContent;
    }

    public void setMeetingContent(String meetingContent) {
        this.meetingContent = meetingContent;
    }

    public String getMeetingPeople() {
        return meetingPeople;
    }

    public void setMeetingPeople(String meetingPeople) {
        this.meetingPeople = meetingPeople;
    }

    public String getMeetingSpecial() {
        return meetingSpecial;
    }

    public void setMeetingSpecial(String meetingSpecial) {
        this.meetingSpecial = meetingSpecial;
    }

    public String getMeetingOverDate() {
        return meetingOverDate;
    }

    public void setMeetingOverDate(String meetingOverDate) {
        this.meetingOverDate = meetingOverDate;
    }

    public float getMeetingCost() {
        return meetingCost;
    }

    public void setMeetingCost(float meetingCost) {
        this.meetingCost = meetingCost;
    }

    public String getMyRole() {
        return myRole;
    }

    public void setMyRole(String myRole) {
        this.myRole = myRole;
    }

    @Override
    public String toString() {
        return "MeetingBean{" +
                "meetingId=" + meetingId +
                ", meetingName='" + meetingName + '\'' +
                ", meetingDate='" + meetingDate + '\'' +
                ", meetingPlace='" + meetingPlace + '\'' +
                ", meetingContent='" + meetingContent + '\'' +
                ", meetingPeople='" + meetingPeople + '\'' +
                ", meetingSpecial='" + meetingSpecial + '\'' +
                ", meetingOverDate='" + meetingOverDate + '\'' +
                ", meetingCost=" + meetingCost +
                '}';
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }
}
