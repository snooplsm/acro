package com.happytap.acro;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class ChatPageAdapter extends PagerAdapter {

	@Override
	public int getCount() {
		return 2;
	}
	
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);

    }

    @Override
    public Parcelable saveState() {
        return null;
    }
    
    @Override
    public Object instantiateItem(View container, int position) {
    	LayoutInflater inflater = LayoutInflater.from(container.getContext());
    	int view;
    	if(position==0) {
    		view = R.layout.chat;
    	} else {
    		view = R.layout.chat;
    	}
    	View v = inflater.inflate(view, null);
    	
    	((ViewPager)container).addView(v,0);
    	return view;
    }

}
