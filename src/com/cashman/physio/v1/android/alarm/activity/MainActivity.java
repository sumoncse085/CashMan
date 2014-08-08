package com.cashman.physio.v1.android.alarm.activity;

import java.io.File;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.cashman.physio.v1.android.alarm.AlarmApplication;
import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.about.AboutUsActivity;
import com.cashman.physio.v1.android.alarm.activity.contact.ContactUsActivity;
import com.cashman.physio.v1.android.alarm.activity.exercise.ExerciseActivity;
import com.cashman.physio.v1.android.alarm.activity.home.HomeActivity;
import com.cashman.physio.v1.android.alarm.activity.profile.ProfileActivity;
import com.cashman.physio.v1.android.alarm.activity.share.ShareActivity;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.data.TabInfo;
import com.cashman.physio.v1.android.alarm.push.PushService;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

public class MainActivity extends TabActivity {
	private ViewSwitcher mSwitcher;
	private int PAGE_TIME_OUT = 2000;
	private TabHost mTabhost;
	private ImageView mImageView_Page;
	 
	public static final String HOME_TAB_TAG = Constant.Tab.TAB_TAG_ARRAY[0];
	public static final String ALARM_TAB_TAG = Constant.Tab.TAB_TAG_ARRAY[1];
	public static final String PROFILE_TAB_TAG = Constant.Tab.TAB_TAG_ARRAY[2];
	public static final String ABOUT_TAB_TAG = Constant.Tab.TAB_TAG_ARRAY[3];
	public static final String CONTACT_TAB_TAG = Constant.Tab.TAB_TAG_ARRAY[4];
	public static final String SHARE_TAB_TAG = Constant.Tab.TAB_TAG_ARRAY[5];
	public static final String INFO_TAB_TAG = Constant.Tab.TAB_TAG_ARRAY[6];
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initViews();
		initPush();
	}

	private void initPush() {
		Intent service = new Intent(PushService.NAME);
		this.startService(service);
	}
	
	private void initViews(){
		this.mSwitcher = ((ViewSwitcher)findViewById(R.id.switcher));
	    this.mImageView_Page = ((ImageView)findViewById(R.id.img_page));
	    this.mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_in));
	    this.mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_out));
	    this.mHandler.removeCallbacks(this.mPageRunnable);
	    this.mHandler.postDelayed(this.mPageRunnable, this.PAGE_TIME_OUT);
	    
	    initTabhost();
	}

	private void initTabhost() {
	    String str1 = ALARM_TAB_TAG;
	    Intent localIntent1 = new Intent(this, ExerciseActivity.class);
	    TabInfo localTabInfo1 = new TabInfo(str1, R.string.tab_alarm, localIntent1);
	    String str2 = CONTACT_TAB_TAG;
	    Intent localIntent2 = new Intent(this, ContactUsActivity.class);
	    TabInfo localTabInfo2 = new TabInfo(str2, R.string.tab_contact, localIntent2);
	    String str3 = HOME_TAB_TAG;
	    Intent localIntent3 = new Intent(this, HomeActivity.class);
	    TabInfo localTabInfo3 = new TabInfo(str3, R.string.tab_home, localIntent3);
	    String str4 = SHARE_TAB_TAG;
	    Intent localIntent4 = new Intent(this, ShareActivity.class);
	    TabInfo localTabInfo4 = new TabInfo(str4, R.string.tab_share, localIntent4);
	    String str5 = PROFILE_TAB_TAG;
	    Intent localIntent5 = new Intent(this, ProfileActivity.class);
	    TabInfo localTabInfo5 = new TabInfo(str5, R.string.tab_profile, localIntent5);
	    String str6 = ABOUT_TAB_TAG;
	    Intent localIntent6 = new Intent(this, AboutUsActivity.class);
	    TabInfo localTabInfo6 = new TabInfo(str6, R.string.tab_about, localIntent6);
	    this.mTabhost = getTabHost();
	    this.mTabhost.bringToFront();
	    LayoutInflater localLayoutInflater = LayoutInflater.from(this);
	    View localView1 = localLayoutInflater.inflate(R.layout.tab_layout, null);
	    ((TextView)localView1.findViewById(R.id.text)).setText(localTabInfo3.getLabelId());
	    TabHost.TabSpec localTabSpec1 = this.mTabhost.newTabSpec(localTabInfo3.getTag()).setIndicator(localView1).setContent(localTabInfo3.getContent());
	    View localView2 = localLayoutInflater.inflate(R.layout.tab_layout, null);
	    ((TextView)localView2.findViewById(R.id.text)).setText(localTabInfo1.getLabelId());
	    TabHost.TabSpec localTabSpec2 = this.mTabhost.newTabSpec(localTabInfo1.getTag()).setIndicator(localView2).setContent(localTabInfo1.getContent());
	    View localView3 = localLayoutInflater.inflate(R.layout.tab_layout, null);
	    ((TextView)localView3.findViewById(R.id.text)).setText(localTabInfo5.getLabelId());
	    TabHost.TabSpec localTabSpec3 = this.mTabhost.newTabSpec(localTabInfo5.getTag()).setIndicator(localView3).setContent(localTabInfo5.getContent());
	    View localView4 = localLayoutInflater.inflate(R.layout.tab_layout, null);
	    ((TextView)localView4.findViewById(R.id.text)).setText(localTabInfo6.getLabelId());
	    TabHost.TabSpec localTabSpec4 = this.mTabhost.newTabSpec(localTabInfo6.getTag()).setIndicator(localView4).setContent(localTabInfo6.getContent());
	    View localView5 = localLayoutInflater.inflate(R.layout.tab_layout, null);
	    ((TextView)localView5.findViewById(R.id.text)).setText(localTabInfo2.getLabelId());
	    TabHost.TabSpec localTabSpec5 = this.mTabhost.newTabSpec(localTabInfo2.getTag()).setIndicator(localView5).setContent(localTabInfo2.getContent());
	    View localView6 = localLayoutInflater.inflate(R.layout.tab_layout, null);
	    ((TextView)localView6.findViewById(R.id.text)).setText(localTabInfo4.getLabelId());
	    TabHost.TabSpec localTabSpec6 = this.mTabhost.newTabSpec(localTabInfo4.getTag()).setIndicator(localView6).setContent(localTabInfo4.getContent());
	    this.mTabhost.addTab(localTabSpec1);
	    this.mTabhost.addTab(localTabSpec2);
	    this.mTabhost.addTab(localTabSpec3);
	    this.mTabhost.addTab(localTabSpec4);
	    this.mTabhost.addTab(localTabSpec5);
	    this.mTabhost.addTab(localTabSpec6);


	}
	
	 private Handler mHandler = new Handler();
	 
	  private Runnable mPageRunnable = new Runnable()
	  {
	    public void run()
	    {
	      if ((MainActivity.this.mImageView_Page == null) || (MainActivity.this.mSwitcher.getNextView() == MainActivity.this.mImageView_Page))
	        return;
	      MainActivity.this.mSwitcher.showNext();
	      MainActivity.this.mSwitcher.removeView(MainActivity.this.mImageView_Page);
	      MainActivity.this.mImageView_Page = null;
	      System.gc();
	    }
	  };

	@Override
	protected void onStart() {

		super.onStart();

		startAlarm();
		
		CheckError localCheckError = new CheckError();
	    localCheckError.process();
	}

	public void startAlarm() {
		AlarmApplication.appInstance.startAlarm();
	}

	public void setCurrentTabByTag(String tag) {
		mTabhost.setCurrentTabByTag(tag);
	}

	private class CheckError {
		private AlarmApplication app = (AlarmApplication) MainActivity.this
				.getApplication();
		private String fileName = "";
		private boolean flag = false;

		public CheckError() {
		}

		private void setErrorFlag(boolean paramBoolean) {
			this.app.saveErrorFlag(paramBoolean);
		}

		public boolean getErrorFlag() {
			this.flag = app.getErrorFlag();
			return this.flag;
		}

		public void process() {
			this.flag = app.getErrorFlag();
			if (flag) {
				this.fileName = app.getErrorFileName();
				
				
				
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(
						MainActivity.this);
				localBuilder.setTitle(R.string.error_report_title);
				localBuilder.setMessage(MainActivity.this.getResources()
						.getText(R.string.error_report_message));
				localBuilder.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface paramDialogInterface,
									int paramInt) {
								
								File file = getFileStreamPath(fileName);
								LocalLog.e("CheckError", "####  fileName = "+fileName+", file.size = "+file.length());
								
								Intent localIntent = new Intent(
										"android.intent.action.SEND");
								String[] arrayOfString = new String[1];
								arrayOfString[0] = ((String) MainActivity.this
										.getResources().getText(R.string.error_report_email));
								localIntent.putExtra(
										"android.intent.extra.EMAIL",
										arrayOfString);
								localIntent
										.putExtra(
												"android.intent.extra.SUBJECT",
												MainActivity.this
														.getString(R.string.crash_log_email_subject));
								localIntent.putExtra(
										"android.intent.extra.STREAM",
										Uri.fromFile(file));
								localIntent.setType("application/octet-stream");
								MainActivity.this.startActivity(Intent
										.createChooser(localIntent,
												MainActivity.this
														.getString(R.string.email_app_title)));
								paramDialogInterface.dismiss();
								MainActivity.CheckError.this
										.setErrorFlag(false);
							}
						});
				localBuilder.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface paramDialogInterface,
									int paramInt) {
								paramDialogInterface.dismiss();
								MainActivity.CheckError.this
										.setErrorFlag(false);
							}
						});
				localBuilder.show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
