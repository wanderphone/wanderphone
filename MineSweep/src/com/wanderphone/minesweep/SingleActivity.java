package com.wanderphone.minesweep;

import java.util.HashMap;

import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

//import com.minesweep.R;
import com.wanderphone.minesweep.xmlparse.GameMessage;
import com.wanderphone.minesweep.xmlparse.GameMessageParse;
import com.wanderphone.minesweep.xmlparse.HttpClientConnector;
import com.wanderphone.minesweep.xmlparse.SplashMessageParse;

public class SingleActivity extends Activity {
	private TextView txtMineCount;
	private TextView txtTimer;
	private ImageButton btnSmile;
	private SharedPreferences sharedPreferences1;
	private SharedPreferences sharedPreferences2;
	private Boolean soundflag;
	private Boolean vibrateflag;
	//震动
	static Vibrator vibrator;
	//音效的音量   
	static int streamVolume;     
	//定义SoundPool 对象   
	private static SoundPool soundPool;    	  
	//定义HASH表   
	private static HashMap<Integer, Integer> soundPoolMap;  
	// 数据库相关操作
	private ToDoDB myToDoDB;
	// MENU菜单相关
	int menuItemOrder = Menu.NONE; 	 
	int MENU_SOUND=Menu.FIRST;
	int MENU_VIBRATOR=Menu.FIRST + 1;
	int groupId = 0;
	
	private TableLayout mineField; // table layout to add mines to

	private Block blocks[][]; // blocks for mine field
	private int blockDimension = 36; // width of each block
	// private int blockPadding = 0; // padding between blocks

	private int numberOfRowsInMineField;
	private int numberOfColumnsInMineField;
	private int totalNumberOfMines;

	// timer to keep track of time elapsed
	private Handler timer = new Handler();
	private int secondsPassed = 0;

