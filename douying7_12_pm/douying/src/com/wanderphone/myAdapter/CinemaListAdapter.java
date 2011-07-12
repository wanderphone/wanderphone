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
import com.wanderphone.getxml.CinemaSubject;

public class CinemaListAdapter extends BaseAdapter{
	private List<CinemaSubject> cinema;
	private LayoutInflater mInflater;
	private ListView listView;
	public CinemaListAdapter(Context context, ListView listView, List<CinemaSubject> cinemas) {
		//Log.v("SubjectListAdapter", "init");
		this.listView = listView;
		this.cinema = cinemas;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return cinema.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return cinema.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int i, View view, ViewGroup vg) {
		// TODO Auto-generated method stub
		ViewCache viewCache = null;
		if (view == null) {
			view = mInflater.inflate(R.layout.cinema_list, null);
			viewCache = new ViewCache(view);
			view.setTag(viewCache);
		}else {
			viewCache = (ViewCache) view.getTag();
		}
		
		CinemaSubject cInfo = cinema.get(i);
		TextView cinema_name = (TextView) view.findViewById(R.id.cinema_name);
		cinema_name.setText(cInfo.get_cinema_name());
		
		TextView cinema_addr = (TextView) view.findViewById(R.id.cinema_addr);
		cinema_addr.setText(cInfo.get_cinema_address());
		return view;
	}

}
