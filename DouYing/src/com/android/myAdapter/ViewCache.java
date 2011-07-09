package com.android.myAdapter;

import android.view.View;
import android.widget.ImageView;

import com.android.douying.R;


public class ViewCache {

	private View baseView;
	public ImageView imageView;
	public ViewCache(View baseView) {
		this.baseView = baseView;
	}

	public ImageView getImageView() {
		if (imageView == null) {
			imageView = (ImageView) baseView.findViewById(R.id.allimageview);
		}
		return imageView;
	}

}