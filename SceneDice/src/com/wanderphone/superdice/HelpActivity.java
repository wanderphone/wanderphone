package com.wanderphone.superdice;

import com.adview.AdViewLayout;
import com.adview.AdViewTargeting;
import com.adview.AdViewTargeting.RunMode;
import com.adview.AdViewTargeting.UpdateMode;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

public class HelpActivity extends Activity

{
	
	public void onCreate(Bundle savedInstanceState)
	{
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.helpactivity);
        LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout1);
        // 下面两行仅仅用于调试，发布前注释掉
        
       // AdViewTargeting.setTestMode(true);
        AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME); 
        AdViewTargeting.setRunMode(RunMode.TEST);       
        AdViewLayout adViewLayout = new AdViewLayout(this, "SDK201109193907596kewgolasc3c7uf");
        RelativeLayout.LayoutParams adViewLayoutParams = new
        RelativeLayout.LayoutParams(LayoutParams. FILL_PARENT, LayoutParams. WRAP_CONTENT);
        layout.addView(adViewLayout, adViewLayoutParams);
        layout.invalidate();
	
		
		
	}
}