package com.cashman.physio.v1.android.alarm.activity.exercise;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cashman.physio.v1.android.alarm.AlarmApplication;
import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.MainActivity;
import com.cashman.physio.v1.android.alarm.activity.profile.AppointmentAlarmActivity;
import com.cashman.physio.v1.android.alarm.data.AlarmItem;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;
import com.cashman.physio.v1.android.alarm.util.RingtoneTool;

public class ExerciseReachActivity extends Activity {

	private static final long RINGTONE_DURATION = Constant.Ringtone.RINGTONE_DURATION;

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
	
	private static final String TAG = "ExerciseReachActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.alarm_reach_alert_window);

		setRingtoneVolume(0);
		initViews();
	}

	
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		//super.onBackPressed();
//		
//	}

	private void initViews() {
		mTextView_Name = (TextView) findViewById(R.id.tv_name);
		mTextView_Instruction = (TextView) findViewById(R.id.tv_instruction);
		mButton_Delete = (ImageButton) findViewById(R.id.btn_delete);
		mButton_Done = (ImageButton) findViewById(R.id.btn_done);
		mButton_View = (Button) findViewById(R.id.btn_view);

		AlarmItem alarmItem = (AlarmItem) getIntent().getExtras()
				.getSerializable(Constant.Alarm.Intent.KEY_ALARM_ITEM);
		if(alarmItem==null){
			mTextView_Name.setText("Next Appointment");
			//mTextView_Instruction.setText(alarmItem.getInstruction());
		}
		else{
			mTextView_Name.setText(alarmItem.getName());
			mTextView_Instruction.setText(alarmItem.getInstruction());
			String videoPath = alarmItem.getVideoPath();
			mVideoPath = videoPath;
			if (alarmItem.isRingtoneEnable() && !checkIfSilent()) {
				mRingtoneUri = alarmItem.getRingtoneUri();
				playRingtone(mRingtoneUri);
			}
			mVibrateEnable = alarmItem.isVibrateEnable();
			if (mVibrateEnable) {
				playVibrate();
			}
		}






		mButton_Done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				Intent intent=new Intent(ExerciseReachActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		mButton_Delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isPlaying = false;

				if (mRingtone != null) {
					if (mRingtone.isPlaying()) {
						mRingtone.stop();
					} else {
						playRingtone(mRingtoneUri);
						if(getRingtoneVolumn() > 0){
							isPlaying = true;
						}

					}
				}
				LocalLog.i(TAG, "*****1  isPlaying = "+isPlaying);
				//				lastDate = new Date();
				if (mVibrateEnable && mVibrator == null) {
					playVibrate();

					isPlaying = true;
				} else {
					if (mVibrator != null) {
						mVibrator.cancel();
					}
					mVibrator = null;
					System.gc();
				}
				LocalLog.i(TAG, "*****2  isPlaying = "+isPlaying);
				if(isPlaying){
					startTimer();
				}else{
					if(mStopRingTimer != null){

						mStopRingTimer.cancel();
						mStopRingTimer.purge();
						mStopRingTimer = null ;
						System.gc();
					}
				}
			}
		});

		mButton_View.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRingtone != null && mRingtone.isPlaying()) {
					mRingtone.stop();
				}
				if (mVibrator != null) {
					mVibrator.cancel();
					mVibrator = null;
					System.gc();
				}
				if (mVideoPath == null || mVideoPath.length() <= 0 || !new File(mVideoPath).exists()) {
					showToast(R.string.video_path_null);
				} else {
					showVideoPlayer();
				}
			}
		});

		// setVisibility(FLAG_SHOW_VIEW_BUTTON);

		unlockScreen();
	}

	private void showVideoPlayer() {

     Intent intent=new Intent(ExerciseReachActivity.this, PlayVedioActivity.class);
     intent.putExtra("vediopath", new File(this.mVideoPath).getAbsolutePath());
      startActivity(intent);
      finish();
//		final Dialog dia = new Dialog(ExerciseReachActivity.this);
//		dia.setContentView(R.layout.alert_video_view);
//
//		dia.setTitle(R.string.video_player_title);
//		dia.setCancelable(true);
//
//		VideoView vv_show = (VideoView) dia.findViewById(R.id.vv_show);
//		
//		vv_show
//		.setVideoPath(new File(this.mVideoPath).getAbsolutePath());
//		vv_show.start();
//		Log.e("Tagged", new File(this.mVideoPath).getAbsolutePath());
////		WindowManager.LayoutParams a = dia.getWindow().getAttributes();
////		a.dimAmount = 0;
////		dia.getWindow().setAttributes(a);
//		Button btn_ok=(Button) dia.findViewById(R.id.btn_ok);
//		btn_ok.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				dia.cancel();
//				finish();
//			}
//		});
//
//		dia.show();

	}
	//	private void showVideoPlayer() {
	//		final Dialog dia = new Dialog(ExerciseReachActivity.this);
	//		
	//		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
	//		localBuilder.setTitle(R.string.video_player_title);
	//		
	//		LinearLayout localLinearLayout = new LinearLayout(this);
	//		VideoView localVideoView = new VideoView(this);
	//		localVideoView.setZOrderOnTop(true);
	//		localLinearLayout.addView(localVideoView,
	//				new LinearLayout.LayoutParams(-2, -2));
	//		localLinearLayout.setGravity(17);
	//		
	//		localVideoView
	//				.setVideoPath(new File(this.mVideoPath).getAbsolutePath());
	//		localVideoView.start();
	//		localBuilder.setView(localLinearLayout);
	//		localBuilder.setPositiveButton(R.string.ok,
	//				new DialogInterface.OnClickListener() {
	//					public void onClick(DialogInterface paramDialogInterface,
	//							int paramInt) {
	//						paramDialogInterface.dismiss();
	//
	//					}
	//				});
	//		localBuilder.create().show();
	//	}

	private boolean checkIfSilent() {
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
	}

	private void playVibrate() {
		mVibrator = (Vibrator) getApplication().getSystemService(
				Context.VIBRATOR_SERVICE);
		mVibrator.vibrate(Constant.Alarm.AlarmRing.DEFAULT_VIBRATE_PATTERN,
				Constant.Alarm.AlarmRing.DEFAULT_VIBRATE_REPEATE_TIMES);

	}

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
			RingtoneTool tool = new RingtoneTool(ExerciseReachActivity.this);
			Ringtone ringtone;
			if (uri == null || uri.length() <= 0) {
				ringtone = tool.getDefaultRingtone(RingtoneManager.TYPE_ALL);
			} else {
				Log.d("Uri", uri.toString());
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
			String volume = PreferencesTool.get(ExerciseReachActivity.this, Constant.Ringtone.NAME, Constant.Ringtone.KEY_RINGTONE_VOLUME);
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
		LocalLog.i(TAG, "current volume = "+setVolume);
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
		Toast.makeText(ExerciseReachActivity.this, textId, Toast.LENGTH_LONG)
		.show();
	}

	// private void setVisibility(int flag) {
	// mTextView_VideoPathNull.setVisibility(View.GONE);
	// if (flag == FLAG_SHOW_VIDEO) {
	// mButton_View.setVisibility(View.INVISIBLE);
	// mTextView_Instruction.setVisibility(View.GONE);
	// mVideoView.setVisibility(View.VISIBLE);
	// if(mVideoPath == null || mVideoPath.length() <= 0){
	// mTextView_VideoPathNull.setVisibility(View.VISIBLE);
	// mVideoView.setVisibility(View.GONE);
	// }
	// } else {
	// mVideoView.setVisibility(View.GONE);
	// mTextView_Instruction.setVisibility(View.VISIBLE);
	// mButton_View.setVisibility(View.VISIBLE);
	// }
	// }


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
		PreferencesTool.save(ExerciseReachActivity.this, Constant.Ringtone.NAME, Constant.Ringtone.KEY_RINGTONE_VOLUME,String.valueOf(mRingtoneVolume));

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

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//	     if (keyCode == KeyEvent.KEYCODE_BACK) {
//	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
//	     return true;
//	     }
//	     return super.onKeyDown(keyCode, event);    
//	}
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (mRingtone != null && mRingtone.isPlaying()) {
//				mRingtone.stop();
//			}
//			mRingtone = null;
//			System.gc();
//
//			if (mVibrator != null) {
//				mVibrator.cancel();
//				mVibrator = null;
//				System.gc();
//			}
//
//			finish();
//		}else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
//			setRingtoneVolume(1);
//		}else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
//			setRingtoneVolume(-1);
//		}
//
//		return true;
//	}

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

			if ((ExerciseReachActivity.this.mRingtone != null)
					&& (ExerciseReachActivity.this.mRingtone.isPlaying()))
				ExerciseReachActivity.this.mRingtone.stop();
			if (ExerciseReachActivity.this.mVibrator != null){
				ExerciseReachActivity.this.mVibrator.cancel();
				mVibrator = null;
				System.gc();
			}

		}
	}
}
