package com.happytap.acro.models;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
  
  public String id = UUID.randomUUID().toString();
  public String fbid;
  public String voteForAcronymId = "";
  public String name = "";
  public String firstName = "Anonymous";
  public String avatarUrl;

  public String avatarUrl() {
    return "http://graph.facebook.com/" + fbid + "/picture?type=large";
  }
  
  public static User parseJson(String response) {
    User ret = new User();
    
    try {
      JSONObject json = new JSONObject(response);
      ret.name = json.getString("name");
      ret.firstName = json.getString("first_name");
      ret.fbid = json.getString("id");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    
    return ret;
  }
  
}
