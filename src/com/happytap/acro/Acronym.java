package com.happytap.acro;

import org.json.JSONObject;

import com.happytap.acro.models.Player;

public class Acronym {

	private JSONObject data;
	
	public Acronym(JSONObject obj) {
		this.data = obj;
	}
	
	public Acronym(String acronym, String userId) {
		data = new JSONObject();
		try {
			data.put("text",acronym);
			data.put("user_id", userId);
		} catch (Exception e) {
			
		}
	}
	
	public Player getPlayer() {
		return Player.parseJson(null, data.optJSONObject("player"));
	}
	
	public String getText() {
		return data.optString("text");
	}
	
	public Integer getVoteCount() {
		return data.optInt("vote_count", -1);
	}
}
