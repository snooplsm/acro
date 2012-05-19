package com.happytap.acro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class LoginActivity extends Activity {

  Facebook facebook = new Facebook("YOUR_APP_ID");

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    facebook.authorize(this, new DialogListener() {
      @Override public void onComplete(Bundle values) {
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

    facebook.authorizeCallback(requestCode, resultCode, data);
  }
  
}
