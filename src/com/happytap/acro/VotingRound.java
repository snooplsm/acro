package com.happytap.acro;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class VotingRound {

	List<Acronym> acronyms;
	
	public VotingRound(JSONObject obj) {
		JSONArray acros = obj.optJSONArray("acronyms");
		acronyms = new ArrayList<Acronym>(acros.length());
		for(int i = 0; i <acros.length(); i++) {
			Acronym acro = new Acronym(acros.optJSONObject(i));
			acronyms.add(acro);
		}
	}
	
}
