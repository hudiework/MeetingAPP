package com.example.dell.demo2.entity;


public class MessageEntity{  
	
	private String meeting;
    private String name;
	private String date;
	private String message;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private String role;
	private boolean isComMeg = true;
	
	public MessageEntity() {
	}

	public MessageEntity(String meeting,String name, String date, String text) {
		this.meeting=meeting;
		this.name = name;
		this.date = date;
		this.message = text;
	}

	public String getMeeting() {
		return meeting;
	}

	public void setMeeting(String meeting) {
		this.meeting = meeting;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getMsgType() {
		return isComMeg;
	}

	public void setMsgType(boolean isComMsg) {
		isComMeg = isComMsg;
	}
    
    
}
