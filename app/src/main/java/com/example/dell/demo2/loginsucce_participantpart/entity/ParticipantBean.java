package com.example.dell.demo2.loginsucce_participantpart.entity;

/**
 * Created by Administrator on 2018/5/1.
 */

public class ParticipantBean {
    private int participantId;
    private String participantMobilePhone;//注册手机号
    private int participantMeetingId;//报名的会议id
    private String participantName;//姓名
    private String participantPosition;//职位
    private String participantCompany;//公司
    private int participantChoseChildMeeting;//选择的子会议id
    private String participantDemaind;//住宿要求
    private int participantTag;//是否已经签到，0代表只报名，还未签到。1代表签到成功
    public ParticipantBean(int participantId, String participantMobilePhone,
                           int participantMeetingId, String participantName,
                           String participantPosition, String participantCompany,
                           int participantChoseChildMeeting, String participantDemaind,
                           int participantTag) {
        super();
        this.participantId = participantId;
        this.participantMobilePhone = participantMobilePhone;
        this.participantMeetingId = participantMeetingId;
        this.participantName = participantName;
        this.participantPosition = participantPosition;
        this.participantCompany = participantCompany;
        this.participantChoseChildMeeting = participantChoseChildMeeting;
        this.participantDemaind = participantDemaind;
        this.participantTag = participantTag;
    }
    public ParticipantBean(String participantMobilePhone,
                           int participantMeetingId, String participantName,
                           String participantPosition, String participantCompany,
                           int participantChoseChildMeeting, String participantDemaind,
                           int participantTag) {
        super();
        this.participantMobilePhone = participantMobilePhone;
        this.participantMeetingId = participantMeetingId;
        this.participantName = participantName;
        this.participantPosition = participantPosition;
        this.participantCompany = participantCompany;
        this.participantChoseChildMeeting = participantChoseChildMeeting;
        this.participantDemaind = participantDemaind;
        this.participantTag = participantTag;
    }
    public ParticipantBean() {
        super();
    }

    public int getParticipantId() {
        return participantId;
    }
    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }
    public String getParticipantMobilePhone() {
        return participantMobilePhone;
    }
    public void setParticipantMobilePhone(String participantMobilePhone) {
        this.participantMobilePhone = participantMobilePhone;
    }
    public int getParticipantMeetingId() {
        return participantMeetingId;
    }
    public void setParticipantMeetingId(int participantMeetingId) {
        this.participantMeetingId = participantMeetingId;
    }
    public String getParticipantName() {
        return participantName;
    }
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
    public String getParticipantPosition() {
        return participantPosition;
    }
    public void setParticipantPosition(String participantPosition) {
        this.participantPosition = participantPosition;
    }
    public String getParticipantCompany() {
        return participantCompany;
    }
    public void setParticipantCompany(String participantCompany) {
        this.participantCompany = participantCompany;
    }
    public int getParticipantChoseChildMeeting() {
        return participantChoseChildMeeting;
    }
    public void setParticipantChoseChildMeeting(int participantChoseChildMeeting) {
        this.participantChoseChildMeeting = participantChoseChildMeeting;
    }
    public String getParticipantDemaind() {
        return participantDemaind;
    }
    public void setParticipantDemaind(String participantDemaind) {
        this.participantDemaind = participantDemaind;
    }
    public int getParticipantTag() {
        return participantTag;
    }
    public void setParticipantTag(int participantTag) {
        this.participantTag = participantTag;
    }


}
