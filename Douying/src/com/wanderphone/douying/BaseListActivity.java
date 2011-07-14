package com.wanderphone.douying;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.google.gdata.client.douban.DoubanService;
import com.mobclick.android.MobclickAgent;
import com.wanderphone.getxml.NetUtil;

public class BaseListActivity extends ListActivity {

	private DoubanService doubanService = NetUtil.getDoubanService();

	public DoubanService getDoubanService() {
		return doubanService;
	}

	public void setDoubanService(DoubanService doubanService) {
		this.doubanService = doubanService;
	}
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedata = getSharedPreferences("data", 0);
		String accessToken = sharedata.getString("accessToken", null);
		String tokenSecret = sharedata.getString("tokenSecret", null);
		String uid = sharedata.getString("uid", null);
		doubanService.setAccessToken(accessToken, tokenSecret);
		NetUtil.setUid(uid);
		 MobclickAgent.onResume(this);

	}
	public void onPause() {
		  super.onPause();
		  MobclickAgent.onPause(this);
		}
	// 退出
	protected void doExit() {
		new AlertDialog.Builder(BaseListActivity.this)
				.setTitle("提示")
				.setMessage("确定要退出豆影吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						finish();
					}
				})
				.setNeutralButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}).show();

	}
	public boolean isConnecting() {
		ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mTelephony = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);

		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return false;
		}

		int netType = info.getType();
		int netSubtype = info.getSubtype();

		if (netType == ConnectivityManager.TYPE_WIFI) {
			return info.isConnected();
		} else if (netType == ConnectivityManager.TYPE_MOBILE
				&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
				&& !mTelephony.isNetworkRoaming()) {
			return info.isConnected();
		} else {
			return false;
		}
	}

	public void showProgressBar() {
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(500);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(500);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
		loading.setVisibility(View.VISIBLE);
		loading.setLayoutAnimation(controller);
	}

	public void closeProgressBar() {

		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(500);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
		animation.setDuration(500);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);

		loading.setLayoutAnimation(controller);

		loading.setVisibility(View.INVISIBLE);
	}
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 0, 0, R.string.app_feedback).setIcon(android.R.drawable.ic_menu_send);
		menu.add(0, 1, 1, R.string.app_about).setIcon(android.R.drawable.ic_menu_info_details);

		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId())
		{
			case 0:
				MobclickAgent.openFeedbackActivity(this);
				break;
			case 1:
				Intent intent = new Intent();
				intent.setClass(BaseListActivity.this, About.class);
				startActivity(intent);
				break;
		}
		
		return true;
	}
	
	
}
