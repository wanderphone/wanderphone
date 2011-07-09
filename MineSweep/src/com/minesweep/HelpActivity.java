package com.minesweep;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class HelpActivity extends Activity {
	private TextView help_title;
	private TextView help_content;
	private ImageButton bt_next;
	private ImageButton bt_back;
	private Boolean flag=true;
	private String string1,string2,string3,string4,string5,string6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.helpactivity);
		help_title = (TextView) findViewById(R.id.help_title);
		help_content = (TextView) findViewById(R.id.help_content);
		bt_next = (ImageButton) findViewById(R.id.help_bt1);
		bt_back = (ImageButton) findViewById(R.id.help_bt2);
		string1=getResources().getString(R.string.bt_front);
		string2=getResources().getString(R.string.bt_next);
		string3=getResources().getString(R.string.help_content);
		string4=getResources().getString(R.string.help_content_two);
		string5=getResources().getString(R.string.help_title);
		string6=getResources().getString(R.string.help_title_two);
		bt_next.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
		bt_back.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
		help_title.setText(getResources().getText(R.string.help_title));
		help_content.setText(getResources().getText(R.string.help_content));
		bt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HelpActivity.this.finish();
			}
		});
		bt_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(flag)
				{
					bt_next.setBackgroundResource(R.drawable.button_last_click);
					help_content.setText(string4);
					help_title.setText(string6);
					flag=false;
				}
				else
				{
					bt_next.setBackgroundResource(R.drawable.button_next_click);
					help_content.setText(string3);
					help_title.setText(string5);
					flag=true;
				}
			}
			
		});

	}

}
