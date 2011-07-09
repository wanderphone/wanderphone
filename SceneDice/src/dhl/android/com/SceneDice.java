package dhl.android.com;

import java.util.List;

import dhl.android.com.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
//主Activity
public class SceneDice extends Activity implements SensorEventListener

{
	//数据库相关
	private ToDoDB myToDoDB;
	private Cursor myCursor;
	//震动
	static Vibrator vibrator;
	// 震动标记
	private static boolean vflag;
	//模式选择标记
	private int arg;
	
	//预设模式选择标记
	public int flag=1;
	private WindowManager.LayoutParams lp;

	//传给GlRender的标记
	public int activityflag=1;
	
    //模式选择中的ListView
	private ListView lv3;
	
	//模式选择对话框
	private Dialog dlg2;
	
	// MENU菜单相关
	int menuItemOrder = Menu.NONE; 	 
	int MENU_DEFINED=Menu.FIRST;
	int MENU_MODE=Menu.FIRST + 1;
	int MENU_SHARE=Menu.FIRST+2; 
	int MENU_HELP=Menu.FIRST+3;
	int MENU_ABOUT=Menu.FIRST+4;
	int groupId = 0; 	
	
	
	GLRender render;
	
	//感应器
	private boolean			mRegisteredSensor;
	private SensorManager		mSensorManager;
	//屏幕锁
	private PowerManager mPowerManager;	
	private WakeLock mWakeLock;


	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
		// 关闭屏幕锁
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        //调整屏幕亮度
        lp = getWindow().getAttributes();   
        lp.screenBrightness = 0.6f;// 1.0f ��-0.0f ֮��,1.0f,
        getWindow().setAttributes(lp);
		
		
		
		//构建模型
		render= new GLRender(activityflag);		 
	    GLImage.load(this.getResources(),flag);			    		
	    GLSurfaceView glView = new GLSurfaceView(this);
		glView.setRenderer(render);
		setContentView(glView);
		
