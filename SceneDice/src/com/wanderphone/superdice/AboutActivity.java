package com.wanderphone.superdice;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AboutActivity extends Activity

{
	
	public void onCreate(Bundle savedInstanceState)
	{
		//全屏显示;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutactivity);
       
		
	}
}