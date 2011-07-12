package com.wanderphone.douying;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gdata.data.douban.SubjectEntry;
import com.mobclick.android.MobclickAgent;
import com.wanderphone.getxml.AsyncImageLoader.ImageCallback;
import com.wanderphone.getxml.ConvertUtil;
import com.wanderphone.getxml.MovieSubject;
import com.wanderphone.getxml.NetUtil;
/**
 * @ClassName: MovieSubjectView
 * @Description: 显示电影详细信息
 * @author：
 * @version：v1.0
 */
public class MovieSubjectView extends BaseActivity implements
	View.OnClickListener{

	private ImageView movie_image;
	private TextView movie_summary;
	private TextView movie_rating;
	private TextView movie_type;
	private TextView movie_cast;
	private TextView movie_duration;
	private TextView movie_pubdate;
	private TextView movie_direcotr;
	private Button view_comment;
	private Button view_cinema;
	private MovieSubject movie_subject;
	private View bg2;
	private View bg1;
	private View view_relayout;
	MovieSubject movie;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.movie_detail);
		Bundle extras = getIntent().getExtras();
		movie_subject = extras != null ? (MovieSubject) extras.getSerializable("subject")
				: null;
		
		movie_rating = (TextView) findViewById(R.id.movie_rating);
		movie_type = (TextView) findViewById(R.id.movie_type);
		movie_cast = (TextView) findViewById(R.id.movie_cast);
		movie_duration = (TextView) findViewById(R.id.movie_pubdate);
		movie_pubdate = (TextView) findViewById(R.id.movie_duration);
		movie_direcotr = (TextView) findViewById(R.id.movie_director);
		movie_summary = (TextView) findViewById(R.id.movie_summary);
		movie_image = (ImageView) findViewById(R.id.movie_img);
		
		TextView movie_detail_titile = (TextView) findViewById(R.id.myTitle);
				
		String movieid = movie_subject.getUrl();
		if (movie_subject != null) {
			FillMovieData(movieid);
			movie_detail_titile.setText(movie_subject.getTitle());
		}
		
		view_comment = (Button) findViewById(R.id.view_comment);
		view_cinema = (Button) findViewById(R.id.view_cinema);
		bg1 = findViewById(R.id.wrapper);
		bg2 = findViewById(R.id.forth);
		view_relayout = findViewById(R.id.loading);

		view_comment.setVisibility(Button.GONE);
		view_cinema.setVisibility(Button.GONE);
		bg1.setVisibility(View.GONE);
		bg2.setVisibility(View.GONE);
		
		/*
		 * 响应查看评论事件，跳转到ViewCommentActivity.class
		 */
		OnClickListener showReviewClicklistener = new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(MovieSubjectView.this,
						ViewCommentActivity.class);
				i.putExtra("subject", movie_subject);
				//Log.v("demos", movie_subject.getId());
				startActivity(i);
			}

		};
		/*
		 * 响应查看影院信息事件，跳转到CinemaInfoWebActivity.class
		 */
		OnClickListener showCinemaInfo = new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(MovieSubjectView.this,
						CinemaInfoWebActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", movie_subject.getId());
				Log.v("demos", movie_subject.getId());
				i.putExtras(bundle);
				startActivity(i);
			}
		};

		view_cinema.setOnClickListener(showCinemaInfo);
		view_comment.setOnClickListener(showReviewClicklistener);
		
	}


	protected void FillMovieData(String MovieId) {

		new AsyncTask<String, Void, SubjectEntry>() {

			@Override
			protected SubjectEntry doInBackground(String... args) {
				String movieId = args[0];
				SubjectEntry entry = null;

				try {
					//getMovie() 豆瓣java客户端
					entry = NetUtil.getDoubanService().getMovie(movieId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return entry;
			}

			@Override
			protected void onPostExecute(SubjectEntry result) {
				super.onPostExecute(result);
				closeProgressBar();
				view_relayout.setVisibility(View.GONE);
				if (result != null) {
					view_comment.setVisibility(Button.VISIBLE);
					view_cinema.setVisibility(Button.VISIBLE);
					bg1.setVisibility(View.VISIBLE);
					bg2.setVisibility(View.VISIBLE);
					//通过豆瓣java客户端，获取movie详细信息，存入MovieSubject
					movie = ConvertUtil.convertMovieSubject(result);

					/*
					 * 评分：
					 * 类型：
					 * 导演：
					 * 演员：
					 * 时长：
					 * 上映时间:
					 */ 
					
					//评分
					if("" != String.valueOf(movie.getRating())){
						movie_rating.setText("评分："+String.valueOf(movie.getRating()));
					}else{
						movie_rating.setText("评分：" + movie_rating.getContext().getString(R.string.default_none));
					}
					
					//类型
					if("" != movie.getType()){
						movie_type.setText("类型："+movie.getType());
					}else{
						movie_type.setText("类型："+movie_type.getContext().getString(R.string.default_none));
					}
					
					//导演
					if("" != movie.get_movie_director()){
						movie_direcotr.setText("导演："+movie.get_movie_director());
					}else{
						movie_direcotr.setText("导演："+ movie_direcotr.getContext().getString(R.string.default_none));
					}
					
					//演员
					if("" != movie.get_movie_cast()){
						movie_cast.setText("演员：" + movie.get_movie_cast());
					}else{
						movie_cast.setText("演员：" + movie_cast.getContext().getString(R.string.default_none));
					}
					
					//时长
					if( "" != movie.get_movie_duration()){
						movie_duration.setText("时长：" + movie.get_movie_duration());	

					}else{
						movie_duration.setText("时长：" + movie_duration.getContext().getString(R.string.default_none));
					}
					
					//上映时间
					if( "" != movie.get_pub_date()){
						movie_pubdate.setText("上映时间："+movie.get_pub_date());	

					}else{
						movie_pubdate.setText("上映时间："+movie_pubdate.getContext().getString(R.string.default_none));
					}
					
					//电影介绍
					if( "" != movie.getSummary()){
						movie_summary.setText("        " + movie.getSummary());	
					}else{
						movie_summary.setText(movie_summary.getContext().getString(R.string.default_none) + "电影介绍");
					}
					
					String imageUrl = movie.getImgUrl();
					Drawable drawable = NetUtil.asyncImageLoader.loadDrawable(
							imageUrl, new ImageCallback() {
								public void imageLoaded(Drawable imageDrawable,
										String imageUrl) {
									movie_image.setImageDrawable(imageDrawable);
								}
							});
					if (drawable != null) {
						movie_image.setImageDrawable(drawable);
					} else {
						movie_image.setImageResource(R.drawable.img_null);
					}
				} else {
					Toast.makeText(MovieSubjectView.this, "数据加载失败！",
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgressBar();
			}

		}.execute(MovieId);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}
	 public void showProgressBar() {
			AnimationSet set = new AnimationSet(true);

			Animation animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(500);
			set.addAnimation(animation);

			animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			animation.setDuration(500);
			set.addAnimation(animation);

			LayoutAnimationController controller = new LayoutAnimationController(
					set, 0.5f);
			RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
			loading.setVisibility(View.VISIBLE);
			loading.setLayoutAnimation(controller);
		}

		public void closeProgressBar() {

			AnimationSet set = new AnimationSet(true);

			Animation animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(500);
			set.addAnimation(animation);

			animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
			animation.setDuration(500);
			set.addAnimation(animation);

			LayoutAnimationController controller = new LayoutAnimationController(
					set, 0.5f);
			RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);

			loading.setLayoutAnimation(controller);

			loading.setVisibility(View.INVISIBLE);
		}

}
