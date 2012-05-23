package com.happytap.acro;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import net.londatiga.android.TwitterApp;
import net.londatiga.android.TwitterApp.TwDialogListener;

import org.json.JSONObject;

import twitter4j.User;
import twitter4j.UserJSONImpl;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happytap.acro.models.Player;

public class LoginActivity extends Activity implements OnClickListener {

	Facebook facebook;
	TwitterApp twitter;
	AsyncFacebookRunner mAsyncRunner;
	Configuration configuration;

	private ProgressDialog progressDialog;

	private SharedPreferences mPrefs;
	
	View loginFacebook,loginTwitter;
	
	Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		loginFacebook = findViewById(R.id.login_facebook);
		loginTwitter = findViewById(R.id.login_twitter);
		loginFacebook.setOnClickListener(this);
		loginTwitter.setOnClickListener(this);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		configuration = new Configuration(this);
		if(mPrefs.contains("me")) {
			try {
				Configuration.me = Player.parseFacebookJson(mPrefs.getString("me",null));
				startAcroActivity();
				return;
			} catch (Exception e) {
				
			}
		}
		if(mPrefs.contains("twitter")) {
			try {
				Configuration.me = Player.parseTwitter(gson.fromJson(mPrefs.getString("twitter", null),TwitterUser.class));
				Player p = Configuration.me;
				startAcroActivity();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		facebook = new Facebook(configuration.getFacebookAppId());
		twitter = new TwitterApp(this, configuration.getTwitterKey(), configuration.getTwitterSecret());
		twitter.setListener(twitterListener);
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
	
	TwDialogListener twitterListener = new TwDialogListener() {
	
		
		public void onComplete(String value) {
			
		};
		
		public void onUser(User user) {
			mPrefs.edit().putString("twitter", gson.toJson(user)).commit();
			LoginActivity.this.startAcroActivity();
			
		}
		
		public void onError(String value) {
			if(!TextUtils.isEmpty(value)) {
				Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT);
			}
		};
	};

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

	@Override
	public void onClick(View v) {
		if(v==loginFacebook) {
			onFacebookLoginClick(v);
		}else
		if(v==loginTwitter) {
			twitter.authorize();
		}
	}

}
