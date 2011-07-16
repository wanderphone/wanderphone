package com.wanderphone.minesweep;

//import com.minesweep.R;

import com.mobclick.android.MobclickAgent;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class HelpActivity extends Activity {
	private TextView help_title;
	private TextView help_content;
	private TextView help_title2;
	private TextView help_content2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.helpactivity);
		ScrollView sView = (ScrollView) findViewById(R.id.ScrollView);
		sView.setVerticalScrollBarEnabled(false);
		sView.setHorizontalScrollBarEnabled(false);

		help_title = (TextView) findViewById(R.id.help_title);
		help_content = (TextView) findViewById(R.id.help_content);

		help_title2 = (TextView) findViewById(R.id.help_title2);
		help_content2 = (TextView) findViewById(R.id.help_content2);

		help_title.setText(getResources().getText(R.string.help_title));
		help_content.setText(getResources().getText(R.string.help_content));

		help_title2.setText(getResources().getString(R.string.help_title_two));
		help_content2.setText(getResources().getString(R.string.help_content_two));

	}
	public void onConfigurationChanged(Configuration newConfig) { 
        super.onConfigurationChanged(newConfig); 
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { 
                // land do nothing is ok 
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { 
                // port do nothing is ok 
        } 
}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
