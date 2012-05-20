package com.happytap.acro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.happytap.acro.models.Player;

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
		JSONObject _players = json.optJSONObject("players");
		if(_players==null) {
			return Collections.emptyList();
		}
		
		List<Player> players = new ArrayList<Player>(_players.length());
		
		Iterator<String> keys = _players.keys();
		
		while (keys.hasNext()) {
		  String id = keys.next();
		  try {
            players.add(Player.parseJson(id, (JSONObject) _players.get(id)));
          } catch (JSONException e) {
            e.printStackTrace();
          }
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

  public CharSequence getHumanizedPlayersCount() {
    if (getPlayers().size() > 1) {
      return "" + getPlayers().size() + " players";
    } else {
      return "" + getPlayers().size() + " player";
    }
  }
	
}
