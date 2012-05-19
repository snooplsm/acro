package com.happytap.acro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class LoginActivity extends Activity {

  Facebook facebook;
  Configuration configuration;

  private SharedPreferences mPrefs;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);

    configuration = new Configuration(this);

    facebook = new Facebook(configuration.getFacebookAppId());

    mPrefs = getPreferences(MODE_PRIVATE);
    String access_token = mPrefs.getString("access_token", null);
    long expires = mPrefs.getLong("access_expires", 0);
    if (access_token != null) {
      facebook.setAccessToken(access_token);
    }
    if (expires != 0) {
      facebook.setAccessExpires(expires);
    }

    if (facebook.isSessionValid()) {
      startAcroActivity();
    }

  }

  private void startAcroActivity() {
    Intent intent = new Intent(LoginActivity.this, AcroActivity.class);
    startActivity(intent);
  }

  public void onFacebookLoginClick(View view) {

    facebook.authorize(this, new DialogListener() {
      @Override public void onComplete(Bundle values) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("access_token", facebook.getAccessToken());
        editor.putLong("access_expires", facebook.getAccessExpires());
        editor.commit();
        
        startAcroActivity();
      }

      @Override public void onFacebookError(FacebookError error) {
      }

      @Override public void onError(DialogError e) {
      }

      @Override public void onCancel() {
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d("XXX", "onActivityResult: " + data.getExtras());
    facebook.authorizeCallback(requestCode, resultCode, data);
  }

}
