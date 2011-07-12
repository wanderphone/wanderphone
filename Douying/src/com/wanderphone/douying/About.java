package com.wanderphone.douying;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
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
}
