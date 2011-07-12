package com.wanderphone.douying;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobclick.android.MobclickAgent;
import com.wanderphone.getxml.NetUtil;
import com.wanderphone.getxml.ReviewSubject;

/**
 * @ClassName: CommentDetailActivity
 * @Description: 显示电影评论详细内容
 * @author：
 * @version：v1.0
 */
public class CommentDetailActivity extends BaseActivity {

	private TextView review_title;
	private TextView review_content;
	private ImageView user_image;
	private TextView movie_title;
	private TextView review_user;
	private View bg_text;
	private View view_bar;
	private View view_layout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment_detail);

		view_layout = findViewById(R.layout.comment_detail);

		review_title = (TextView) findViewById(R.id.review_title);
		review_content = (TextView) findViewById(R.id.review_content);
		user_image = (ImageView) findViewById(R.id.user_img);
		movie_title = (TextView) findViewById(R.id.subject_title);
		review_user = (TextView) findViewById(R.id.user_info);
		bg_text = findViewById(R.id.review_title);
		bg_text.setVisibility(View.GONE);

		view_bar = findViewById(R.id.loading);
		Bundle extras = getIntent().getExtras();
		ReviewSubject review = extras != null ? (ReviewSubject) extras
				.getSerializable("review") : null;
		if (review != null) {
			TextView titleView = (TextView) findViewById(R.id.myTitle);
			titleView.setText(review.getSubject().getTitle());
			FillDetailComment(review);
		}

	}

	/**
	 * @Title: FillDetailComment
	 * @Description: 获取评论详细内容，并显示
	 * @param：review
	 * @return:
	 * @throws: printStackTrace()
	 */
	private void FillDetailComment(ReviewSubject review) {
		new AsyncTask<ReviewSubject, Void, ReviewSubject>() {

			@Override
			protected ReviewSubject doInBackground(ReviewSubject... args) {
				ReviewSubject review = args[0];
				try {
					review = NetUtil.getReviewContentAndComments(review);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return review;
			}

			@Override
			protected void onPostExecute(ReviewSubject review) {
				closeProgressBar();
				view_bar.setVisibility(View.GONE);
				super.onPostExecute(review);
				bg_text.setVisibility(View.VISIBLE);

				review_title.setText(review.getTitle());
				review_content.setText(Html.fromHtml(review.getContent()));
				if (review.getAuthorImage() != null) {
					user_image.setImageBitmap(review.getAuthorImage());
				}
				review_user.setText(" 评论人：" + review.getAuthorName());
				movie_title.setText("《" + review.getSubject().getTitle()
						+ "》的评论");
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgressBar();
			}

		}.execute(review);
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