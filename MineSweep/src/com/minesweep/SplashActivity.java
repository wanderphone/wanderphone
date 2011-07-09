package com.minesweep;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.xmlparse.HttpClientConnector;
import com.xmlparse.SplashMessage;
import com.xmlparse.SplashMessageParse;

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
			
		}, 5000);
	}
	private void SelectDataOnInternet(){
		new AsyncTask<Void, Void, Boolean>(){

			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				try{
					final String splashMessageUrl = "http://10.0.2.2:8080/Test?phone_id="+uniqueId+"&which_use=1";
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
						easyTime = splashMessage.getEasyTime();
						easyRank = splashMessage.getEasyRank();
						normalTime = splashMessage.getNormalTime();
						normalRank = splashMessage.getNormalRank();
						hardTime = splashMessage.getHardTime();
						hardRank = splashMessage.getHardRank();
						Toast.makeText(SplashActivity.this, "您目前在全国的游戏排名情况：\n简单难度：第"+easyRank+
								"名；\n中等难度：第"+normalRank+"名；\n困难难度：第"+hardRank+"名", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(SplashActivity.this, "网络数据加载失败", Toast.LENGTH_LONG)
								.show();
				}
			}
			
		}.execute();
	}
}
