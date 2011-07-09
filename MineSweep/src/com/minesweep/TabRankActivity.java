package com.minesweep;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.TabHost;
import android.widget.TextView;

public class TabRankActivity extends TabActivity {
	private TabHost tab_host;
	private TextView tv;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private String string1,string2,string3;
	int currentView = 0;
	private static int maxTabIndex = 2;
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.tabrankactivity);
		string1=this.getResources().getString(R.string.select_easy);
		string2=this.getResources().getString(R.string.select_normal);
		string3=this.getResources().getString(R.string.select_hard);
		TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(string1)
				.setContent(new Intent(this, EasyRankActivity.class)));

		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(string2)
				.setContent(new Intent(this, NormalRankActivity.class)));

		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(string3)
				.setContent(new Intent(this, HardRankActivity.class)));		
		tabHost.setCurrentTab(0);
		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		}; 			
    }
    class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			TabHost tabHost = getTabHost();
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i("test ", "right");
					if (currentView == maxTabIndex) {
						currentView = 0;
					} else {
						currentView++;
					}
					tabHost.setCurrentTab(currentView);					
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					Log.i("test ", "left");
					if (currentView == 0) {
						currentView = maxTabIndex;
					} else {
						currentView--;
					}
					tabHost.setCurrentTab(currentView);					
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}


	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		if(gestureDetector.onTouchEvent(event)){
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		
		return super.dispatchTouchEvent(event);		
	}
}