		//震动
		vibrator=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		mRegisteredSensor = false;
		//ȡ��SensorManagerʵ��
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0)
		{
			Sensor sensor = sensors.get(0);
			//ע��SensorManager
			mRegisteredSensor = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
		}		 	 
	}
	
	//menu菜单
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		
		menu.add(groupId, MENU_MODE, menuItemOrder, R.string.string11)
		.setIcon(android.R.drawable.ic_menu_view); 	
	
		
		menu.add(groupId, MENU_DEFINED, menuItemOrder, R.string.string1)
		.setIcon(android.R.drawable.ic_menu_manage); 
		
		menu.add(groupId, MENU_SHARE, menuItemOrder, R.string.string43)
		.setIcon(android.R.drawable.ic_menu_share); 
		
		menu.add(groupId, MENU_HELP, menuItemOrder, R.string.string53)
		.setIcon(android.R.drawable.ic_menu_help); 
		
		menu.add(groupId, MENU_ABOUT, menuItemOrder, R.string.string54)
		.setIcon(android.R.drawable.ic_menu_info_details); 
		
		
	 
		
		//true��ʾҪ��ʾmenu; false��ʾ����ʾmenu ;
    	return true;     	
    } 
	
	//Menuѡ��ѡ����;
	public boolean onOptionsItemSelected(MenuItem item) 
    {     
    	  final int id=item.getItemId();
    	  
    	
    	 
    	  //���ò˵�
    	  if(id==MENU_DEFINED)
    	  {
    		  
    		  Intent intent=new Intent();
    		  intent.setClass(SceneDice.this, Setting.class);
    		  startActivity(intent);
    		  
    		
    	  }
    	  //ģʽѡ��˵�
    	  if(id==MENU_MODE)
    	  {    	  		
	    	  SimpleCursorAdapter adapter3 = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_single_choice, 
	    			  myCursor, new String[] { ToDoDB.FIELD_TEXT}, new int[] { android.R.id.text1});	    	    		
	    	  LayoutInflater factory2=LayoutInflater.from(SceneDice.this);
	    	  final View DialogView2=factory2.inflate(R.layout.mylayout2, null);	    	 
	    	  lv3=(ListView)DialogView2.findViewById(R.id.lv3);
	    	  lv3.setChoiceMode(ListView.CHOICE_MODE_SINGLE); 
	    	  lv3.setAdapter(adapter3);
	    	  
	    	  lv3.setItemsCanFocus(false);
	    	  //ģʽ���������
	    	  lv3.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub		
						arg=arg2;					   							 			       
					}
					});  	
	    	  
	    	  
	    	  
	    	  //dialog����ʾ�Զ��������ԭʼ��ݵ�listview��
	    	  dlg2=new AlertDialog.Builder(SceneDice.this)
	    	  .setTitle(R.string.string11)
	    	  .setView(DialogView2)
	    	  .setPositiveButton(R.string.string9,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(arg==0)
					{														
						
			    		 activityflag=1;
			    		 render= new GLRender(activityflag);
			    		 GLImage.load(SceneDice.this.getResources(),2);
			    		 GLSurfaceView glView = new GLSurfaceView(SceneDice.this);
			    		 glView.setRenderer(render);
			    		 setContentView(glView);
			    		 dlg2.dismiss();
			    		
					}
					//����ģʽ
					if(arg==1)
				    {
						
						activityflag=1;
						render= new GLRender(activityflag);
						GLImage.load(SceneDice.this.getResources(),1);
						GLSurfaceView glView = new GLSurfaceView(SceneDice.this);
						glView.setRenderer(render);
						setContentView(glView);
						dlg2.dismiss();
						
				    }
					if(arg==2)
				    {
						
						activityflag=1;
						render= new GLRender(activityflag);
						GLImage.load(SceneDice.this.getResources(),3);
						GLSurfaceView glView = new GLSurfaceView(SceneDice.this);
						glView.setRenderer(render);
						setContentView(glView);
						dlg2.dismiss();
						
				    }
					else if(arg!=0&&arg!=1&&arg!=2) {
						  
						    myCursor.moveToPosition(arg);			        
	 			            myCursor.getInt(0); 
 		    	   
 							//��ȡ�Զ���ģʽ�ĸ���ѡ����ݣ����ԡ�,��������
 			 			    String string2=myCursor.getString(2); 
 			 			    //jΪ��,����������nΪ���ַ����ҡ�,���ķ��ؽ��-1Ϊû�ҵ���
 			 			    int j,n;
 			 			    n=string2.indexOf(",");
 			 			    j=0;
 			 			    if(n!=-1)
 			 			    {
 			 			    	
 			 			    	 j=j+1;
 			 			    	 n=string2.indexOf(",", n+1);
 			 			    	 if(n!=-1)
 			 			    	 {
 			 			    		 j=j+1;
 			 			    		 n=string2.indexOf(",", n+1);
 			 			    		 if(n!=-1)
 			 			    		 {
 			 			    			 j=j+1;
 			 			    			 n=string2.indexOf(",", n+1);
 			 			    			 if(n!=-1)
 			 			    			 {
 			 			    				 j=j+1;
 			 			    				 n=string2.indexOf(",", n+1);
 			 			    				 if(n!=-1)
 			 			    				 {
 			 			    					 j=j+1;
 			 			    				 }
 			 			    			 }
 			 			    		 }
 			 			    	  }			 			    	 			 			    	  
 			 			      }
 			 			      if(j==0)
 			 			      {
 			 			    	  
 			 			      }
 			 			      //������Զ���
 			 			      if(j==1)
 			 			      {			 			    	
 			 					 activityflag=1;
 			 					 render= new GLRender(activityflag);
 			 			  		 GLImage.load2(SceneDice.this.getResources(),myCursor,2);
 			 			  		 GLSurfaceView glView = new GLSurfaceView(SceneDice.this);
 			 			  		 glView.setRenderer(render);
 			 			  		 setContentView(glView); 
 			 			      }
 			 			      //������Զ���
 			 			      if(j==2)
 			 			      {
 			 			    	 activityflag=1;
 			 					 render= new GLRender(activityflag); 					 
 			 					 GLImage.load3(SceneDice.this.getResources(),myCursor,2);
 			 			  		 GLSurfaceView glView = new GLSurfaceView(SceneDice.this);
 			 			  		 glView.setRenderer(render);
 			 			  		 setContentView(glView);
 			 			      }
 			 			      //������Զ���
 			 			      if(j==3)
 			 			      {
 			 			    	 activityflag=4;
 			 					 render= new GLRender(activityflag);
 			 					GLImage.load4(SceneDice.this.getResources(),myCursor,2);
 			 			  		 GLSurfaceView glView = new GLSurfaceView(SceneDice.this);
 			 			  		 glView.setRenderer(render);
 			 			  		 setContentView(glView);
 			 			      }
 			 			      //������Զ���
 			 			      if(j==4)
 			 			      {
 			 			    	 activityflag=5;
 			 					 render= new GLRender(activityflag);
 			 					GLImage.load5(SceneDice.this.getResources(),myCursor,2);
 			 			  		 GLSurfaceView glView = new GLSurfaceView(SceneDice.this);
 			 			  		 glView.setRenderer(render);
 			 			  		 setContentView(glView);
 			 			      }
 			 			      //������Զ���
 			 			      if(j==5)
 			 			      {
 			 			    	 activityflag=1;
 			 					 render= new GLRender(activityflag);
 			 					 GLImage.load6(SceneDice.this.getResources(),myCursor,2);
 			 			  		 GLSurfaceView glView = new GLSurfaceView(SceneDice.this);
 			 			  		 glView.setRenderer(render);
 			 			  		 setContentView(glView);
 			 			      }
 			 			     
 			 			      dlg2.dismiss();
 						}
					
				}
			})
			  .setNegativeButton(R.string.string10, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					dlg2.dismiss();
				}
			})
	    	  .create();
	    	  
	    	  dlg2.show();
	    	  
    			
    	   }
    	  if(id==MENU_SHARE)
    	  {
    		  Intent intent=new Intent(Intent.ACTION_SEND);   	      
    	      intent.setType("text/plain");
    	      intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
    	      intent.putExtra(Intent.EXTRA_TEXT, "在www.wanderphone.com上有个不错的应用叫做超级骰子");
    	      startActivity(Intent.createChooser(intent, getTitle()));
    	      

    	
    	  }
    	  if(id==MENU_HELP)
    	  {
    		   Intent intent=new Intent();
    		   intent.setClass(SceneDice.this, HelpActivity.class);
    		   startActivity(intent);
    		 
    	  }
    	  if(id==MENU_ABOUT)
    	  {
    		  Intent intent=new Intent();
   		   	  intent.setClass(SceneDice.this, AboutActivity.class);
   		   	   startActivity(intent);
    	  }
    	 
		return true;
    	
    	  }
	public static void abc() {
		// TODO Auto-generated method stub

		
    	if(vflag)
    	{	
        long[] pattern = {200, 110, 200, 110,220,100,230,90,240,80,240,70,250,70}; // OFF/ON/OFF/ON...
        vibrator.vibrate(pattern, -1);//-1���ظ�����-1Ϊ��pattern��ָ���±꿪ʼ�ظ���
    	}
    	else
    	{
    		//do nothing
    	}
	}	
	
	protected void onResume()
	{
		myToDoDB = new ToDoDB(this);
		myCursor=myToDoDB.select();
		if(myCursor.getCount()==0)
		{
			  myToDoDB.insert(this.getString(R.string.string20),this.getString(R.string.string33)+","+this.getString(R.string.string34)+","+this.getString(R.string.string35)+","+this.getString(R.string.string36)+","+this.getString(R.string.string37)+","+this.getString(R.string.string38));
			  myToDoDB.insert(this.getString(R.string.string21),this.getString(R.string.string27)+","+this.getString(R.string.string28)+","+this.getString(R.string.string29)+","+this.getString(R.string.string30)+","+this.getString(R.string.string31)+","+this.getString(R.string.string32) );
			  myToDoDB.insert(this.getString(R.string.string58),this.getString(R.string.string59)+","+this.getString(R.string.string60)+","+this.getString(R.string.string61)+","+this.getString(R.string.string62)+","+this.getString(R.string.string63)+","+this.getString(R.string.string64) );			  
			  myCursor.requery();
			 			  
		}
		SharedPreferences sharedPreferences3=getSharedPreferences("vmy_flag",MODE_PRIVATE);		
	    vflag=sharedPreferences3.getBoolean("vflag", false);   
		super.onResume();
		mWakeLock.acquire();
		//����SensorManager��һ���б�(Listener)
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0)
		{
			Sensor sensor = sensors.get(0);
			//ע��SensorManager
			mRegisteredSensor = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		}
	}
	@Override
	//��onPause()������ȡ�������ע�᣻
	protected void onPause()
	{
		myCursor.close();
		myToDoDB.close();
		
		 mWakeLock.release();
		if (mRegisteredSensor)
		{
			mSensorManager.unregisterListener(this);
			mRegisteredSensor = false;			
		}
		super.onPause();
	}
	
	//���������ȱ仯ʱ���ã�
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	
	}
	//������ֵ�仯ʱ����
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//����render�еĴ��������?��
		render.onSensorChanged(event);		
		
	}
	
	
}


