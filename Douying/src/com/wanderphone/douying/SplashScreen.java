package com.wanderphone.douying;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.adview.AdViewLayout;
import com.adview.AdViewManager;
import com.adview.AdViewTargeting;
//import com.adview.R;
import com.adview.AdViewTargeting.RunMode;
import com.adview.AdViewTargeting.UpdateMode;
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

        LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);
        if (layout == null) 
            return;
        /*����}��ֻ���ڲ���,��ɺ�һ��Ҫȥ��,�ο��ĵ�˵��*/
        AdViewManager.setConfigExpireTimeout(-1);
        AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME); //��֤ÿ�ζ��ӷ�����ȡ����
        AdViewTargeting.setRunMode(RunMode.NORMAL);         //��֤����ѡ�еĹ�湫˾��Ϊ����״̬ 
      //  AdViewTargeting.setTestMode(true);
        AdViewLayout adViewLayout = new AdViewLayout(SplashScreen.this, "SDK20110919400711zhdlaagi2x153px");
        RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        layout.addView(adViewLayout, adViewLayoutParams);
        layout.invalidate();

		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = null;
				mainIntent = new Intent(SplashScreen.this,
						MainTabActivity.class);
				SplashScreen.this.startActivity(mainIntent);
				SplashScreen.this.finish();

			}
		}, 2000); 
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
