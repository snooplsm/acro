package com.happytap.acro;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AcroAdapter extends BaseAdapter {

	private Context context;

	private String myUserId = "1";

	private String myVote = "2";
	
	private State state = State.VOTING;

	private Handler handler = new Handler();
	
	public enum State {
		VOTING,
		RESULTS
	}

	private Runnable notifyData = new Runnable() {
		public void run() {
			notifyDataSetChanged();
		};
	};

	public void setData(String myUserId, String myVote, List<Acronym> acronyms, State state) {
		this.myUserId = myUserId;
		this.myVote = myVote;
		this.acronyms = acronyms;
		this.state = state;
		handler.post(notifyData);
	}

	public AcroAdapter(Context context) {
		this.context = context;

	}

	private List<Acronym> acronyms = new ArrayList<Acronym>();

	@Override
	public int getCount() {
		return acronyms.size();
	}

	@Override
	public Acronym getItem(int arg0) {
		return acronyms.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.vote_list_item, null);
		} else {
			view = convertView;
		}
		ImageView fav = (ImageView) view.findViewById(R.id.voted_for);
		TextView index = (TextView) view.findViewById(R.id.index);
		TextView text = (TextView) view.findViewById(R.id.text);
		TextView username = (TextView) view.findViewById(R.id.username);
		
		index.setText(String.valueOf(position + 1));
		Acronym acro = acronyms.get(position);
		username.setText(acro.getUsername());
		text.setText(acro.getText());
		fav.setImageResource(android.R.drawable.star_big_on);
		if (myUserId.equals(acro.getUserId())) {
			text.setTextColor(Color.RED);
			fav.setImageDrawable(null);
		} else {
			if (acro.getUserId().equals(myVote)) {
				text.setTextColor(Color.GREEN);
			} else {
				text.setTextColor(Color.WHITE);
				fav.setImageDrawable(null);
			}
		}
		
		if(State.VOTING==state) {
			username.setVisibility(View.GONE);
		}
		if(State.RESULTS==state) {
			username.setVisibility(View.VISIBLE);
		}

		return view;
	}

}
