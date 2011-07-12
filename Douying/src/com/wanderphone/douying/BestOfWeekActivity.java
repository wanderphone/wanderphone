package com.wanderphone.douying;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wanderphone.getxml.MovieSubject;
import com.wanderphone.getxml.NetUtil;
import com.wanderphone.myAdapter.ClassicListAdapter;
import com.wanderphone.myAdapter.MovieListAdapter;
import com.wanderphone.sqlite.DataBaseAdapter;

/**
 * @ClassName: BestOfWeekActivity
 * @Description: 豆瓣排行榜
 * @author：
 * @version：v1.0
 */
public class BestOfWeekActivity extends BaseListActivity {
	private List<MovieSubject> movies = new ArrayList<MovieSubject>();
	DataBaseAdapter db_adapter = new DataBaseAdapter(this);
	SQLiteDatabase db;
	boolean flag = true;// 是否有网络 标识
	private ShowBestOfWeekList show_week_list;
	boolean flg = true;// listview首行显示 刷新 标识

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		db_adapter.open();

		super.onCreate(savedInstanceState);

		setContentView(R.layout.movie_list);
		if (db_adapter.if_week_exists()) {
			addListHeaderView();
			movies = db_adapter.loadWeekData();
			setListAdapter(new MovieListAdapter(BestOfWeekActivity.this,
					getListView(), movies));

			showListAsyncTask();
		} else {
			showListAsyncTask();

		}

		/*
		 * 正在放映List ClickListener
		 */
		ListView listView = (ListView) findViewById(android.R.id.list);
		if (MovieSubject.MOVIE.equals("movie")) {
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					if (flag && position != 0) {
						MovieSubject subject = movies.get(position - 1);
						Intent i = new Intent(BestOfWeekActivity.this,
								MovieSubjectView.class);
						i.putExtra("subject", subject);
						startActivity(i);
					} else if (position == 0) {
						showListAsyncTask();
					} else if (!flag && position != 0) {
						Toast.makeText(BestOfWeekActivity.this, "数据加载失败！",
								Toast.LENGTH_SHORT).show();

					}
				}
			});
		}

	}

	private class ShowBestOfWeekList extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				movies = NetUtil.getBestOfWeekMovie();
				Log.v("movies2", movies.toString());
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
				setListAdapter(new MovieListAdapter(
						BestOfWeekActivity.this, getListView(), movies));
				flag = true;
				saveData();
			} else {
				Toast.makeText(BestOfWeekActivity.this, "数据加载失败！",
						Toast.LENGTH_SHORT).show();
				flag = false;
				if (db_adapter.if_week_exists()) {
					addListHeaderView();
					movies = db_adapter.loadWeekData();
					setListAdapter(new MovieListAdapter(
							BestOfWeekActivity.this, getListView(), movies));
				}else{
					addListHeaderView();
					setListAdapter(new MovieListAdapter(
							BestOfWeekActivity.this, getListView(), movies));
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
	private TextView textview;
	private String name;
	private String content;
	private byte[] img;

	private void saveData() {
		db_adapter.delete_all_week();
		for (MovieSubject ms : movies) {
			name = ms.getTitle();
			content = ms.getDescription();
			img = ms.get_img_bytes();
			db_adapter.insertWeekData(name, content, img);
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
	private void showListAsyncTask() {
		show_week_list = new ShowBestOfWeekList();
		show_week_list.execute();
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		db_adapter.close();
		super.onDestroy();
	}
}
