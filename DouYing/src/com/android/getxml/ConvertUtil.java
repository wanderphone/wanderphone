package com.android.getxml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import android.util.Log;

import com.google.gdata.data.Person;
import com.google.gdata.data.TextContent;
import com.google.gdata.data.douban.Attribute;
import com.google.gdata.data.douban.CollectionEntry;
import com.google.gdata.data.douban.CollectionFeed;
import com.google.gdata.data.douban.SubjectEntry;
import com.google.gdata.data.douban.SubjectFeed;
import com.google.gdata.data.douban.Tag;
import com.google.gdata.data.douban.ReviewFeed;
import com.google.gdata.data.douban.ReviewEntry;

public class ConvertUtil {
	private static ArrayList<String> names;
	private static SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	static {
		names = new ArrayList<String>();
		names.add("authors");
		names.add("pubdate");
		names.add("publisher");
		names.add("price");
		names.add("pages");
		names.add("binding");
	}

	/**
	 * 获取条目列表
	 * 
	 * @param subjectFeed
	 * @param cat
	 * @return
	 */
	public static List<MovieSubject> ConvertSubjects(SubjectFeed subjectFeed,
			String cat) {
		List<MovieSubject> movies = new ArrayList<MovieSubject>();

		for (SubjectEntry entry : subjectFeed.getEntries()) {
			MovieSubject movie = new MovieSubject();
			movie.setTitle(entry.getTitle().getPlainText());
			Log.v("ConvertUtil", entry.getTitle().getPlainText());
			
			movie.setDescription(getDescription(entry));
			Log.v("ConvertUtil", getDescription(entry));
			
			movie.setUrl(entry.getId());
			//Log.v("ConvertUtil", entry.getId());
			
			movie.setImgUrl(entry.getLink("image", null).getHref());
			//Log.v("ConvertUtil", entry.getLink("image", null).getHref());

			movie.setRating(entry.getRating().getAverage());
			Log.v("ConvertUtil", String.valueOf(entry.getRating().getAverage()));

			movie.setType(cat);
			movies.add(movie);
		}
		return movies;
	}

	/**
	 * 获取条目描述信息
	 * 
	 * @param entry
	 * @return
	 */
	private static String getDescription(SubjectEntry entry) {
		String description = "";
		List<Attribute> attributes = entry.getAttributes();
		String authors = "";
		for (Person author : entry.getAuthors()) {
			authors += "," + author.getName();
		}
		if (authors.length() > 0) {
			authors = authors.substring(1);
		}

		Map<String, String> map = new HashMap<String, String>();

		for (Attribute attribute : attributes) {
			if (names.contains(attribute.getName())) {
				map.put(attribute.getName(), attribute.getContent());
			}
		}
		map.put("authors", authors);
		for (String name : names) {
			if (map.get(name) != null) {
				if ("price".equals(name)) {
					description += "/" + map.get(name) + "元";
				} else if ("pages".equals(name)) {
					description += "/" + map.get(name) + "页";
				} else {
					description += "/" + map.get(name);
				}
			}
		}
		if (description.length() > 0) {
			description = description.substring(1);
		}

		return description;
	}

	/**
	 * 转换一个条目
	 * 
	 * @param entry
	 * @return
	 */
	public static MovieSubject convertOneSubject(SubjectEntry entry) {
		MovieSubject book = new MovieSubject();
		if (entry.getTitle() != null) {
			book.setTitle(entry.getTitle().getPlainText());
		}
		book.setDescription(getDescription(entry));
		book.setUrl(entry.getId());
		if (entry.getLink("image", null) != null) {
			book.setImgUrl(entry.getLink("image", null).getHref());
		}
		if (entry.getRating() != null) {
			book.setRating(entry.getRating().getAverage());
		}
		if (entry.getSummary() != null) {
			book.setSummary(entry.getSummary().getPlainText());
		} else {
			book.setSummary("");
		}
		book.setTags(entry.getTags());
		book.setAuthorIntro(getAuthorInfo(entry));
		return book;
	}
	
