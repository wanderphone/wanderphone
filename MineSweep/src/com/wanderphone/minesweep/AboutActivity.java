package com.wanderphone.minesweep;

//import com.minesweep.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

//import com.mobclick.android.MobclickAgent;

public class AboutActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aboutactivity);
        TextView aboutTitle = (TextView)findViewById(R.id.myTitle);
        TextView designTitle = (TextView)findViewById(R.id.designTitle);
        TextView teamName = (TextView)findViewById(R.id.teamName);
        TextView informationTitle = (TextView)findViewById(R.id.informationTitle);
        TextView informationWeb = (TextView)findViewById(R.id.informationWeb);
        TextView questionTitle = (TextView)findViewById(R.id.questionTitle);
        TextView questionEmail = (TextView)findViewById(R.id.questionEmail);
        TextView attentionTitle = (TextView)findViewById(R.id.attentionTitle);       
        TextView sinaBlog = (TextView)findViewById(R.id.sinaBlog);
        TextView qqBlog = (TextView)findViewById(R.id.qqBlog);
        TextView developmentTitle = (TextView)findViewById(R.id.developmentTitle);
        TextView teamEnglishName = (TextView)findViewById(R.id.teamEnglishName);
        TextView moreInformationTitle = (TextView)findViewById(R.id.moreInformationTitle);
        TextView informationEnglish = (TextView)findViewById(R.id.informationEnglish);
        TextView feedBackTitle = (TextView)findViewById(R.id.feedbackTitle);
        TextView feedBackEnglish = (TextView)findViewById(R.id.feedbackEnglish);
        
        aboutTitle.setText(R.string.about);
        designTitle.setText(R.string.designTitle);
        informationTitle.setText(R.string.informationTitle);
        informationWeb.setText(R.string.informationWeb);
        questionTitle.setText(R.string.questionTitle);
        questionEmail.setText(R.string.questionEmail);
        attentionTitle.setText(R.string.attentionTitle);
        teamName.setText(R.string.teamName);
        sinaBlog.setText(R.string.sinaBlog);
        qqBlog.setText(R.string.qqBlog);
        
        developmentTitle.setText(R.string.developmentTitle);
        teamEnglishName.setText(R.string.teamEnglishName);
        moreInformationTitle.setText(R.string.moreInformationTitle);
        informationEnglish.setText(R.string.informationWeb);
        feedBackTitle.setText(R.string.feedBack);
        feedBackEnglish.setText(R.string.questionEmail);
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//MobclickAgent.onPause(this); 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//MobclickAgent.onResume(this);
	}
    
}
