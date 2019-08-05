package com.example.dell.demo2.entity;

import java.io.Serializable;

public class ContentEntity implements Serializable{

	private String meeting;
	private String content;
	private String date;
	private String tag;
	private String checkMessage;
	private String count;
	private int id;


	public ContentEntity(String meeting, String content, String date,
						 String tag) {
		super();
		this.meeting = meeting;
		this.content = content;
		this.date = date;
		this.tag = tag;
	}

	public void setMeeting(String meeting) {
		this.meeting = meeting;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCheckMessage() {
		return checkMessage;
	}

	public void setCheckMessage(String checkMessage) {
		this.checkMessage = checkMessage;
	}

	public String getMeeting() {
		return meeting;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
