package com.happytap.acro.models;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.happytap.acro.TwitterUser;

public class Player {

	public String getId() {
		return id;
	}

	public String getName() {
		return firstName;
	}

	public String id = UUID.randomUUID().toString();
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
		return avatarUrl;
	}

	public static Player parseFacebookJson(String response) {
		Player ret = new Player();

		JSONObject json = null;
		try {
			json = new JSONObject(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret.name = json.optString("name");
		ret.firstName = json.optString("first_name");
		String last = json.optString("last_name");
		if (last != null && last.length() > 0) {
			ret.firstName = ret.firstName + " " + last.charAt(0) + ".";
		}
		ret.id = "+f" + json.optString("id");
		ret.avatarUrl = "http://graph.facebook.com/" + json.optString("id")
				+ "/picture?type=large";

		return ret;
	}

	public static Player parseTwitter(TwitterUser user) {
		Player ret = new Player();
		ret.name = user.getName();
		ret.firstName = user.getScreenName();

		ret.id = "+t" + user.getId();
		ret.avatarUrl = user.getProfileImageUrl();

		return ret;
	}

	public static Player parseJson(String id, JSONObject data) {
		Player ret = new Player();

		ret.name = data.optString("name");
		ret.firstName = data.optString("username");
		ret.id = data.optString("user_id");
		ret.currentRoundVoteCount = data.optInt("current_round_vote_count");
		ret.totalVoteCount = data.optInt("total_vote_count");

		return ret;
	}
}
