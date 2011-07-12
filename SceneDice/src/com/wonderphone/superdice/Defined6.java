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
//编辑自定义的情景
public class Defined6 extends Activity

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
        setContentView(R.layout.main6);
        bt1=(Button)findViewById(R.id.bt1);
        bt2=(Button)findViewById(R.id.bt2);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        et4=(EditText)findViewById(R.id.et4);
        et5=(EditText)findViewById(R.id.et5);
        et6=(EditText)findViewById(R.id.et6);
        et7=(EditText)findViewById(R.id.et7);
        Bundle bundle=this.getIntent().getExtras();
        string1=bundle.getString("string1");
        string2=bundle.getString("string3");
        string3=bundle.getString("string4");
        string4=bundle.getString("string5");
        string5=bundle.getString("string6");
        string6=bundle.getString("string7");
        string7=bundle.getString("string8");
        _id=bundle.getInt("id");
        
        et1.setText(string1);
        et2.setText(string2);
        et3.setText(string3);
        et4.setText(string4);
        et5.setText(string5);
        et6.setText(string6);
        et7.setText(string7);
        bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				string1=et1.getText().toString();
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
				if(string4.equals("")&&!string5.equals("")&&!string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string5+","+string6+","+string7;
				}
				if(!string4.equals("")&&string5.equals("")&&!string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string6+","+string7;
				}
				if(!string4.equals("")&&!string5.equals("")&&string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string5+","+string7;
				}
				if(!string4.equals("")&&!string5.equals("")&&!string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string5+","+string6;
				}
				
				
				if(string4.equals("")&&string5.equals("")&&!string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string6+","+string7;
				}
				if(string4.equals("")&&!string5.equals("")&&string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string5+","+string7;
				}
				if(string4.equals("")&&!string5.equals("")&&!string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string5+","+string6;
				}
				if(!string4.equals("")&&string5.equals("")&&string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string7;
				}
				if(!string4.equals("")&&string5.equals("")&&!string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string6;
				}
				if(!string4.equals("")&&!string5.equals("")&&string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string4+","+string5;
				}
				
				
				if(string4.equals("")&&string5.equals("")&&string6.equals("")&&!string7.equals(""))
				{
					string8=string2+","+string3+","+string7;
				}
				if(!string4.equals("")&&string5.equals("")&&string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string4;
				}
				if(string4.equals("")&&!string5.equals("")&&string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string5;
				}
				if(string4.equals("")&&string5.equals("")&&!string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3+","+string6;
				}
				
				if(string4.equals("")&&string5.equals("")&&string6.equals("")&&string7.equals(""))
				{
					string8=string2+","+string3;
				}
				
				if(!string1.equals("")&&!string2.equals("")&&!string3.equals(""))
				{
					
					 myToDoDB.update(_id, string1,string8); 
					 myCursor.requery(); 
					 
					 Defined6.this.finish();
					 
				}
				else
				{
					AlertDialog dlg=new AlertDialog.Builder(Defined6.this)
		 			.setTitle(R.string.string39)
		 			.setMessage(R.string.string40)
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
				Defined6.this.finish();
			}
		});
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		myToDoDB.close(); 
		myCursor.close();
		et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        et7.setText("");
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