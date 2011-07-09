package com.android.myAdapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.douying.R;
import com.android.getxml.AsyncImageLoader.ImageCallback;
import com.android.getxml.MovieSubject;
import com.android.getxml.NetUtil;

public class NowPlayingListAdapter extends BaseAdapter {
	private List<MovieSubject> subjects;
	private LayoutInflater mInflater;
	private ListView listView;

	public NowPlayingListAdapter(Context context, ListView listView,
			List<MovieSubject> subjects) {
		this.listView = listView;
		this.subjects = subjects;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return subjects.size();
	}

	public Object getItem(int i) {
		return subjects.get(i);
	}

	public long getItemId(int i) {
		return i;
	}

	public View getView(int i, View view, ViewGroup vg) {
		// TODO Auto-generated method stub
		ViewCache viewCache = null;
		if (view == null) {
			view = mInflater.inflate(R.layout.nowplayinglist, null);
			viewCache = new ViewCache(view);
			view.setTag(viewCache);
		} else {
			viewCache = (ViewCache) view.getTag();
		}

		view.setBackgroundResource(R.drawable.listviewselector);
		MovieSubject movie = subjects.get(i);

		/**
		 * 名称 
		 * 类型 
		 * 放映时间
		 * 出品地区
		 * 放映影院
		 * 评分
		 * 海报
		 */
		TextView movie_title = (TextView) view.findViewById(R.id.title_name);
		TextView movie_type = (TextView) view.findViewById(R.id.np_type);
		TextView movie_duration = (TextView) view.findViewById(R.id.np_duration);
		TextView movie_area = (TextView) view.findViewById(R.id.np_area);
		TextView movie_cinema = (TextView) view.findViewById(R.id.np_cinema);
		RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);

		movie_title.setText(movie.getTitle());
		movie_type.setText(movie.getType());
		movie_duration.setText(movie.get_movie_duration());
		movie_area.setText(movie.get_pub_area());
		movie_cinema.setText(movie.getSummary());
		ratingBar.setRating(movie.getRating() / 2);

		String imgUrl = movie.getImgUrl();
		ImageView imgMovie = viewCache.getImageView();
		imgMovie.setTag(imgUrl);
		// 给view添加一个额外的数据 用getTag()取出
		Drawable drawable = NetUtil.asyncImageLoader.loadDrawable(imgUrl,
				new ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						ImageView imageViewByTag = (ImageView) listView
								.findViewWithTag(imageUrl);
						if (imageViewByTag != null) {
							imageViewByTag.setImageDrawable(imageDrawable);
						}
					}
				});

		if (drawable != null) {
			imgMovie.setImageDrawable(drawable);
		} else {
			imgMovie.setImageResource(R.drawable.img_null);
		}
		return view;
	}

}
