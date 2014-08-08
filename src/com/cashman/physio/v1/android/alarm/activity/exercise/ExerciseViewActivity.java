package com.cashman.physio.v1.android.alarm.activity.exercise;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.adapter.AlarmAdapter;
import com.cashman.physio.v1.android.alarm.data.Alarm;
import com.cashman.physio.v1.android.alarm.data.CommonSetting;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.db.DbManager;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.RingtoneTool;

public class ExerciseViewActivity extends Activity implements View.OnClickListener{
	
	private TextView mTextView_Name;
	private TextView mTextView_Instruction;
	private TextView mTextView_Weekday;
	private TextView mTextView_Ringtone;
	private CheckBox mCheckBox_Activate;
	private CheckBox mCheckBox_Ringtone;
	private CheckBox mCheckBox_Vibrate;
	private Button mButton_Back;
	private Button mButton_Edit;
	private Button mButton_Configure;
	private Button mButton_Delete;
	private LinearLayout mLinear_StartTime;
//	private VideoView mVideoView;
	private LinearLayout mLinear_VideoPlay;
	private ProgressDialog mProgress;
	
	private int mAlarmId = 1;
	private String mVideoPath = "";
	
	private static final String TAG = "ExerciseViewActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_view);
		initViews();
	}
	
	private void initViews(){
		mTextView_Name = (TextView) findViewById(R.id.tv_alarm_name);
		mTextView_Instruction = (TextView) findViewById(R.id.tv_alarm_instruction);
		mTextView_Weekday = (TextView) findViewById(R.id.tv_alarm_weekday);
		mTextView_Ringtone = (TextView) findViewById(R.id.tv_ringtone_title);
		mCheckBox_Activate = (CheckBox) findViewById(R.id.chk_activate);
		mCheckBox_Ringtone = (CheckBox)findViewById(R.id.chk_ringtone_enable);
		mCheckBox_Vibrate = (CheckBox) findViewById(R.id.chk_vibrate_enable);
		mButton_Back = (Button) findViewById(R.id.btn_back);
		mButton_Edit = (Button) findViewById(R.id.btn_edit);
		mButton_Configure = (Button) findViewById(R.id.btn_configure);
		mButton_Delete = (Button)findViewById(R.id.btn_delete);
		mLinear_StartTime = (LinearLayout) findViewById(R.id.alarm_time_detail);
//		mVideoView = (VideoView) findViewById(R.id.video_view);
		mLinear_VideoPlay = (LinearLayout) findViewById(R.id.linear_play_video);
		
		
		mButton_Back.setOnClickListener(this);
		mButton_Edit.setOnClickListener(this);
		mButton_Configure.setOnClickListener(this);
		mButton_Delete.setOnClickListener(this);
		mLinear_VideoPlay.setOnClickListener(this);
		
		initShows();
	}
	
	private void initShows(){
		Intent intent = getIntent();
		int alarmId = intent.getIntExtra(Constant.Alarm.Intent.KEY_ALARM_ID, 0);
		if(alarmId <= 0){
			LocalLog.e(TAG, "initShows","alarm id is <= 0");
			finish();
		}else{
			mAlarmId = alarmId;
			DbManager dbManager = new DbManager(ExerciseViewActivity.this);
			Alarm alarm = dbManager.getAlarmById(alarmId);
			CommonSetting common = dbManager.getCommonSettingByAlarmId(alarmId);
			List<Alarm> subAlarmList = dbManager.getAlarmListByForeignId(alarmId);
			
			mTextView_Name.setText(common.getName());
			mTextView_Instruction.setText(common.getInstruction());
			mTextView_Ringtone.setText(getRingtoneTitle(common.getRingtonePath()));
			mTextView_Weekday.setText(getWeekday(common.getWeekday()));
			
			mCheckBox_Activate.setChecked(common.isActivate());
			mCheckBox_Ringtone.setChecked(common.isRingtoneEnable());
			mCheckBox_Vibrate.setChecked(common.isVibrateEnable());
			
			List<String> startTimeList = new ArrayList<String>();
			startTimeList.add(alarm.getStartTime());
			int subSize = subAlarmList.size();
			for(int i = 0; i < subSize; i++){
				startTimeList.add(subAlarmList.get(i).getStartTime());
			}
			addStartTimeViews(startTimeList);
//			String thumbPath = common.getVideoThumbPath();
			
			String videoPath = common.getVideoPath();
			if(videoPath != null && videoPath.length() > 0){
				mVideoPath = videoPath;
				
			}else{
				mLinear_VideoPlay.setVisibility(View.GONE);
			}
		}
	}
	
	
	private String getRingtoneTitle(String uri){
		RingtoneTool tool = new RingtoneTool(ExerciseViewActivity.this);
		Ringtone ringtone = tool.getRingtoneByUriPath(RingtoneManager.TYPE_ALL, uri);
		if(ringtone == null){
			ringtone = tool.getDefaultRingtone(RingtoneManager.TYPE_ALL);
		}
		return ringtone.getTitle(ExerciseViewActivity.this);
	}
	
	private String getWeekday(String weekdayState){
		String[] weekdayShortArray = getResources().getStringArray(R.array.weekday_selection_short);
		char[] weekdayStateArray = weekdayState.toCharArray();
		String result = "";
		for(int i = 0; i < weekdayStateArray.length; i++){
			if(weekdayStateArray[i] == '1'){
				result += weekdayShortArray[i]+",";
			}
		}
		if(result.length() > 0){
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
	
	private void addStartTimeViews(List<String> list){
		LinearLayout linear = mLinear_StartTime;
		LayoutInflater inflater = LayoutInflater.from(ExerciseViewActivity.this);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		int size = list.size();
		for(int i = 0; i < size; i++){
			View subAlarmView = inflater.inflate(R.layout.sub_alarm_view, null);
			TextView tv_title = (TextView) subAlarmView.findViewById(R.id.tv_sub_title);
			TextView tv_startTime = (TextView) subAlarmView.findViewById(R.id.tv_sub_start_time);
			
			if( i== 0){
				tv_title.setText(R.string.start_time_colon);
			}
			tv_startTime.setText(list.get(i));
			linear.addView(subAlarmView, params);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		LocalLog.i(TAG, "onClick","view = "+v.getClass().toString());
		if(v == mButton_Back){
			finish();
		}else if(v == mButton_Edit){
			Intent intent = new Intent(ExerciseViewActivity.this,ExerciseAddActivity.class);
			intent.putExtra(Constant.Alarm.Intent.KEY_ACTION, Constant.Alarm.Intent.ACTION_EDIT_EXERCISE);
			intent.putExtra(Constant.Alarm.Intent.KEY_ALARM_ID,mAlarmId);
			startActivity(intent);
		}else if(v == mButton_Configure){
			Intent intent = new Intent(ExerciseViewActivity.this,ExerciseConfigureActivity.class);
			intent.putExtra(Constant.Alarm.Intent.KEY_ALARM_ID,mAlarmId);
			startActivity(intent);
		}else if(v == mButton_Delete){
			showDeleteAlertDialog(R.string.delete_alarm_warning_title,R.string.delete_alarm_warning_content);
		}else if(v == mLinear_VideoPlay){
			if(mVideoPath == null || mVideoPath.length() <= 0){
				LocalLog.e(TAG, "playVideo","voideo path is null");
				showToast(R.string.video_path_null,Toast.LENGTH_LONG);
			}else{
				showVideoPlayer();
			}
		}
	}
	
	private void showVideoPlayer(){
		Intent localIntent = new Intent(ExerciseViewActivity.this,
				VideoViewActivity.class);
		localIntent.putExtra(Constant.Alarm.Intent.KEY_VIDEO_PATH, this.mVideoPath);
		startActivity(localIntent);
	}
	
	private void initVideoPlayer(VideoView videoView,String path){
		MediaController controller = new MediaController(ExerciseViewActivity.this);
		controller.setAnchorView(videoView);
		videoView.setMediaController(controller);
		videoView.setVideoPath(new File(mVideoPath).getAbsolutePath());
		videoView.start();
//		videoView.pause();
//		videoView.start();
//		controller.invalidate();
	}
	
	private void showToast(int textId,int duration){
		Toast.makeText(ExerciseViewActivity.this, textId, duration).show();
	}
	
	private void showDeleteAlertDialog(int titleId,int messageId){
		AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseViewActivity.this);
		builder.setTitle(titleId);
		builder.setMessage(messageId);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteAlarm();
			}
		});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		builder.create().show();
	}
	
	private void deleteAlarm(){
		final DeleteTask deleteTask = new DeleteTask(ExerciseViewActivity.this){
			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
					mHandler.sendEmptyMessage(Constant.Handler.DELETE_SUCCESS);
				}else{
					mHandler.sendEmptyMessage(Constant.Handler.DELETE_FAIL);
				}
			}
		};
		
		mProgress = getProgressInstance(R.string.deleting);
		mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if(deleteTask.isCancelled()){
					deleteTask.cancel(true);
				}
			}
		});
		mProgress.show();
		
		deleteTask.execute(String.valueOf(mAlarmId));
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Constant.Handler.DELETE_FAIL:
				showToastResult(R.string.delete_fail,0);
				break;
			case Constant.Handler.DELETE_SUCCESS:
				showToastResult(R.string.delete_successfully,0);
				break;
			default:
				break;
			}
		}
		
	};
	
	private void showToastResult(int messageId,int duration){
		mProgress.dismiss();
		if(duration != 1){
			Toast.makeText(ExerciseViewActivity.this, messageId, Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(ExerciseViewActivity.this, messageId, Toast.LENGTH_SHORT).show();
		}
		finish();
	}
	
	private ProgressDialog getProgressInstance(int messageId){
		ProgressDialog progress = null;
		if(ExerciseViewActivity.this.isChild()){
			progress = new ProgressDialog(ExerciseViewActivity.this.getParent());
		}else{
			progress = new ProgressDialog(ExerciseViewActivity.this);
		}
		progress.setMessage(getResources().getString(messageId));
		return progress;
	}
}
