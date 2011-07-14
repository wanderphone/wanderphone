package com.wanderphone.douying;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gdata.data.douban.SubjectFeed;
import com.google.gdata.util.ServiceException;
import com.mobclick.android.MobclickAgent;
import com.wanderphone.getxml.ConvertUtil;
import com.wanderphone.getxml.MovieSubject;
import com.wanderphone.getxml.NetUtil;
import com.wanderphone.myAdapter.MovieListAdapter;

/**
 * @ClassName: SearchActivity
 * @Description: 电影搜索
 * @author：
 * @version：v1.0
 */
public class SearchActivity extends BaseActivity {

	public List<MovieSubject> movies = new ArrayList<MovieSubject>();
	private int movieIndex = 1;
	private int count = 10; // 每次获取数目
	private int movieTotal; // 最大条目数
	public MovieListAdapter movieListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.search);

		initView(R.id.search_movie, R.string.movie_search_hint, "movie");

	}

	private void initView(int layoutId, int hintId, final String cat) {
		final View searchView = findViewById(layoutId);
		EditText searchText = (EditText) searchView
				.findViewById(R.id.search_edit);
		searchText.setHint(hintId);

		ImageButton searchButton = (ImageButton) searchView
				.findViewById(R.id.search_button);

		searchView.setTag(cat);
		searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				doSearch(searchView, cat);
			}
		});

		ListView listView = (ListView) searchView
				.findViewById(android.R.id.list);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(SearchActivity.this,
						MovieSubjectView.class);
				MovieSubject subject = movies.get(position);
				i.putExtra("subject", subject);
				startActivity(i);
			}
		});
	}

	private void doSearch(final View searchView, final String cat) {
		EditText searchText = (EditText) searchView
				.findViewById(R.id.search_edit);
		String searchTitle = searchText.getText().toString();
		if ("".equals(searchTitle.trim())) {
			return;
		}
		if (MovieSubject.MOVIE.equals("movie")) {
			movieIndex = 1;
			movieListAdapter = null;
			movies.clear();
		}
		fillData(searchView, cat);
	}

	private void fillData(final View searchView, final String cat) {
		new AsyncTask<View, Void, SubjectFeed>() {

			@Override
			protected SubjectFeed doInBackground(View... args) {
				// TODO Auto-generated method stub

				EditText searchText = (EditText) searchView
						.findViewById(R.id.search_edit);

				String title = searchText.getText().toString();

				SubjectFeed feed = null;
				try {
					feed = NetUtil.getDoubanService().findMovie(title, "",
							movieIndex, count);
					movieTotal = feed.getTotalResults();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return feed;
			}

			@Override
			protected void onPostExecute(SubjectFeed result) {
				super.onPostExecute(result);
				closeProgressBar(searchView);
				if (result != null) {

					ListView listView = (ListView) searchView
							.findViewById(android.R.id.list);
					if (MovieSubject.MOVIE.equals(cat)) {
						movies.addAll(ConvertUtil.ConvertSubjects(result, cat));
						if (movieListAdapter == null) {
							movieListAdapter = new MovieListAdapter(
									SearchActivity.this, listView, movies);
							listView.setAdapter(movieListAdapter);
						} else {
							movieListAdapter.notifyDataSetChanged();
						}
					}
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgressBar(searchView);

			}

		}.execute(searchView);

	};


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

}
