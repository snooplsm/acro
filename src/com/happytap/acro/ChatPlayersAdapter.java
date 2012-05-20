package com.happytap.acro;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.happytap.acro.models.Player;

public class ChatPlayersAdapter extends BaseAdapter {

	private Handler handler = new Handler();
	
	private List<Player> players = new ArrayList<Player>();
	
	private Room room;
	
	private LayoutInflater inf;
	
	public ChatPlayersAdapter(Context context) {
		inf = LayoutInflater.from(context);
	}
	
	
	private Runnable notifyData = new Runnable() {
		public void run() {
			notifyDataSetChanged();
		};
	};
	
	public void setData(Room room) {
		this.room = room;
		this.players.clear();
		this.players.addAll(room.getPlayers());
		handler.post(notifyData);
	}

	
	@Override
	public int getCount() {
		return players.size();
	}

	@Override
	public Object getItem(int arg0) {
		return players.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if(convertView==null) {
			view = inf.inflate(R.layout.user_line, null);
		} else {
			view = convertView;
		}
		Player user = players.get(position);
		TextView name = (TextView) view.findViewById(R.id.chat_user_name);
		name.setText(user.firstName + " (" + user.getTotalVoteCount() + ")");
		return view;
	}

}
