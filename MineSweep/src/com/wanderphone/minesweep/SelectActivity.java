package com.wanderphone.minesweep;

//import com.minesweep.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class SelectActivity extends Activity {
	//选择难度界面按钮
	private ImageButton select_bt1;
	private ImageButton select_bt2;
	private ImageButton select_bt3;
	
	public void onCreate(Bundle savedInstanceState) {
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectactivity);
        select_bt1=(ImageButton)findViewById(R.id.select_bt1);
        select_bt2=(ImageButton)findViewById(R.id.select_bt2);
        select_bt3=(ImageButton)findViewById(R.id.select_bt3);
        
        
        //简单难度按钮监听
        select_bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(SelectActivity.this, SingleActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("numberOfRowsInMineField", 9);
				bundle.putInt("numberOfColumnsInMineField",9);
				bundle.putInt("totalNumberOfMines", 10);
				intent.putExtras(bundle);
				startActivity(intent);
				SelectActivity.this.finish();
			}
		});
        //中等难度按钮监听
        select_bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(SelectActivity.this, SingleActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("numberOfRowsInMineField", 16);
				bundle.putInt("numberOfColumnsInMineField", 16);
				bundle.putInt("totalNumberOfMines", 40);
				intent.putExtras(bundle);
				startActivity(intent);
				SelectActivity.this.finish();
			}
		});
        //困难难度按钮监听
        select_bt3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(SelectActivity.this, SingleActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("numberOfRowsInMineField", 20);
				bundle.putInt("numberOfColumnsInMineField", 20);
				bundle.putInt("totalNumberOfMines", 60);
				intent.putExtras(bundle);
				startActivity(intent);
				SelectActivity.this.finish();
			}
		});
	}
}