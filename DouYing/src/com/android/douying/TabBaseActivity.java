package com.android.douying;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

import com.android.getxml.NetUtil;
import com.google.gdata.client.douban.DoubanService;
import com.mobclick.android.MobclickAgent;

public class TabBaseActivity extends TabActivity {

	private DoubanService doubanService = NetUtil.getDoubanService();

	public DoubanService getDoubanService() {
		return doubanService;
	}

	public void setDoubanService(DoubanService doubanService) {
		this.doubanService = doubanService;
	}

	protected void doExit() {
		new AlertDialog.Builder(TabBaseActivity.this)
				.setTitle("提示")
				.setMessage("确定要退出豆瓣客户端吗？")
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

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedata = getSharedPreferences("data", 0);
		String accessToken = sharedata.getString("accessToken", null);
		String tokenSecret = sharedata.getString("tokenSecret", null);
		doubanService.setAccessToken(accessToken, tokenSecret);
		MobclickAgent.onResume(this);

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
