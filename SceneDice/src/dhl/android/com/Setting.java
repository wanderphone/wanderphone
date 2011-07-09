package dhl.android.com;

import android.app.AlertDialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Setting extends PreferenceActivity 

{
	private ToDoDB myToDoDB;
	private Cursor myCursor1;
	private Dialog dlg2;
	private ListView lv3;
	private int _id;
	static Vibrator vibrator;	

	private static boolean vflag;
	static Boolean dd;
	
	protected int arg;
	
	

	@Override
	


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        
		
       
       
   }


	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,Preference preference) 
	{
		if(preference.getKey().equals("bb"))
		{
			Intent intent=new Intent();
	   		Bundle bundle=new Bundle();
	   		intent.setClass(Setting.this, Defined.class);
	   		intent.putExtras(bundle);
	   		startActivity(intent);
			
		}
		if(preference.getKey().equals("cc"))
		{
		
	    	  SimpleCursorAdapter adapter3 = new SimpleCursorAdapter(Setting.this, android.R.layout.simple_list_item_single_choice, myCursor1, new String[] { ToDoDB.FIELD_TEXT}, new int[] { android.R.id.text1});	    	    		
	    	  LayoutInflater factory2=LayoutInflater.from(Setting.this);
	    	  final View DialogView2=factory2.inflate(R.layout.mylayout2, null);	    	 
	    	  lv3=(ListView)DialogView2.findViewById(R.id.lv3);
	    	  lv3.setChoiceMode(ListView.CHOICE_MODE_SINGLE); 
	    	  lv3.setAdapter(adapter3);
	    	  
	    	 
	    	  //ģʽ���������
	    	  lv3.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						
						//myCursor1=myToDoDB.select();
						 myCursor1.moveToPosition(arg2);
	 			            _id = myCursor1.getInt(0); 
	 			            arg=arg2;
						
						
	 			       
	 			       
					}
					});
	    	  dlg2=new AlertDialog.Builder(Setting.this)
	    	  .setTitle(R.string.string51)
	    	  .setView(DialogView2)
	    	  .setPositiveButton(R.string.string46, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//��ȡ�Զ���ģʽ�ĸ���ѡ����ݣ����ԡ�,��������
					   //myCursor1=myToDoDB.select();
						String string1=null;
						String string3=null;
						String string4=null;
						String string5=null;
						String string6=null;
						String string7=null;
						String string8=null;
					    myCursor1.moveToPosition(arg);
		 			    String string2=myCursor1.getString(2); 
		 			    string1=myCursor1.getString(1);
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
		 			      //�����
		 			      if(j==1)
		 			      {			 			    	
		 			    	
		 				    int i2=string2.length();
		 				    int n2=string2.indexOf(",");
		 				    string3=string2.substring(0,n2);
		 				    string4=string2.substring(n2+1,i2);
		 			      }
		 			      //�����
		 			      if(j==2)
		 			      {
		 			    	int i3=string2.length();
		 				    int n3=string2.indexOf(",");
		 				    int n33=string2.indexOf(",", n3+1);
		 				    string3=string2.substring(0,n3);
		 				    string4=string2.substring(n3+1,n33);
		 				    string5=string2.substring(n33+1,i3);
		 			      }
		 			      //�����
		 			      if(j==3)
		 			      {
		 			    	int i4=string2.length();
		 				    int n4=string2.indexOf(",");
		 				    int n44=string2.indexOf(",", n4+1);
		 				    int n444=string2.indexOf(",", n44+1);
		 				    string3=string2.substring(0,n4);
		 				    string4=string2.substring(n4+1,n44);
		 				    string5=string2.substring(n44+1, n444);
		 				    string6=string2.substring(n444+1, i4);
		 			      }
		 			      //�����
		 			      if(j==4)
		 			      {
		 			    	int i5=string2.length();
		 				    int n5=string2.indexOf(",");
		 				    int n55=string2.indexOf(",",n5+1);
		 				    int n555=string2.indexOf(",",n55+1);
		 				    int n5555=string2.indexOf(",",n555+1);
		 				   
		 				    string3=string2.substring(0,n5);
		 				    string4=string2.substring(n5+1,n55);
		 				    string5=string2.substring(n55+1,n555);
		 				    string6=string2.substring(n555+1,n5555);
		 				    string7=string2.substring(n5555+1,i5);
		 			      }
		 			      //�����
		 			      if(j==5)
		 			      {
		 			    	int i6=string2.length();
		 				    int n6=string2.indexOf(",");
		 				    int n66=string2.indexOf(",",n6+1);
		 				    int n666=string2.indexOf(",",n66+1);
		 				    int n6666=string2.indexOf(",",n666+1);
		 				    int n66666=string2.indexOf(",", n6666+1);
		 				    
		 				    string3=string2.substring(0,n6);
		 				    string4=string2.substring(n6+1,n66);
		 				    string5=string2.substring(n66+1,n666);
		 				    string6=string2.substring(n666+1,n6666);
		 				    string7=string2.substring(n6666+1,n66666);
		 				    string8=string2.substring(n66666+1,i6);
		 			      }
		 			      Intent intent=new Intent();
		 			      intent.setClass(Setting.this, Defined6.class);
		 			      Bundle bundle=new Bundle();
		 			      bundle.putString("string1", string1);
		 			      bundle.putString("string3", string3);
		 			      bundle.putString("string4", string4);
		 			      bundle.putString("string5", string5);
		 			      bundle.putString("string6", string6);
		 			      bundle.putString("string7", string7);
		 			      bundle.putString("string8", string8);
		 			      bundle.putInt("id", _id);
		 			      intent.putExtras(bundle);
		 			      startActivity(intent);
		 			     
		 			      
		 			      
				}
			})
			  
	    	  .setNeutralButton(R.string.string19, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						
						
						if(_id == 1)
						{
							AlertDialog dlg=new AlertDialog.Builder(Setting.this)
				 			.setTitle(R.string.string39)
				 			.setMessage(R.string.string56)
				 			.setPositiveButton(R.string.string10, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							})
						
				 			.create();
				 		    dlg.show();
						}
						if(_id == 2)
						{
							AlertDialog dlg=new AlertDialog.Builder(Setting.this)
				 			.setTitle(R.string.string39)
				 			.setMessage(R.string.string56)
				 			.setPositiveButton(R.string.string10, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							})
						
				 			.create();
				 		    dlg.show();
						}
						if(_id == 3)
						{
							AlertDialog dlg=new AlertDialog.Builder(Setting.this)
				 			.setTitle(R.string.string39)
				 			.setMessage(R.string.string56)
				 			.setPositiveButton(R.string.string10, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							})
						
				 			.create();
				 		    dlg.show();
						}
						else if(_id>3)
						{
							/* ɾ����� */ 
						    myToDoDB.delete(_id); 
						    myCursor1.requery();
							
						}
						   
						   
						
					}
				})
				.setNegativeButton(R.string.string10,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					dlg2.dismiss();
				}
			})
		    	  .create();
	    	  dlg2.show();
	
		}
		if(preference.getKey().equals("dd"))
		{
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			dd = sp.getBoolean("dd", false);
			vflag=dd;
			SharedPreferences preference3 = getSharedPreferences("vmy_flag",MODE_PRIVATE);
            Editor edit = preference3.edit();
            edit.putBoolean("vflag", vflag);
            edit.commit();	
			



		}
	      return false;
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		myToDoDB.close(); 
		myCursor1.close();
		super.onPause();
	}



	protected void onResume() {
		// TODO Auto-generated method stub
		myToDoDB = new ToDoDB(this);
		myCursor1=myToDoDB.select();
		super.onResume();
	}






	





	}