	/**
	 * @Title: convertMovieSubject
	 * @Description: 通过豆瓣java客户端，获取movie详细信息，存入MovieSubject
	 * @param：entry
	 * @return: MovieSubject
	 * @throws: 
	 */
	public static MovieSubject convertMovieSubject(SubjectEntry entry) {
		MovieSubject movie = new MovieSubject();
		/**
		 * 名称：
		 * 评分：
		 * 类型：
		 * 导演：
		 * 演员：
		 * 时长：
		 * 上映时间:
		 */ 
		//名称
		if (entry.getTitle() != null) {
			movie.setTitle(entry.getTitle().getPlainText());
		}
		Log.v("MovieDetail", entry.getTitle().getPlainText());

		
		//评分
		if (entry.getRating() != null) {
			movie.setRating(entry.getRating().getAverage());
		}else{
			movie.setRating(1f);
		}
		Log.v("MovieDetail", String.valueOf(entry.getRating().getAverage()) + "****");
		
		//类型
		movie.setType(getMovieType(entry));
		//导演
		movie.set_movie_director(getMovieDirector(entry));		
		//演员
		movie.set_cast(getMovieCast(entry));
		//时长
		movie.set_movie_duration(getMovieDuration(entry));
		//上映时间
		movie.set_pub_date(getPubDate(entry));
		//电影介绍
		if (entry.getSummary() != null) {
			movie.setSummary(entry.getSummary().getPlainText());
		} else {
			movie.setSummary("");
		}
		movie.setDescription(getDescription(entry));
		movie.setUrl(entry.getId());	
		//图片
		if (entry.getLink("image", null) != null) {
			movie.setImgUrl(entry.getLink("image", null).getHref());
		}
		movie.setTags(entry.getTags());
		movie.setAuthorIntro(getAuthorInfo(entry));
		return movie;
	}

	private static String getAuthorInfo(SubjectEntry entry) {
		String authorInfo = "";
		for (Attribute attribute : entry.getAttributes()) {
			if ("author-intro".equals(attribute.getName())) {
				authorInfo = attribute.getContent();
			}
		}
		return authorInfo;
	}
	/**
	 * @Title: getPubDate
	 * @Description: 通过豆瓣java客户端，获取电影上映时间
	 * @param：entry
	 * @return: String
	 * @throws: 
	 */
	private static String getPubDate(SubjectEntry entry){
		String pubdate = "";
		for(Attribute attribute : entry.getAttributes()){
			if ("pubdate".equals(attribute.getName()) && (attribute.getContent().length()>=10)){
				pubdate = attribute.getContent().substring(0, 10);
			}else if("pubdate".equals(attribute.getName()) && (attribute.getContent().length()<10)){
				pubdate = attribute.getContent();
			}
		}
		return pubdate;
	}
	/**
	 * @Title: getMovieDuration
	 * @Description: 通过豆瓣java客户端，获取电影时长
	 * @param：entry
	 * @return: String
	 * @throws: 
	 */
	private static String getMovieDuration(SubjectEntry entry){
		String movie_duration = "";
		for(Attribute attribute : entry.getAttributes()){
			if ("movie_duration".equals(attribute.getName())){
				movie_duration = attribute.getContent();
			}
		}
		return movie_duration;
	}
	/**
	 * @Title: getMovieType
	 * @Description: 通过豆瓣java客户端，获取电影类型
	 * @param：entry
	 * @return: String
	 * @throws: 
	 */
	private static String getMovieType(SubjectEntry entry){
		List<String> movie_type = new ArrayList<String>();
		String type = "";
		for(Attribute attribute : entry.getAttributes()){
			if ("movie_type".equals(attribute.getName())){
				movie_type.add(attribute.getContent());
			}
		}
		if(0 == movie_type.size()){
			return type;
		}
		for(int j=0; j<movie_type.size(); j++){
			type = type + movie_type.get(j) + "/";
		}
		String finalType = type.substring(0, type.length()-1);
		return finalType;
	}
	/**
	 * @Title: getMovieDirector
	 * @Description: 通过豆瓣java客户端，获取电影导演
	 * @param：entry
	 * @return: String
	 * @throws: 
	 */
	private static String getMovieDirector(SubjectEntry entry){
		List<String> movie_director = new ArrayList<String>();
		String director = "";
		for(Attribute attribute : entry.getAttributes()){
			if ("director".equals(attribute.getName())){
				movie_director.add(attribute.getContent());
			}	
		}
		if(0 == movie_director.size()){
			return director;
		}
		for(int j=0; j<movie_director.size(); j++){
			director = director + movie_director.get(j) + ",";
		}
		String finalDirector = director.substring(0, director.length()-1);
		return finalDirector;
	}
	/**
	 * @Title: getMovieCast
	 * @Description: 通过豆瓣java客户端，获取电影演员
	 * @param：entry
	 * @return: String
	 * @throws: 
	 */
	private static String getMovieCast(SubjectEntry entry){
		List<String> movie_cast = new ArrayList<String>();
		String cast = "";
		for(Attribute attribute : entry.getAttributes()){
			if ("cast".equals(attribute.getName())){
				movie_cast.add(attribute.getContent());
			}	
		}
		if(movie_cast.size()>=3){
			for(int j=0; j<3; j++){
				cast = cast + movie_cast.get(j) + ",";
			}		
		}else if(movie_cast.size() <3  &&  movie_cast.size()> 0){
			for(int j=0; j<movie_cast.size(); j++){
				cast = cast + movie_cast.get(j) + ",";
			}
		}else{
			cast = "";
		}
		if( "" != cast){
			String finalCast = cast.substring(0, cast.length()-1);
			return finalCast;
		}else{
			return cast;
		}

	}


