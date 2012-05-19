package com.happytap.acro;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.facebook.android.Facebook;
import com.happytap.acro.models.User;

public class Configuration {

  public static User me = new User();
  
  private Context ctx;

  public Configuration(Context ctx) {
    this.ctx = ctx;
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

  public void setFacebook(Facebook facebook) {
    Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
    editor.putLong("fb_expires", facebook.getAccessExpires());
    editor.putString("fb_token", facebook.getAccessToken());
    editor.commit();
  }

}
