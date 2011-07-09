package com.listAdapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.minesweep.R;
import com.xmlparse.RankInfo;

public class RankListAdapter extends BaseAdapter{
	private List<RankInfo> rankInfos;
	private LayoutInflater mInflater;
	private ListView listView;
	
	public RankListAdapter(Context context, ListView listView, List<RankInfo> rankInfos){
		this.listView = listView;
		this.rankInfos = rankInfos;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount(){
		return rankInfos.size();
	}
	public Object getItem(int i){
		return rankInfos.get(i);
	}
	public long getItemId(int i){
		return i;
	}
	
	public View getView(int i,View view, ViewGroup vg){
		if(view == null)
		{
			view = mInflater.inflate(R.layout.show_rank_result, null);
		}
		RankInfo rankInfo = rankInfos.get(i);
		TextView txtRankInfo = (TextView)view.findViewById(R.id.rank);
		TextView txtUsername = (TextView)view.findViewById(R.id.username);
		TextView txtTime = (TextView)view.findViewById(R.id.time);
		txtRankInfo.setText(rankInfo.getRank());
		txtUsername.setText(rankInfo.getUsername());
		txtTime.setText(rankInfo.getTime());
		return view;
	}
}
