package com.wanderphone.minesweep;

//import com.minesweep.R;

import java.util.Locale;

import com.adview.AdViewLayout;
import com.adview.AdViewTargeting;
import com.adview.AdViewTargeting.AdArea;
import com.adview.AdViewTargeting.RunMode;
import com.adview.AdViewTargeting.UpdateMode;
import com.mobclick.android.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;

public class ServerCilentActivity extends Activity {
	//联机游戏选择按钮
	private ImageButton server_bt1;
	private ImageButton server_bt2;
	private ImageButton server_bt3;
		
	public void onCreate(Bundle savedInstanceState) {
		//全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	
        super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

        setContentView(R.layout.servercilentactivity);
        server_bt1=(ImageButton)findViewById(R.id.server_bt1);
        server_bt2=(ImageButton)findViewById(R.id.server_bt2);
        server_bt3=(ImageButton)findViewById(R.id.server_bt3);
        String string1=getLocaleLanguage();
        
		LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);
        AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME); 
        AdViewTargeting.setRunMode(RunMode.NORMAL);         
      //  AdViewManager.setConfigExpireTimeout(-1);
     //   AdViewTargeting.setUpdateMode(true);
        AdViewTargeting.setAdArea(AdArea.BOTTOM);
        AdViewLayout adViewLayout = new AdViewLayout(this, "SDK20110919390740btotf179h73quve");
        RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        layout.addView(adViewLayout, adViewLayoutParams);
        layout.invalidate();
		if(string1.equals("zh-CN")||string1.equals("zh-TW"))
        {
			
        }
        else
        {
        	server_bt1.setBackgroundResource(R.drawable.create_button_en);
        	server_bt2.setBackgroundResource(R.drawable.join_button_en);
        	server_bt3.setBackgroundResource(R.drawable.back_button_en);
        }
        
        //建立游戏按钮监听
        server_bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(ServerCilentActivity.this, ServerActivity.class);
				startActivity(intent);
			}
		});
        //加入游戏按钮监听
        server_bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(ServerCilentActivity.this, CilentActivity.class);
				startActivity(intent);
			}
		});
        //返回主界面按钮监听
        server_bt3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ServerCilentActivity.this.finish();				
			}
		});
	}
	public void onConfigurationChanged(Configuration newConfig) { 
        super.onConfigurationChanged(newConfig); 
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { 
                // land do nothing is ok 
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { 
                // port do nothing is ok 
        } 
}
	public String getLocaleLanguage() {
		Locale l = Locale.getDefault();
		return String.format("%s-%s", l.getLanguage(), l.getCountry());
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