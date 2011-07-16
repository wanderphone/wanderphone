package com.wanderphone.minesweep;
import com.mobclick.android.MobclickAgent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.WindowManager;

public class SetActivity extends PreferenceActivity  {

	private static boolean vflag;
	private static boolean soundflag;
	private static boolean vibrateflag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(SetActivity.this);

		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		SharedPreferences sharedPreferences1=getSharedPreferences("the_soundflag",MODE_PRIVATE);        
        soundflag=sharedPreferences1.getBoolean("soundflag", true);
		SharedPreferences sharedPreferences2=getSharedPreferences("the_vibrateflag",MODE_PRIVATE);        
        vibrateflag=sharedPreferences2.getBoolean("vibrateflag", true);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Editor spe = sp.edit();   
 	    spe.putBoolean("dd", soundflag);
 	    spe.putBoolean("ee", vibrateflag);
 	    spe.commit();
		addPreferencesFromResource(R.xml.setting);
		//震动和音效标记；
		

	}
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,Preference preference) 
	{
		if(preference.getKey().equals("bb"))
		{
		  Intent intent=new Intent(Intent.ACTION_SEND);   	      
  	      intent.setType("text/plain");
  	      intent.putExtra(Intent.EXTRA_SUBJECT, R.string.share);
  	      intent.putExtra(Intent.EXTRA_TEXT, R.string.share_content);
  	      startActivity(Intent.createChooser(intent, getTitle()));
			
		}
		if(preference.getKey().equals("cc"))
		{
			MobclickAgent.openFeedbackActivity(SetActivity.this); 
			
		}
		if(preference.getKey().equals("dd"))
		{
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			vflag = sp.getBoolean("dd", true);
			
			SharedPreferences preference3 = getSharedPreferences("the_soundflag",MODE_PRIVATE);
            Editor edit = preference3.edit();
            edit.putBoolean("soundflag", vflag);
            edit.commit();	
			
		}
		if(preference.getKey().equals("ee"))
		{
			
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			vflag = sp.getBoolean("ee", true);
			
			SharedPreferences preference3 = getSharedPreferences("the_vibrateflag",MODE_PRIVATE);
            Editor edit = preference3.edit();
            edit.putBoolean("vibrateflag", vflag);
            edit.commit();
            Log.v("!!!!", vflag+"");
		}
		return false;
	}
	public void onConfigurationChanged(Configuration newConfig) { 
        super.onConfigurationChanged(newConfig); 
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { 
                // land do nothing is ok 
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { 
                // port do nothing is ok 
        } 
}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(SetActivity.this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(SetActivity.this);
	}
 

}