package com.example.dell.demo2.create_meeting.entity;

import java.io.Serializable;

/**
 * Created by Stephanie_z on 2018/3/25.
 */

public class MapWorkerMeeting implements Serializable {
    private String  role,meetingId,workerId;


    public MapWorkerMeeting(String meetingId, String workerId, String role) {
        this.meetingId = meetingId;
        this.workerId = workerId;
        this.role = role;
    }

    public MapWorkerMeeting(String workerId , String role) {
        this.role = role;
        this.workerId = workerId;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
