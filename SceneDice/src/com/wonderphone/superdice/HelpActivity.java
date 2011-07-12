package com.wonderphone.superdice;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class HelpActivity extends Activity

{
	
	public void onCreate(Bundle savedInstanceState)
	{
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.helpactivity);
     
	
		
		
	}
}