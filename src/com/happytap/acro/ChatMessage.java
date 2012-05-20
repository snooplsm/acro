package com.happytap.acro;

import org.json.JSONObject;

public class ChatMessage {

	private String msg;
	private String fromId;
	private String username;
	private long timeStamp;
	
	public ChatMessage(JSONObject obj) {
		msg = obj.optString("message");
		fromId = obj.optString("user_id");
		username = obj.optString("username");
	}

	public String getMsg() {
		return msg;
	}

	public String getUsername() {
		return username;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}

