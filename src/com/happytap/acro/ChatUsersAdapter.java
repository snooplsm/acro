package com.happytap.acro;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.happytap.acro.models.User;

public class ChatUsersAdapter extends BaseAdapter {

	private Handler handler = new Handler();
	
	private List<User> users = new ArrayList<User>();
	
	private Room room;
	
	private LayoutInflater inf;
	
	public ChatUsersAdapter(Context context) {
		inf = LayoutInflater.from(context);
	}
	
	
	private Runnable notifyData = new Runnable() {
		public void run() {
			notifyDataSetChanged();
		};
	};
	
	public void setData(Room room, List<User> users) {
		this.room = room;
		this.users.addAll(users);
		handler.post(notifyData);
	}

	
	@Override
	public int getCount() {
		return users.size();
	}

	@Override
	public Object getItem(int arg0) {
		return users.get(arg0);
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
		User user = users.get(position);
		TextView name = (TextView) view.findViewById(R.id.chat_user_name);
		name.setText(user.firstName);
		return view;
	}

}
