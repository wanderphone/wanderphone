package com.minesweep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */

	// 主菜单按键
	private ImageButton bt_single;
	private ImageButton bt_multiple;
	private ImageButton bt_rank;
	private ImageButton bt_help;
	private ImageButton bt_about;
	private ImageButton bt_quit;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		findView();	
		setListeners();

	}
	//控件初始化	
	private void findView() {
		bt_single = (ImageButton) findViewById(R.id.bt_single);
		bt_multiple = (ImageButton) findViewById(R.id.bt_multiple);
		bt_rank = (ImageButton) findViewById(R.id.bt_rank);
		bt_help = (ImageButton) findViewById(R.id.bt_help);
		bt_about = (ImageButton) findViewById(R.id.bt_about);
		bt_quit = (ImageButton) findViewById(R.id.bt_quit);
		
	}

	private void setListeners() {
		bt_single.setOnClickListener(cal_single);
		bt_multiple.setOnClickListener(cal_multiple);
		bt_rank.setOnClickListener(cal_rank);
		bt_help.setOnClickListener(cal_help);
		bt_about.setOnClickListener(cal_about);
		bt_quit.setOnClickListener(cal_quit);
		bt_single.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
		bt_multiple.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
		bt_rank.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
		bt_help.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
		bt_about.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
		bt_quit.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
	}

	
	// 单机游戏按钮监听
	private ImageButton.OnClickListener cal_single = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SelectActivity.class);
			startActivity(intent);	
		}

	};
	// 联机游戏按钮监听
	private ImageButton.OnClickListener cal_multiple = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ServerCilentActivity.class);
			startActivity(intent);
		}

	};
	// 排行榜按钮监听
	private ImageButton.OnClickListener cal_rank = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, TabRankActivity.class);
			startActivity(intent);
		}
	};
	// 帮助按钮监听
	private ImageButton.OnClickListener cal_help = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, HelpActivity.class);
			startActivity(intent);

		}

	};
	// 关于按钮监听
	private ImageButton.OnClickListener cal_about = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, AboutActivity.class);
			startActivity(intent);
		}

	};
	// 退出按钮监听
	private ImageButton.OnClickListener cal_quit = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			doExit();
		}

	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doExit();
			return true;
		}
		return true;
	}

	protected void doExit() {
		new AlertDialog.Builder(MainActivity.this)
				.setTitle("确认退出扫雷")
				.setIcon(android.R.drawable.ic_menu_help)
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
}