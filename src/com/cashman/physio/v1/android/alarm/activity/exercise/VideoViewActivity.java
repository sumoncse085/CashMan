package com.cashman.physio.v1.android.alarm.activity.exercise;

import java.io.File;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoViewActivity extends Activity
{
  private Button mButton_Back;
  private Button mButton_Save;
  private TextView mTextView_Title;
  private VideoView mVideoView;

  private void initViews()
  {
    this.mVideoView = ((VideoView)findViewById(R.id.video_view));
    VideoView localVideoView = this.mVideoView;
    localVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener()
    {
      public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2)
      {
        VideoViewActivity.this.showToast(R.string.video_play_error);
        return true;
      }
    });
    String str = getIntent().getStringExtra(Constant.Alarm.Intent.KEY_VIDEO_PATH);
    if ((str != null) && (str.length() > 0))
    {
    	File file  = new File(str);
    	if(file.exists()){
    		localVideoView.setMediaController(new MediaController(this));
    	      localVideoView.setVideoPath(str);
    	      localVideoView.start();
    	      localVideoView.pause();
    	}else{
    		showToast(R.string.video_path_null);
    	}
      
    }else{
    	showToast(R.string.video_path_null);
    }
  }

  private void showToast(int paramInt)
  {
    Toast.makeText(this, paramInt, 1).show();
    finish();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.video_view_layout);
    initViews();
  }

  protected void onDestroy()
  {
    super.onDestroy();
  }

  protected void onStart()
  {
    super.onStart();
    this.mVideoView.requestFocus();
    this.mVideoView.start();
  }
}