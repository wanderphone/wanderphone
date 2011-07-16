package com.wanderphone.minesweep;

import java.util.Random;

//import com.mobclick.android.MobclickAgent;

//import com.minesweep.R;


import android.app.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

public class ServerActivity extends Activity {
	/****************************************
	 * 扫雷相关变量
	 */
	private TextView txtMineCount;
	private TextView txtTimer;
	private TextView txtTimer2;
	private ImageButton btnSmile;
	private TableLayout mineField; // table layout to add mines to
	private Block blocks[][]; // blocks for mine field	
	private int blockDimension = 50; // width of each block
	private int numberOfRowsInMineField=13 ;
	private int numberOfColumnsInMineField=14 ;
	private int totalNumberOfMines=41 ;
	// timer to keep track of time elapsed
	private Handler timer = new Handler();
	private int findCount = 0;
	private int findCount2 = 0;
	private Boolean clickFlag=true;
	private boolean isTimerStarted; // check if timer already started or not
	private boolean areMinesSet; // check if mines are planted in blocks
	private boolean isGameOver;
	private int minesToFind; // number of mines yet to be discovered
	/***************************
	 * 蓝牙相关变量
	 */
	// Name of the connected device
    private String mConnectedDeviceName = null;
	// Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

	// Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    
    //界面顶端的文字
    private TextView server_tv1;
    //两个button
    private Button server_bt1;
    private Button server_bt2;
    //
    private String tmp="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.serveractivity);
		server_tv1=(TextView)findViewById(R.id.server_tv1);
		server_bt1=(Button)findViewById(R.id.server_bt1);
		server_bt2=(Button)findViewById(R.id.server_bt2);
		server_bt1.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
		server_bt2.getBackground().setColorFilter(0xFFEEEE00, PorterDuff.Mode.MULTIPLY);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        server_bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage("start");
				setContentView(R.layout.multiplayeractivity);
				 /****************************************************
		         * 扫雷相关
		         */
		        txtMineCount = (TextView) findViewById(R.id.MineCount);
				txtTimer = (TextView) findViewById(R.id.Timer);
				txtTimer2 = (TextView) findViewById(R.id.Timer2);
				
				// set font style for timer and mine count to LCD style
				Typeface lcdFont = Typeface.createFromAsset(getAssets(),
						"fonts/lcd2mono.ttf");
				txtMineCount.setTypeface(lcdFont);
				txtTimer.setTypeface(lcdFont);
				txtTimer2.setTypeface(lcdFont);
				btnSmile = (ImageButton) findViewById(R.id.Smiley);
				btnSmile.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						endExistingGame();
						startNewGame();
						setMines();
				       
