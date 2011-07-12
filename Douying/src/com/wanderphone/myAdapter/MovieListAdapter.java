package com.wanderphone.myAdapter;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wanderphone.douying.R;
import com.wanderphone.getxml.AsyncImageLoader.ImageCallback;
import com.wanderphone.getxml.MovieSubject;
import com.wanderphone.getxml.NetUtil;

/**
 * @ClassName: MovieListAdapter
 * @Description: 用于显示ListView的ListAdapter
 * @author：
 * @date：2011-5-12
 * @version：v1.0
 */
public class MovieListAdapter extends BaseAdapter {
	private List<MovieSubject> subjects;
	private LayoutInflater mInflater;
	private ListView listView;

	public MovieListAdapter(Context context, ListView listView, List<MovieSubject> subjects) {
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

	@Override
	public View getView(int i, View view, ViewGroup vg) {
		// TODO Auto-generated method stub
		Log.v("MovieListAdapter", "init MovieListAdapter");

		ViewCache viewCache = null;
		if (view == null) {
			view = mInflater.inflate(R.layout.show_search_result, null);
			viewCache = new ViewCache(view);
			view.setTag(viewCache);
		}else {
			viewCache = (ViewCache) view.getTag();
		}
		
		view.setBackgroundResource(R.drawable.listviewselector);
		MovieSubject movie = subjects.get(i);
		
		//Log.v("SubjectListAdapter", "i: " + String.valueOf(i));
		
		/**
		 * 名称
		 * 描述
		 * 评分
		 * 海报
		 */
		TextView movie_title  = (TextView) view.findViewById(R.id.title_name);
		movie_title.setText(movie.getTitle());
		
		TextView movie_description = (TextView) view.findViewById(R.id.descrition);
		int des_size = movie.getDescription().length();
		if(des_size == 0){
			movie_description.setVisibility(TextView.GONE);
		}
		if(80 >= des_size){
			movie_description.setText(movie.getDescription());
		}else{
			String movie_cut = movie.getDescription().substring(0, 80);
			movie_description.setText(movie_cut.substring(0, movie_cut.lastIndexOf("/")));
		}

		RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);
		ratingBar.setRating(movie.getRating()/2);
		
		
		String imgUrl = movie.getImgUrl();
		ImageView imgMovie = viewCache.getImageView();
		imgMovie.setTag(imgUrl);
		//给view添加一个额外的数据   用getTag()取出
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

		byte[] ibs = null;
		if (drawable != null) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			subjects.get(i).set_img_bytes(os.toByteArray());
			imgMovie.setImageDrawable(drawable);
		}else {
			imgMovie.setImageResource(R.drawable.img_null);
		}
		ibs = subjects.get(i).get_img_bytes();
		if (ibs != null) {
			Log.v("ibs", "ok");
			Bitmap bitmap = Bytes2Bimap(ibs);
			Drawable drawables = new BitmapDrawable(bitmap);
			imgMovie.setImageDrawable(drawables);
		}else{
			Log.v("ibs", "bad");

			imgMovie.setImageResource(R.drawable.img_null);
		}
		return view;
	}

	private Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}



}