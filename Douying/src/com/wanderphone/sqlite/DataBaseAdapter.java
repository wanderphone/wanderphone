package com.wanderphone.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wanderphone.getxml.MovieSubject;

public class DataBaseAdapter {

	// 表中一条数据的名称
	public static final String KEY_ID = "_id";
	// 表中一条数据的内容
	public static final String KEY_NUM = "num";
	public static final String KEY_NAME = "name";
	public static final String KEY_TYPE = "type";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_PUB_AREA = "pub_area";
	public static final String KEY_CINEMA = "cinema";
	public static final String KEY_RATING = "rating";
	public static final String KEY_IMG = "img";
	public static final String KEY_MOVIEID = "movie_id";
	public static final String KEY_CONTENT = "content";
	// 表中一条数据的id
	public static final String KEY_DATA = "data";
	// 数据库名称为data
	private static final String DB_NAME = "douying.db";
	// 数据库表名
	private static final String DB_TABLE_NP = "nowplaying";

	private static final String DB_TABLE_WEEK = "week";
	private static final String DB_TABLE_BEST = "best";
	// 数据库版本
	private static final int DB_VERSION = 3;
	// 本地Context对象
	private Context mContext = null;
	// 创建一个表
	private static final String DB_CREATE_NP = "create table " + DB_TABLE_NP
			+ " (" + KEY_ID + " integer primary key autoincrement," + KEY_NAME
			+ " text," + KEY_TYPE + " text," + KEY_DURATION + " text,"
			+ KEY_PUB_AREA + " text," + KEY_RATING + " text," + KEY_CINEMA
			+ " text," + KEY_IMG + " blbo)";
	private static final String DB_CREATE_WEEK = "create table "
			+ DB_TABLE_WEEK + "(" + KEY_ID
			+ " integer primary key autoincrement," + KEY_NAME + " text,"
			+ KEY_CONTENT + " text," + KEY_IMG + " blbo)";

	private static final String DB_CREATE_BEST = "create table "
			+ DB_TABLE_BEST + "(" + KEY_ID
			+ " integer primary key autoincrement," + KEY_NAME + " text,"
			+ KEY_IMG + " blbo)";

	// 执行open()打开数据库时，保存返回的数据库对象
	public SQLiteDatabase mSQLiteDatabase = null;
	// 由SQLiteOpenHelper继承过来
	public DatabaseHelper mDatabaseHelper = null;

	// 使用SQLiteOpenHelper
	public static class DatabaseHelper extends SQLiteOpenHelper {
		// 构造函数-创建一个数据库
		DatabaseHelper(Context context) {
			// 当调用getWritableDatabase()或 getReadableDatabase()方法时，创建一个数据库
			super(context, DB_NAME, null, DB_VERSION);
		}

		// 创建一个表
		@Override
		public void onCreate(SQLiteDatabase db) {
			// 数据库没有表时创建一个表
			db.execSQL(DB_CREATE_NP);
			db.execSQL(DB_CREATE_WEEK);
			db.execSQL(DB_CREATE_BEST);
			Log.v("create", DB_CREATE_NP);
			Log.v("create", DB_CREATE_WEEK);
			Log.v("create", DB_CREATE_BEST);
		}

