package com.happytap.acro;

import java.util.List;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AlphaAfterAnimation extends AlphaAnimation implements AnimationListener{

	private List<View> next;
	int pos = 0;
	
	public AlphaAfterAnimation(List<View> next) {
		super(0,1);
		this.next = next;
		setAnimationListener(this);
	}
	
	public void startme() {
		pos = 0;
		if(pos<next.size()) {
			next.get(pos).startAnimation(this);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		next.get(pos).setVisibility(View.VISIBLE);
		pos++;
		reset();
		if(pos<next.size()) {			
			next.get(pos).startAnimation(this);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		//next.get(pos).setVisibility(View.VISIBLE);
	}
	
}
