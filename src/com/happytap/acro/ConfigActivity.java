package com.happytap.acro;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivity extends Activity {

	private EditText serverUrl;
	
	private Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		setContentView(R.layout.config_layout);
		serverUrl = findView(R.id.server_url);
		serverUrl.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(KeyEvent.KEYCODE_ENTER==keyCode && event.getAction()==KeyEvent.ACTION_UP) {
					editor.putString("server_url", serverUrl.getText().toString()).commit();
					Toast.makeText(ConfigActivity.this, serverUrl.getText().toString() + " saved.", Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
			
		});
	}
	

	@SuppressWarnings("unchecked")
	private <T> T findView(int id) {
		return (T) super.findViewById(id);
	}
}
