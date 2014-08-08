package com.cashman.physio.v1.android.alarm.activity.profile;


import java.io.IOException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;

public class ProfileNotifyActivity extends Activity{
	private Button btnOk;
	private TextView notifyMessage;
	MediaPlayer mMediaPlayer;
 @Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.profile_notify_activity);
	
	btnOk = (Button)findViewById(R.id.btnSure);
	notifyMessage = (TextView)findViewById(R.id.notifyMessage);
	notifyMessage.setText(Html.fromHtml(getIntent().getStringExtra("message")));
	playAlarmRing();
	btnOk.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			stopAlarmRing();
			ProfileNotifyActivity.this.finish();
		}
	});
	
}
 
 private void playAlarmRing() {
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(this, alert);
			final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.setLooping(true);
				mMediaPlayer.prepare();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMediaPlayer.start();
	}
 
 private void stopAlarmRing() {
		mMediaPlayer.stop();
		AlarmManager am = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("android.alarm.demo.action");
		PendingIntent sender = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
	}
}
