package com.happytap.acro;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {

	private Handler handler = new Handler();
	
	private List<ChatMessage> messages = new ArrayList<ChatMessage>();
	
	private Room room;
	
	private LayoutInflater inf;
	
	public ChatAdapter(Context context) {
		inf = LayoutInflater.from(context);
	}
	
	
	private Runnable notifyData = new Runnable() {
		public void run() {
			notifyDataSetChanged();
		};
	};
	
	public void setData(Room room, List<ChatMessage> messages) {
		this.room = room;
		this.messages.addAll(messages);
		handler.post(notifyData);
	}

	
	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int arg0) {
		return messages.get(arg0);
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
			view = inf.inflate(R.layout.chat_line, null);
		} else {
			view = convertView;
		}
		ChatMessage message = messages.get(position);
		TextView name = (TextView) view.findViewById(android.R.id.text1);
		//room.getPl
		final String username;
		Player player = room.getPlayer(message.getFromId());
		if(player==null) {
			username = "unknown";
		} else {
			username = player.getName();
		}
		name.setText(username + "> " + message.getMsg());
		return view;
	}

}
