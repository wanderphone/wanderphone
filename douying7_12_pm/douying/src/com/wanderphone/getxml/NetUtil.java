package com.wanderphone.getxml;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gdata.client.douban.DoubanService;

//网络访问工具类
public class NetUtil {

	private static String loginUrl = "http://www.douban.com/accounts/login";
	private static String reviewUrl = "http://www.douban.com/review/";
	private static String peopleUrl = "http://api.douban.com/people/";
	private static String movieUrl = "http://api.douban.com/movie/subject/";

	private static String apiKey = "078384efea978d64168a8ecfbfc6a82c";
	private static String secret = "ca6942b431c15f98";
	private static DoubanService doubanService = new DoubanService(
			"doubanMovie", apiKey, secret);

	// 登录用户的ID
	private static String uid;
	private static String selfUri;
	private static String accessToken;
	private static String tokenSecret;

	public static String getSecret() {
		return secret;
	}

	public static String getUid() {
		return uid;
	}

	public static void setUid(String uid) {
		NetUtil.uid = uid;
		NetUtil.selfUri = peopleUrl + uid;
	}

	public static String getSelfUri() {
		return selfUri;
	}

	public static String getAccessToken() {
		return accessToken;
	}

	public static void setAccessToken(String accessToken) {
		NetUtil.accessToken = accessToken;
	}

	public static String getTokenSecret() {
		return tokenSecret;
	}

	public static void setTokenSecret(String tokenSecret) {
		NetUtil.tokenSecret = tokenSecret;
	}

	public static DoubanService getDoubanService() {
		return doubanService;
	}

	// 图片加载管理器
	public static AsyncImageLoader asyncImageLoader = new AsyncImageLoader();

	// 获取验证码ID
	public static String getCaptchaId() throws Exception {
		HttpGet request = new HttpGet(loginUrl);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Source source = new Source(response.getEntity().getContent());
				List<Element> inputElements = source
						.getAllElements(HTMLElementName.INPUT);
				for (Element element : inputElements) {
					String name = element.getAttributeValue("name");
					if ("captcha-id".equals(name)) {
						return element.getAttributeValue("value");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取验证码失败！");
		}
		throw new Exception("获取验证码失败！");
	}

	// 获取验证码图片
	public static Bitmap getCaptchaImg(String captchaId) throws Exception {
		String url = "http://www.douban.com/misc/captcha?id=";
		url = url + captchaId + "&amp;size=m";
		try {
			URL imageUri = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) imageUri
					.openConnection();
			httpConn.setDoInput(true);
			httpConn.connect();
			InputStream is = httpConn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			Bitmap bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取验证码图片失败！");
		}
	}

