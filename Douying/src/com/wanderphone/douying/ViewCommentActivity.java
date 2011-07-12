package com.wanderphone.douying;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gdata.data.douban.ReviewFeed;
import com.mobclick.android.MobclickAgent;
import com.wanderphone.getxml.ConvertUtil;
import com.wanderphone.getxml.MovieSubject;
import com.wanderphone.getxml.ReviewSubject;
import com.wanderphone.myAdapter.ReviewListAdapter;

/**
 * @ClassName: ViewCommentActivity
 * @Description: 显示电影评论ListView
 * @author：
 * @version：v1.0
 */
public class ViewCommentActivity extends BaseListActivity {

	private static final String ORDERBY = "score";// 按评分排序
	private List<ReviewSubject> reviews = new ArrayList<ReviewSubject>();
	private ReviewListAdapter listAdapter;
	private int index = 1;
	private int count = 10; // 每次获取数目
	private int total; // 最大条目数
	private boolean isFilling = false; // 判断是否正在获取数据

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment);

		Bundle extras = getIntent().getExtras();
		TextView movie_title_view = (TextView) findViewById(R.id.myTitle);
		final MovieSubject subject = extras != null ? (MovieSubject) extras
				.getSerializable("subject") : null;

		Log.v("tests", subject.getId());
		String title = subject.getTitle();
		movie_title_view.setText(title);

		fillDataBySubject(subject);

		/**
		 * 判断ListView滚动到底部
		 */
		getListView().setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						loadRemnantListItem(subject);
					}
				}
			}

		});
	}

	/**
	 * @Title: loadRemnantListItem
	 * @Description: 加载更多评论
	 * @param：subject
	 * @return:
	 * @throws:
	 */
	private void loadRemnantListItem(MovieSubject subject) {
		if (isFilling) {
			return;
		}
		index = index + count;
		if (index > total) {
			return;
		}
		RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
		LayoutParams lp = (LayoutParams) loading.getLayoutParams();
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		loading.setLayoutParams(lp);
		fillDataBySubject(subject);
	}

	/**
	 * @Title: fillDataBySubject
	 * @Description: 获取评论数据
	 * @param：subject
	 * @return:
	 * @throws:
	 */
	private void fillDataBySubject(final MovieSubject subject) {
		new AsyncTask<MovieSubject, Void, ReviewFeed>() {

			@Override
			protected ReviewFeed doInBackground(MovieSubject... args) {
				ReviewFeed feed = null;
				MovieSubject subject = args[0];
				try {

					// getMovieReviews 豆瓣java客户端
					feed = getDoubanService().getMovieReviews(subject.getId(),
							index, count, ORDERBY);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return feed;
			}

			@Override
			protected void onPostExecute(ReviewFeed feed) {
				super.onPostExecute(feed);
				closeProgressBar();
				if (feed != null) {
					total = feed.getTotalResults();
					reviews.addAll(ConvertUtil.ConvertReviews(feed, subject));
					if (listAdapter == null) {
						listAdapter = new ReviewListAdapter(
								ViewCommentActivity.this, getListView(),
								reviews);
						setListAdapter(listAdapter);
					} else {
						listAdapter.notifyDataSetChanged();
					}

					if (reviews.size() == 0) {
						Toast.makeText(ViewCommentActivity.this, "没有找到相关评论！",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(ViewCommentActivity.this, "数据加载失败！",
							Toast.LENGTH_SHORT).show();
				}
				isFilling = false;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgressBar();
				isFilling = true;
			}

		}.execute(subject);
	}

	/**
	 * @Title: onListItemClick
	 * @Description: 点击ListView事件
	 * @param：l, v, position, id
	 * @return:
	 * @throws:
	 */
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, CommentDetailActivity.class);
		ReviewSubject review = reviews.get(position);
		i.putExtra("review", review);
		startActivity(i);
	}
}
