package com.wanderphone.douying;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
/**
 * @ClassName: MainTabActivity
 * @Description: 初始化显示的四个tab（NowPlayingListActivity，BestOfDoubanActivity，BestOfWeekActivity，SearchActivity）
 * @author：
 * @version：v1.0
 */
public class MainTabActivity extends TabBaseActivity {

	private TabHost tab_host;
	private TextView title_view;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		initTabHost();
		title_view = (TextView) findViewById(R.id.myTitle);
		title_view.setText("豆影");
	}

	private View prepareTabView(Context context, int titleId, int drawable) {
		View view = LayoutInflater.from(context).inflate(R.layout.main_tab_bar,
				null);
		TextView tv = (TextView) view.findViewById(R.id.tvTitle);
		tv.setText(getText(titleId).toString());
		ImageView iv = (ImageView) view.findViewById(R.id.ivIcon);
		iv.setImageResource(drawable);
		return view;
	}

	private void initTabHost() {
		if (tab_host != null) {
			throw new IllegalStateException(
					"Trying to intialize already initializd TabHost");
		}
		tab_host = getTabHost();

		Intent nowplaying = new Intent(this, NowPlayingListActivity.class);//NowPlayingListActivity
		Intent bestofdouban = new Intent(this, BestOfDoubanActivity.class);
		Intent bestofweek = new Intent(this, BestOfWeekActivity.class);
		Intent search = new Intent(this, SearchActivity.class);

		// 正在热映
		tab_host.addTab(tab_host
				.newTabSpec("one")
				.setIndicator(
						prepareTabView(tab_host.getContext(),
								R.string.tab_nowplaying,
								R.drawable.tab_main_nowplaying))//
				.setContent(nowplaying));

		// 豆瓣250
		tab_host.addTab(tab_host
				.newTabSpec("two")
				.setIndicator(
						prepareTabView(tab_host.getContext(),
								R.string.tab_week,
								R.drawable.tab_main_bestofweek))
				.setContent(bestofweek));

		// 豆瓣周榜
		tab_host.addTab(tab_host
				.newTabSpec("three")
				.setIndicator(
						prepareTabView(tab_host.getContext(),
								R.string.tab_best, R.drawable.tab_main_best))
				.setContent(bestofdouban));

		// 搜索
		tab_host.addTab(tab_host
				.newTabSpec("four")
				.setIndicator(
						prepareTabView(tab_host.getContext(),
								R.string.tab_search, R.drawable.tab_main_search))
				.setContent(search));

		tab_host.setCurrentTab(0);

	}

}