package com.happytap.acro;

import org.json.JSONObject;

public class Player {
	
	private JSONObject obj;

	public Player(JSONObject obj) {
		this.obj = obj;
	}

	public String getId() {
		return obj.optString("user_id");
	}
	
	public String getName() {
		return obj.optString("username");
	}
	
}
