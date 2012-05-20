package com.happytap.acro;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.facebook.android.Facebook;
import com.happytap.acro.models.Player;

public class Configuration {

  public static Player me = new Player();
  
  private Context ctx;
  
  private SharedPreferences prefs;

  public Configuration(Context ctx) {
    this.ctx = ctx;
    this.prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
  }

  public boolean isTest() {
    return ctx.getResources().getBoolean(R.bool.isTest);
  }

  public int getSecondsPerRound() {
    return ctx.getResources().getInteger(R.integer.secondsPerRound);
  }
  
  public int getSecondsPerChooseCategory() {
	  return ctx.getResources().getInteger(R.integer.secondsPerChooseCategoryRound);
  }

  public int getSecondsPerVotingRound() {
    return ctx.getResources().getInteger(R.integer.secondsPerVotingRound);
  }

  public int getSecondsPerResultsRound() {
    return ctx.getResources().getInteger(R.integer.secondsPerResultsRound);
  }

  public String getServerUrl() {
	if(!TextUtils.isEmpty(prefs.getString("server_url", null))) {
		return prefs.getString("server_url", null);
	}
    return ctx.getResources().getString(R.string.serverUrl);
  }

  public String getFacebookAppId() {
    return ctx.getResources().getString(R.string.facebookAppId);
  }

  public String getFacebookAppSecret() {
    return ctx.getResources().getString(R.string.facebookAppSecret);
  }

  public Facebook getFacebook() {
    return null;
  }

  public boolean isLoggingEnbled() {
    return ctx.getResources().getBoolean(R.bool.log);
  }

}
