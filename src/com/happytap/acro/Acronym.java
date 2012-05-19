package com.happytap.acro;

import org.json.JSONObject;

public class Acronym {

	private JSONObject data;
	
	public Acronym(JSONObject obj) {
		this.data = obj;
	}
	
	public String getUserId() {
		return data.optString("user_id");
	}
	
	public String getUsername() {
		return data.optString("username");
	}
	
	public String getText() {
		return data.optString("text");
	}
	
	public Integer getVoteCount() {
		return data.optInt("vote_count", -1);
	}
}
