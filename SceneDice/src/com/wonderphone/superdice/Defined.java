package com.wonderphone.superdice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//自定义情景
public class Defined extends Activity

{
	private ToDoDB myToDoDB;
	private Cursor myCursor;
	private Button bt1;
	private Button bt2;
	private EditText et1;
	private EditText et2;
	private EditText et3;
	private EditText et4;
	private EditText et5;
	private EditText et6;
	private EditText et7;
	private String string1;
	private String string2;
	private String string3;
	private String string4;
	private String string5;
	private String string6;
	private String string7;
	private String string8;
	int _id;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        bt1=(Button)findViewById(R.id.bt1);
        bt2=(Button)findViewById(R.id.bt2);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        et4=(EditText)findViewById(R.id.et4);
        et5=(EditText)findViewById(R.id.et5);
        et6=(EditText)findViewById(R.id.et6);
        et7=(EditText)findViewById(R.id.et7);
        bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//情景名
				string1=et1.getText().toString();
				//骰子每个面
				string2=et2.getText().toString();
				string3=et3.getText().toString();
				string4=et4.getText().toString();
				string5=et5.getText().toString();
				string6=et6.getText().toString();
				string7=et7.getText().toString();
				if(!string4.equals("")&&!string5.equals("")&&!string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string5+","+string6+","+string7;
				}
				else if(string4.equals("")&&!string5.equals("")&&!string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string5+","+string6+","+string7;
				}
				else if(!string4.equals("")&&string5.equals("")&&!string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string6+","+string7;
				}
				else if(!string4.equals("")&&!string5.equals("")&&string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string5+","+string7;
				}
				else if(!string4.equals("")&&!string5.equals("")&&!string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string5+","+string6;
				}
				
				
				else if(string4.equals("")&&string5.equals("")&&!string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string6+","+string7;
				}
				else if(string4.equals("")&&!string5.equals("")&&string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string5+","+string7;
				}
				else if(string4.equals("")&&!string5.equals("")&&!string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string5+","+string6;
				}
				else if(!string4.equals("")&&string5.equals("")&&string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string7;
				}
				else if(!string4.equals("")&&string5.equals("")&&!string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string6;
				}
				else if(!string4.equals("")&&!string5.equals("")&&string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string5;
				}
				
				
				else if(string4.equals("")&&string5.equals("")&&string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string7;
				}
				else if(!string4.equals("")&&string5.equals("")&&string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string4;
				}
				else if(string4.equals("")&&!string5.equals("")&&string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string5;
				}
				else if(string4.equals("")&&string5.equals("")&&!string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string6;
				}
				
				else if(string4.equals("")&&string5.equals("")&&string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3;
				}
				
				//前三项必须定义，否则不能创建情景
				if(!string1.equals("")&&!string2.equals("")&&!string3.equals(""))
				{
					
					Defined.this.addTodo();					
					Defined.this.finish();
				}
				else
				{
					AlertDialog dlg=new AlertDialog.Builder(Defined.this)
		 			.setTitle(R.string.string39)
		 			.setMessage(R.string.string52)
		 			.setPositiveButton(R.string.string9, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					})
				
		 			.create();
		 		    dlg.show();
				}
			}
		});
        bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Defined.this.finish();
			}
		});
	}
	public void addTodo() 

	  {
	    
	    /* ������ݵ���ݿ� */
	    myToDoDB.insert(string1,string8);
	   
	    myCursor.requery(); 
	    _id = 0; 
	    }
	protected void onPause() {
		// TODO Auto-generated method stub
		myToDoDB.close(); 
		myCursor.close();
		super.onPause();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		myToDoDB = new ToDoDB(this);
		myCursor=myToDoDB.select();
		super.onResume();
	}

}