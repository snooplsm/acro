package com.happytap.acro;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoomAdapter extends BaseAdapter {

	private List<Room> rooms = Collections.emptyList();
	
	private Handler handler = new Handler();
	
	Context context;
	
	public RoomAdapter(Context context) {
		super();
		this.context = context;
	}

	private Runnable notifyData = new Runnable() {
		public void run() {
			notifyDataSetChanged();
		};
	};
	
	public void clear() {
		rooms = Collections.emptyList();
		
		handler.post(notifyData);
	}
	
	
	public void setData(List<Room> rooms) {
		this.rooms = rooms;
		handler.post(notifyData);
	}
	
	@Override
	public int getCount() {
		return rooms.size();
	}

	@Override
	public Room getItem(int position) {
		return rooms.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null);
		}
		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		Room room = getItem(position);
		text.setText(room.getName());
		TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
		text2.setText(String.valueOf(room.getSize()));
		return convertView;
	}

}
