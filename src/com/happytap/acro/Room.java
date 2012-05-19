package com.happytap.acro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Room  {

	private JSONObject json;
	
	private Map<String, Player> playerIdToPlayer = new HashMap<String,Player>();
	
	public String getName() {
		return json.optString("name");
	}
	
	public String getId() {
		return json.optString("id");
	}
	
	public boolean isAdult() {
		return json.optBoolean("is_adult");
	}
	
	public List<Player> getPlayers() {	
		JSONArray _players = json.optJSONArray("players");
		if(_players==null) {
			return Collections.emptyList();
		}
		List<Player> players = new ArrayList<Player>(_players.length());
		for(int i = 0; i < _players.length(); i++) {
			players.add(new Player(_players.optJSONObject(i)));
		}
		return players;
	}

	public Room(JSONObject json) {
		super();
		this.json = json;
		for(Player p : getPlayers()) {
			playerIdToPlayer.put(p.getId(), p);
		}
	}
	
	public Player getPlayer(String playerId) {
		return playerIdToPlayer.get(playerId);
	}
	
}
