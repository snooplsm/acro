package com.happytap.acro;

import org.json.JSONObject;

public class Round {

	String acronym;
	String category;
	
	public Round(JSONObject obj) {
		
		this.acronym = obj.optString("acronym");
		this.category = obj.optString("category");
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
}
