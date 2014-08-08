package com.cashman.physio.v1.android.alarm.activity.profile;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.cashman.physio.v1.android.alarm.AlarmApplication;
import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.exercise.ExerciseReachActivity;
import com.cashman.physio.v1.android.alarm.activity.exercise.PlayVedioActivity;
import com.cashman.physio.v1.android.alarm.data.AlarmItem;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;
import com.cashman.physio.v1.android.alarm.util.RingtoneTool;

import android.media.AudioManager;
import android.media.MediaMetadataEditor;
import android.media.MediaMetadataRetriever;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.accounts.OnAccountsUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class AppointmentAlarmActivity extends Activity {
	private static final long RINGTONE_DURATION = Constant.Ringtone.RINGTONE_DURATION;
	private static final int FLAG_DUE = Constant.Profile.FLAG_DUE;
	private static final int FLAG_GOALS = Constant.Profile.FLAG_GOALS;
	private static final int FLAG_NEXT_APPOINT = Constant.Profile.FLAG_NEXT_APPOINT;
	private static final int FLAG_LOCATION = 0;
	private static final int FLAG_NAME_1 = 12;
	private static final int FLAG_NAME_2 = 22;
	private static final int FLAG_NAME_3 = 32;
	private static final int FLAG_NAME_4 = 42;
	private static final int FLAG_ROLE_1 = 11;
	private static final int FLAG_ROLE_2 = 21;
	private static final int FLAG_ROLE_3 = 31;
	private static final int FLAG_ROLE_4 = 41;




	private ImageButton mButton_Done;
	private ImageButton mButton_Delete;
	private Button mButton_View;
	private TextView mTextView_Name;
	private TextView mTextView_Instruction;
	private Ringtone mRingtone;
	private Vibrator mVibrator;
	private String mVideoPath = "";
	private String mRingtoneUri = null;
	private boolean mVibrateEnable = false;
	private WakeLock mWakelock = null;
	private Timer mStopRingTimer = null;
	private int mRingtoneVolume = -2;
	Button btn_stop;
	private static final String TAG = "AppointmentAlarmActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appoint_alerm_polay);
		btn_stop=(Button) findViewById(R.id.btn_stop);
		btn_stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mRingtone != null && mRingtone.isPlaying()) {
					mRingtone.stop();
				}
				finish();
			}
		});
		setRingtoneVolume(0);
		initViews();

	}

	private void initViews() {
		TextView tv_mesage=(TextView) findViewById(R.id.tv_mesage);
		String message="You have an Appoinment in 1 hour at ";

		


		if(initTextView(FLAG_NAME_1).equals("please select")){
			if(initTextView(FLAG_NAME_2).equals("please select")){
				if(initTextView(FLAG_NAME_3).equals("please select")){
					if(!initTextView(FLAG_NAME_4).equals("please select")){

						message=message+" "+initTextView(FLAG_NAME_4)+" "+initTextView(FLAG_ROLE_4);
						message=message.replace("please select", "");

					}

				}
				else{
					message=message+" "+initTextView(FLAG_NAME_3)+" "+initTextView(FLAG_ROLE_3);
					message=message.replace("please select", "");
					if(!initTextView(FLAG_NAME_4).equals("please select")){

						message=message+" and at "+initTextView(FLAG_NAME_4)+" "+initTextView(FLAG_ROLE_4);
						message=message.replace("please select", "");

					}

				}

			}
			else{
				message=message+" "+initTextView(FLAG_NAME_2)+" "+initTextView(FLAG_ROLE_2);
				message=message.replace("please select", "");


				if(!initTextView(FLAG_NAME_3).equals("please select")){
					if(initTextView(FLAG_NAME_4).equals("please select")){
						message=message+" and at "+initTextView(FLAG_NAME_3)+" "+initTextView(FLAG_ROLE_3);
						message=message.replace("please select", "");
					}
					else{
						message=message+", "+initTextView(FLAG_NAME_3)+" "+initTextView(FLAG_ROLE_3);
						message=message.replace("please select", "");
					}
				}
				if(!initTextView(FLAG_NAME_4).equals("please select")){

					message=message+" and at "+initTextView(FLAG_NAME_4)+" "+initTextView(FLAG_ROLE_4);
					message=message.replace("please select", "");

				}

			}

		}
		else{
			message=message+" "+initTextView(FLAG_NAME_1)+" "+initTextView(FLAG_ROLE_1);
			message=message.replace("please select", "");
			if(!initTextView(FLAG_NAME_2).equals("please select")){
				if((initTextView(FLAG_NAME_2).equals("please select"))&&(initTextView(FLAG_NAME_2).equals("please select"))){
					message=message+" and at "+initTextView(FLAG_NAME_2)+" "+initTextView(FLAG_ROLE_2);
					message=message.replace("please select", "");
				}
				else{
					message=message+", "+initTextView(FLAG_NAME_2)+" "+initTextView(FLAG_ROLE_2);
					message=message.replace("please select", "");
				}

			}

			if(!initTextView(FLAG_NAME_3).equals("please select")){
				if(initTextView(FLAG_NAME_4).equals("please select")){
					message=message+" and at "+initTextView(FLAG_NAME_3)+" "+initTextView(FLAG_ROLE_3);
					message=message.replace("please select", "");
				}
				else{
					message=message+", "+initTextView(FLAG_NAME_3)+" "+initTextView(FLAG_ROLE_3);
					message=message.replace("please select", "");
				}
			}
			if(!initTextView(FLAG_NAME_4).equals("please select")){

				message=message+" and at "+initTextView(FLAG_NAME_4)+" "+initTextView(FLAG_ROLE_4);
				message=message.replace("please select", "");

			}

		}
		//		this.mTvList_Role_2.setText(initTextView(FLAG_ROLE_2));
		//		this.mTvList_Role_3.setText(initTextView(FLAG_ROLE_3));
		//		this.mTvList_Role_4.setText(initTextView(FLAG_ROLE_4));
		//		
		//		this.mTvList_Name_1.setText(initTextView(FLAG_NAME_1));
		//		this.mTvList_Name_2.setText(initTextView(FLAG_NAME_2));
		//		this.mTvList_Name_3.setText(initTextView(FLAG_NAME_3));
		//		this.mTvList_Name_4.setText(initTextView(FLAG_NAME_4));

		tv_mesage.setText(message);
		unlockScreen();
	}

	private void showVideoPlayer() {

		Intent intent=new Intent(AppointmentAlarmActivity.this, PlayVedioActivity.class);
		intent.putExtra("vediopath", new File(this.mVideoPath).getAbsolutePath());
		startActivity(intent);
		finish();}


	private boolean checkIfSilent() {
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
	}

	//	private void playVibrate() {
	//		mVibrator = (Vibrator) getApplication().getSystemService(
	//				Context.VIBRATOR_SERVICE);
	//		mVibrator.vibrate(Constant.Alarm.AlarmRing.DEFAULT_VIBRATE_PATTERN,
	//				Constant.Alarm.AlarmRing.DEFAULT_VIBRATE_REPEATE_TIMES);
	//
	//	}

	public void unlockScreen() {
		// 获取PowerManager的实例
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// 得到一个WakeLock唤醒锁
		mWakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, TAG);
		if (!mWakelock.isHeld()) {
			// 唤醒屏幕
			mWakelock.acquire();
		}

		// 获得一个KeyguardManager的实例
		KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		// 得到一个键盘锁KeyguardLock
		KeyguardLock mKeyguardLock = km.newKeyguardLock("SimpleTimer");
		if (km.inKeyguardRestrictedInputMode()) {
			// 解锁键盘
			mKeyguardLock.disableKeyguard();
		}
	}

	private void playRingtone(String uri) {
		if (mRingtone == null) {
			RingtoneTool tool = new RingtoneTool(AppointmentAlarmActivity.this);
			Ringtone ringtone;
			if (uri == null || uri.length() <= 0) {
				ringtone = tool.getDefaultRingtone(RingtoneManager.TYPE_ALL);
//				ringtone = tool.getRingtoneByUriPath(RingtoneManager.TYPE_ALL,
//						uri);
				
//				
//				String notification = RingtoneManager.getValidRingtoneUri(AppointmentAlarmActivity.this).toString();
//				Log.e("tag", notification);
//				ringtone = tool.getRingtoneByUriPath(RingtoneManager.TYPE_ALL,
//						notification);
			} else {
				ringtone = tool.getRingtoneByUriPath(RingtoneManager.TYPE_ALL,
						uri);
			}
			mRingtone = ringtone;
		}
		mRingtone.play();
	}

	private void setRingtoneVolume(int delta){
		int setVolume = mRingtoneVolume;
		if(setVolume == -2){
			String volume = PreferencesTool.get(AppointmentAlarmActivity.this, Constant.Ringtone.NAME, Constant.Ringtone.KEY_RINGTONE_VOLUME);
			if(volume != null && volume.length() > 0){
				setVolume = Integer.parseInt(volume);
			}else{
				setVolume = Constant.Ringtone.DEFAULT_RINGTONE_VOLUME;
			}
		}

		setVolume += delta;
		int alarmStreamType = AudioManager.STREAM_RING;
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int max = audioManager.getStreamMaxVolume( alarmStreamType );
		if(setVolume > max){
			setVolume = max;
		}else if(setVolume < 0){
			setVolume = 0;
		}

		int flags = AudioManager.FLAG_PLAY_SOUND;
		if(delta != 0){
			flags |= AudioManager.FLAG_SHOW_UI;
		}
		if(setVolume <= max && setVolume >= 0){
			audioManager.setStreamVolume(alarmStreamType, setVolume, flags);
		}
		mRingtoneVolume = setVolume;
		if(setVolume > 0){
			playRingtone(mRingtoneUri);
		}
		//		PreferencesTool.save(ExerciseReachActivity.this, Constant.Ringtone.NAME, Constant.Ringtone.KEY_RINGTONE_VOLUME,String.valueOf(setVolume));
	}

	private int getRingtoneVolumn(){
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		return audioManager.getStreamVolume(AudioManager.STREAM_RING);
	}

	private void showToast(int textId) {
		Toast.makeText(AppointmentAlarmActivity.this, textId, Toast.LENGTH_LONG)
		.show();
	}



	@Override
	protected void onStart() {
		super.onStart();

		startTimer();
	}



	private void startTimer(){
		if (this.mStopRingTimer == null)
			this.mStopRingTimer = new Timer();
		//		 mStopRingTimer.schedule(new StopRingTimer(), when)
		this.mStopRingTimer.schedule(new StopRingTimer(), RINGTONE_DURATION);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		PreferencesTool.save(AppointmentAlarmActivity.this, Constant.Ringtone.NAME, Constant.Ringtone.KEY_RINGTONE_VOLUME,String.valueOf(mRingtoneVolume));

		if (mRingtone != null && mRingtone.isPlaying()) {
			mRingtone.stop();
		}
		mRingtone = null;
		System.gc();
		if (mVibrator != null) {
			mVibrator.cancel();
			mVibrator = null;
			System.gc();
		}

		if (mWakelock != null) {
			mWakelock.release();
			mWakelock = null;
		}
		System.gc();
		updateAlarm();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mRingtone != null && mRingtone.isPlaying()) {
				mRingtone.stop();
			}
			mRingtone = null;
			System.gc();

			if (mVibrator != null) {
				mVibrator.cancel();
				mVibrator = null;
				System.gc();
			}

			finish();
		}else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			setRingtoneVolume(1);
		}else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
			setRingtoneVolume(-1);
		}

		return true;
	}

	private void updateAlarm() {
		AlarmApplication.appInstance.startAlarm();
	}

	//	private Date lastDate = new Date();

	private class StopRingTimer extends TimerTask {
		private StopRingTimer() {
		}

		public void run() {

			//			Date current = new Date();
			//			LocalLog.i(TAG,"&&&&&  "+(current.getTime() - lastDate.getTime()));
			//			lastDate = current;

			if ((mRingtone != null)
					&& (mRingtone.isPlaying()))
				mRingtone.stop();
			if (mVibrator != null){
				mVibrator.cancel();
				mVibrator = null;
				System.gc();
			}

		}
	}

	private String getPreferences(String paramString) {
		return getSharedPreferences(
				Constant.SharePreferences.KEY_PROFILE_PREFERENCES_NAME, 0)
				.getString(paramString, "");
	}

	private String initTextView(int flag)
	{
		String str = "";
		switch (flag)
		{
		default:

		case FLAG_ROLE_1:
			str = getPreferences(Constant.SharePreferences.KEY_ROLE_1);
			break;
		case FLAG_ROLE_2:
			str = getPreferences(Constant.SharePreferences.KEY_ROLE_2);
			break;
		case FLAG_ROLE_3:
			str = getPreferences(Constant.SharePreferences.KEY_ROLE_3);
			break;
		case FLAG_ROLE_4:
			str = getPreferences(Constant.SharePreferences.KEY_ROLE_4);
			break;
		case FLAG_LOCATION:
			str = getPreferences(Constant.SharePreferences.KEY_LOCATION);
			break;
		case FLAG_NAME_1:
			str = getPreferences(Constant.SharePreferences.KEY_NAME_1);
			break;
		case FLAG_NAME_2:
			str = getPreferences(Constant.SharePreferences.KEY_NAME_2);
			break;
		case FLAG_NAME_3:
			str = getPreferences(Constant.SharePreferences.KEY_NAME_3);
			break;
		case FLAG_NAME_4:
			str = getPreferences(Constant.SharePreferences.KEY_NAME_4);
			break;
		case FLAG_NEXT_APPOINT:
			str = getPreferences(Constant.SharePreferences.KEY_NEXT_APPOINT);
			break;
		case FLAG_GOALS:
			str = getPreferences(Constant.SharePreferences.KEY_GOALS);
			break;
		case FLAG_DUE:
			str = getPreferences(Constant.SharePreferences.KEY_DUE);
			break;
		}
		if(str == null || str.length() <= 0){
			if(flag != FLAG_DUE && flag != FLAG_GOALS && flag != FLAG_NEXT_APPOINT){
				str = getString(R.string.please_select);
			}else{
				str = getString(R.string.none);
			}
		}


		return str;
	}
}