	private boolean isTimerStarted; // check if timer already started or not
	private boolean areMinesSet; // check if mines are planted in blocks
	private boolean isGameOver;
	private int minesToFind; // number of mines yet to be discovered

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singleactivity);
		// 数据库相关
		myToDoDB = new ToDoDB(this);
		//震动
		vibrator=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		//震动和音效标记；
		sharedPreferences1=getSharedPreferences("the_soundflag",MODE_PRIVATE);        
        soundflag=sharedPreferences1.getBoolean("soundflag", true);
		sharedPreferences2=getSharedPreferences("the_vibtateflag",MODE_PRIVATE);        
        vibrateflag=sharedPreferences2.getBoolean("vibrateflag", true);
		// 修改部分，获取难度
		Bundle bundle = this.getIntent().getExtras();
		numberOfRowsInMineField = bundle.getInt("numberOfRowsInMineField");
		numberOfColumnsInMineField = bundle
				.getInt("numberOfColumnsInMineField");
		totalNumberOfMines = bundle.getInt("totalNumberOfMines");
		//
		txtMineCount = (TextView) findViewById(R.id.MineCount);
		txtTimer = (TextView) findViewById(R.id.Timer);

		// set font style for timer and mine count to LCD style
		Typeface lcdFont = Typeface.createFromAsset(getAssets(),
				"fonts/lcd2mono.ttf");
		txtMineCount.setTypeface(lcdFont);
		txtTimer.setTypeface(lcdFont);
		//音效相关初始化
		initSounds();
        loadSfx(R.raw.sound1,123);

		btnSmile = (ImageButton) findViewById(R.id.Smiley);
		mineField = (TableLayout) findViewById(R.id.MineField);

		endExistingGame();
		startNewGame();
		btnSmile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				endExistingGame();
				startNewGame();
			}
		});

	}
	//menu菜单
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		if(soundflag)
		{
			menu.add(groupId, MENU_SOUND, menuItemOrder, R.string.sound_on)
			.setIcon(android.R.drawable.ic_lock_silent_mode_off); 	
		}
		else
		{
			menu.add(groupId, MENU_SOUND, menuItemOrder, R.string.sound_off)
			.setIcon(android.R.drawable.ic_lock_silent_mode); 	
		}
		if(vibrateflag)
		{
			menu.add(groupId, MENU_VIBRATOR, menuItemOrder, R.string.vibrate_on)
			.setIcon(android.R.drawable.btn_star_big_on); 
		}
		else
		{
			menu.add(groupId, MENU_VIBRATOR, menuItemOrder, R.string.vibrate_off)
			.setIcon(android.R.drawable.btn_star_big_off); 
		}
		

    	return true;     	
    } 
	public boolean onOptionsItemSelected(MenuItem item) 
    {
		final int id=item.getItemId();   
    	if(id==MENU_SOUND)
    	{
    		if(soundflag)
    		{
    			soundflag=false;
    			item.setIcon(android.R.drawable.ic_lock_silent_mode);
    			item.setTitle(R.string.sound_off);
    			Editor editor=sharedPreferences1.edit();
      		    editor.putBoolean("soundflag", false);
      		    editor.commit();
    		}
    		else
    		{
    			soundflag=true;
    			item.setIcon(android.R.drawable.ic_lock_silent_mode_off);
    			item.setTitle(R.string.sound_on);
    			Editor editor=sharedPreferences1.edit();
      		    editor.putBoolean("soundflag", true);
      		    editor.commit();
    		}
    	}
    	else if(id==MENU_VIBRATOR)
    	{
    		if(vibrateflag)
    		{
    			vibrateflag=false;
    			item.setIcon(android.R.drawable.btn_star_big_off);
    			item.setTitle(R.string.vibrate_off);
    			Editor editor=sharedPreferences2.edit();
      		    editor.putBoolean("vibrateflag", false);
      		    editor.commit();
    		}
    		else
    		{
    			vibrateflag=true;
    			item.setIcon(android.R.drawable.btn_star_big_on);
    			item.setTitle(R.string.vibrate_on);
    			Editor editor=sharedPreferences2.edit();
      		    editor.putBoolean("vibrateflag", true);
      		    editor.commit();
    		}
    	}
    	  
		return true; 
    }
	/***************************************************************  
	  * Function:     initSounds();  
	  * Parameters:   null  
	  * Returns:      None.  
	  * Description:  初始化声音系统  
	  * Notes:        none.  
	  ***************************************************************/  
	  public void initSounds() {    
	       //初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质   
	      soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 100);    
	  
	      //初始化HASH表   
	      soundPoolMap = new HashMap<Integer, Integer>();    
	          
	      //获得声音设备和设备音量   
	      AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);   
	      streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	  }  
	  /***************************************************************  
	   * Function:     loadSfx();  
	   * Parameters:   null  
	   * Returns:      None.  
	   * Description:  加载音效资源  
	   * Notes:        none.  
	   ***************************************************************/  
	   public void loadSfx(int raw, int ID) {   
	    //把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)   
	    soundPoolMap.put(ID, soundPool.load(SingleActivity.this, raw, ID));    
	   }       
	   
	   /***************************************************************  
	    * Function:     play();  
	    * Parameters:   sound:要播放的音效的ID, loop:循环次数  
	  * Returns:      None.  
	    * Description:  播放声音  
	    * Notes:        none.  
	    ***************************************************************/  
	    public static void play(int sound, int uLoop) {  
	    
	      soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, uLoop, 1f);    
	    }  

	private void startNewGame() {
		// plant mines and do rest of the calculations
		createMineField();
		// display all blocks in UI
		showMineField();

		minesToFind = totalNumberOfMines;
		isGameOver = false;
		secondsPassed = 0;
		txtMineCount.setText(String.valueOf(totalNumberOfMines));

	}

	private void showMineField() {
		// remember we will not show 0th and last Row and Columns
		// they are used for calculation purposes only
		for (int row = 1; row < numberOfRowsInMineField + 1; row++) {
			TableRow tableRow = new TableRow(this);
			tableRow.setLayoutParams(new LayoutParams(blockDimension
					* numberOfColumnsInMineField, blockDimension));

			for (int column = 1; column < numberOfColumnsInMineField + 1; column++) {
				blocks[row][column].setLayoutParams(new LayoutParams(
						blockDimension, blockDimension));
				// blocks[row][column].setPadding(blockPadding, blockPadding,
				// blockPadding, blockPadding);
				tableRow.addView(blocks[row][column]);
			}
			mineField.addView(tableRow,
					new TableLayout.LayoutParams(blockDimension
							* numberOfColumnsInMineField, blockDimension));
		}
	}

	private void endExistingGame() {
		stopTimer(); // stop if timer is running
		txtTimer.setText("000"); // revert all text
		txtMineCount.setText("00"); // revert mines count
		mineField.removeAllViews();

		// set all variables to support end of game
		isTimerStarted = false;
		areMinesSet = false;
		isGameOver = false;
		minesToFind = 0;
	}

	private void createMineField() {
		// we take one row extra row for each side
		// overall two extra rows and two extra columns
		// first and last row/column are used for calculations purposes only
		// x|xxxxxxxxxxxxxx|x
		// ------------------
		// x| |x
		// x| |x
		// ------------------
		// x|xxxxxxxxxxxxxx|x
		// the row and columns marked as x are just used to keep counts of near
		// by mines

		blocks = new Block[numberOfRowsInMineField + 2][numberOfColumnsInMineField + 2];

		for (int row = 0; row < numberOfRowsInMineField + 2; row++) {
			for (int column = 0; column < numberOfColumnsInMineField + 2; column++) {
				blocks[row][column] = new Block(this);
				blocks[row][column].setDefaults();

				// pass current row and column number as final int's to event
				// listeners
				// this way we can ensure that each event listener is associated
				// to
				// particular instance of block only
				final int currentRow = row;
				final int currentColumn = column;

				// add Click Listener
				// this is treated as Left Mouse click
				blocks[row][column].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						// start timer on first click
						if (!isTimerStarted) {
							startTimer();
							isTimerStarted = true;
						}

						// set mines on first click
						if (!areMinesSet) {
							areMinesSet = true;
							setMines(currentRow, currentColumn);
						}

						// this is not first click
						// check if current block is flagged
						// if flagged the don't do anything
						// as that operation is handled by LongClick
						// if block is not flagged then uncover nearby blocks
						// till we get numbered mines
						if (!blocks[currentRow][currentColumn].isFlagged()) {
							// open nearby blocks till we get numbered blocks
							rippleUncover(currentRow, currentColumn);

							// did we clicked a mine
							if (blocks[currentRow][currentColumn].hasMine()) {
								// Oops, game over
								//爆炸音效
								if(soundflag)
								{
									play(123, 0);
								}
								else
								{
									
								}
								//震动
							    if(vibrateflag)
								{									
									long[] pattern = {50,180,}; // OFF/ON/OFF/ON...								
							        vibrator.vibrate(pattern, -1);
								}
							    else
							    {
							    	
							    }
								
								finishGame(currentRow, currentColumn);
							}

							// check if we win the game
							if (checkGameWin()) {
								// mark game as win
								winGame();
							}
						}
					}
				});

				// add Long Click listener
				// this is treated as right mouse click listener
				blocks[row][column]
						.setOnLongClickListener(new OnLongClickListener() {
							public boolean onLongClick(View view) {
								// simulate a left-right (middle) click
								// if it is a long click on an opened mine then
								// open all surrounding blocks
								if (!blocks[currentRow][currentColumn]
										.isCovered()
										&& (blocks[currentRow][currentColumn]
												.getNumberOfMinesInSorrounding() > 0)
										&& !isGameOver) {
									int nearbyFlaggedBlocks = 0;
									for (int previousRow = -1; previousRow < 2; previousRow++) {
										for (int previousColumn = -1; previousColumn < 2; previousColumn++) {
											if (blocks[currentRow + previousRow][currentColumn
													+ previousColumn]
													.isFlagged()) {
												nearbyFlaggedBlocks++;
											}
										}
									}

									// if flagged block count is equal to nearby
									// mine count
									// then open nearby blocks
									if (nearbyFlaggedBlocks == blocks[currentRow][currentColumn]
											.getNumberOfMinesInSorrounding()) {
										for (int previousRow = -1; previousRow < 2; previousRow++) {
											for (int previousColumn = -1; previousColumn < 2; previousColumn++) {
												// don't open flagged blocks
												if (!blocks[currentRow
														+ previousRow][currentColumn
														+ previousColumn]
														.isFlagged()) {
													// open blocks till we get
													// numbered block
													rippleUncover(
															currentRow
																	+ previousRow,
															currentColumn
																	+ previousColumn);

													// did we clicked a mine
													if (blocks[currentRow
															+ previousRow][currentColumn
															+ previousColumn]
															.hasMine()) {
														// oops game over
														finishGame(
																currentRow
																		+ previousRow,
																currentColumn
																		+ previousColumn);

													}

													// did we win the game
													if (checkGameWin()) {
														// mark game as win
														winGame();
													}
												}
											}
										}
									}

									// as we no longer want to judge this
									// gesture so return
									// not returning from here will actually
									// trigger other action
									// which can be marking as a flag or
									// question mark or blank
									return true;
								}

								// if clicked block is enabled, clickable or
								// flagged
								if (blocks[currentRow][currentColumn]
										.isClickable()
										&& (blocks[currentRow][currentColumn]
												.isEnabled() || blocks[currentRow][currentColumn]
												.isFlagged())) {

									// for long clicks set:
									// 1. empty blocks to flagged
									// 2. flagged to question mark
									// 3. question mark to blank

									// case 1. set blank block to flagged
									if (!blocks[currentRow][currentColumn]
											.isFlagged()
											&& !blocks[currentRow][currentColumn]
													.isQuestionMarked()) {
										blocks[currentRow][currentColumn]
												.setBlockAsDisabled(false);
										blocks[currentRow][currentColumn]
												.setFlagIcon(true);
										blocks[currentRow][currentColumn]
												.setFlagged(true);
										minesToFind--; // reduce mine count
										updateMineCountDisplay();
									}
									// case 2. set flagged to question mark
									else if (!blocks[currentRow][currentColumn]
											.isQuestionMarked()) {
										blocks[currentRow][currentColumn]
												.setBlockAsDisabled(true);
										blocks[currentRow][currentColumn]
												.setQuestionMarkIcon(true);
										blocks[currentRow][currentColumn]
												.setFlagged(false);
										blocks[currentRow][currentColumn]
												.setQuestionMarked(true);

										minesToFind++; // increase mine count
										updateMineCountDisplay();
									}
									// case 3. change to blank square
									else {
										blocks[currentRow][currentColumn]
												.setBlockAsDisabled(true);
										blocks[currentRow][currentColumn]
												.clearAllIcons();
										blocks[currentRow][currentColumn]
												.setQuestionMarked(false);
										// if it is flagged then increment mine
										// count
										if (blocks[currentRow][currentColumn]
												.isFlagged()) {
											minesToFind++; // increase mine
															// count
											updateMineCountDisplay();
										}
										// remove flagged status
										blocks[currentRow][currentColumn]
												.setFlagged(false);
									}

									updateMineCountDisplay(); // update mine
																// display
								}

								return true;
							}
						});
			}
		}
	}

	private boolean checkGameWin() {
		for (int row = 1; row < numberOfRowsInMineField + 1; row++) {
			for (int column = 1; column < numberOfColumnsInMineField + 1; column++) {
				if (!blocks[row][column].hasMine()
						&& blocks[row][column].isCovered()) {
					return false;
				}
			}
		}
		return true;
	}

	private void updateMineCountDisplay() {
		if (minesToFind < 0) {
			txtMineCount.setText(Integer.toString(minesToFind));
		} else if (minesToFind < 10) {
			txtMineCount.setText("0" + Integer.toString(minesToFind));
		} else if (minesToFind < 100) {
			txtMineCount.setText(Integer.toString(minesToFind));
		} else {
			txtMineCount.setText(Integer.toString(minesToFind));
		}
	}

	private void winGame() {
		stopTimer();
		isTimerStarted = false;
		isGameOver = true;
		minesToFind = 0; // set mine count to 0

		// set icon to cool dude
		//btnSmile.setBackgroundResource(R.drawable.cool);

		updateMineCountDisplay(); // update mine count

		// disable all buttons
		// set flagged all un-flagged blocks
		for (int row = 1; row < numberOfRowsInMineField + 1; row++) {
			for (int column = 1; column < numberOfColumnsInMineField + 1; column++) {
				blocks[row][column].setClickable(false);
				if (blocks[row][column].hasMine()) {
					blocks[row][column].setBlockAsDisabled(false);
					blocks[row][column].setFlagIcon(true);
				}
			}
		}
		showscorediag();

	}

	String levelDetail;
	int finish_time;
	int finish_level;
	String timeNow, timeBefore, rankBefore, rankNow;
	int flag = 0;
	GameMessage gameMessage = new GameMessage();

	private void showscorediag(){
		finish_level = numberOfRowsInMineField;
		Log.v("finish_level", Integer.toString(finish_level));
		finish_time = secondsPassed;
		Log.v("finish_time",String.valueOf(finish_time));
		switch (finish_level) {
		case 9:
			levelDetail = "easy";
			break;
		case 16:
			levelDetail = "normal";
			break;
		case 20:
			levelDetail = "hard";
			break;
		}
		Log.v("level", levelDetail);

		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		final String uniqueId = deviceUuid.toString();
		new AsyncTask<Void, Void, Boolean>(){

			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				try{
					final String gameReturnUrl = getResources().getString(R.string.websit) + "?phone_id="
						+ uniqueId + "&which_use=3&level=" + levelDetail + "&time="
						+ finish_time;
					String gameReturnString = HttpClientConnector.getStringByUrl(gameReturnUrl);
					if(gameReturnString != ""&&gameReturnString!=null)
					{
						gameMessage = GameMessageParse.parse(gameReturnString);
						if(gameMessage != null)
							return true;
					}
				//Log.v("gameReturnUrl", gameReturnUrl );

				}catch (Exception e){
					e.printStackTrace();
				}
				return false;
			}
			protected void onPostExecute(Boolean result){
				super.onPostExecute(result);
				if(result){
					if(gameMessage.getRankThis().equals("no")||gameMessage.getRankThis()==null)
						flag = 1;
					else
					{
						timeNow = gameMessage.getTimeThis();
						rankNow = gameMessage.getRankThis();
						timeBefore = gameMessage.getTimeBefore();
						rankBefore = gameMessage.getRankBefore();
						Log.v("rankNow", rankNow);
						if (gameMessage != null && !rankNow.equals("no")) {
							 //Dialog dialog = new Dialog(SingleActivity.this, R.style.dialog);
							 Dialog dialog = new AlertDialog.Builder(SingleActivity.this)
									.setIcon(R.drawable.cool)
									.setTitle("congratulation")
									.setMessage("您的本次成绩是" + gameMessage.getTimeThis() + "s; 在全国排名为"
													+ gameMessage.getRankThis() + "\n"
													+ "您的最好成绩是" + gameMessage.getTimeBefore()
													+ "s; 在全国排名为" + gameMessage.getRankBefore())
									.setPositiveButton(R.string.go_on_game, new DialogInterface.OnClickListener(){
										 public void onClick(DialogInterface dialog, int which) {
										     // TODO Auto-generated method stub
												btnSmile.setBackgroundResource(R.drawable.ib_forward);
										     }
										 }).setNegativeButton(R.string.select_level_again, new DialogInterface.OnClickListener(){
											 public void onClick(DialogInterface dialog, int which) {
											     // TODO Auto-generated method stub
												 Intent intent = new Intent();
													intent.setClass(SingleActivity.this, SelectActivity.class);
													startActivity(intent);
													SingleActivity.this.finish();
											     }
											}).create();
							dialog.show();
						}
					}
				}else{
					Toast.makeText(SingleActivity.this, "Congratulations!", Toast.LENGTH_SHORT)
								.show();
				}
			}
			
		}.execute();
	}

	private void finishGame(int currentRow, int currentColumn) {
		isGameOver = true; // mark game as over
		stopTimer(); // stop timer
		isTimerStarted = false;
		btnSmile.setBackgroundResource(R.drawable.mine_start);

		// show all mines
		// disable all blocks

		for (int row = 1; row < numberOfRowsInMineField + 1; row++) {
			for (int column = 1; column < numberOfColumnsInMineField + 1; column++) {
				// disable block
				// blocks[row][column].setBlockAsDisabled(false);
				blocks[row][column].setClickable(false);
				blocks[row][column].setEnabled(false);

				// block has mine and is not flagged
				if (blocks[row][column].hasMine()
						&& !blocks[row][column].isFlagged()) {
					// set mine icon
					blocks[row][column].setMineIcon(false);
				}

				// block is flagged and doesn't not have mine
				if (!blocks[row][column].hasMine()
						&& blocks[row][column].isFlagged()) {
					// set flag icon
					blocks[row][column].setFlagIcon(false);
				}

				// block is flagged
				if (blocks[row][column].isFlagged()) {
					// disable the block
					blocks[row][column].setClickable(false);
				}

			}
		}

		// trigger mine
		blocks[currentRow][currentColumn].triggerMine();
	}

	private void setMines(int currentRow, int currentColumn) {
		// set mines excluding the location where user clicked
		Random rand = new Random();
		int mineRow, mineColumn;

		for (int row = 0; row < totalNumberOfMines; row++) {
			mineRow = rand.nextInt(numberOfColumnsInMineField);
			mineColumn = rand.nextInt(numberOfRowsInMineField);
			if ((mineRow + 1 != currentColumn)
					|| (mineColumn + 1 != currentRow)) {
				if (blocks[mineColumn + 1][mineRow + 1].hasMine()) {
					row--; // mine is already there, don't repeat for same block
				}
				// plant mine at this location
				blocks[mineColumn + 1][mineRow + 1].plantMine();
			}
			// exclude the user clicked location
			else {
				row--;
			}
		}

		int nearByMineCount;

		// count number of mines in surrounding blocks
		for (int row = 0; row < numberOfRowsInMineField + 2; row++) {
			for (int column = 0; column < numberOfColumnsInMineField + 2; column++) {
				// for each block find nearby mine count
				nearByMineCount = 0;
				if ((row != 0) && (row != (numberOfRowsInMineField + 1))
						&& (column != 0)
						&& (column != (numberOfColumnsInMineField + 1))) {
					// check in all nearby blocks
					for (int previousRow = -1; previousRow < 2; previousRow++) {
						for (int previousColumn = -1; previousColumn < 2; previousColumn++) {
							if (blocks[row + previousRow][column
									+ previousColumn].hasMine()) {
								// a mine was found so increment the counter
								nearByMineCount++;
							}
						}
					}
					blocks[row][column]
							.setNumberOfMinesInSurrounding(nearByMineCount);
				}
				// for side rows (0th and last row/column)
				// set count as 9 and mark it as opened
				else {
					blocks[row][column].setNumberOfMinesInSurrounding(9);
					blocks[row][column].OpenBlock();
				}
			}
		}
	}

	private void rippleUncover(int rowClicked, int columnClicked) {
		// don't open flagged or mined rows
		if (blocks[rowClicked][columnClicked].hasMine()
				|| blocks[rowClicked][columnClicked].isFlagged()) {
			return;
		}

		// open clicked block
		blocks[rowClicked][columnClicked].OpenBlock();
		blocks[rowClicked][columnClicked].setClickable(false);

		// if clicked block have nearby mines then don't open further
		if (blocks[rowClicked][columnClicked].getNumberOfMinesInSorrounding() != 0) {
			return;
		}

		// open next 3 rows and 3 columns recursively
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 3; column++) {
				// check all the above checked conditions
				// if met then open subsequent blocks
				if (blocks[rowClicked + row - 1][columnClicked + column - 1]
						.isCovered()
						&& (rowClicked + row - 1 > 0)
						&& (columnClicked + column - 1 > 0)
						&& (rowClicked + row - 1 < numberOfRowsInMineField + 1)
						&& (columnClicked + column - 1 < numberOfColumnsInMineField + 1)) {
					rippleUncover(rowClicked + row - 1, columnClicked + column
							- 1);
				}
			}
		}
		return;
	}

	public void startTimer() {
		if (secondsPassed == 0) {
			timer.removeCallbacks(updateTimeElasped);
			// tell timer to run call back after 1 second
			timer.postDelayed(updateTimeElasped, 1000);
		}
	}

	public void stopTimer() {
		// disable call backs
		timer.removeCallbacks(updateTimeElasped);
	}

	// timer call back when timer is ticked
	private Runnable updateTimeElasped = new Runnable() {
		public void run() {
			long currentMilliseconds = System.currentTimeMillis();
			++secondsPassed;

			if (secondsPassed < 10) {
				txtTimer.setText("00" + Integer.toString(secondsPassed));
			} else if (secondsPassed < 100) {
				txtTimer.setText("0" + Integer.toString(secondsPassed));
			} else {
				txtTimer.setText(Integer.toString(secondsPassed));
			}

			// add notification
			timer.postAtTime(this, currentMilliseconds);
			// notify to call back after 1 seconds
			// basically to remain in the timer loop
			timer.postDelayed(updateTimeElasped, 1000);
		}
	};

	private void showDialog(String message, int milliseconds,
			boolean useSmileImage, boolean useCoolImage) {
		// show message
		Toast dialog = Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_LONG);

		dialog.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout dialogView = (LinearLayout) dialog.getView();
		ImageView coolImage = new ImageView(getApplicationContext());
		if (useSmileImage) {
			coolImage.setImageResource(R.drawable.cool);
		} else if (useCoolImage) {
			coolImage.setImageResource(R.drawable.cool);
		} else {
			coolImage.setImageResource(R.drawable.sad);
		}
		dialogView.addView(coolImage, 0);
		dialog.setDuration(milliseconds);
		dialog.show();
	}

	// 将新纪录保存到数据库中
	private void saveScore() {
		switch (totalNumberOfMines) {

		case 10:
			Cursor cursor1 = myToDoDB.myquery1("Single:\\Easy");

			if (cursor1.getCount() == 0) {
				myToDoDB.insert("Single:\\Easy",
						Integer.toString(secondsPassed));

			} else {

				cursor1.moveToFirst();
				int id1 = cursor1.getInt(0);

				int score1 = Integer.parseInt(cursor1.getString(2));
				// 小于现存的成绩时更新成绩
				if (secondsPassed < score1) {
					myToDoDB.update(id1, "Single:\\Easy",
							Integer.toString(secondsPassed));
				}
			}

			break;
		case 20:
			Cursor cursor2 = myToDoDB.myquery1("Single:\\Medium");
			if (cursor2.getCount() == 0) {
				myToDoDB.insert("Single:\\Medium",
						Integer.toString(secondsPassed));
			} else {
				int id2 = cursor2.getInt(0);
				int score2 = Integer.parseInt(cursor2.getString(2));
				// 小于现存的成绩时更新成绩
				if (secondsPassed < score2) {
					myToDoDB.update(id2, "Single:\\Medium",
							Integer.toString(secondsPassed));
				}
			}

			break;
		case 25:
			Cursor cursor3 = myToDoDB.myquery1("Single:\\Hard");
			if (cursor3.getCount() == 0) {
				myToDoDB.insert("Single:\\Hard",
						Integer.toString(secondsPassed));
			} else {
				int id3 = cursor3.getInt(0);
				int score3 = Integer.parseInt(cursor3.getString(2));
				// 小于现存的成绩时更新成绩
				if (secondsPassed < score3) {
					myToDoDB.update(id3, "Single:\\Hard",
							Integer.toString(secondsPassed));
				}
			}
			break;
		}
		showDialog(
				"congratulations! You create a new record: "
						+ Integer.toString(secondsPassed) + " seconds!", 1000,
				false, true);
	}

	// Activity暂停时关闭数据库操作
	protected void onPause() {
		// TODO Auto-generated method stub
		myToDoDB.close();
		super.onPause();
	}

	// Activity onResume时加载数据库
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}