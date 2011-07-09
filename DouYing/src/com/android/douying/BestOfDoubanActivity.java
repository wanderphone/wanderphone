package com.android.douying;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.getxml.MovieSubject;
import com.android.getxml.NetUtil;
import com.android.myAdapter.ClassicListAdapter;
/**
 * @ClassName: BestOfDoubanActivity
 * @Description: 显示豆瓣250
 * @author：
 * @version：v1.0
 */
public class BestOfDoubanActivity extends BaseListActivity {
	private List<MovieSubject> movies = new ArrayList<MovieSubject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_list);
		ShowBestDoubanList();

		/*
		 * 正在放映List ClickListener
		 */
		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(BestOfDoubanActivity.this,
						MovieSubjectView.class);
				MovieSubject subject = movies.get(position);
				i.putExtra("subject", subject);
				startActivity(i);
			}
		});
	}

	private void ShowBestDoubanList() {
		new AsyncTask<Void, Void, Boolean>() {

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
				if (result) {
					setListAdapter(new ClassicListAdapter(
							BestOfDoubanActivity.this, getListView(), movies));
				} else {
					Toast.makeText(BestOfDoubanActivity.this, "数据加载失败！",
							Toast.LENGTH_LONG).show();
				}

			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgressBar();
			}

		}.execute();

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
