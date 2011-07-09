package com.xmlparse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

//import basic.android.xml.sax.R.string;

import android.util.Log;

public class HttpClientConnector {

	public static String getStringByUrl(String url) {

		String outputString = "";

		// DefaultHttpClient
		DefaultHttpClient httpclient = new DefaultHttpClient();
		// HttpGet
		HttpGet httpget = new HttpGet(url);
		// ResponseHandler
		BasicResponseHandler responseHandler = new BasicResponseHandler();
		//String responseHandler = new BasicResponseHandler();

		try {
			outputString = httpclient.execute(httpget, responseHandler);
			Log.i("yao", "连接成功");
		} catch (Exception e) {
			Log.i("yao", "连接失败");
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		return outputString;
	}
}
