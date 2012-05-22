package com.happytap.acro;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.happytap.acro.models.Player;

public class LoginActivity extends Activity {

	Facebook facebook;
	AsyncFacebookRunner mAsyncRunner;
	Configuration configuration;

	private ProgressDialog progressDialog;

	private SharedPreferences mPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		mPrefs = getPreferences(MODE_PRIVATE);
		configuration = new Configuration(this);
		if(mPrefs.contains("me")) {
			try {
				Configuration.me = Player.parseFacebookJson(mPrefs.getString("me",null));
				startAcroActivity();
				return;
			} catch (Exception e) {
				
			}
		}

		facebook = new Facebook(configuration.getFacebookAppId());
		mAsyncRunner = new AsyncFacebookRunner(facebook);

		

		String access_token = mPrefs.getString("fb_token", null);
		long expires = mPrefs.getLong("fb_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (facebook.isSessionValid()) {
			progressDialog = ProgressDialog.show(LoginActivity.this, "",
					"Loading...");
			mAsyncRunner.request("me", meListener);
		}

	}

	private void startAcroActivity() {
		Intent intent = new Intent(LoginActivity.this, AcroActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		if(progressDialog!=null && progressDialog.getWindow()!=null)			
			progressDialog.dismiss();
		finish();
	}

	public void onFacebookLoginClick(View view) {

		facebook.authorize(this, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				SharedPreferences.Editor editor = mPrefs.edit();
				editor.putString("fb_token", facebook.getAccessToken());
				editor.putLong("fb_expires", facebook.getAccessExpires());
				editor.commit();
				progressDialog = ProgressDialog.show(LoginActivity.this, "",
						"Loading...");
				mAsyncRunner.request("me", meListener);
			}

			@Override
			public void onFacebookError(FacebookError error) {
			}

			@Override
			public void onError(DialogError e) {
			}

			@Override
			public void onCancel() {
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	RequestListener meListener = new RequestListener() {

		@Override
		public void onComplete(String response, Object state) {
			mPrefs.edit().putString("me", response).commit();
			Configuration.me = Player.parseFacebookJson(response);
			startAcroActivity();
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			progressDialog.dismiss();
		}

		@Override
		public void onIOException(IOException e, Object state) {
			progressDialog.dismiss();
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			progressDialog.dismiss();
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			progressDialog.dismiss();
		}

	};

}
