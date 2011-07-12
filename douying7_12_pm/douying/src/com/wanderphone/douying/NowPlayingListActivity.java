package com.wanderphone.douying;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.wanderphone.myAdapter.NowPlayingListAdapter;
import com.wanderphone.sqlite.DataBaseAdapter;

/**
 * @ClassName: NowPlayingList
 * @Description: 显示 正在热映 电影
 * @author：
 * @version：v1.0
 */

public class NowPlayingListActivity extends BaseListActivity {
	private List<MovieSubject> movies = new ArrayList<MovieSubject>();
	DataBaseAdapter db_adapter = new DataBaseAdapter(this);
	SQLiteDatabase db;
	
	boolean flag = true;//是否有网络  标识
	private ShowNowPlayingList show_np_list;
	boolean flg = true;// listview首行显示 刷新 标识

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		db_adapter.open();
		setContentView(R.layout.movie_list);
		if (db_adapter.if_np_exists()) {
			addListHeaderView();
			movies = db_adapter.loadNpData();
			setListAdapter(new NowPlayingListAdapter(
					NowPlayingListActivity.this, getListView(), movies));
			showListAsyncTask();
		} else {
			showListAsyncTask();
		}

		/**
		 * 响应点击 正在放映 ListView 事件，每个item详细信息，跳转到MovieSubjectView.class
		 */
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (flag && position != 0) {
					MovieSubject subject = movies.get(position - 1);
					Intent i = new Intent(NowPlayingListActivity.this,
							MovieSubjectView.class);
					i.putExtra("subject", subject);
					startActivity(i);
				} else if (position == 0) {
					showListAsyncTask();
				} else if (!flag && position != 0) {
					Toast.makeText(NowPlayingListActivity.this, "数据加载失败！",
							Toast.LENGTH_SHORT).show();

				}
			}

		});

	}

	private TextView textview;
	private String name;
	private String type;
	private String duration;
	private String pub_area;
	private String rating;
	private String cinema;
	private byte[] img;

	private void saveData() {
		db_adapter.delete_all_np();
		for (MovieSubject ms : movies) {
			name = ms.getTitle();
			type = ms.getType();
			rating = String.valueOf(ms.getRating());
			duration = ms.get_movie_duration();
			pub_area = ms.get_pub_area();
			cinema = ms.getSummary();
			img = ms.get_img_bytes();
			db_adapter.insertNpData(name, type, duration, pub_area, cinema, rating, img);
		}
	}

	private View buildHeader() {
		textview = new TextView(this);
		textview.setText("刷新");
		textview.setTextColor(0xfffff7ff);
		textview.setTextSize(18);

		textview.setWidth(60);
		textview.setHeight(60);
		textview.setGravity(Gravity.CENTER);
		textview.setBackgroundResource(R.drawable.listviewselector);

		return (textview);
	}

	/**
	 * @Title: ShowNowPlayingList
	 * @Description: 用ListView显示 正在放映 电影列表
	 * @param：
	 * @return:
	 * @throws: printStackTrace()
	 */

	private class ShowNowPlayingList extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				movies = NetUtil.getDoubanNowPlaying();
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
			if(!flg){
				textview.setText("刷新");

			}
			if (result) {
				addListHeaderView();
				setListAdapter(new NowPlayingListAdapter(
						NowPlayingListActivity.this, getListView(), movies));
				flag = true;
				saveData();
			} else {
				Toast.makeText(NowPlayingListActivity.this, "数据加载失败！",
						Toast.LENGTH_LONG).show();
				flag = false;
				if (db_adapter.if_np_exists()) {
					addListHeaderView();
					movies = db_adapter.loadNpData();
					setListAdapter(new NowPlayingListAdapter(
							NowPlayingListActivity.this, getListView(), movies));
				}else{
					addListHeaderView();
					setListAdapter(new NowPlayingListAdapter(
							NowPlayingListActivity.this, getListView(), movies));
				}
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressBar();
			if(!flg){
				textview.setText("");
			}

		}
	}

	private void showListAsyncTask() {
		show_np_list = new ShowNowPlayingList();
		show_np_list.execute();
	}

	private void addListHeaderView() {
		if (flg) {
			getListView().addHeaderView(buildHeader());
			flg = false;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		db_adapter.close();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			super.openOptionsMenu();
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doExit();
			return true;
		}
		return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
	}

}
