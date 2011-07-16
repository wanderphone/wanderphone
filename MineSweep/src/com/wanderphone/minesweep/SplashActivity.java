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
//import com.mobclick.android.MobclickAgent;
import com.wanderphone.minesweep.xmlparse.HttpClientConnector;
import com.wanderphone.minesweep.xmlparse.SplashMessage;
import com.wanderphone.minesweep.xmlparse.SplashMessageParse;

public class SplashActivity extends Activity {
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
		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		uniqueId = deviceUuid.toString();
		// String splashMessageParameter =
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题栏、状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.splash);

		SelectDataOnInternet();
	}

	private void SelectDataOnInternet() {
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				try {
					final String splashMessageUrl = getResources().getString(
							R.string.websit)
							+ "?phone_id=" + uniqueId + "&which_use=1";
					Log.v("splashMessageUrl", splashMessageUrl);
					String splashReturnMessage = HttpClientConnector
							.getStringByUrl(splashMessageUrl);
					if (splashReturnMessage != ""
							&& splashReturnMessage != null) {
						splashMessage = SplashMessageParse
								.parse(splashReturnMessage);
						if (splashMessage != null)
							return true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}

			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					if (splashMessage.getIsRegister().equals("no")
							|| splashMessage.getIsRegister() == null) {
						Intent in = new Intent();
						in.setClass(SplashActivity.this, RegisterActivity.class);
						startActivity(in);
						SplashActivity.this.finish();
					} else if (splashMessage.getIsRegister().equals("yes")) {
						if (splashMessage.getEasyRank().equals("-1"))
							easyRank = getResources().getString(
									R.string.string_no_play);
						else
							easyRank = "第" + splashMessage.getEasyRank() + "名";

						if (splashMessage.getNormalRank().equals("-1"))
							normalRank = getResources().getString(
									R.string.string_no_play);
						else
							normalRank = "第" + splashMessage.getNormalRank()
									+ "名";

						if (splashMessage.getHardRank().equals("-1"))
							hardRank = getResources().getString(
									R.string.string_no_play);
						else
							hardRank = "第" + splashMessage.getHardRank() + "名";

						Toast.makeText(
								SplashActivity.this,
								getResources().getString(
										R.string.string_rank_prompt)
										+ "\n"
										+ getResources().getString(
												R.string.string_easy)
										+ easyRank
										+ "；\n"
										+ getResources().getString(
												R.string.string_normal)
										+ normalRank
										+ "；\n"
										+ getResources().getString(
												R.string.string_hard)
										+ hardRank, Toast.LENGTH_LONG).show();
						Intent in = new Intent();
						in.setClass(SplashActivity.this, MainActivity.class);
						startActivity(in);
						SplashActivity.this.finish();
					}
				} else {
					Toast.makeText(SplashActivity.this,
							R.string.failed_connect, Toast.LENGTH_LONG).show();
					Intent in = new Intent();
					in.setClass(SplashActivity.this, MainActivity.class);
					startActivity(in);
					SplashActivity.this.finish();
				}
			}

		}.execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//MobclickAgent.onPause(SplashActivity.this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//MobclickAgent.onResume(SplashActivity.this);
	}
}
