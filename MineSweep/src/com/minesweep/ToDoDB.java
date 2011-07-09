package com.minesweep; 
import android.content.ContentValues; 
import android.content.Context; 
import android.database.Cursor; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class ToDoDB extends SQLiteOpenHelper
{
  private final static String DATABASE_NAME = "todo_db";
  private final static int DATABASE_VERSION = 1;
  private final static String TABLE_NAME = "todo_table1"; 
  public final static String FIELD_id = "_id";
  public final static String FIELD_KEY = "todo_text1"; 
  public final static String FIELD_SCORE="todo_score1";
  public ToDoDB(Context context) 
  { 
    super(context, DATABASE_NAME, null, DATABASE_VERSION); }
  @Override 
  public void onCreate(SQLiteDatabase db)
  {
    /* 建表 */ 
    String sql = "CREATE TABLE " 
      + TABLE_NAME + " (" + FIELD_id +
      " INTEGER primary key autoincrement, "
       + FIELD_KEY +" text"+", "+FIELD_SCORE+ " text);"; 
    db.execSQL(sql); 
    } 
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
  { 
    String sql = "DROP TABLE IF EXISTS " 
      + TABLE_NAME; db.execSQL(sql);
      onCreate(db); 
      }
  public Cursor select() 
  { 
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
    return cursor;
    }
  public long insert(String text1,String text2)
  { 
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(FIELD_KEY, text1);
    cv.put(FIELD_SCORE, text2);
    long row = db.insert(TABLE_NAME, null, cv); 
    return row; 
    } 
  public void delete(int id) 
  { 
    SQLiteDatabase db = this.getWritableDatabase();
    String where = FIELD_id + " = ?"; 
    String[] whereValue = { Integer.toString(id) };
    db.delete(TABLE_NAME, where, whereValue); 
    } 
  public void update(int id, String text1,String text2) 
  { 
    SQLiteDatabase db = this.getWritableDatabase(); 
    String where = FIELD_id + " = ?";
    String[] whereValue = { Integer.toString(id) }; 
    /* ���޸ĵ�ֵ����ContentValues */ 
    ContentValues cv = new ContentValues(); 
    cv.put(FIELD_KEY, text1); 
    cv.put(FIELD_SCORE, text2);
    db.update(TABLE_NAME, cv, where, whereValue); 
    }
public Cursor myquery1(String s) {
	// TODO Auto-generated method stub
	SQLiteDatabase db = this.getReadableDatabase();
	String how=FIELD_KEY+" = '"+s+"'";
    Cursor cursor = db.query(TABLE_NAME, null,how ,null, null, null, null);
    return cursor;
}
public Cursor myquery2(String s) {
	// TODO Auto-generated method stub
	SQLiteDatabase db = this.getReadableDatabase();
	String how=FIELD_SCORE+" = '"+s+"'";
    Cursor cursor = db.query(TABLE_NAME, null,how ,null, null, null, null);
    return cursor;
}
public Cursor myquery3(String s,String k) {
	// TODO Auto-generated method stub
	SQLiteDatabase db = this.getReadableDatabase();
	String how=FIELD_KEY+" = '"+s+"'"+"AND "+FIELD_SCORE+" = '"+k+"'";
    Cursor cursor = db.query(TABLE_NAME, null,how ,null, null, null, null);
    return cursor;
}
  }