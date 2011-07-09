package com.android.douying;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.mobclick.android.MobclickAgent;

public class SplashScreen extends Activity {

	public ProgressDialog myDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);

		new Handler().postDelayed(new Runnable() {
			public void run() {

				Intent mainIntent = null;
				mainIntent = new Intent(SplashScreen.this,
						MainTabActivity.class);
				SplashScreen.this.startActivity(mainIntent);
				SplashScreen.this.finish();

			}
		}, 1500); 
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
