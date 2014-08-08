package com.cashman.physio.v1.android.alarm.activity.home;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.MainActivity;
import com.cashman.physio.v1.android.alarm.data.Constant;

public class HomeActivity extends Activity {

	private LinearLayout mLinear_Contact;
	private LinearLayout mLinear_Alarm;
	private LinearLayout mLinear_Profile;
	private LinearLayout mLinear_About;
	private LinearLayout mLinear_More;
	private LinearLayout mLinear_Share;
	private LinearLayout mLinear_MoreDetail;
	private LinearLayout mLinear_HomeBasic;
	private ImageView mImageView_Back;
	
	private LinearLayout mLinear_linear_notification_control;
	private LinearLayout mLinear_Notification;
	private TextView mText_ContentNotification;
	private TextView mText_DateNotification;
	private Button mText_View;
	private  TextView headTxt;
	
	private Button mButton_save;
	private Button mButton_back;
	
	private TranslateAnimation mAction_ShowMore;
	private TranslateAnimation mAction_BackBasic;
	
	private static final String TAG = "HomeActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		initViews();
	}
	
	private void initViews(){
		mLinear_Contact = (LinearLayout) findViewById(R.id.linear_contact);
		mLinear_Alarm   = (LinearLayout) findViewById(R.id.linear_alarm);
		mLinear_Profile = (LinearLayout) findViewById(R.id.linear_profile);
		mLinear_About   = (LinearLayout) findViewById(R.id.linear_about);
		mLinear_More    = (LinearLayout) findViewById(R.id.linear_more);
		mLinear_Share   = (LinearLayout) findViewById(R.id.linear_share);
		mLinear_MoreDetail = (LinearLayout) findViewById(R.id.linear_more_detail);
		mLinear_HomeBasic = (LinearLayout) findViewById(R.id.linear_home_basic);
		mImageView_Back = (ImageView) findViewById(R.id.btn_back);
		headTxt = (TextView)findViewById(R.id.head_txt_title);
		  headTxt.setText("Cashman Physio");
		  
		  mLinear_linear_notification_control = (LinearLayout)findViewById(R.id.linear_specials);
		  mLinear_Notification = (LinearLayout)findViewById(R.id.linear_notification);
		  mText_ContentNotification = (TextView)findViewById(R.id.content_notification);
		  mText_DateNotification = (TextView)findViewById(R.id.date_notification);
		  mText_View = (Button)findViewById(R.id.btn_view);
		  mButton_save = (Button)findViewById(R.id.head_btn_save);
		  mButton_back = (Button)findViewById(R.id.head_btn_back);
		  mButton_save.setVisibility(View.GONE);
		  mButton_back.setVisibility(View.GONE);
		  
		  mText_View.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				openBrowser(HomeActivity.this, Constant.Notification.URL);
			}
		});
		  
		  mButton_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 headTxt.setText("Cashman Physio");
				 mLinear_MoreDetail.setVisibility(View.VISIBLE);
				 mLinear_Notification.setVisibility(View.GONE);
				 mButton_back.setVisibility(View.GONE);
			}
		});
		  
		mAction_ShowMore = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, 
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
		mAction_ShowMore.setDuration(500);
		
		mAction_BackBasic = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, 
				Animation.RELATIVE_TO_SELF, 0.0f, 
                Animation.RELATIVE_TO_SELF, -1.0f, 
                Animation.RELATIVE_TO_SELF, 0.0f);  
		mAction_BackBasic.setDuration(500);
		
		mImageView_Back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mLinear_MoreDetail.setVisibility(View.GONE);
				mLinear_HomeBasic.startAnimation(mAction_BackBasic);
				mLinear_HomeBasic.setVisibility(View.VISIBLE);
			}
		});
		
		mLinear_Contact.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				performTabchangeByTag(MainActivity.CONTACT_TAB_TAG);
			}
		});
		
		mLinear_Alarm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				performTabchangeByTag(MainActivity.ALARM_TAB_TAG);
			}
		});
		
		mLinear_Profile.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				performTabchangeByTag(MainActivity.PROFILE_TAB_TAG);
			}
		});
		
		mLinear_About.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				performTabchangeByTag(MainActivity.ABOUT_TAB_TAG);
			}
		});
		
		mLinear_More.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mLinear_HomeBasic.setVisibility(View.GONE);
				mLinear_MoreDetail.startAnimation(mAction_ShowMore);
				mLinear_MoreDetail.setVisibility(View.VISIBLE);
			}
		});
		
		mLinear_Share.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				performTabchangeByTag(MainActivity.SHARE_TAB_TAG);
			}
		});
		
		mLinear_linear_notification_control.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mLinear_MoreDetail.setVisibility(View.GONE);
				mLinear_Notification.setVisibility(View.VISIBLE);
				headTxt.setText("Notifications");
				mButton_back.setVisibility(View.VISIBLE);
				
				SharedPreferences  sharedPrefs = getSharedPreferences(Constant.Notification.NOTIFICATION_NAME, Context.MODE_PRIVATE);
				mText_ContentNotification.setText(sharedPrefs.getString(Constant.Notification.NOTIFICATION_CONTENT, ""));
				mText_DateNotification.setText(sharedPrefs.getString(Constant.Notification.NOTIFICATION_DATE, ""));
				
			}
		});
	}
	
	private void performTabchangeByTag(String tag){
		MainActivity parent = (MainActivity) HomeActivity.this.getParent();
		parent.setCurrentTabByTag(tag);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	private void openBrowser(Context ctx, String url) {
		Uri uri = Uri.parse(url);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		ctx.startActivity(it);
	}

}
