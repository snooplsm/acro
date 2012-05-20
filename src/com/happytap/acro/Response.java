package com.happytap.acro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Response extends JSONObject {

	public boolean isJoinRoomResponse() {
		return "jr".equals(optString("type"));
	}
	
	//public boolean isRoomList

	public Response(String json) throws JSONException {
		super(json);
	}

	public Room getRoom() {
		return new Room(optJSONObject("data"));
	}

	public boolean isRoomListResponse() {
		return "rl".equals(optString("type"));
	}
	
	public boolean isMessage() {
		return "m".equals(optString("type"));
	}
	
	public boolean isStartRound() {
		return "sr".equals(optString("type"));
	}
	
	public boolean isVotingRound() {
		return "vr".equals(optString("type"));
	}
	
	public VotingRound getVotingRound() {
		return new VotingRound(optJSONObject("data"));
	}
	
	public Round getRound() {
		return new Round(optJSONObject("data"));
	}
	
	public ChatMessage getMessage() {
		return new ChatMessage(optJSONObject("data"));
	}
	
	public List<Room> getRooms() {
		try {
			JSONArray _rooms = getJSONArray("data");
		
		if(_rooms==null) {
			return Collections.emptyList();
		}
		List<Room> rooms = new ArrayList<Room>(_rooms.length());
		for(int i = 0; i < _rooms.length(); i++) {
			Room room = new Room(_rooms.optJSONObject(i));
			rooms.add(room);
		}
		return rooms;
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
}