		// 升级数据库，实际项目中不使用此方法，会丢失原数据
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists nowplaying");
			db.execSQL("drop table if exists week");
			db.execSQL("drop table if exists best");
			// onCreate(db);
			onCreate(db);
		}
	}

	public boolean if_best_exists() {
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_BEST, new String[] {
				KEY_NAME}, null, null, null, null, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			return false;
		} else {
			cursor.close();
			return true;
		}

	}
	public boolean if_week_exists() {
		
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_WEEK, new String[] {
				KEY_NAME}, null, null, null, null, null);
		if (cursor.getCount() == 0) {
			Log.v("ss", "0_w");
			cursor.close();
			return false;
		} else {
			cursor.close();
			return true;
		}

	}
	public boolean if_np_exists() {

		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_NP, new String[] {
				KEY_NAME, KEY_TYPE, KEY_DURATION, KEY_PUB_AREA, KEY_CINEMA,
				KEY_RATING, KEY_IMG }, null, null, null, null, null);

		if (cursor.getCount() == 0) {
			Log.v("ss", "0_n");
			cursor.close();
			return false;
		} else {
			cursor.close();
			return true;
		}

	}

	// 构造函数-取得Context
	public DataBaseAdapter(Context context) {
		mContext = context;
	}

	// 打开数据库，返回数据库对象
	public void open() throws SQLException {
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}

	// 关闭数据库
	public void close() {
		mDatabaseHelper.close();
	}
	public void delete_all(){
		String sql_delete = "delete from nowplaying";
		String sql_delete1 = "delete from week";
		String sql_delete2 = "delete from best";
		mSQLiteDatabase.execSQL(sql_delete);
		mSQLiteDatabase.execSQL(sql_delete1);
		mSQLiteDatabase.execSQL(sql_delete2);

	}
	public void delete_all_np() {
		String sql_delete = "delete from nowplaying";
		mSQLiteDatabase.execSQL(sql_delete);
	}

	public void delete_all_week() {
		String sql_delete = "delete from week";
		mSQLiteDatabase.execSQL(sql_delete);
	}

	public void delete_all_best() {
		String sql_delete = "delete from best";
		mSQLiteDatabase.execSQL(sql_delete);
	}

	public long insertWeekData(String name, String content, byte[] img) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_CONTENT, content);
		initialValues.put(KEY_IMG, img);

		return mSQLiteDatabase.insert(DB_TABLE_WEEK, KEY_ID, initialValues);

	}

	public long insertBestData(String name, byte[] img) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_IMG, img);
		return mSQLiteDatabase.insert(DB_TABLE_BEST, KEY_ID, initialValues);

	}

	public List<MovieSubject> loadWeekData() {
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_WEEK, new String[] {
				KEY_NAME, KEY_CONTENT, KEY_IMG }, null, null, null, null, null);
		List<MovieSubject> movies = new ArrayList<MovieSubject>();
		while (cursor.moveToNext()) {
			MovieSubject movie = new MovieSubject();
			movie.setTitle(cursor.getString(0));
			movie.setDescription(cursor.getString(1));
			movie.set_img_bytes(cursor.getBlob(2));
			movies.add(movie);
		}
		cursor.close();
		return movies;
	}

	public List<MovieSubject> loadBestData() {
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_BEST, new String[] {
				KEY_NAME, KEY_IMG }, null, null, null, null, null);
		List<MovieSubject> movies = new ArrayList<MovieSubject>();
		while (cursor.moveToNext()) {
			MovieSubject movie = new MovieSubject();
			movie.setTitle(cursor.getString(0));
			movie.set_img_bytes(cursor.getBlob(1));
			movies.add(movie);
		}
		cursor.close();
		return movies;
	}

	// 插入一条数据
	public long insertNpData(String name, String type, String duration,
			String pub_area, String cinema, String rating, byte[] img) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_TYPE, type);
		initialValues.put(KEY_DURATION, duration);
		initialValues.put(KEY_PUB_AREA, pub_area);
		initialValues.put(KEY_CINEMA, cinema);
		initialValues.put(KEY_RATING, rating);
		initialValues.put(KEY_IMG, img);
		return mSQLiteDatabase.insert(DB_TABLE_NP, KEY_ID, initialValues);
	}

	public List<MovieSubject> loadNpData() {
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_NP, new String[] {
				KEY_NAME, KEY_TYPE, KEY_DURATION, KEY_PUB_AREA, KEY_CINEMA,
				KEY_RATING, KEY_IMG }, null, null, null, null, null);
		List<MovieSubject> movies = new ArrayList<MovieSubject>();
		while (cursor.moveToNext()) {
			MovieSubject movie = new MovieSubject();
			movie.setTitle(cursor.getString(0));
			movie.setType(cursor.getString(1));
			movie.set_movie_duration(cursor.getString(2));
			movie.set_pub_area(cursor.getString(3));
			movie.setSummary(cursor.getString(4));
			movie.setRating(Float.valueOf(cursor.getString(5)));
			movie.set_img_bytes(cursor.getBlob(6));
			movies.add(movie);
		}
		cursor.close();
		return movies;
	}

	// 删除一条数据
	public boolean deleteData(long rowId) {
		return mSQLiteDatabase.delete(DB_TABLE_NP, KEY_ID + "=" + rowId, null) > 0;
	}

	// 删除一条数据
	public void deleteAll() {
		mSQLiteDatabase.delete(DB_TABLE_NP, null, null);
	}

	// 通过Cursor查询所有数据
	public Cursor fetchAllData() {
		Cursor cursor = mSQLiteDatabase.query(DB_TABLE_NP, new String[] {
				KEY_NAME, KEY_TYPE, KEY_DURATION, KEY_PUB_AREA, KEY_CINEMA,
				KEY_RATING }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Log.v("cursor", " name: " + cursor.getString(0) + " type: "
					+ cursor.getString(1) + " duration: " + cursor.getString(2)
					+ " pub_area: " + cursor.getString(3) + " cinema: "
					+ cursor.getString(4) + " rating: " + cursor.getString(5));
		}
		Log.v("cursor", String.valueOf(cursor.getColumnCount()));
		Log.v("cursor", String.valueOf(cursor.getCount()));

		cursor.close();
		return mSQLiteDatabase.query(DB_TABLE_NP, new String[] { KEY_NAME,
				KEY_TYPE, KEY_DURATION, KEY_PUB_AREA, KEY_CINEMA, KEY_RATING },
				null, null, null, null, null);
	}

	// 查询指定数据
	public Cursor fetchData(long rowId) throws SQLException {
		Cursor mCursor = mSQLiteDatabase.query(true, DB_TABLE_NP, new String[] {
				KEY_ID, KEY_NUM, KEY_DATA }, KEY_ID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// 更新一条数据
	public boolean updateData(long rowId, int num, String data) {
		ContentValues args = new ContentValues();
		args.put(KEY_NUM, num);
		args.put(KEY_DATA, data);
		return mSQLiteDatabase.update(DB_TABLE_NP, args, KEY_ID + "=" + rowId,
				null) > 0;
	}

}