//绘制图片
class GLImage
{
	//����������ͼƬ��
	public static Bitmap mBitmap1=null;
	public static Bitmap mBitmap2=null;
	public static Bitmap mBitmap3=null;
	public static Bitmap mBitmap4=null;
	public static Bitmap mBitmap5=null;
	public static Bitmap mBitmap6=null;
	//����ͼƬ��
	public static Bitmap wood;
	//�������ֺ���
	public static void load(Resources resources, int flag )
	{
		//����ģʽ
		if(flag==1)
		{
			//��һ��ͼƬ
			int n=(resources.getString(R.string.string27)).length();
			wood=BitmapFactory.decodeResource(resources, R.drawable.tt);
			Bitmap mBitmap11 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			.copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap11 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap11);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			//����ʱ
			if(n==2)
			{
			p.setTextSize(50);
			c.drawText(resources.getString(R.string.string27), 12.5f,77.5f, p);
			}
			//Ӣ��ʱ
			if(n>2)
			{
				int k=(resources.getString(R.string.string27)).indexOf(",");
				String string1=(resources.getString(R.string.string27)).substring(0, k);
				String string2=(resources.getString(R.string.string27)).substring(k+1, n);
				p.setTextSize(42.5f);
				c.drawText(string1, 15.0f,52.5f, p);
				c.drawText(string2, 5.0f,102.5f, p);
			}
			p.setAntiAlias(true);             //���巴���			
			c.save();
			mBitmap1=mBitmap11;
			mBitmap11=null;
			}}
		    //�ڶ���ͼƬ
		    int n2=(resources.getString(R.string.string28)).length();
			Bitmap mBitmap12 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap12 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c1 = new Canvas(mBitmap12);
			Paint p1 = new Paint();
			p1.setColor(Color.rgb(255,127, 36));
			if(n2==2)
			{
			p1.setTextSize(50);
			c1.drawText(resources.getString(R.string.string28), 12.5f,77.5f, p1);
			}
			if(n2>2)
			{
				int k=(resources.getString(R.string.string28)).indexOf(",");
				String string1=(resources.getString(R.string.string28)).substring(0, k);
				String string2=(resources.getString(R.string.string28)).substring(k+1, n2);
				p1.setTextSize(50);
				c1.drawText(string1, 12.5f,52.5f, p1);
				c1.drawText(string2, 12.5f,102.5f, p1);
			}
			p1.setAntiAlias(true);             //���巴���			
			c1.save();
			mBitmap2=mBitmap12;
			mBitmap12=null;
			}}
		    //������
		    int n3=(resources.getString(R.string.string29)).length();
			Bitmap mBitmap13 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);;
		    if (mBitmap13 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c2 = new Canvas(mBitmap13);
			Paint p2 = new Paint();
			p2.setColor(Color.rgb(255,127, 36));
			if(n3==2)
			{
			p2.setTextSize(50);
			c2.drawText(resources.getString(R.string.string29), 12.5f,77.5f, p2);
			}
			if(n3>2)
			{
				int k=(resources.getString(R.string.string29)).indexOf(",");
				String string1=(resources.getString(R.string.string29)).substring(0, k);
				String string2=(resources.getString(R.string.string29)).substring(k+1, n3);
				p2.setTextSize(50);
				c2.drawText(string1, 12.5f,52.5f, p2);
				c2.drawText(string2, 12.5f,102.5f, p2);
			}
			p2.setAntiAlias(true);             //���巴���			
			c2.save();
			mBitmap3=mBitmap13;
			mBitmap13=null;
			}}
		    //������
		    Bitmap mBitmap14 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap14 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c3 = new Canvas(mBitmap14);
			Paint p3 = new Paint();
			p3.setColor(Color.rgb(255,127, 36));
			p3.setTextSize(50);
			p3.setAntiAlias(true);             //���巴���
			c3.drawText(resources.getString(R.string.string30), 12.5f,77.5f, p3);
			c3.save();
			mBitmap4=mBitmap14;
			mBitmap14=null;
			}}
		    //������
		    Bitmap mBitmap15 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			.copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap15 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c4 = new Canvas(mBitmap15);
			Paint p4= new Paint();
			p4.setColor(Color.rgb(255,127, 36));
			
			p4.setTextSize(50);
			p4.setAntiAlias(true);             //���巴���
			c4.drawText(resources.getString(R.string.string31), 12.5f,77.5f, p4);
			c4.save();
			mBitmap5=mBitmap15;
			mBitmap15=null;
			}}
		    //������
		    int n6=(resources.getString(R.string.string32)).length();
		    Bitmap mBitmap16 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap16 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c2 = new Canvas(mBitmap16);
			Paint p2 = new Paint();
			p2.setColor(Color.rgb(255,127, 36));
			if(n6==2)
			{
			p2.setTextSize(50);
			c2.drawText(resources.getString(R.string.string32), 12.5f,77.5f, p2);
			}
			if(n6>2)
			{
				int k=(resources.getString(R.string.string32)).indexOf(",");
				String string1=(resources.getString(R.string.string32)).substring(0, k);
				String string2=(resources.getString(R.string.string32)).substring(k+1, n6);
				p2.setTextSize(42.5f);
				c2.drawText(string1, 12.5f,52.5f, p2);
				p2.setTextSize(42.5f);
				c2.drawText(string2, 5.0f,102.5f, p2);
			}
			p2.setAntiAlias(true);             //���巴���
			c2.save();
			mBitmap6=mBitmap16;
			mBitmap16=null;
			}}
		
		}
		//�ư�ģʽ
		if(flag==2)
		{
			
	 
			wood=BitmapFactory.decodeResource(resources, R.drawable.tt);
			 int n=(resources.getString(R.string.string33)).length();
			Bitmap mBitmap11 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888,true);
		    if (mBitmap11 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c21 = new Canvas(mBitmap11);
			Paint p21 = new Paint();
			p21.setColor(Color.rgb(255,127, 36));
			if(n==3)
			{
				p21.setTextSize(37.5f);
				c21.drawText(resources.getString(R.string.string33), 7.5f,77.5f, p21);
			}
			if(n>3)
			{
				int k=(resources.getString(R.string.string33)).indexOf(",");
				String string1=(resources.getString(R.string.string33)).substring(0, k);
				String string2=(resources.getString(R.string.string33)).substring(k+1, n);
				p21.setTextSize(50);
				c21.drawText(string1, 12.5f,52.5f, p21);
				c21.drawText(string2, 12.5f,102.5f, p21);
			}
			
			p21.setAntiAlias(true);             //���巴���
			
			c21.save();
			mBitmap1=mBitmap11;
			mBitmap11=null;
			}}
		    int n2=(resources.getString(R.string.string34)).length();
			Bitmap mBitmap12 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap12 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c1 = new Canvas(mBitmap12);
			Paint p1 = new Paint();
			p1.setColor(Color.rgb(255,127, 36));
			if(n2==3)
			{
				p1.setTextSize(37.5f);
				c1.drawText(resources.getString(R.string.string34), 7.5f,77.5f, p1);
			}
			if(n2>3)
			{
				int k=(resources.getString(R.string.string34)).indexOf(",");
				String string1=(resources.getString(R.string.string34)).substring(0, k);
				String string2=(resources.getString(R.string.string34)).substring(k+1, n2);
				p1.setTextSize(50);
				c1.drawText(string1, 12.5f,52.5f, p1);
				c1.drawText(string2, 12.50f,102.5f, p1);
			}
		
			p1.setAntiAlias(true);             //���巴���
			
			c1.save();
			mBitmap2=mBitmap12;
			mBitmap12=null;
			}}
		    int n3=(resources.getString(R.string.string35)).length();
			Bitmap mBitmap13 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap13 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c2 = new Canvas(mBitmap13);
			Paint p2 = new Paint();
			p2.setColor(Color.rgb(255,127, 36));
			if(n3==3)
			{
				p2.setTextSize(37.5f);
				c2.drawText(resources.getString(R.string.string35), 7.5f,77.5f, p2);
			}
			if(n3>3)
			{
				int k=(resources.getString(R.string.string35)).indexOf(",");
				String string1=(resources.getString(R.string.string35)).substring(0, k);
				String string2=(resources.getString(R.string.string35)).substring(k+1, n3);
				p2.setTextSize(50);
				c2.drawText(string1, 12.5f,52.5f, p2);
				c2.drawText(string2, 5.0f,102.5f, p2);
			}
			
			p2.setAntiAlias(true);             //���巴���
			
			c2.save();
			mBitmap3=mBitmap13;
			mBitmap13=null;
			}}
		    int n4=(resources.getString(R.string.string36)).length();
		    Bitmap mBitmap14 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);;
		    if (mBitmap14 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c3 = new Canvas(mBitmap14);
			Paint p3 = new Paint();
			p3.setColor(Color.rgb(255,127, 36));
			
			if(n4==2)
			{
				p3.setTextSize(50);
				c3.drawText(resources.getString(R.string.string36), 12.5f,77.5f, p3);
			}
			if(n4>2)
			{
				p3.setTextSize(35);
				c3.drawText(resources.getString(R.string.string36), 5.0f,77.5f, p3);
			}
			p3.setAntiAlias(true);             //���巴���
			
			c3.save();
			mBitmap4=mBitmap14;
			mBitmap14=null;
			}}
		    int n5=(resources.getString(R.string.string37)).length();
		    Bitmap mBitmap15 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap15 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c4 = new Canvas(mBitmap15);
			Paint p4= new Paint();
			p4.setColor(Color.rgb(255,127, 36));
			if(n5==2)
			{
				p4.setTextSize(50);
				c4.drawText(resources.getString(R.string.string37), 12.5f,77.5f, p4);
			}
			if(n5>2)
			{
				p4.setTextSize(35);
				c4.drawText(resources.getString(R.string.string37), 5.0f,77.5f, p4);
			}
			
			p4.setAntiAlias(true);             //���巴���
			
			c4.save();
			mBitmap5=mBitmap15;
			mBitmap15=null;
			}}
		    int n6=(resources.getString(R.string.string38)).length();
		    Bitmap mBitmap16 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap16 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c2 = new Canvas(mBitmap16);
			Paint p2 = new Paint();
			p2.setColor(Color.rgb(255,127, 36));
			
			if(n6==2)
			{
				p2.setTextSize(50);
				c2.drawText(resources.getString(R.string.string38), 7.5f,77.5f, p2);
			}
			if(n6>2)
			{
				int k=(resources.getString(R.string.string38)).indexOf(",");
				String string1=(resources.getString(R.string.string38)).substring(0, k);
				String string2=(resources.getString(R.string.string38)).substring(k+1, n6);
				p2.setTextSize(50);
				c2.drawText(string1, 12.5f,52.5f, p2);
				c2.drawText(string2, 12.5f,102.5f, p2);
			}
			p2.setAntiAlias(true);             //���巴���
			
			c2.save();
			mBitmap6=mBitmap16;
			mBitmap16=null;
			}}
		}
		
		if(flag==3)
		{
			wood=BitmapFactory.decodeResource(resources, R.drawable.tt);
			 int n=(resources.getString(R.string.string59)).length();
			Bitmap mBitmap11 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888,true);
		    if (mBitmap11 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c21 = new Canvas(mBitmap11);
			Paint p21 = new Paint();
			p21.setColor(Color.rgb(255,127, 36));
			if(n==3)
			{
				p21.setTextSize(37.5f);
				c21.drawText(resources.getString(R.string.string59), 7.5f,77.5f, p21);
			}
			if(n>3)
			{
				p21.setTextSize(50);
				c21.drawText(resources.getString(R.string.string59), 7.5f,77.5f, p21);
			}
			
			p21.setAntiAlias(true);             //���巴���
			
			c21.save();
			mBitmap1=mBitmap11;
			mBitmap11=null;
			}}
		    int n2=(resources.getString(R.string.string60)).length();
			Bitmap mBitmap12 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap12 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c1 = new Canvas(mBitmap12);
			Paint p1 = new Paint();
			p1.setColor(Color.rgb(255,127, 36));
			if(n2==3)
			{
				p1.setTextSize(37.5f);
				c1.drawText(resources.getString(R.string.string60), 7.5f,77.5f, p1);
			}
			if(n2>3)
			{
				p1.setTextSize(50);
				c1.drawText(resources.getString(R.string.string60), 7.5f,77.5f, p1);
			}
		
			p1.setAntiAlias(true);             //���巴���
			
			c1.save();
			mBitmap2=mBitmap12;
			mBitmap12=null;
			}}
		    int n3=(resources.getString(R.string.string61)).length();
			Bitmap mBitmap13 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap13 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c2 = new Canvas(mBitmap13);
			Paint p2 = new Paint();
			p2.setColor(Color.rgb(255,127, 36));
			if(n3==3)
			{
				p2.setTextSize(37.5f);
				c2.drawText(resources.getString(R.string.string61), 7.5f,77.5f, p2);
			}
			if(n3>3)
			{
				p2.setTextSize(50);
				c2.drawText(resources.getString(R.string.string61), 7.5f,77.5f, p2);
			}
			
			p2.setAntiAlias(true);             //���巴���
			
			c2.save();
			mBitmap3=mBitmap13;
			mBitmap13=null;
			}}
		    int n4=(resources.getString(R.string.string62)).length();
		    Bitmap mBitmap14 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);;
		    if (mBitmap14 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c3 = new Canvas(mBitmap14);
			Paint p3 = new Paint();
			p3.setColor(Color.rgb(255,127, 36));
			
			if(n4==3)
			{
				p3.setTextSize(37.5f);
				c3.drawText(resources.getString(R.string.string62), 7.5f,77.5f, p3);
			}
			if(n4>3)
			{
				p3.setTextSize(20);
				c3.drawText(resources.getString(R.string.string62), 5.0f,77.5f, p3);
			}
			p3.setAntiAlias(true);             //���巴���
			
			c3.save();
			mBitmap4=mBitmap14;
			mBitmap14=null;
			}}
		    int n5=(resources.getString(R.string.string63)).length();
		    Bitmap mBitmap15 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap15 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c4 = new Canvas(mBitmap15);
			Paint p4= new Paint();
			p4.setColor(Color.rgb(255,127, 36));
			if(n5==3)
			{
				p4.setTextSize(37.5f);
				c4.drawText(resources.getString(R.string.string63), 7.5f,77.5f, p4);
			}
			if(n5>3)
			{
				int k=(resources.getString(R.string.string63)).indexOf(",");
				String string1=(resources.getString(R.string.string63)).substring(0, k);
				String string2=(resources.getString(R.string.string63)).substring(k+1, n5);
				p4.setTextSize(37.5f);
				c4.drawText(string1, 30.5f,52.5f, p4);
				p4.setTextSize(25f);
				c4.drawText(string2, 5.0f,80.5f, p4);
			}
			
			p4.setAntiAlias(true);             //���巴���
			
			c4.save();
			mBitmap5=mBitmap15;
			mBitmap15=null;
			}}
		    int n6=(resources.getString(R.string.string64)).length();
		    Bitmap mBitmap16 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap16 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c2 = new Canvas(mBitmap16);
			Paint p2 = new Paint();
			p2.setColor(Color.rgb(255,127, 36));
			
			if(n6==3)
			{
				p2.setTextSize(37.5f);
				c2.drawText(resources.getString(R.string.string64), 7.5f,77.5f, p2);
			}
			if(n6>3)
			{
				int k=(resources.getString(R.string.string64)).indexOf(",");
				String string1=(resources.getString(R.string.string64)).substring(0, k);
				String string2=(resources.getString(R.string.string64)).substring(k+1, n6);
				p2.setTextSize(30);
				c2.drawText(string1, 12.5f,52.5f, p2);
				c2.drawText(string2, 12.5f,102.5f, p2);
			}
			p2.setAntiAlias(true);             //���巴���
			
			c2.save();
			mBitmap6=mBitmap16;
			mBitmap16=null;
			}}
		}
			
		
		
	}
	
	//��ѡ������ַ�ͼƬ
	public static void load2(Resources resources, Cursor cursor,int loadint )
	{
			//����Ǹն���ã�
		    if(loadint==1)
		    {
		    	//k-1Ϊ���һ�����ID
		    	int k=cursor.getCount();
				//������һ�����
			    cursor.moveToPosition(k-1);
		    }
		    //��ȡ����õĸ���ѡ����ַ���ݣ��м��á�,�������ģ�
		    String string2=cursor.getString(2);
		    //string3 ,string4�ֱ��Ǹ���ѡ���ֵ��
		    String string3;
		    String string4;
		    int i=string2.length();
		    int n=string2.indexOf(",");
		    string3=string2.substring(0,n);
		    string4=string2.substring(n+1,i);
		    //��ȡѡ���ַ�ĳ����Ա���Ƶ�ͼƬ�ϣ�
		    int tmp=string3.length();
		    int tmp2=string4.length();
		    
			wood=BitmapFactory.decodeResource(resources, R.drawable.tt);
			Bitmap mBitmap11 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap11 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap11);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp==1)
			{
				p.setTextSize(50);
				c.drawText(string3, 37.5f,77.5f, p);
			}
			if(tmp==2)
			{
				p.setTextSize(50);
				c.drawText(string3, 12.5f,77.5f, p);
			}
			if(tmp==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string3, 7.5f,77.5f, p);
			}
			if(tmp==4)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			if(tmp>4&&tmp<=8)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp>8&&tmp<=12)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, 8);
				String str3=string3.substring(8, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp>12)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			
			p.setAntiAlias(true);             //���巴���
			c.save();
			mBitmap1=mBitmap11;
			mBitmap11=null;
			}}
		    Bitmap mBitmap12 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap12 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap12);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp2==1)
			{
				p.setTextSize(50);
				c.drawText(string4, 37.5f,77.5f, p);
			}
			if(tmp2==2)
			{
				p.setTextSize(50);
				c.drawText(string4, 12.5f,77.5f, p);
			}
			if(tmp2==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string4, 7.5f,77.5f, p);
			}
			if(tmp2==4)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			if(tmp2>4&&tmp2<=8)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp2>8&&tmp2<=12)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, 8);
				String str3=string4.substring(8, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp2>12)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			c.save();
			mBitmap2=mBitmap12;
			mBitmap12=null;
			}}
		    Bitmap mBitmap13 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap13 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap13);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp==1)
			{
				p.setTextSize(50);
				c.drawText(string3, 37.5f,77.5f, p);
			}
			if(tmp==2)
			{
				p.setTextSize(50);
				c.drawText(string3, 12.5f,77.5f, p);
			}
			if(tmp==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string3, 7.5f,77.5f, p);
			}
			if(tmp==4)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			if(tmp>4&&tmp<=8)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp>8&&tmp<=12)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, 8);
				String str3=string3.substring(8, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp>12)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap3=mBitmap13;
			mBitmap13=null;
			}}
		    Bitmap mBitmap14 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap14 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap14);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp2==1)
			{
				p.setTextSize(50);
				c.drawText(string4, 37.5f,77.5f, p);
			}
			if(tmp2==2)
			{
				p.setTextSize(50);
				c.drawText(string4, 12.5f,77.5f, p);
			}
			if(tmp2==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string4, 7.5f,77.5f, p);
			}
			if(tmp2==4)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			if(tmp2>4&&tmp2<=8)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp2>8&&tmp2<=12)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, 8);
				String str3=string4.substring(8, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp2>12)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			c.save();
			mBitmap4=mBitmap14;
			mBitmap14=null;
			}}
		    Bitmap mBitmap15 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap15 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap15);
			Paint p= new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp==1)
			{
				p.setTextSize(50);
				c.drawText(string3, 37.5f,77.5f, p);
			}
			if(tmp==2)
			{
				p.setTextSize(50);
				c.drawText(string3, 12.5f,77.5f, p);
			}
			if(tmp==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string3, 7.5f,77.5f, p);
			}
			if(tmp==4)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			if(tmp>4&&tmp<=8)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp>8&&tmp<=12)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, 8);
				String str3=string3.substring(8, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp>12)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			c.save();
			mBitmap5=mBitmap15;
			mBitmap15=null;
			}}
		    Bitmap mBitmap16 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap16 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap16);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp2==1)
			{
				p.setTextSize(50);
				c.drawText(string4, 37.5f,77.5f, p);
			}
			if(tmp2==2)
			{
				p.setTextSize(50);
				c.drawText(string4, 12.5f,77.5f, p);
			}
			if(tmp2==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string4, 7.5f,77.5f, p);
			}
			if(tmp2==4)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			if(tmp2>4&&tmp2<=8)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp2>8&&tmp2<=12)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, 8);
				String str3=string4.substring(8, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp2>12)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap6=mBitmap16;
			mBitmap16=null;
			}}
		   
			

		
		
	}
	//��ѡ�����ͼƬ
	public static void load3(Resources resources, Cursor cursor,int loadint )
	{	
			if(loadint==1)
			{
			//k-1Ϊ���һ�����ID
			int k=cursor.getCount();
			//������һ�����
		    cursor.moveToPosition(k-1);
			}
		    String string2=cursor.getString(2);
		    String string3;
		    String string4;
		    String string5;
		    int i=string2.length();
		    int n=string2.indexOf(",");
		    int n1=string2.indexOf(",", n+1);
		    string3=string2.substring(0,n);
		    string4=string2.substring(n+1,n1);
		    string5=string2.substring(n1+1,i);
		    int tmp=string3.length();
		    int tmp2=string4.length();
		    int tmp3=string5.length();
			wood=BitmapFactory.decodeResource(resources, R.drawable.tt);
			Bitmap mBitmap11 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap11 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap11);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp==1)
			{
				p.setTextSize(50);
				c.drawText(string3, 37.5f,77.5f, p);
			}
			if(tmp==2)
			{
				p.setTextSize(50);
				c.drawText(string3, 12.5f,77.5f, p);
			}
			if(tmp==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string3, 7.5f,77.5f, p);
			}
			if(tmp==4)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			if(tmp>4&&tmp<=8)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp>8&&tmp<=12)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, 8);
				String str3=string3.substring(8, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp>12)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap1=mBitmap11;
			mBitmap11=null;
			}}
		    Bitmap mBitmap12 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap12 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap12);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp2==1)
			{
				p.setTextSize(50);
				c.drawText(string4, 37.5f,77.5f, p);
			}
			if(tmp2==2)
			{
				p.setTextSize(50);
				c.drawText(string4, 12.5f,77.5f, p);
			}
			if(tmp2==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string4, 7.5f,77.5f, p);
			}
			if(tmp2==4)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			if(tmp2>4&&tmp2<=8)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp2>8&&tmp2<=12)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, 8);
				String str3=string4.substring(8, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp2>12)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			c.save();
			mBitmap2=mBitmap12;
			mBitmap12=null;
			}}
		    Bitmap mBitmap13 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap13 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap13);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp3==1)
			{
				p.setTextSize(50);
				c.drawText(string5, 37.5f,77.5f, p);
			}
			if(tmp3==2)
			{
				p.setTextSize(50);
				c.drawText(string5, 12.5f,77.5f, p);
			}
			if(tmp3==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string5, 7.5f,77.5f, p);
			}
			if(tmp3==4)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			if(tmp3>4&&tmp3<=8)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp3>8&&tmp3<=12)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, 8);
				String str3=string5.substring(8, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp3>12)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap3=mBitmap13;
			mBitmap13=null;
			}}
		    Bitmap mBitmap14 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap14 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap14);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp==1)
			{
				p.setTextSize(50);
				c.drawText(string3, 37.5f,77.5f, p);
			}
			if(tmp==2)
			{
				p.setTextSize(50);
				c.drawText(string3, 12.5f,77.5f, p);
			}
			if(tmp==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string3, 7.5f,77.5f, p);
			}
			if(tmp==4)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			if(tmp>4&&tmp<=8)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp>8&&tmp<=12)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, 8);
				String str3=string3.substring(8, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp>12)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap4=mBitmap14;
			mBitmap14=null;
			}}
		    Bitmap mBitmap15 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap15 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap15);
			Paint p= new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp2==1)
			{
				p.setTextSize(50);
				c.drawText(string4, 37.5f,77.5f, p);
			}
			if(tmp2==2)
			{
				p.setTextSize(50);
				c.drawText(string4, 12.5f,77.5f, p);
			}
			if(tmp2==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string4, 7.5f,77.5f, p);
			}
			if(tmp2==4)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			if(tmp2>4&&tmp2<=8)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp2>8&&tmp2<=12)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, 8);
				String str3=string4.substring(8, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp2>12)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap5=mBitmap15;
			mBitmap15=null;
			}}
		    Bitmap mBitmap16 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap16 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap16);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp3==1)
			{
				p.setTextSize(50);
				c.drawText(string5, 37.5f,77.5f, p);
			}
			if(tmp3==2)
			{
				p.setTextSize(50);
				c.drawText(string5, 12.5f,77.5f, p);
			}
			if(tmp3==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string5, 7.5f,77.5f, p);
			}
			if(tmp3==4)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			if(tmp3>4&&tmp3<=8)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp3>8&&tmp3<=12)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, 8);
				String str3=string5.substring(8, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp3>12)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			c.save();
			mBitmap6=mBitmap16;
			mBitmap16=null;
			}}
		    
	}
	 //��ѡ�����ͼƬ
	public static void load4(Resources resources, Cursor cursor,int loadint )
	{	
			if(loadint==1)
			{
			//kΪ���һ�����ID
			int k=cursor.getCount();
			//������һ�����
		    cursor.moveToPosition(k-1);
			}
		    String string2=cursor.getString(2);
		    String string3;
		    String string4;
		    String string5;
		    String string6;
		    int i=string2.length();
		    int n=string2.indexOf(",");
		    int n1=string2.indexOf(",", n+1);
		    int n2=string2.indexOf(",", n1+1);
		    string3=string2.substring(0,n);
		    string4=string2.substring(n+1,n1);
		    string5=string2.substring(n1+1, n2);
		    string6=string2.substring(n2+1, i);
		    int tmp=string3.length();
		    int tmp2=string4.length();
		    int tmp3=string5.length();
		    int tmp4=string6.length();
			wood=BitmapFactory.decodeResource(resources, R.drawable.tt);
			Bitmap mBitmap11 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap11 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap11);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp==1)
			{
				p.setTextSize(50);
				c.drawText(string3, 37.5f,77.5f, p);
			}
			if(tmp==2)
			{
				p.setTextSize(50);
				c.drawText(string3, 12.5f,77.5f, p);
			}
			if(tmp==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string3, 7.5f,77.5f, p);
			}
			if(tmp==4)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			if(tmp>4&&tmp<=8)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp>8&&tmp<=12)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, 8);
				String str3=string3.substring(8, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp>12)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			c.save();
			mBitmap1=mBitmap11;
			mBitmap11=null;
			}}
		    Bitmap mBitmap12 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap12 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap12);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp2==1)
			{
				p.setTextSize(50);
				c.drawText(string4, 37.5f,77.5f, p);
			}
			if(tmp2==2)
			{
				p.setTextSize(50);
				c.drawText(string4, 12.5f,77.5f, p);
			}
			if(tmp2==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string4, 7.5f,77.5f, p);
			}
			if(tmp2==4)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			if(tmp2>4&&tmp2<=8)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp2>8&&tmp2<=12)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, 8);
				String str3=string4.substring(8, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp2>12)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap2=mBitmap12;
			mBitmap12=null;
			}}
		    Bitmap mBitmap13 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap13 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap13);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp3==1)
			{
				p.setTextSize(50);
				c.drawText(string5, 37.5f,77.5f, p);
			}
			if(tmp3==2)
			{
				p.setTextSize(50);
				c.drawText(string5, 12.5f,77.5f, p);
			}
			if(tmp3==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string5, 7.5f,77.5f, p);
			}
			if(tmp3==4)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			if(tmp3>4&&tmp3<=8)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp3>8&&tmp3<=12)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, 8);
				String str3=string5.substring(8, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp3>12)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap3=mBitmap13;
			mBitmap13=null;
			}}
		    Bitmap mBitmap14 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap14 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap14);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp4==1)
			{
				p.setTextSize(50);
				c.drawText(string6, 37.5f,77.5f, p);
			}
			if(tmp4==2)
			{
				p.setTextSize(50);
				c.drawText(string6, 12.5f,77.5f, p);
			}
			if(tmp4==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string6, 7.5f,77.5f, p);
			}
			if(tmp4==4)
			{
				p.setTextSize(30);
				c.drawText(string6, 5.0f,77.5f, p);
			}
			if(tmp4>4&&tmp4<=8)
			{
				String str1=string6.substring(0, 4);
				String str2=string6.substring(4, tmp4);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp4>8&&tmp4<=12)
			{
				String str1=string6.substring(0, 4);
				String str2=string6.substring(4, 8);
				String str3=string6.substring(8, tmp4);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp4>12)
			{
				p.setTextSize(30);
				c.drawText(string6, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap4=mBitmap14;
			mBitmap14=null;
			}}
		    Bitmap mBitmap15 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap15 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c4 = new Canvas(mBitmap15);
			Paint p4= new Paint();
			p4.setColor(Color.rgb(255,127, 36));
			p4.setTextSize(100);
			p4.setAntiAlias(true);             //���巴���
			c4.drawText("", 25.0f,140.0f, p4);
			c4.save();
			mBitmap5=mBitmap15;
			mBitmap15=null;
			}}
		    Bitmap mBitmap16 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap16 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c2 = new Canvas(mBitmap16);
			Paint p2 = new Paint();
			p2.setColor(Color.rgb(255,127, 36));
			p2.setTextSize(200);
			p2.setAntiAlias(true);             //���巴���
			c2.drawText("", 50.0f,280.0f, p2);
			c2.save();
			mBitmap6=mBitmap16;
			mBitmap16=null;
			}}
		  
	}
	//��ѡ�����ͼƬ
	public static void load5(Resources resources, Cursor cursor,int loadint )
	{	
			if(loadint==1)
			{
			//kΪ���һ�����ID
			int k=cursor.getCount();
			//������һ�����
		    cursor.moveToPosition(k-1);
			}
		    String string2=cursor.getString(2);
		    String string3;
		    String string4;
		    String string5;
		    String string6;
		    String string7;
		    
		    int i=string2.length();
		    int n=string2.indexOf(",");
		    int n1=string2.indexOf(",",n+1);
		    int n2=string2.indexOf(",",n1+1);
		    int n3=string2.indexOf(",",n2+1);
		   
		    string3=string2.substring(0,n);
		    string4=string2.substring(n+1,n1);
		    string5=string2.substring(n1+1,n2);
		    string6=string2.substring(n2+1,n3);
		    string7=string2.substring(n3+1,i);
		    
		    int tmp=string3.length();
		    int tmp2=string4.length();
		    int tmp3=string5.length();
		    int tmp4=string6.length();
		    int tmp5=string7.length();

			wood=BitmapFactory.decodeResource(resources, R.drawable.tt);
			Bitmap mBitmap11 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap11 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap11);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp==1)
			{
				p.setTextSize(50);
				c.drawText(string3, 37.5f,77.5f, p);
			}
			if(tmp==2)
			{
				p.setTextSize(50);
				c.drawText(string3, 12.5f,77.5f, p);
			}
			if(tmp==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string3, 7.5f,77.5f, p);
			}
			if(tmp==4)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			if(tmp>4&&tmp<=8)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp>8&&tmp<=12)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, 8);
				String str3=string3.substring(8, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp>12)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap1=mBitmap11;
			mBitmap11=null;
			}}
		    Bitmap mBitmap12 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap12 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap12);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp2==1)
			{
				p.setTextSize(50);
				c.drawText(string4, 37.5f,77.5f, p);
			}
			if(tmp2==2)
			{
				p.setTextSize(50);
				c.drawText(string4, 12.5f,77.5f, p);
			}
			if(tmp2==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string4, 7.5f,77.5f, p);
			}
			if(tmp2==4)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			if(tmp2>4&&tmp2<=8)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp2>8&&tmp2<=12)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, 8);
				String str3=string4.substring(8, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp2>12)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap2=mBitmap12;
			mBitmap12=null;
			}}
		    Bitmap mBitmap13 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap13 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap13);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp3==1)
			{
				p.setTextSize(50);
				c.drawText(string5, 37.5f,77.5f, p);
			}
			if(tmp3==2)
			{
				p.setTextSize(50);
				c.drawText(string5, 12.5f,77.5f, p);
			}
			if(tmp3==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string5, 7.5f,77.5f, p);
			}
			if(tmp3==4)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			if(tmp3>4&&tmp3<=8)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp3>8&&tmp3<=12)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, 8);
				String str3=string5.substring(8, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp3>12)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap3=mBitmap13;
			mBitmap13=null;
			}}
		    Bitmap mBitmap14 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap14 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap14);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp4==1)
			{
				p.setTextSize(50);
				c.drawText(string6, 37.5f,77.5f, p);
			}
			if(tmp4==2)
			{
				p.setTextSize(50);
				c.drawText(string6, 12.5f,77.5f, p);
			}
			if(tmp4==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string6, 7.5f,77.5f, p);
			}
			if(tmp4==4)
			{
				p.setTextSize(30);
				c.drawText(string6, 5.0f,77.5f, p);
			}
			if(tmp4>4&&tmp4<=8)
			{
				String str1=string6.substring(0, 4);
				String str2=string6.substring(4, tmp4);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp4>8&&tmp4<=12)
			{
				String str1=string6.substring(0, 4);
				String str2=string6.substring(4, 8);
				String str3=string6.substring(8, tmp4);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp4>12)
			{
				p.setTextSize(30);
				c.drawText(string6, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap4=mBitmap14;
			mBitmap14=null;
			}}
		    Bitmap mBitmap15 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap15 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap15);
			Paint p= new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp5==1)
			{
				p.setTextSize(50);
				c.drawText(string7, 37.5f,77.5f, p);
			}
			if(tmp5==2)
			{
				p.setTextSize(50);
				c.drawText(string7, 12.5f,77.5f, p);
			}
			if(tmp5==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string7, 7.5f,77.5f, p);
			}
			if(tmp5==4)
			{
				p.setTextSize(30);
				c.drawText(string7, 5.0f,77.5f, p);
			}
			if(tmp5>4&&tmp5<=8)
			{
				String str1=string7.substring(0, 4);
				String str2=string7.substring(4, tmp5);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp5>8&&tmp5<=12)
			{
				String str1=string7.substring(0, 4);
				String str2=string7.substring(4, 8);
				String str3=string7.substring(8, tmp5);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp5>12)
			{
				p.setTextSize(30);
				c.drawText(string7, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap5=mBitmap15;
			mBitmap15=null;
			}}
		    Bitmap mBitmap16 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap16 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap16);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			p.setTextSize(200);
			p.setAntiAlias(true); //���巴���
			c.drawText("", 50.0f,280.0f, p);
			c.save();
			mBitmap6=mBitmap16;
			mBitmap16=null;
			}}
		   

	}
	//��ѡ�����ͼƬ
	public static void load6(Resources resources, Cursor cursor,int loadint )
	{	
			if(loadint==1)
			{
				//kΪ���һ�����ID
				int k=cursor.getCount();
				//������һ�����
				cursor.moveToPosition(k-1);
		    }
		    String string2=cursor.getString(2);
		    String string3;
		    String string4;
		    String string5;
		    String string6;
		    String string7;
		    String string8;
		    
		    int i=string2.length();
		    int n=string2.indexOf(",");
		    int n1=string2.indexOf(",",n+1);
		    int n2=string2.indexOf(",",n1+1);
		    int n3=string2.indexOf(",",n2+1);
		    int n4=string2.indexOf(",", n3+1);
		    
		    string3=string2.substring(0,n);
		    string4=string2.substring(n+1,n1);
		    string5=string2.substring(n1+1,n2);
		    string6=string2.substring(n2+1,n3);
		    string7=string2.substring(n3+1,n4);
		    string8=string2.substring(n4+1,i);
		    
		    int tmp=string3.length();
		    int tmp2=string4.length();
		    int tmp3=string5.length();
		    int tmp4=string6.length();
		    int tmp5=string7.length();
		    int tmp6=string8.length();
			wood=BitmapFactory.decodeResource(resources, R.drawable.tt);
			Bitmap mBitmap11 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap11 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap11);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp==1)
			{
				p.setTextSize(50);
				c.drawText(string3, 37.5f,77.5f, p);
			}
			if(tmp==2)
			{
				p.setTextSize(50);
				c.drawText(string3, 12.5f,77.5f, p);
			}
			if(tmp==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string3, 7.5f,77.5f, p);
			}
			if(tmp==4)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			if(tmp>4&&tmp<=8)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp>8&&tmp<=12)
			{
				String str1=string3.substring(0, 4);
				String str2=string3.substring(4, 8);
				String str3=string3.substring(8, tmp);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp>12)
			{
				p.setTextSize(30);
				c.drawText(string3, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap1=mBitmap11;
			mBitmap11=null;
			}}
		    Bitmap mBitmap12 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap12 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap12);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp2==1)
			{
				p.setTextSize(50);
				c.drawText(string4, 37.5f,77.5f, p);
			}
			if(tmp2==2)
			{
				p.setTextSize(50);
				c.drawText(string4, 12.5f,77.5f, p);
			}
			if(tmp2==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string4, 7.5f,77.5f, p);
			}
			if(tmp2==4)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			if(tmp2>4&&tmp2<=8)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp2>8&&tmp2<=12)
			{
				String str1=string4.substring(0, 4);
				String str2=string4.substring(4, 8);
				String str3=string4.substring(8, tmp2);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp2>12)
			{
				p.setTextSize(30);
				c.drawText(string4, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap2=mBitmap12;
			mBitmap12=null;
			}}
		    Bitmap mBitmap13 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap13 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap13);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp3==1)
			{
				p.setTextSize(50);
				c.drawText(string5, 37.5f,77.5f, p);
			}
			if(tmp3==2)
			{
				p.setTextSize(50);
				c.drawText(string5, 12.5f,77.5f, p);
			}
			if(tmp3==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string5, 7.5f,77.5f, p);
			}
			if(tmp3==4)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			if(tmp3>4&&tmp3<=8)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp3>8&&tmp3<=12)
			{
				String str1=string5.substring(0, 4);
				String str2=string5.substring(4, 8);
				String str3=string5.substring(8, tmp3);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp3>12)
			{
				p.setTextSize(30);
				c.drawText(string5, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap3=mBitmap13;
			mBitmap13=null;
			}}
		    Bitmap mBitmap14 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap14 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap14);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp4==1)
			{
				p.setTextSize(50);
				c.drawText(string6, 37.5f,77.5f, p);
			}
			if(tmp4==2)
			{
				p.setTextSize(50);
				c.drawText(string6, 12.5f,77.5f, p);
			}
			if(tmp4==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string6, 7.5f,77.5f, p);
			}
			if(tmp4==4)
			{
				p.setTextSize(30);
				c.drawText(string6, 5.0f,77.5f, p);
			}
			if(tmp4>4&&tmp4<=8)
			{
				String str1=string6.substring(0, 4);
				String str2=string6.substring(4, tmp4);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp4>8&&tmp4<=12)
			{
				String str1=string6.substring(0, 4);
				String str2=string6.substring(4, 8);
				String str3=string6.substring(8, tmp4);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp4>12)
			{
				p.setTextSize(30);
				c.drawText(string6, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap4=mBitmap14;
			mBitmap14=null;
			}}
		    Bitmap mBitmap15 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap15 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap15);
			Paint p= new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp5==1)
			{
				p.setTextSize(50);
				c.drawText(string7, 37.5f,77.5f, p);
			}
			if(tmp5==2)
			{
				p.setTextSize(50);
				c.drawText(string7, 12.5f,77.5f, p);
			}
			if(tmp5==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string7, 7.5f,77.5f, p);
			}
			if(tmp5==4)
			{
				p.setTextSize(30);
				c.drawText(string7, 5.0f,77.5f, p);
			}
			if(tmp5>4&&tmp5<=8)
			{
				String str1=string7.substring(0, 4);
				String str2=string7.substring(4, tmp5);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp5>8&&tmp5<=12)
			{
				String str1=string7.substring(0, 4);
				String str2=string7.substring(4, 8);
				String str3=string7.substring(8, tmp5);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp5>12)
			{
				p.setTextSize(30);
				c.drawText(string7, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap5=mBitmap15;
			mBitmap15=null;
			}}
		    Bitmap mBitmap16 = BitmapFactory.decodeResource(resources, R.drawable.im1)
			 .copy(Bitmap.Config.ARGB_8888, true);
		    if (mBitmap16 != null) {
			if (!TextUtils.isEmpty("123")) {
			Canvas c = new Canvas(mBitmap16);
			Paint p = new Paint();
			p.setColor(Color.rgb(255,127, 36));
			if(tmp6==1)
			{
				p.setTextSize(50);
				c.drawText(string8, 37.5f,77.5f, p);
			}
			if(tmp6==2)
			{
				p.setTextSize(50);
				c.drawText(string8, 12.5f,77.5f, p);
			}
			if(tmp6==3)
			{
				p.setTextSize(37.5f);
				c.drawText(string8, 7.5f,77.5f, p);
			}
			if(tmp6==4)
			{
				p.setTextSize(30);
				c.drawText(string8, 5.0f,77.5f, p);
			}
			if(tmp6>4&&tmp6<=8)
			{
				String str1=string8.substring(0, 4);
				String str2=string8.substring(4, tmp6);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,90f, p);
			}
			if(tmp6>8&&tmp6<=12)
			{
				String str1=string8.substring(0, 4);
				String str2=string8.substring(4, 8);
				String str3=string8.substring(8, tmp6);
				p.setTextSize(30);
				c.drawText(str1, 5.0f,50f, p);
				p.setTextSize(30);
				c.drawText(str2, 5.0f,80f, p);
				p.setTextSize(30);
				c.drawText(str3, 5.0f,110f, p);
			}
			if(tmp6>12)
			{
				p.setTextSize(30);
				c.drawText(string8, 5.0f,77.5f, p);
			}
			p.setAntiAlias(true);             //���巴���
			
			c.save();
			mBitmap6=mBitmap16;
			mBitmap16=null;
			}}
		    
	}
}