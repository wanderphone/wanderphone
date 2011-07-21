package com.wanderphone.douying;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;

import com.adview.AdViewLayout;
import com.adview.AdViewManager;
import com.adview.AdViewTargeting;
import com.adview.AdViewTargeting.AdArea;
import com.adview.AdViewTargeting.RunMode;
import com.adview.AdViewTargeting.UpdateMode;
import com.mobclick.android.MobclickAgent;

/**
 * @ClassName: CinemaInfoWebActivity
 * @Description: 显示正在上映电影院的webview
 * @author：
 * @version：v1.0
 */
public class CinemaInfoWebActivity extends BaseActivity {
	Animation myAnimation;
	private WebView mWebView;
	private Handler mHandler;
	private String movieId;
	private String movieUrl;
	private String cinema;
	private ProgressDialog progressBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cinema_info_web);

		Bundle bundle = this.getIntent().getExtras();
		movieId = bundle.getString("id");
		movieUrl = "http://movie.douban.com/subject/";
		cinema = "/cinema";
		Log.v("cinema_info", movieId);
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setVisibility(WebView.GONE);
		

		LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);
       // AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME); 
        //AdViewTargeting.setRunMode(RunMode.TEST);
		AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);
		AdViewTargeting.setRunMode(RunMode.TEST);
        AdViewManager.setConfigExpireTimeout(-1);
       // AdViewTargeting.setUpdateMode(true);
        AdViewTargeting.setAdArea(AdArea.BOTTOM);
        AdViewLayout adViewLayout = new AdViewLayout(this, "SDK20110919400711zhdlaagi2x153px");
        RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        layout.addView(adViewLayout, adViewLayoutParams);
        layout.invalidate();

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		progressBar = ProgressDialog.show(CinemaInfoWebActivity.this, getResources().getString(R.string.cinema_info),
				getResources().getString(R.string.load_info));

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				if (progressBar.isShowing())
					progressBar.dismiss();
			}
		});
		mWebView.loadUrl(movieUrl + movieId + cinema);
		mWebView.setVisibility(WebView.VISIBLE);
	}

	final class MyWebChromeClient extends WebChromeClient {
		MyWebChromeClient() {
		}
	}

	final class DemoJavaScriptInterface {
		DemoJavaScriptInterface() {
		}

		/*
		 * This is not called on the UI thread. Post a runnable to invoke
		 * loadUrl on the UI thread.
		 */
		public void left() {
			mHandler.post(new Runnable() {
				public void run() {
					mWebView.loadUrl("javascript:keyControl(37)");
				}
			});
		}
	}
}
