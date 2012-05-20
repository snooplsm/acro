package com.happytap.acro;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class VotingRound {

	List<Acronym> acronyms;
	
	public VotingRound(JSONArray acros) {
		acronyms = new ArrayList<Acronym>(acros.length());
		for(int i = 0; i <acros.length(); i++) {
			Acronym acro = new Acronym(acros.optJSONObject(i));
			acronyms.add(acro);
		}
	}
	
	public VotingRound() {
		acronyms = new ArrayList<Acronym>();
		acronyms.add(new Acronym("ACD",""));
		acronyms.add(new Acronym("DCB","a"));
	}
	
	public List<Acronym> getAcronyms() {
		return acronyms;
	}
	
}
