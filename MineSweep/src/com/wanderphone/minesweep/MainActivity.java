package com.wanderphone.minesweep;

//import com.minesweep.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private ImageButton bt_set;
	

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
		bt_set = (ImageButton) findViewById(R.id.bt_set);
		
	}

	private void setListeners() {
		bt_single.setOnClickListener(cal_single);
		bt_multiple.setOnClickListener(cal_multiple);
		bt_rank.setOnClickListener(cal_rank);
		bt_help.setOnClickListener(cal_help);
		bt_about.setOnClickListener(cal_about);
		bt_set.setOnClickListener(cal_set);
		
	}

	
	// 单机游戏按钮监听
	private ImageButton.OnClickListener cal_single = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			SharedPreferences sharedPreferences1=getSharedPreferences("the_gameflag",MODE_PRIVATE);        
	        int difficultyflag=sharedPreferences1.getInt("gameflag", 1);
	        //简单难度
			if(difficultyflag==1)
			{
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, SingleActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("numberOfRowsInMineField", 9);
				bundle.putInt("numberOfColumnsInMineField",9);
				bundle.putInt("totalNumberOfMines", 10);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
			//中等难度
			if(difficultyflag==2)
			{
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, SingleActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("numberOfRowsInMineField", 16);
				bundle.putInt("numberOfColumnsInMineField", 16);
				bundle.putInt("totalNumberOfMines", 40);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
			//困难难度
			if(difficultyflag==3)
			{
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, SingleActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("numberOfRowsInMineField", 20);
				bundle.putInt("numberOfColumnsInMineField", 20);
				bundle.putInt("totalNumberOfMines", 60);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
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
	// 设置监听
	private ImageButton.OnClickListener cal_set = new ImageButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SetActivity.class);
			startActivity(intent);
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
				.setTitle(R.string.dialog_sure_quit)
				.setIcon(android.R.drawable.ic_menu_help)
				.setPositiveButton(R.string.bt_sure, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						finish();
					}
				})
				.setNeutralButton(R.string.bt_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}).show();
	}
}