	// 获取网络上的图片
	public static Bitmap getNetImage(String url) throws Exception {
		try {
			URL imageUri = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection) imageUri
					.openConnection();
			httpConn.setDoInput(true);
			httpConn.connect();
			InputStream is = httpConn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			Bitmap bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("获取验证码图片失败！");
		}
	}

	/**
	 * @Title: getBestOfDoubanMovie
	 * @Description: 解析豆瓣中的经典电影250，把所得电影信息存入List<MovieSubject>中
	 * @param：
	 * @return: List<MovieSubject>
	 * @throws: Exception
	 */
	public static List<MovieSubject> getBestOfDoubanMovie() throws Exception {
		List<MovieSubject> movies = new ArrayList<MovieSubject>();
		URL uri = new URL("http://movie.douban.com/chart");
		HttpURLConnection httpConn = (HttpURLConnection) uri.openConnection();
		httpConn.setDoInput(true);
		httpConn.connect();
		InputStream is = httpConn.getInputStream();
		Source source = new Source(is);
		List<Element> divs = source.getAllElements("dl");

		for (Element e : divs) {
			List<Element> childs = e.getChildElements();
			Log.v("test", String.valueOf(childs.size()));

			String movieId = childs.get(1).getChildElements().get(0)
					.getAttributeValue("href");
			Log.v("id", movieId);
			MovieSubject movie = new MovieSubject();

			// 设置电影ID
			movieId = movieId.substring(0, movieId.length() - 1);
			movieId = movieId.substring(movieId.lastIndexOf("/") + 1);

			movieId = movieUrl + movieId;
			// 设置名称
			movie.setTitle(childs.get(1).getChildElements().get(0).getContent()
					.toString());

			movie.setImgUrl(childs.get(0).getChildElements().get(0)
					.getChildElements().get(0).getAttributeValue("src"));

			movie.setUrl(movieId);
			movie.setType(MovieSubject.MOVIE);
			movies.add(movie);
		}
		is.close();
		return movies;
	}

	/**
	 * @Title: getBestOfWeekMovie
	 * @Description: 解析豆瓣中的新片榜，把所得电影信息存入List<MovieSubject>中
	 * @param：
	 * @return: List<MovieSubject>
	 * @throws: Exception
	 */
	public static List<MovieSubject> getBestOfWeekMovie() throws Exception {
		List<MovieSubject> movies = new ArrayList<MovieSubject>();
		URL uri = new URL("http://movie.douban.com/chart");
		HttpURLConnection httpConn = (HttpURLConnection) uri.openConnection();
		httpConn.setDoInput(true);
		httpConn.connect();
		InputStream is = httpConn.getInputStream();
		Source source = new Source(is);
		List<Element> divs = source.getAllElements("table");
		for (Element e : divs) {
			List<Element> childs = e.getChildElements();

			String movieId = childs.get(0).getChildElements().get(0)
					.getChildElements().get(0).getAttributeValue("href");
			Log.v("id", movieId);
			MovieSubject movie = new MovieSubject();

			// 设置电影ID
			movieId = movieId.substring(0, movieId.length() - 1);
			movieId = movieId.substring(movieId.lastIndexOf("/") + 1);

			movieId = movieUrl + movieId;
			movie.setUrl(movieId);
			String name = childs.get(0).getChildElements().get(1)
					.getChildElements().get(0).getChildElements().get(0)
					.getTextExtractor().toString();

			if (name.contains("/")) {
				name = name.substring(0, name.indexOf("/") - 1);

			}

			movie.setTitle(name);
			Log.v("name", name);
			movie.setDescription(childs.get(0).getChildElements().get(1)
					.getChildElements().get(1).getTextExtractor().toString());

			Log.v("Description", childs.get(0).getChildElements().get(1)
					.getChildElements().get(1).getTextExtractor().toString());
			Log.v("size1",
					Integer.toString(childs.get(0).getChildElements().get(1)
							.getChildElements().size()));

			movie.setImgUrl(childs.get(0).getChildElements().get(0)
					.getChildElements().get(0).getChildElements().get(0)
					.getAttributeValue("src"));
			Log.v("URL", childs.get(0).getChildElements().get(0)
					.getChildElements().get(0).getChildElements().get(0)
					.getAttributeValue("src"));

			// 获取评分，若获取不到则设为-1
			if (childs.get(0).getChildElements().get(1).getChildElements()
					.size() > 2) {
				movie.setSummary(childs.get(0).getChildElements().get(1)
						.getChildElements().get(2).getTextExtractor()
						.toString());
				String rating = childs.get(0).getChildElements().get(1)
						.getChildElements().get(2).getChildElements().get(1)
						.getTextExtractor().toString();

				Log.v("rating", rating);
				if (rating != "") {
					movie.setRating(Float.valueOf(rating));

				} else {
					movie.setRating(-1f);
				}
			} else {
				movie.setRating(-1f);
			}
			movie.setType(MovieSubject.MOVIE);
			movies.add(movie);

		}
		is.close();
		return movies;
	}

	/**
	 * @Title: getDoubanNowPlaying
	 * @Description: 解析豆瓣正在热映的html，把所得电影信息存入List<MovieSubject>中
	 * @param：
	 * @return: List<MovieSubject>
	 * @throws: Exception
	 */
	public static List<MovieSubject> getDoubanNowPlaying() throws Exception {
		List<MovieSubject> movies = new ArrayList<MovieSubject>();
		URL uri = new URL("http://movie.douban.com/nowplaying");
		HttpURLConnection httpConn = (HttpURLConnection) uri.openConnection();
		httpConn.setDoInput(true);// 如果想使用 URL 连接输入，则设置 DoInput 标志为 true ，否则为
									// false
		httpConn.connect();// URLConnection 对象经历两个阶段：首先它们被生成，然后被连接。
		InputStream is = httpConn.getInputStream();// 返回从该打开的连接读取的输入流。
		Source source = new Source(is);
		List<Element> divs = source.getAllElements("div");
		List<Element> childs = null;
		String div_id = null;
		for (Element e : divs) {

			String div_tmp = e.getAttributeValue("id");
			if ("showing-now".equals(div_tmp)) {
				div_id = div_tmp;
				childs = e.getChildElements();
			}
		}
		int size = childs.size();
		if ("showing-now".equals(div_id)) {
			for (int i = 0; i < size; i++) {
				Element contents = childs.get(i);
				String id = contents.getChildElements().get(0)
						.getAttributeValue("href");
				id = id.substring(0, id.length() - 1);
				id = id.substring(id.lastIndexOf("/") + 1);
				id = movieUrl + id;
				String img = contents.getChildElements().get(0)
						.getChildElements().get(0).getAttributeValue("src");

				String intro_tmp = String.valueOf(contents.getChildElements()
						.get(1).getAttributeValue("class"));
				if ("intro".equals(intro_tmp)) {
					MovieSubject movie = new MovieSubject();

					List<Element> content_list = contents.getChildElements()
							.get(1).getChildElements().get(1)
							.getChildElements();
					int content_size = content_list.size();
					if (content_size == 5) {
						String movie_name = String.valueOf(contents
								.getChildElements().get(1).getChildElements()
								.get(0).getTextExtractor());
						String rating = String.valueOf(contents
								.getChildElements().get(1).getChildElements()
								.get(1).getChildElements().get(0)
								.getTextExtractor());
						String type = String.valueOf(content_list.get(1)
								.getTextExtractor());
						String movie_duration = String.valueOf(content_list
								.get(2).getTextExtractor());
						String pub_area = String.valueOf(content_list.get(3)
								.getTextExtractor());
						String cinema_num = String.valueOf(content_list.get(4)
								.getTextExtractor());
						if (rating != "") {
							movie.setRating(Float.valueOf(rating));// 评分
						} else {
							movie.setRating(-1f);// 评分
						}
						cinema_num = cinema_num.substring(0,
								cinema_num.length() - 3);

						if (-1 != movie_name.indexOf("：")) {
							movie_name = movie_name.substring(0,
									movie_name.indexOf("：") - 1);
							movie.setTitle(movie_name);// 电影名
						} else {
							movie.setTitle(movie_name);// 电影名
						}

						movie.setUrl(id);
						movie.setImgUrl(img);// 图片地址
						movie.setType(type);// 类型
						movie.set_movie_duration(movie_duration);// 片长
						movie.set_pub_area(pub_area);// 出品地区
						movie.setSummary(cinema_num);// 上映影院

						movies.add(movie);
					}

				}
			}
		}
		is.close();
		return movies;

	}

	/**
	 * @Title: getReviewContentAndComments
	 * @Description: 解析详细评论html，把所得电影信息存入ReviewSubject中
	 * @param：review
	 * @return: ReviewSubject
	 * @throws:
	 */
	public static ReviewSubject getReviewContentAndComments(ReviewSubject review)
			throws Exception {
		HttpGet request = new HttpGet(reviewUrl + review.getId() + "/");
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Source source = new Source(response.getEntity().getContent());
			Element contentDivElement = source.getElementById("content");
			for (Element e : contentDivElement.getAllElements("img")) {
				if ("pil".equals(e.getAttributeValue("class"))) {
					review.setAuthorImageUrl(e.getAttributeValue("src"));
					review.setAuthorImage(getNetImage(review
							.getAuthorImageUrl()));
					break;
				}
			}

			for (Element e : contentDivElement.getAllElements("span")) {
				if ("v:description".equals(e.getAttributeValue("property"))) {
					String content = e.getContent().toString();
					review.setContent(content);
					break;
				}
			}

			Element commentsDiv = source.getElementById("comments");
			if (commentsDiv != null) {
				String comments = commentsDiv.getContent().toString();
				review.setComments(comments);
			}
		}
		return review;
	}
}
