package com.wanderphone.douying;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobclick.android.MobclickAgent;
import com.wanderphone.getxml.MovieSubject;
import com.wanderphone.getxml.NetUtil;
import com.wanderphone.myAdapter.ClassicListAdapter;
import com.wanderphone.myAdapter.NowPlayingListAdapter;
import com.wanderphone.sqlite.DataBaseAdapter;

/**
 * @ClassName: BestOfDoubanActivity
 * @Description: 显示豆瓣250
 * @author：
 * @version：v1.0
 */
public class BestOfDoubanActivity extends BaseListActivity {
	private List<MovieSubject> movies = new ArrayList<MovieSubject>();
	DataBaseAdapter db_adapter = new DataBaseAdapter(this);
	SQLiteDatabase db;
	boolean flag = true;// 是否有网络 标识
	private ShowBestDoubanList show_best_list;
	boolean flg = true;// listview首行显示 刷新 标识

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		db_adapter.open();
		boolean isExists = db_adapter.if_best_exists();// 数据库是否存在
		boolean isConnecting = isConnecting();// 网络连接是否正常
		
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

		setContentView(R.layout.movie_list);

		if (isExists && isConnecting) {
			addListHeaderView();
			movies = db_adapter.loadNpData();
			setListAdapter(new ClassicListAdapter(BestOfDoubanActivity.this,
					getListView(), movies));
			flag = false;
			showListAsyncTask();
		} else if (!isExists && isConnecting) {
			showListAsyncTask();
		} else if (isExists && !isConnecting) {
			addListHeaderView();
			movies = db_adapter.loadNpData();
			setListAdapter(new ClassicListAdapter(BestOfDoubanActivity.this,
					getListView(), movies));
			Toast.makeText(BestOfDoubanActivity.this,
					getResources().getString(R.string.network_failed),
					Toast.LENGTH_SHORT).show();
		} else {
			addListHeaderView();
			setListAdapter(new ClassicListAdapter(BestOfDoubanActivity.this,
					getListView(), movies));
			Toast.makeText(BestOfDoubanActivity.this,
					getResources().getString(R.string.network_failed),
					Toast.LENGTH_SHORT).show();
		} /*
		 * 正在放映List ClickListener
		 */
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (flag && position != 0) {
					MovieSubject subject = movies.get(position - 1);
					Intent i = new Intent(BestOfDoubanActivity.this,
							MovieSubjectView.class);
					i.putExtra("subject", subject);
					startActivity(i);
				} else if (position == 0) {
					showListAsyncTask();
				} else if (!flag && position != 0 && isConnecting()) {
					Toast.makeText(BestOfDoubanActivity.this,
							getResources().getString(R.string.load_info),
							Toast.LENGTH_SHORT).show();

				} else if (!flag && position != 0 && !isConnecting()) {
					Toast.makeText(BestOfDoubanActivity.this,
							getResources().getString(R.string.network_failed),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private class ShowBestDoubanList extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				movies = NetUtil.getBestOfDoubanMovie();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			closeProgressBar();
			if (!flg) {
				textview.setText(R.string.app_refresh);
			}
			if (result) {
				addListHeaderView();
				setListAdapter(new ClassicListAdapter(
						BestOfDoubanActivity.this, getListView(), movies));
				flag = true;
				saveData();
			} else {
				if (isConnecting()) {
					Toast.makeText(BestOfDoubanActivity.this,
							getResources().getString(R.string.load_failed),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(BestOfDoubanActivity.this,
							getResources().getString(R.string.network_failed),
							Toast.LENGTH_SHORT).show();
				}
				flag = false;
				if (db_adapter.if_best_exists()) {
					addListHeaderView();
					movies = db_adapter.loadBestData();
					setListAdapter(new ClassicListAdapter(
							BestOfDoubanActivity.this, getListView(), movies));
				} else {
					addListHeaderView();
					setListAdapter(new ClassicListAdapter(
							BestOfDoubanActivity.this, getListView(), movies));
				}
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressBar();
			if (!flg) {
				textview.setText(R.string.nulls);
			}
		}

	}

	private TextView textview;
	private String name;
	private byte[] img;

	private void saveData() {
		db_adapter.delete_all_best();
		for (MovieSubject ms : movies) {
			name = ms.getTitle();
			img = ms.get_img_bytes();
			db_adapter.insertBestData(name, img);
		}
	}

	private View buildHeader() {
		textview = new TextView(this);
		textview.setText(R.string.app_refresh);
		textview.setTextColor(0xfffff7ff);
		textview.setTextSize(20);

		float fDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());  
		int iDip = Math.round(fDip);  
		textview.setWidth(iDip);
		textview.setHeight(iDip);
		textview.setGravity(Gravity.CENTER);
		textview.setBackgroundResource(R.drawable.listviewselector);

		return (textview);
	}

	private void showListAsyncTask() {
		show_best_list = new ShowBestDoubanList();
		show_best_list.execute();
	}

	private void addListHeaderView() {
		if (flg) {
			getListView().addHeaderView(buildHeader());
			flg = false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			super.openOptionsMenu();
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (isExit == false) {
				isExit = true;
				Toast.makeText(this,
						getResources().getString(R.string.app_quit),
						Toast.LENGTH_SHORT).show();
				if (!hasTask) {
					tExit.schedule(task, 5000);
				}
			} else {
				finish();
				System.exit(0);
			}
		}
		return true;
	}

	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	Timer tExit = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (movies.size() != 0) {
			saveData();
		}
		db_adapter.close();
		super.onDestroy();
	}
}
