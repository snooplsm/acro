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
  public String voteForAcronymId = "";
  public String name = "";
  public String firstName = "Anonymous";
  public String avatarUrl;

  public String avatarUrl() {
    return "http://graph.facebook.com/" + id + "/picture?type=large";
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

    try {
      ret.name = data.getString("name");
      ret.firstName = data.getString("username");
      ret.id = data.getString("user_id");
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return ret;
  }
}