				        sendMessage(tmp);
				        tmp="";
				     
						
																
					}
				});
				mineField = (TableLayout)findViewById(R.id.MineField);
			}
		});
        server_bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 ensureDiscoverable();
			}
		});
       
		
	}
	 public void onStart() {
	        super.onStart();

	        // If BT is not on, request that it be enabled.
	        // setupChat() will then be called during onActivityResult
	        if (!mBluetoothAdapter.isEnabled()) {
	            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	        // Otherwise, setup the chat session
	        } else {
	            if (mChatService == null) setupChat();
	        }
	    }
	 public synchronized void onResume() {
	        super.onResume();
			//MobclickAgent.onResume(this);


	        // Performing this check in onResume() covers the case in which BT was
	        // not enabled during onStart(), so we were paused to enable it...
	        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
	        if (mChatService != null) {
	            // Only if the state is STATE_NONE, do we know that we haven't started already
	            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
	              // Start the Bluetooth chat services
	              mChatService.start(); 
	            }
	        }
	    }
	 public synchronized void onPause() {
	        super.onPause();
			//MobclickAgent.onPause(this);

	    }

	    @Override
	 public void onStop() {
	        super.onStop();
	        
	    }

	    @Override
	 public void onDestroy() {
	        super.onDestroy();
	        // Stop the Bluetooth chat services
	        if (mChatService != null) mChatService.stop();
	        
	    }
	 private void setupChat() {	      

	        // Initialize the BluetoothChatService to perform bluetooth connections
	        mChatService = new BluetoothChatService(this, mHandler);
	        // Initialize the buffer for outgoing messages
	        mOutStringBuffer = new StringBuffer("");
	    }
	 private void ensureDiscoverable() {
	        
	        if (mBluetoothAdapter.getScanMode() !=
	            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
	            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
	            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
	            startActivity(discoverableIntent);
	        }
	    }
	 /**
	     * Sends a message.
	     * @param message  A string of text to send.
	     */
	    private void sendMessage(String message) {
	        // Check that we're actually connected before trying anything
	        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
	            Toast.makeText(this, R.string.no_connect, Toast.LENGTH_SHORT).show();
	            return;
	        }

	        // Check that there's actually something to send
	        if (message.length() > 0) {
	            // Get the message bytes and tell the BluetoothChatService to write
	            byte[] send = message.getBytes();
	            mChatService.write(send);

	            // Reset out string buffer to zero and clear the edit text field
	            mOutStringBuffer.setLength(0);
	           
	        }
	    }
	 // The Handler that gets information back from the BluetoothChatService
	    private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case MESSAGE_STATE_CHANGE:
	                
	                switch (msg.arg1) {
	                case BluetoothChatService.STATE_CONNECTED:
	                    server_tv1.setText(R.string.connected);
	                    server_tv1.append(mConnectedDeviceName);
	                    server_bt1.setEnabled(true);
	                   
	                    break;
	                case BluetoothChatService.STATE_CONNECTING:
	                    break;
	                case BluetoothChatService.STATE_LISTEN:
	                case BluetoothChatService.STATE_NONE:
	                    server_tv1.setText(R.string.waiting);
	                    break;
	                }
	                break;
	            case MESSAGE_WRITE:
	                byte[] writeBuf = (byte[]) msg.obj;
	                // construct a string from the buffer
	                String writeMessage = new String(writeBuf);
	                
	                break;
	            case MESSAGE_READ:
	                byte[] readBuf = (byte[]) msg.obj;
	                // construct a string from the valid bytes in the buffer
	                String readMessage = new String(readBuf, 0, msg.arg1);
	                int len=readMessage.length();
	               
	                //打开相同位置的按钮
	                int l=readMessage.indexOf(".");
	                //标记相同位置的雷
	                int p=readMessage.indexOf("/");
	                //打开位置数据
	                if(l!=-1)
	                {
	                	int l1=readMessage.indexOf(".", 0);
	                	int y1=Integer.parseInt(readMessage.substring(0, l1));
	                	int y2=Integer.parseInt(readMessage.substring(l1+1, len));
	                	rippleUncover(y1, y2);
	                	for (int row = 0; row < numberOfRowsInMineField + 2; row++)
						{
							for (int column = 0; column < numberOfColumnsInMineField + 2; column++)
							{
								if(blocks[row][column].isCovered())
								{
									blocks[row][column].setEnabled(true);
								}
								//blocks[row][column].isClickable();
								
							}
						}
	                	showDialog(getResources().getString(R.string.change_turn), 500, true, false);
	                }
	                if(p!=-1)
	                {
	                	int p1=readMessage.indexOf("/", 0);
	                	int y1=Integer.parseInt(readMessage.substring(0, p1));
	                	int y2=Integer.parseInt(readMessage.substring(p1+1, len));
	                	blocks[y1][y2].setBlockAsDisabled(false);
						blocks[y1][y2].setFlagIcon(true);
						blocks[y1][y2].setFlagged(true);
						minesToFind--; //reduce mine count
						findCount2++;
						if(findCount2<10)
						{
							txtTimer2.setText("0"+findCount2);
						}
						if(findCount2>=10)
						{
							txtTimer2.setText(""+findCount2);
						}
						updateMineCountDisplay();
						
	                }
	                if(readMessage.equals("win"))
	                {
	                	findCount2++;
	                	txtTimer2.setText(""+findCount2);
	                	loseGame();
	                	for (int row = 0; row < numberOfRowsInMineField + 2; row++)
						{
							for (int column = 0; column < numberOfColumnsInMineField + 2; column++)
							{
									blocks[row][column].setEnabled(false);
								
							}
						}
        				
	                }
	              
	                break;
	            case MESSAGE_DEVICE_NAME:
	                // save the connected device's name
	                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
	                Toast.makeText(getApplicationContext(), "Connected to "
	                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
	                break;
	            case MESSAGE_TOAST:
	                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
	                               Toast.LENGTH_SHORT).show();
	                break;
	            }
	        }
	    };
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        switch (requestCode) {
	        case REQUEST_CONNECT_DEVICE:
	            // When DeviceListActivity returns with a device to connect
	            if (resultCode == Activity.RESULT_OK) {
	                // Get the device MAC address
	                String address = data.getExtras()
	                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
	                // Get the BLuetoothDevice object
	                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
	                // Attempt to connect to the device
	                mChatService.connect(device);
	            }
	            break;
	        case REQUEST_ENABLE_BT:
	            // When the request to enable Bluetooth returns
	            if (resultCode == Activity.RESULT_OK) {
	                // Bluetooth is now enabled, so set up a chat session
	                setupChat();
	            } else {
	                // User did not enable Bluetooth or an error occured
	                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
	                finish();
	            }
	        }
	    }
	    private void startNewGame()
		{
			// plant mines and do rest of the calculations
			createMineField();
			// display all blocks in UI
			showMineField();
			
			minesToFind = totalNumberOfMines;
			isGameOver = false;
			txtMineCount.setText(String.valueOf(totalNumberOfMines));

		}

		private void showMineField()
		{
			// remember we will not show 0th and last Row and Columns
			// they are used for calculation purposes only
			for (int row = 1; row < numberOfRowsInMineField + 1; row++)
			{
				TableRow tableRow = new TableRow(this);  
				tableRow.setLayoutParams(new LayoutParams(blockDimension * numberOfColumnsInMineField, blockDimension));

				for (int column = 1; column < numberOfColumnsInMineField + 1; column++)
				{
					blocks[row][column].setLayoutParams(new LayoutParams(  
							blockDimension,  
							blockDimension)); 
					tableRow.addView(blocks[row][column]);
				}
				mineField.addView(tableRow,new TableLayout.LayoutParams(  
						blockDimension * numberOfColumnsInMineField, blockDimension));  
			}
		}

		private void endExistingGame()
		{
			
			txtTimer.setText("00"); // revert all text
			txtTimer2.setText("00"); 
			txtMineCount.setText("00"); // revert mines count
			
			// remove all rows from mineField TableLayout
			mineField.removeAllViews();
			
			// set all variables to support end of game
			isTimerStarted = false;
			areMinesSet = false;
			isGameOver = false;
			minesToFind = 0;
			findCount=0;
			
		}

		private void createMineField()
		{
			// we take one row extra row for each side
			// overall two extra rows and two extra columns
			// first and last row/column are used for calculations purposes only
			//	 x|xxxxxxxxxxxxxx|x
			//	 ------------------
			//	 x|              |x
			//	 x|              |x
			//	 ------------------
			//	 x|xxxxxxxxxxxxxx|x
			// the row and columns marked as x are just used to keep counts of near by mines

			blocks = new Block[numberOfRowsInMineField + 2][numberOfColumnsInMineField + 2];

			for (int row = 0; row < numberOfRowsInMineField + 2; row++)
			{
				for (int column = 0; column < numberOfColumnsInMineField + 2; column++)
				{	
					blocks[row][column] = new Block(this);
					blocks[row][column].setDefaults();
					

					// pass current row and column number as final int's to event listeners
					// this way we can ensure that each event listener is associated to 
					// particular instance of block only
					final int currentRow = row;
					final int currentColumn = column;

					// add Click Listener
					// this is treated as Left Mouse click
					blocks[row][column].setOnClickListener(new OnClickListener()
					{
						Boolean toast=true;
						public void onClick(View view)
						{
							// start timer on first click
							if (!isTimerStarted)
							{
								
								isTimerStarted = true;
							}

							// set mines on first click
							if (!areMinesSet)
							{
								areMinesSet = true;
								//发送雷得布局								
							}

							// this is not first click
							// check if current block is flagged
							// if flagged the don't do anything
							// as that operation is handled by LongClick
							// if block is not flagged then uncover nearby blocks
							// till we get numbered mines
							if (!blocks[currentRow][currentColumn].isFlagged())
							{
								// open nearby blocks till we get numbered blocks
								if(blocks[currentRow][currentColumn].isCovered())
								{
									rippleUncover(currentRow, currentColumn);
								}
								
								
								for (int row = 0; row < numberOfRowsInMineField + 2; row++)
								{
									for (int column = 0; column < numberOfColumnsInMineField + 2; column++)
									{
										blocks[row][column].setEnabled(false);
										//blocks[row][column].isClickable();
										
									}
								}
								
								
								/*********************************************/
								//客户端打开相同位置
								String Uncover=currentRow+"."+currentColumn;
								
								// did we clicked a mine
								if (blocks[currentRow][currentColumn].hasMine())
								{
									/*************************************/
									blocks[currentRow][currentColumn].setBlockAsDisabled(false);
									blocks[currentRow][currentColumn].setFlagIcon(true);
									blocks[currentRow][currentColumn].setFlagged(true);
									minesToFind--; //reduce mine count
									/*****************
									 * 
									 */
									findCount++;
									if(findCount<10)
									{
										txtTimer.setText("0"+findCount);
									}
									if(findCount>=10)
									{
										txtTimer.setText(""+findCount);
									}
									
									updateMineCountDisplay();
									//发送标志位置
									Uncover=currentRow+"/"+currentColumn;
									for (int row = 0; row < numberOfRowsInMineField + 2; row++)
									{
										for (int column = 0; column < numberOfColumnsInMineField + 2; column++)
										{
											if(blocks[row][column].isCovered())
											{
												blocks[row][column].setEnabled(true);
											}
											toast=false;
										}
									}
									// Oops, game over
								}

								// check if we win the game
								if (checkGameWin())
								{
									// mark game as win									
									winGame();
									
									for (int row = 0; row < numberOfRowsInMineField + 2; row++)
									{
										for (int column = 0; column < numberOfColumnsInMineField + 2; column++)
										{											
												blocks[row][column].setEnabled(false);												
										}
									}
									Uncover="win";
								}
								
								if(toast)
								{
									showDialog(getResources().getString(R.string.wait_other), 500, true, false);
								}
								
								sendMessage(Uncover);
							}
						}
					});					
				}
			}
		}

		private boolean checkGameWin()
		{
			if(findCount==21)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		private void updateMineCountDisplay()
		{
			if (minesToFind < 0)
			{
				txtMineCount.setText(Integer.toString(minesToFind));
			}
			else if (minesToFind < 10)
			{
				txtMineCount.setText("0" + Integer.toString(minesToFind));
			}
			else if (minesToFind < 100)
			{
				txtMineCount.setText("" + Integer.toString(minesToFind));
			}
			else
			{
				txtMineCount.setText(Integer.toString(minesToFind));
			}
		}

		private void winGame()
		{
			
			isTimerStarted = false;
			isGameOver = true;
			minesToFind = 0; //set mine count to 0
			
			//set icon to cool dude
			btnSmile.setBackgroundResource(R.drawable.cool);

			updateMineCountDisplay(); // update mine count

			// disable all buttons
			// set flagged all un-flagged blocks
			for (int row = 1; row < numberOfRowsInMineField + 1; row++)
			{
				for (int column = 1; column < numberOfColumnsInMineField + 1; column++)
				{
					blocks[row][column].setClickable(false);
					if (blocks[row][column].hasMine())
					{
						blocks[row][column].setBlockAsDisabled(false);
						blocks[row][column].setFlagIcon(true);
					}
				}
			}

			showDialog(getResources().getString(R.string.win), 1000, true, false);

		}
		private void loseGame()
		{
			
			
			isGameOver = true;
			minesToFind = 0; //set mine count to 0
			
			//set icon to cool dude
			btnSmile.setBackgroundResource(R.drawable.sad);
			updateMineCountDisplay(); // update mine count

			// disable all buttons
			// set flagged all un-flagged blocks
			for (int row = 1; row < numberOfRowsInMineField + 1; row++)
			{
				for (int column = 1; column < numberOfColumnsInMineField + 1; column++)
				{
					blocks[row][column].setClickable(false);
					if (blocks[row][column].hasMine())
					{
						blocks[row][column].setBlockAsDisabled(false);
						blocks[row][column].setFlagIcon(true);
					}
				}
			}

			showDialog(getResources().getString(R.string.lose), 1000, false, false);

		}

		

		private void setMines()
		{
			// set mines excluding the location where user clicked
			
			Random rand = new Random();
			int mineRow, mineColumn;

			for (int row = 0; row < totalNumberOfMines; row++)
			{
				
				mineRow = rand.nextInt(numberOfColumnsInMineField);				
				mineColumn = rand.nextInt(numberOfRowsInMineField);
				if (!(blocks[mineColumn + 1][mineRow + 1].hasMine()))
				{
					tmp=tmp+mineRow+","+mineColumn+",";
				}
				
		
				if (blocks[mineColumn + 1][mineRow + 1].hasMine())
				{
					row--; // mine is already there, don't repeat for same block
				}
				// plant mine at this location
				blocks[mineColumn + 1][mineRow + 1].plantMine();				
				// exclude the user clicked location				
			}

			int nearByMineCount;

			// count number of mines in surrounding blocks 
			for (int row = 0; row < numberOfRowsInMineField + 2; row++)
			{
				for (int column = 0; column < numberOfColumnsInMineField + 2; column++)
				{
					// for each block find nearby mine count
					nearByMineCount = 0;
					if ((row != 0) && (row != (numberOfRowsInMineField + 1)) && (column != 0) && (column != (numberOfColumnsInMineField + 1)))
					{
						// check in all nearby blocks
						for (int previousRow = -1; previousRow < 2; previousRow++)
						{
							for (int previousColumn = -1; previousColumn < 2; previousColumn++)
							{
								if (blocks[row + previousRow][column + previousColumn].hasMine())
								{
									// a mine was found so increment the counter
									nearByMineCount++;
								}
							}
						}

						blocks[row][column].setNumberOfMinesInSurrounding(nearByMineCount);
					}
					// for side rows (0th and last row/column)
					// set count as 9 and mark it as opened
					else
					{
						blocks[row][column].setNumberOfMinesInSurrounding(9);
						
						blocks[row][column].OpenBlock();
						
						
						//blocks[row][column].setEnabled(false);
						
					}
				}
			}
		}

		private void rippleUncover(int rowClicked, int columnClicked)
		{
			// don't open flagged or mined rows
			if (blocks[rowClicked][columnClicked].hasMine() || blocks[rowClicked][columnClicked].isFlagged())
			{
				return;
			}

			// open clicked block			
			blocks[rowClicked][columnClicked].OpenBlock();
						
			//blocks[rowClicked][columnClicked].setEnabled(false);
			// if clicked block have nearby mines then don't open further
			if (blocks[rowClicked][columnClicked].getNumberOfMinesInSorrounding() != 0 )
			{
				return;
			}

			// open next 3 rows and 3 columns recursively
			for (int row = 0; row < 3; row++)
			{
				for (int column = 0; column < 3; column++)
				{
					// check all the above checked conditions
					// if met then open subsequent blocks
					if (blocks[rowClicked + row - 1][columnClicked + column - 1].isCovered()
							&& (rowClicked + row - 1 > 0) && (columnClicked + column - 1 > 0)
							&& (rowClicked + row - 1 < numberOfRowsInMineField + 1) && (columnClicked + column - 1 < numberOfColumnsInMineField + 1))
					{
						rippleUncover(rowClicked + row - 1, columnClicked + column - 1 );
					} 
				}
			}
			return;
		}
		
		private void showDialog(String changeTurn, int milliseconds, boolean useSmileImage, boolean useCoolImage)
		{
			// show message
			Toast dialog = Toast.makeText(
					getApplicationContext(),
					changeTurn,
					Toast.LENGTH_LONG);

			dialog.setGravity(Gravity.CENTER, 0, 0);
			LinearLayout dialogView = (LinearLayout) dialog.getView();
			ImageView coolImage = new ImageView(getApplicationContext());
			if (useSmileImage)
			{
				coolImage.setImageResource(R.drawable.smallsmile);
			}
			else if (useCoolImage)
			{
				coolImage.setImageResource(R.drawable.smallcool);
			}
			else
			{
				coolImage.setImageResource(R.drawable.smallsad);
			}
			dialogView.addView(coolImage, 0);
			dialog.setDuration(milliseconds);
			dialog.show();
		}

}
