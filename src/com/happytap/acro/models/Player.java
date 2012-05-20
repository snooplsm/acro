package com.happytap.acro.models;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Player {

	public String getId() {
		return id;
	}

	public String getName() {
		return firstName;
	}

	public String id = UUID.randomUUID().toString();
	public String fbid = "";
	public String voteForAcronymId = "";
	public String name = "";
	public String firstName = "Anonymous";
	public String avatarUrl;
	public int currentRoundVoteCount;
	public int totalVoteCount;

	public int getCurrentRoundVoteCount() {
		return currentRoundVoteCount;
	}
	
	public int getTotalVoteCount() {
		return totalVoteCount;
	}

	public String avatarUrl() {
		return "http://graph.facebook.com/" + fbid + "/picture?type=large";
	}

	public static Player parseFacebookJson(String response) {
		Player ret = new Player();

		try {
			JSONObject json = new JSONObject(response);
			ret.name = json.getString("name");
			ret.firstName = json.getString("first_name");
			ret.id = json.getString("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static Player parseJson(String id, JSONObject data) {
		Player ret = new Player();

		ret.name = data.optString("name");
		ret.firstName = data.optString("username");
		ret.id = data.optString("user_id");
		ret.fbid = data.optString("fbid");
		ret.currentRoundVoteCount = data.optInt("current_round_vote_count");
		ret.totalVoteCount = data.optInt("total_vote_count");

		return ret;
	}
}
