package com.wanderphone.myAdapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wanderphone.douying.R;
import com.wanderphone.getxml.ReviewSubject;

/**
 * @ClassName: ReviewListAdapter
 * @Description: 显示电影评论ListView的ListAdapter
 * @author：
 * @date：2011-5-12
 * @version：v1.0
 */
public class ReviewListAdapter extends BaseAdapter {

		private ListView listView;
		private List<ReviewSubject> reviews;
		private LayoutInflater mInflater;

		public ReviewListAdapter(Context context, ListView listView,
				List<ReviewSubject> reviews) {
			this.listView = listView;
			this.reviews = reviews;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return reviews.size();
		}

		public Object getItem(int i) {
			return reviews.get(i);
		}

		public long getItemId(int i) {
			return i;
		}

		public View getView(int i, View view, ViewGroup vg) {
			if (view == null) {
				view = mInflater.inflate(R.layout.comment_item, null);
			}

			ReviewSubject review = reviews.get(i);
			/**
			 * ListView单双行显示不同颜色
			 */
			if(0 == i%2){
				view.setBackgroundResource(R.drawable.comment_list_selector_one);
			}else{
				view.setBackgroundResource(R.drawable.comment_list_selector_two);
			}
			
			
			/**
			 * 评论题目
			 * 评论概要
			 * 评论人
			 */
			TextView review_title = (TextView) view.findViewById(R.id.review_title);
			TextView review_summary = (TextView) view
					.findViewById(R.id.review_summary);

			TextView author_name = (TextView) view
					.findViewById(R.id.author_name);

			review_title.setText(review.getTitle());
			String summary = review.getSummary();
			summary = summary.replaceAll("\\\n", "");
			summary = summary.replaceAll("\\\t", "");
			summary = summary.replaceAll(" ", "");
			review_summary.setText(summary);
			author_name.setText("评论人:" + review.getAuthorName());
			return view;
		}
	}