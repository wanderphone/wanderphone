package dhl.android.com;

import java.util.Locale;

import dhl.android.com.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Start extends Activity

{
	private int count = 6;
	private int[] imgIDs = {R.id.widget29,R.id.widget30,R.id.widget31,R.id.widget32,R.id.widget33,R.id.widget34};
	
	private final int EDIT_TYPE_SELECTED = 1;     //选中的   
	private final int EDIT_TYPE_NO_SELECTED = 2;
	private RelativeLayout rl;
	public void onCreate(Bundle savedInstanceState)
	{
		//ȫ����ʾ;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
		String string1=getLocaleLanguage();
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.startmain);
        rl=(RelativeLayout)findViewById(R.id.rl);
        if(string1.equals("zh-CN")||string1.equals("zh-TW"))
        {
        	Resources resources = this.getResources();   
        	Drawable Drawable = resources.getDrawable(R.drawable.back);  
        	rl.setBackgroundDrawable(Drawable); 
        }
        else
        {
        	Resources resources = this.getResources();   
        	Drawable Drawable = resources.getDrawable(R.drawable.back2);  
        	rl.setBackgroundDrawable(Drawable); 
        }
        final IndexThread thread = new IndexThread();
        for(int id : imgIDs)
        ((ImageView)findViewById(id)).setBackgroundResource(R.drawable.progress1);
        thread.start();
        

       new CountDownTimer(3000,100) {
       	   @Override
       	   public void onTick(long millisUntilFinished) {
       	    // TODO Auto-generated method stub
       	    
       	   }
       	         @Override
       	         public void onFinish() {
       	        	 Intent intent=new Intent();
       	        	 intent.setClass(Start.this, SceneDice.class);
       	        	 startActivity(intent);
       	        	 Start.this.finish();
       	         }
         }.start();
	}

	public String getLocaleLanguage() {
		Locale l = Locale.getDefault();
		return String.format("%s-%s", l.getLanguage(), l.getCountry());
		}

	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i("Test","---"+ msg.arg1);
			switch(msg.what)
			{
			case EDIT_TYPE_SELECTED:
				((ImageView)findViewById(msg.arg1)).setBackgroundResource(R.drawable.progress2);
				break;
			case EDIT_TYPE_NO_SELECTED:
				((ImageView)findViewById(msg.arg1)).setBackgroundResource(R.drawable.progress1);
				break;
			}
		}
    };
    class IndexThread extends Thread
    {
    	boolean flag = true;
    	@Override
	     public void run()
	     {
    		Message msg;
    		while(flag)
    		{
    			for(int i= 0 ; i < count ; i++)
    			{
    				Log.i("Test","---"+ count);
    				msg = new Message();
    				msg.what = EDIT_TYPE_SELECTED;
    				msg.arg1 = imgIDs[i];
    				myHandler.sendMessage(msg);
    				//findViewById(imgIDs[i]).setBackgroundResource(R.drawable.progress_go_small);
    				msg = new Message();
    				if(i==0)
    				{
    					msg.what = EDIT_TYPE_NO_SELECTED;
    					msg.arg1 = imgIDs[count-1];
    					myHandler.sendMessage(msg);
    					//findViewById(imgIDs[count-1]).setBackgroundResource(R.drawable.progress_bg_small);
    				}
    				else
    				{
    					msg.what = EDIT_TYPE_NO_SELECTED;
    					msg.arg1 = imgIDs[i-1];
    					myHandler.sendMessage(msg);
    					//findViewById(imgIDs[i-1]).setBackgroundResource(R.drawable.progress_bg_small);
    				}
    				SystemClock.sleep(300);
    			}
    		}
    		
	     }
    }
}