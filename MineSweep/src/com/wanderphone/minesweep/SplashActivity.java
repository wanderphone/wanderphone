package com.wanderphone.minesweep;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

//import com.minesweep.R;
import com.wanderphone.minesweep.xmlparse.HttpClientConnector;
import com.wanderphone.minesweep.xmlparse.SplashMessage;
import com.wanderphone.minesweep.xmlparse.SplashMessageParse;

public class SplashActivity extends Activity{
	int flag = 0;
	SplashMessage splashMessage = new SplashMessage();
	String uniqueId;
	String easyTime;
	String easyRank;
	String normalTime;
	String normalRank;
	String hardTime;
	String hardRank;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);    
        final String tmDevice, tmSerial, androidId;    
        tmDevice = "" + tm.getDeviceId();    
        tmSerial = "" + tm.getSimSerialNumber();    
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);    
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());    
        uniqueId = deviceUuid.toString();
	//	String splashMessageParameter = 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去标题栏、状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.splash);
		
		SelectDataOnInternet();
		
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(flag == 0)
				{
					Intent mainIntent = null;
					mainIntent = new Intent(SplashActivity.this,
							MainActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
				else if(flag ==1)
				{
					Intent mainIntent = null;
					mainIntent = new Intent(SplashActivity.this,
							RegisterActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
			}
			
		}, 1500);
	}
	private void SelectDataOnInternet(){
		new AsyncTask<Void, Void, Boolean>(){

			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				try{
					final String splashMessageUrl = getResources().getString(R.string.websit) + "?phone_id=" + uniqueId + "&which_use=1";
					Log.v("splashMessageUrl",splashMessageUrl);
					String splashReturnMessage = HttpClientConnector.getStringByUrl(splashMessageUrl);
					if(splashReturnMessage != ""&&splashReturnMessage!=null)
					{
						splashMessage = SplashMessageParse.parse(splashReturnMessage);
						if(splashMessage != null)
						return true;
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				return false;
			}
			protected void onPostExecute(Boolean result){
				super.onPostExecute(result);
				if(result){
					if(splashMessage.getIsRegister().equals("no")||splashMessage.getIsRegister()==null)
						flag = 1;
					else if(splashMessage.getIsRegister().equals("yes"))
					{
						if(splashMessage.getEasyRank().equals("-1"))			
							easyRank = "您还没有玩过这个难度";
						else
							easyRank = "第"+splashMessage.getEasyRank()+"名";
						
						if(splashMessage.getNormalRank().equals("-1"))			
							normalRank = "您还没有玩过这个难度";
						else
							normalRank = "第"+splashMessage.getNormalRank()+"名";
						
						if(splashMessage.getHardRank().equals("-1"))			
							hardRank = "您还没有玩过这个难度";
						else
							hardRank = "第"+splashMessage.getHardRank()+"名";
						
						Toast.makeText(SplashActivity.this, "您目前在全国的游戏排名情况：\n简单难度："+easyRank+
								"；\n中等难度："+normalRank+"；\n困难难度："+hardRank, Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(SplashActivity.this, R.string.failed_connect, Toast.LENGTH_LONG)
								.show();
				}
			}
			
		}.execute();
	}
}