	// 组装条目描述信息
	private static String getDescription(
			com.google.gdata.data.douban.Subject subject) {
		String description = "";
		List<Attribute> attributes = subject.getAttributes();
		String authors = "";
		for (Person author : subject.getAuthors()) {
			authors += "," + author.getName();
		}
		if (authors.length() > 0) {
			authors = authors.substring(1);
		}

		Map<String, String> map = new HashMap<String, String>();

		for (Attribute attribute : attributes) {
			if (names.contains(attribute.getName())) {
				map.put(attribute.getName(), attribute.getContent());
			}
		}
		map.put("authors", authors);
		for (String name : names) {
			if (map.get(name) != null) {
				if ("price".equals(name)) {
					description += "/" + map.get(name) + "元";
				} else if ("pages".equals(name)) {
					description += "/" + map.get(name) + "页";
				} else {
					description += "/" + map.get(name);
				}
			}
		}
		if (description.length() > 0) {
			description = description.substring(1);
		}

		return description;
	}

	


	/**
	 * @Title: ConvertReviews
	 * @Description: 通过豆瓣java客户端，获取电影演员
	 * @param：feed,entry
	 * @return: List<ReviewSubject>
	 * @throws: 
	 */
	public static List<ReviewSubject> ConvertReviews(ReviewFeed feed, MovieSubject subject) {
		List<ReviewSubject> reviews = new ArrayList<ReviewSubject>();
		for (ReviewEntry entry : feed.getEntries()) {
			ReviewSubject review = new ReviewSubject();
			
			/**
			 * id
			 * 名称
			 * 评论题目
			 * 评论概要
			 * 评论中评分
			 * 评论提交时间
			 */
			if (entry.getId() != null) {
				review.setUrl(entry.getId());
			}
			if (entry.getTitle() != null) {
				review.setTitle(entry.getTitle().getPlainText());
			}
			if (entry.getSummary() != null) {
				review.setSummary(entry.getSummary().getPlainText());
			}
			if (entry.getRating() != null) {
				review.setRating(entry.getRating().getValue());
			}

			if (entry.getUpdated() != null) {
				review.setUpdated(df.format(new Date(entry.getUpdated()
						.getValue())));
			}

			List<Person> authors = entry.getAuthors();
			if (authors != null && authors.size() > 0) {
				Person author = entry.getAuthors().get(0);
				review.setAuthorName(author.getName());
				review.setAuthorId(author.getUri());
			}

			// 如果不存在subject，则获取
			if (subject == null) {
				subject = new MovieSubject();
				com.google.gdata.data.douban.Subject googleSubject = entry
						.getSubjectEntry();
				subject.setTitle(googleSubject.getTitle().getPlainText());
				subject.setDescription(getDescription(googleSubject));
				subject.setUrl(googleSubject.getId());
			}
			review.setSubject(subject);
			reviews.add(review);
		}
		return reviews;
	}

}
