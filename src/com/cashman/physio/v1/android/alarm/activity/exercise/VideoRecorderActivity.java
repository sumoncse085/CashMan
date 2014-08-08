package com.cashman.physio.v1.android.alarm.activity.exercise;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.CheckEnvironment;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoRecorderActivity extends Activity implements SurfaceHolder.Callback{

	private VideoView mVideoView;
	private MediaRecorder mRecorder;
	private SurfaceHolder mHolder;
    private Button mStart, mStop;
    private TextView mStatus;    
    private long _currentStartTime;
    private final int UPDATE_UI = 1;
    private Timer mTimer;
    private boolean isRecording = false;
    private String mVideoFilePath = "";
    
    private static final String TAG = "VideoRecorderActivity";
    
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_recorder);
		
		mVideoView = (VideoView) findViewById(R.id.camrecorder_preview);
		mHolder = mVideoView.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mStatus = (TextView)findViewById(R.id.rec_status);
		mStatus.setText("00:00:00");
		
		mStart = (Button) findViewById(R.id.start_rec);
		mStart.setEnabled(false);
		mStart.setTextColor(Color.GRAY);
		mStart.setVisibility(View.VISIBLE);
		
		mStart.setOnClickListener(new OnClickListener(){

	
			public void onClick(View v) 
			{
				try 
				{
					
                    beginRecording(mHolder);       		                    
                } 
				catch (Exception e) 
				{                   
                    e.printStackTrace();
                }
				
			}
			
		});
		
		mStop = (Button) findViewById(R.id.stop_rec);
		mStop.setEnabled(false);
		mStop.setTextColor(Color.GRAY);
		mStop.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) 
			{
				 try 
				 {					 
	                 stopRecording();	
	                 
	                 finish();
	             } 
				 catch (Exception e) 
				 {	                   
	                 e.printStackTrace();
	             }
			}
			
		});

	}
    
	Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI: {
                	long elapstedTime = System.currentTimeMillis() - _currentStartTime;
                    long hour = elapstedTime / (1000*3600);
                    long minute = (elapstedTime%(1000*3600))/(1000*60);
                    long second = (elapstedTime%(1000*60))/1000;
                    String strHour = hour >= 10? Long.toString(hour) : "0" + Long.toString(hour);
                    String strMinute =  minute >= 10? Long.toString(minute) : "0" + Long.toString(minute);
                    String strSecond =  second >= 10? Long.toString(second) : "0" + Long.toString(second);
                    String strDisplay = strHour + ":" + strMinute +":"+strSecond;
                    mStatus.setText(strDisplay);
                    break;
                }
                default:
                    break;
            }
         }
     };

     private void stopRecording() throws Exception {
         if (mRecorder != null) {
        	 isRecording = false;
             mStatus.setText("00:00:00");
             mStart.setEnabled(true);
     		 mStart.setTextColor(Color.WHITE);
     		 mStop.setEnabled(false);
    		 mStop.setTextColor(Color.GRAY);
             mRecorder.stop();
     		 if(null!=mTimer)
             {
				mTimer.cancel();
             }
         }
         setResult();
     }
     
     private void setResult(){
    	 Intent i = getIntent();
         Bundle b = new Bundle();
         b.putString(Constant.Alarm.Intent.KEY_VIDEO_PATH,mVideoFilePath);
         i.putExtra(Constant.Alarm.Intent.BUNDLE_FOR_VIDEO, b);
         setResult(Constant.Alarm.Intent.RESULT_CODE,i);
     }

     private void beginRecording(SurfaceHolder holder) throws Exception {
    	 LocalLog.i(TAG, "beginRecording","recorder == null is " + (mRecorder == null ? true:false));
         if(mRecorder!=null)
         {
             mRecorder.stop();
             mRecorder.release();
         }

         /*File outFile = new File(mFilePath);
         if(outFile.exists())
         {
             outFile.delete();
         }*/

         try 
         {
        	 _currentStartTime = System.currentTimeMillis();
             mRecorder = new MediaRecorder();
             mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
             mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
             mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
             mRecorder.setVideoSize(320, 240);
             mRecorder.setVideoFrameRate(15);
             mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
             mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
             //mRecorder.setMaxDuration(90000); // limit to 90 seconds
             mRecorder.setPreviewDisplay(holder.getSurface());
             String path = CheckEnvironment.getPath() + "" + "video_"+DateFormat.format(
						"yyyyMMdd.hhmms", new Date(_currentStartTime).getTime())+".3gp";
             CheckEnvironment.createFile(path);
             mVideoFilePath = path;
             mRecorder.setOutputFile(path);
             mRecorder.prepare();
             isRecording = true;
 			 if(null == mTimer)
 			 {
 				 mTimer = new Timer();
 			 }
 			 mTimer.scheduleAtFixedRate(new StatusTimerTask(), 1000, 1000);
 			
             mStop.setEnabled(true);
     		 mStop.setTextColor(Color.WHITE);
     		 mStart.setEnabled(false);
     		 mStart.setTextColor(Color.GRAY);
             mRecorder.start();               
         }
         catch(Exception e) 
         {
             e.printStackTrace();
         }
     }
     
     
     protected void onDestroy() {
         super.onDestroy();
         if (mRecorder != null) {
             mRecorder.release();
         }
     }
	   
     public void finish() {
 		
 		if(isRecording)
 		{			
 			try {
				stopRecording();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
            if(null!=mTimer)
            {
				mTimer.cancel();
            }
 		}
 		super.finish();		
 	}

	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
		
	}

	
	public void surfaceCreated(SurfaceHolder arg0) {
		
		mStart.setEnabled(true);
		mStart.setTextColor(Color.WHITE);
	}

	
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
		
	}
	
	class StatusTimerTask extends TimerTask 
    {
        public void run()
        {
        	mHandler.sendEmptyMessage(UPDATE_UI);
        }
    }
}
