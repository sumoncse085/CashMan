package com.cashman.physio.v1.android.alarm.activity.exercise;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Alarm;
import com.cashman.physio.v1.android.alarm.data.CommonSetting;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.db.DbManager;
import com.cashman.physio.v1.android.alarm.util.CheckEnvironment;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.widget.NumericWheelAdapter;
import com.cashman.physio.v1.android.alarm.widget.WheelView;

public class ExerciseAddActivity extends Activity {

	private Button mButton_Back;
	private Button mButton_Save;
	private Button mButton_Delete;
	private LinearLayout mLinear_Name;
	private TextView mTextView_Name;
	private LinearLayout mLinear_Instruction;
	private TextView mTextView_Instruction;
	private LinearLayout mLinear_Times;
	private TextView mTextView_Times;
	private LinearLayout mLinear_Every;
	private TextView mTextView_Every;
	private LinearLayout mLinear_StartTime;
	private TextView mTextView_StartTime;
	private LinearLayout mLinear_Weekday;
	private LinearLayout mLinear_Video;
	private LinearLayout mLinear_Activate;
	private CheckBox mCheckbox_Activate;
	private ProgressDialog mProgress;
//	private String mAlarmName = null;
//	private String mAlarmInstruction = null;
//	private int mTimesPerday = 0;
//	private int mEvery = 0;
//	private int mStartTime_Hour = 0;
//	private int mStartTime_Minute = 0;
	private String mVideoFilePath = "";
	private boolean[] mWeekday_State = Constant.Alarm.WEEKDAY_SELECTION_DEFAULT;
	private float density = 1.0f;
	private static final int TEXT_SIZE = 18;
	private static final int TEXT_SIZE_VALUE = 20;
	
	private static final String TAG = "AlarmAddActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_add);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		density = dm.density;
		
		initViews();
	}
	
	private void initViews(){
		 TextView headTxt = (TextView)findViewById(R.id.head_nab_txt_title);
		 headTxt.setText("Exercise");
		mButton_Back = (Button) findViewById(R.id.btn_back);
		mButton_Save = (Button) findViewById(R.id.btn_save);
		mButton_Delete = (Button) findViewById(R.id.btn_delete);
		mLinear_Weekday = (LinearLayout) findViewById(R.id.linear_weekday);
		
		mLinear_Name = (LinearLayout) findViewById(R.id.linear_name);
		mTextView_Name = (TextView) findViewById(R.id.txt_name);
		
		mLinear_Instruction = (LinearLayout) findViewById(R.id.linear_instruction);
		mTextView_Instruction = (TextView) findViewById(R.id.txt_instruction);
		
		mLinear_Times = (LinearLayout) findViewById(R.id.linear_times_per_day);
		mTextView_Times = (TextView) findViewById(R.id.txt_times_per_day);
		
		mLinear_Every = (LinearLayout) findViewById(R.id.linear_every);
		mTextView_Every = (TextView) findViewById(R.id.txt_every);
		
		mLinear_StartTime = (LinearLayout) findViewById(R.id.linear_start_time);
		mTextView_StartTime = (TextView) findViewById(R.id.txt_start_time);
		
		mLinear_Video = (LinearLayout) findViewById(R.id.linear_video);
		mLinear_Activate = (LinearLayout) findViewById(R.id.linear_activate);
		mCheckbox_Activate = (CheckBox) findViewById(R.id.chk_activate);
		
		mLinear_Weekday.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showWeekdayDialog();
			}
		});
		
		mLinear_Name.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ExerciseAddActivity.this,NameSettingActivity.class);
				Bundle b = new Bundle();
				b.putString(Constant.Alarm.Intent.KEY_ALARM_NAME, mTextView_Name.getText().toString());
				i.putExtra(Constant.Alarm.Intent.BUNDLE_FOR_NAME, b);
				startActivityForResult(i, Constant.Alarm.Intent.REQUEST_NAME);
			}
		});
		
		mLinear_Instruction.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ExerciseAddActivity.this,InstructionSettingActivity.class);
				Bundle b = new Bundle();
				b.putString(Constant.Alarm.Intent.KEY_ALARM_INSTRUCTION, mTextView_Instruction.getText().toString());
				i.putExtra(Constant.Alarm.Intent.BUNDLE_FOR_INSTRUCTION, b);
				startActivityForResult(i, Constant.Alarm.Intent.REQUEST_INSTRUCTION);
			}
		});
		
		mButton_Back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				finish();
			}
		});
		
		mButton_Delete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int titleId = R.string.reset_alarm_warning_title;
				int messageId = R.string.reset_alarm_warning_content;
				int action = getIntent().getIntExtra(Constant.Alarm.Intent.KEY_ACTION, 0);
				if(action == Constant.Alarm.Intent.ACTION_EDIT_EXERCISE){
					titleId = R.string.delete_alarm_warning_title;
					messageId = R.string.delete_alarm_warning_content;
					showDeleteDialog(titleId,messageId,Constant.Alarm.Intent.ACTION_EDIT_EXERCISE);
				}else{
					showDeleteDialog(titleId,messageId,Constant.Alarm.Intent.ACTION_ADD_EXERCISE);
				}
			}
		});
		
		mButton_Save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				String name = ""+mTextView_Name.getText().toString().trim();
				String instruction = ""+mTextView_Instruction.getText().toString().trim();
				if(name.length()<=0){
					showInputWarning(R.string.input_warning_title,R.string.name_empty_warning);
					return;
				}
				if(instruction.length() <= 0){
					showInputWarning(R.string.input_warning_title,R.string.instruction_empty_wraning);
					return;
				}
				String weekdaySelectedStr = getWeekdaySelecedStr(mWeekday_State);
				if(weekdaySelectedStr == null || weekdaySelectedStr.length() != 7 || weekdaySelectedStr.equals("0000000")){
					showInputWarning(R.string.input_warning_title,R.string.weekday_selected_null_content);
					return;
				}
				String timesPerday = ""+mTextView_Times.getText().toString();
				String every = ""+mTextView_Every.getText().toString();
				String startTime = ""+mTextView_StartTime.getText().toString();
				final SaveTask saveTask = new SaveTask(ExerciseAddActivity.this,startTime,
						weekdaySelectedStr, name, instruction, timesPerday,
						every, mVideoFilePath, "", mCheckbox_Activate.isChecked(),
						DateFormat.format("yyyy/MM/dd kk:mm", new Date()).toString(),
						"", true, true) {

					@Override
					protected void onPostExecute(Boolean result) {
						int result_code = Constant.Handler.SAVE_FAIL;
						if(result){
							result_code = Constant.Handler.SAVE_SUCCESS;
						}
						mHandler.sendEmptyMessage(result_code);
					}
				};
				
				mProgress  = getProgressInstance(R.string.saving);
				
				mProgress.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						if(!saveTask.isCancelled()){
							saveTask.cancel(true);
							Toast.makeText(ExerciseAddActivity.this, "Canceled",Toast.LENGTH_LONG).show();
						}
					}
				});
				mProgress.show();
				Intent intent = getIntent();
				int action = intent.getIntExtra(Constant.Alarm.Intent.KEY_ACTION,0);
				if(action == Constant.Alarm.Intent.ACTION_EDIT_EXERCISE){
					int alarmId = intent.getIntExtra(Constant.Alarm.Intent.KEY_ALARM_ID, -1);
					saveTask.execute(String.valueOf(alarmId));
				}else{
					saveTask.execute();
				}
			}
		});
		
		mLinear_Times.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
//				Intent i = new Intent(ExerciseAddActivity.this,TimeSettingActivity.class);
//				Bundle bundle = new Bundle();
//				
//				i.putExtra(Constant.Alarm.Intent.INTENT_FOR_TIMES_PER_DAY, true);
//				bundle.putString(Constant.Alarm.Intent.KEY_TIMES_PER_DAY, mTextView_Times.getText().toString());
//				i.putExtra(Constant.Alarm.Intent.BUNDLE_FOR_TIME_SETTING, bundle);
//				
//				startActivityForResult(i, Constant.Alarm.Intent.REQUEST_CODE);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseAddActivity.this);
				builder.setTitle(R.string.times_per_day);
				final WheelView timesView = new WheelView(ExerciseAddActivity.this);
				
				timesView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
				timesView.setAdapter(new NumericWheelAdapter(1, 24));
				timesView.setTextSize((int)(TEXT_SIZE * density),(int) (TEXT_SIZE_VALUE * density));
				timesView.setCurrentItem(Integer.parseInt(mTextView_Times.getText().toString()) - 1);
				builder.setView(timesView);
				builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mTextView_Times.setText(String.valueOf(timesView.getCurrentItem() + 1));
					}
				});
				builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				builder.create().show();
			}
		});
		
		mLinear_Every.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseAddActivity.this);
				builder.setTitle(R.string.every);
				final WheelView everyView = new WheelView(ExerciseAddActivity.this);
				everyView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
				everyView.setAdapter(new NumericWheelAdapter(0, 24));
				everyView.setTextSize((int)(TEXT_SIZE * density),(int) (TEXT_SIZE_VALUE * density));
				everyView.setLabel("hours");
				String everyStr = mTextView_Every.getText().toString();
				everyView.setCurrentItem(Integer.parseInt(everyStr.substring(0,everyStr.indexOf(" "))));
				builder.setView(everyView);
				builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int num = everyView.getCurrentItem();
						String label = num <= 1 ? " hour" : " hours";
						mTextView_Every.setText(String.valueOf(num)+label);
					}
				});
				builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				builder.create().show();
			}
		});
		
		mLinear_StartTime.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseAddActivity.this);
				builder.setTitle(R.string.start_time);
				LayoutInflater inflater = LayoutInflater.from(ExerciseAddActivity.this);
				View contentView = inflater.inflate(R.layout.start_time, null);
				final WheelView hourView = (WheelView) contentView.findViewById(R.id.wheel_start_time_hour);
				hourView.setAdapter(new NumericWheelAdapter(0, 23,"%02d"));
				hourView.setTextSize((int)(TEXT_SIZE * density),(int) (TEXT_SIZE_VALUE * density));
				final WheelView minuteView = (WheelView) contentView.findViewById(R.id.wheel_start_time_minute);
				minuteView.setAdapter(new NumericWheelAdapter(0, 59,"%02d"));
				minuteView.setTextSize((int)(TEXT_SIZE * density),(int) (TEXT_SIZE_VALUE * density));
//				String startTimeStr = mTextView_StartTime.getText().toString();
//				String[] split = startTimeStr.split(":");
//				hourView.setCurrentItem(Integer.parseInt(split[0]));
//				minuteView.setCurrentItem(Integer.parseInt(split[1]));
				
				Date date = new Date();
				hourView.setCurrentItem(date.getHours());
				minuteView.setCurrentItem(date.getMinutes());
				
				builder.setView(contentView);
				builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int hour = hourView.getCurrentItem();
						int minute = minuteView.getCurrentItem();
						String result = String.valueOf(hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0"+minute:minute);
						mTextView_StartTime.setText(result);
					}
				});
				builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				builder.create().show();
			}
		});
		
		mLinear_Activate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mCheckbox_Activate.setChecked(!mCheckbox_Activate.isChecked());
			}
		});
		
		mLinear_Video.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				if(!CheckEnvironment.isSdAvailable()){
					AlertDialog.Builder builder  =  new AlertDialog.Builder(ExerciseAddActivity.this);
					builder.setTitle(R.string.sd_warning);
					builder.setMessage(R.string.sd_warning_content);
					builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							doRecorder();
						}
					});
					builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					
					builder.create().show();
				}else{
					doRecorder();
				}
				
			}
		});
		
		initShows();
	}
	
	private void initShows(){
		Intent intent = getIntent();
		int action = intent.getIntExtra(Constant.Alarm.Intent.KEY_ACTION,0);
		if(action == Constant.Alarm.Intent.ACTION_EDIT_EXERCISE){
			
			int alarmId = intent.getIntExtra(Constant.Alarm.Intent.KEY_ALARM_ID, 0);
			DbManager dbManager = new DbManager(ExerciseAddActivity.this);
			Alarm alarm = dbManager.getAlarmById(alarmId);
			CommonSetting common = dbManager.getCommonSettingByAlarmId(alarmId);
			mTextView_Name.setText(common.getName());
			mTextView_Instruction.setText(common.getInstruction());
			mCheckbox_Activate.setChecked(common.isActivate());
			mWeekday_State = getWeekdayState(common.getWeekday());
			mTextView_Times.setText(String.valueOf(dbManager.getAlarmCountByForeignId(alarmId) + 1));
			mTextView_StartTime.setText(alarm.getStartTime());
			mTextView_Every.setText(getEvery(common.getEvery()));
			mVideoFilePath = common.getVideoPath();
		}else{
			mButton_Delete.setText(R.string.reset);
		}
	}
	
	private void showInputWarning(int titleId,int messageId){
		AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseAddActivity.this);
		builder.setTitle(titleId);
		builder.setMessage(messageId);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	protected static boolean[] getWeekdayState(String currentState){
		char[] stateArr = currentState.toCharArray();
		boolean[] stateBooleanArr = new boolean[stateArr.length];
		for(int i = 0; i < stateArr.length; i++){
			stateBooleanArr[i] = stateArr[i] == '1'? true:false;
		}
		return stateBooleanArr;
	}
	
	private String getEvery(String startTime,int alarmId,DbManager dbManager){
		List<Alarm> alarmList = dbManager.getAlarmListByForeignId(alarmId);
		if(alarmList == null || alarmList.size() <= 0){
			return "0 hour";
		}else{
			Alarm subAlarm = alarmList.get(0);
			String sub_startTime = subAlarm.getStartTime();
			int sub_hour = Integer.parseInt(sub_startTime.substring(0,sub_startTime.indexOf(":")));
			int hour = Integer.parseInt(startTime.substring(0, startTime.indexOf(":")));
			if(sub_hour <= hour){
				sub_hour = sub_hour + 24;
			}
			int every = sub_hour - hour;
			if(every > 1){
				return ""+every+" hours";
			}else{
				return ""+every+" hour";
			}
		}
	}
	
	private String getEvery(int every){
		String everyStr = ""+every+" hour";
		if(every > 1){
			everyStr += "s";
		}
		return everyStr;
	}
	
	private void showDeleteDialog(int titleId,int messageId,int action){
		AlertDialog.Builder builder  = new AlertDialog.Builder(ExerciseAddActivity.this);
		builder.setTitle(titleId);
		builder.setMessage(messageId);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = getIntent();
				int action = intent.getIntExtra(Constant.Alarm.Intent.KEY_ACTION,0);
				if(action == Constant.Alarm.Intent.ACTION_EDIT_EXERCISE){
					int alarmId = intent.getIntExtra(Constant.Alarm.Intent.KEY_ALARM_ID, 0);
					deleteExerciseOnExist(alarmId);
				}else{
					reset();
				}
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
	
	private void reset(){
		mTextView_Name.setText("");
		mTextView_Instruction.setText("");
		mWeekday_State = Constant.Alarm.WEEKDAY_SELECTION_DEFAULT;
		mTextView_Every.setText(R.string.text_every_default);
		mTextView_Times.setText(R.string.text_times_default);
		mTextView_StartTime.setText(R.string.text_start_time_default);
		mCheckbox_Activate.setChecked(false);
		CheckEnvironment.deleteFile(mVideoFilePath);
	}
	
	private void doRecorder(){
		Intent i = new Intent(ExerciseAddActivity.this,VideoRecorderActivity.class);
		startActivityForResult(i, Constant.Alarm.Intent.REQUEST_VIDEO);
	}
	
	private void showWeekdayDialog(){
		Resources res = getResources();
		AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseAddActivity.this);
		builder.setIcon(android.R.drawable.btn_star);
		builder.setTitle(res.getString(R.string.dialog_title_weekday));
		builder.setMultiChoiceItems(R.array.weekday_selection, mWeekday_State, new OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				mWeekday_State[which] = isChecked;
			}
		});
		
		builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean result = false;
				boolean[] weekdayState = mWeekday_State;
				for(int i = 0; i<weekdayState.length; i++){
					if(weekdayState[i]){
						result = true;
						break;
					}
				}
				if(!result){
					mWeekday_State = Constant.Alarm.WEEKDAY_SELECTION_DEFAULT;
					Toast.makeText(ExerciseAddActivity.this, R.string.weekday_selected_null_wraning, Toast.LENGTH_LONG).show();
				}
			}
		});
		
		builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mWeekday_State = Constant.Alarm.WEEKDAY_SELECTION_DEFAULT;
			}
		});
		
		builder.create().show();
	}
	
	public static String getWeekdaySelecedStr(boolean[] selected){
		char[] charArray = new char[7];
		for(int i = 0; i < selected.length; i++){
			if(selected[i]){
				charArray[i] = '1';
			}else{
				charArray[i] = '0';
			}
		}
		return String.valueOf(charArray);
	}
	
	private ProgressDialog getProgressInstance(int message){
		ProgressDialog dialog = null;
		if(ExerciseAddActivity.this.isChild()){
			dialog = new ProgressDialog(ExerciseAddActivity.this.getParent());
		}else{
			dialog = new ProgressDialog(ExerciseAddActivity.this);
		}
		dialog.setMessage(getResources().getString(message));
		return dialog;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode != Constant.Alarm.Intent.RESULT_CODE_CANCEL){
			if(data != null){
				Bundle bundle = null;
				switch(requestCode){
				case Constant.Alarm.Intent.REQUEST_NAME:
					bundle = data.getBundleExtra(Constant.Alarm.Intent.BUNDLE_FOR_NAME);
					mTextView_Name.setText(bundle.getString(Constant.Alarm.Intent.KEY_ALARM_NAME));
					break;
				case Constant.Alarm.Intent.REQUEST_INSTRUCTION:
					bundle = data.getBundleExtra(Constant.Alarm.Intent.BUNDLE_FOR_INSTRUCTION);
					mTextView_Instruction.setText(bundle.getString(Constant.Alarm.Intent.KEY_ALARM_INSTRUCTION));
					break;
				case Constant.Alarm.Intent.REQUEST_VIDEO:
					bundle = data.getBundleExtra(Constant.Alarm.Intent.BUNDLE_FOR_VIDEO);
					mVideoFilePath = bundle.getString(Constant.Alarm.Intent.KEY_VIDEO_PATH);
					break;
				default:
					break;
				}
			}else{
				LocalLog.e(TAG, "onActivityResult", "the intent return is null");
			}
		}else{
			LocalLog.e(TAG, "onActivityResult", "canceled");
		}
	}
	
	private void deleteExerciseOnExist(int alarmId){
		final DeleteTask deleteTask = new DeleteTask(ExerciseAddActivity.this){
			@Override
			protected void onPostExecute(Boolean result) {
				int result_code = Constant.Handler.DELETE_FAIL;
				if(result){
					result_code = Constant.Handler.DELETE_SUCCESS;
				}
				mHandler.sendEmptyMessage(result_code);
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
		
		deleteTask.execute(String.valueOf(alarmId));
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Constant.Handler.SAVE_FAIL:
				showResult(R.string.save_fail);
				break;
			case Constant.Handler.SAVE_SUCCESS:
				showResult(R.string.save_successfully);
				break;
			case Constant.Handler.DELETE_FAIL:
				showResult(R.string.delete_fail);
				break;
			case Constant.Handler.DELETE_SUCCESS:
				showResult(R.string.delete_successfully);
				break;
			default:
				break;
			}
		}
	};
	
	private void showResult(int messageId){
		mProgress.dismiss();
		Toast.makeText(ExerciseAddActivity.this, messageId, Toast.LENGTH_LONG).show();
		finish();
	}
	
//	private class SaveTask extends AsyncTask<String, String, Boolean>{
//		
//		private int alarmId = -1;
//		private String startTime ;
//		private String weekday;
//		private String name ;
//		private String instruction;
//		private String times;
//		private String every;
//		private String videoPath;
//		private boolean activate;
//		private String videoThumPath;
//		private String createTime;
//		private String ringtonePath;
//		private boolean ringtoneEnable;
//		private boolean vibrateEnable;
//		private DbManager mDbmanager;
//		
//		protected SaveTask(String startTime,String weekday,String name,String instruction,String times,String every,String videoPath,String videoThumPath,boolean activate,String createTime,String ringtonePath,boolean ringtoneEnable,boolean vibrateEnable){
//			this(-1,startTime,weekday,name,instruction,times,every,videoPath,videoThumPath,activate,createTime,ringtonePath,ringtoneEnable,vibrateEnable);
//		}
//		
//		protected SaveTask(int alarmId,String startTime,String weekday,String name,String instruction,String times,String every,String videoPath,String videoThumPath,boolean activate,String createTime,String ringtonePath,boolean ringtoneEnable,boolean vibrateEnable){
//			this.alarmId = alarmId;
//			this.startTime = startTime;
//			this.weekday = weekday;
//			this.name = name;
//			this.instruction = instruction;
//			this.times = times;
//			this.every = every;
//			this.videoPath = videoPath;
//			this.videoThumPath = videoThumPath;
//			this.activate = activate;
//			this.createTime = createTime;
//			this.ringtonePath = ringtonePath;
//			this.ringtoneEnable = ringtoneEnable;
//			this.vibrateEnable = vibrateEnable;
//			mDbmanager = new DbManager(ExerciseAddActivity.this);
//		}
//		@Override
//		protected Boolean doInBackground(String... params) {
//			long result = 0L;
//			if(params == null || params.length <= 0){
//				Alarm alarm = new Alarm(startTime,-1);
//				
//				result = mDbmanager.insertAlarm(alarm);
//				if(result != -1){
//					alarm = mDbmanager.getLastAlarm();
//					if(alarm != null){
//						int alarmId = alarm.getId();
//						CommonSetting common = new CommonSetting(
//								alarmId,
//								name,
//								instruction,
//								createTime,
//								weekday,
//								ringtonePath,
//								ringtoneEnable,
//								vibrateEnable,
//								videoPath,
//								videoThumPath,
//								activate);
//						result = mDbmanager.insertCommonSetting(common);
//						if(result != -1){
//							if(addAlarmByForeignId(alarmId,startTime,every,times)){
//								return true;
//							}else{
//								LocalLog.e(TAG,"SaveTask.doInbackground","add alarm by foreignId = "+alarmId+" error");
//							}
//						}else{
//							LocalLog.e(TAG, "SaveTask.doInbackground", "insert common setting error");
//						}
//					
//					}else{
//						LocalLog.e(TAG, "SaveTask.doInbackground", "get last alarm error" );
//					}
//				}else{
//					LocalLog.e(TAG, "SaveTask.doInbackground","insert alarm error");
//				}
//			}else{
//				int alarmId = Integer.parseInt(params[0]);
//				Alarm alarm = mDbmanager.getAlarmById(alarmId);
//				if(alarm == null){
//					LocalLog.e(TAG,"SaveTask.doInbackground","get alarm with alarmId = "+alarmId+" null");
//					return false;
//				}
//				alarm.setForeignId(-1);
//				alarm.setStartTime(startTime);
//				result = mDbmanager.updateAlarm(alarm);
//				if(result > 0){
//					CommonSetting common = mDbmanager.getCommonSettingByAlarmId(alarmId);
//					if(!common.getVideoPath().equals(videoPath)){
//						CheckEnvironment.deleteFile(common.getVideoPath());
//						CheckEnvironment.deleteFile(common.getVideoThumbPath());
//					}
//					common.setActivate(activate);
//					common.setName(name);
//					common.setInstruction(instruction);
//					common.setCreateTime(createTime);
//					common.setWeekday(weekday);
//					common.setRingtonePath(ringtonePath);
//					common.setRingtoneEnable(ringtoneEnable);
//					common.setVideoPath(videoPath);
//					common.setVideoThumbPath(videoThumPath);
//					common.setActivate(activate);
//					
//					result = mDbmanager.updateCommonSetting(common);
//					if(result > 0){
//						result = mDbmanager.deleteAlarmByForeignId(alarmId);
//						if(result > 0){
//							if(addAlarmByForeignId(alarmId,startTime,every,times)){
//								return true;
//							}else{
//								LocalLog.e(TAG,"SaveTask.doInbackground","add alarm by foreignId = "+alarmId+" error");
//							}
//						}else{
//							LocalLog.e(TAG,"SaveTask.doInbackground", "delete alarm with foreignId = "+alarmId+" error");
//						}
//					}else{
//						LocalLog.e(TAG,"SaveTask.doInbackground", "update the common setting with alarmId = "+alarmId+" error");
//					}
//				}else{
//					LocalLog.e(TAG,"SaveTask.doInbackground", "update the alarm with alarmId = "+alarmId+" error");
//				}
//			}
//			return false;
//		}
//		
//		private boolean addAlarmByForeignId(int foreignId,String startTime,String every,String times){
//			long result = 0L;
//			String[] startTimeSplit = startTime.split(":");
//			int hour = Integer.parseInt(startTimeSplit[0]);
//			int everyAlarm = Integer.parseInt(every.substring(0,every.indexOf(" ")));
//			int timesPerday = Integer.parseInt(times);
//			
//			for(int i = 1; i < timesPerday; i++){
//				int startHour =  (hour + everyAlarm * i) % 24;
//				String startTimei = String.valueOf(startHour < 10 ? "0"+startHour : startHour)+":"+startTimeSplit[1];
//				Alarm alarm = new Alarm(startTimei,foreignId);
//				result = mDbmanager.insertAlarm(alarm);
//				if(result == -1){
//					LocalLog.e(TAG, "addAlarmByForeignId","add alarm with foreignId = "+foreignId+" error");
//				}
//			}
//			return true;
//		}
//	}
	
//	private class DeleteTask extends AsyncTask<String, String, Boolean>{
//		
//		@Override
//		protected Boolean doInBackground(String... params) {
//			if(params != null && params.length > 0){
//				int alarmId = Integer.parseInt(params[0]);
//				DbManager manager = new DbManager(ExerciseAddActivity.this);
//				int result = 0;
//				result = manager.deleteAlarmById(alarmId);
//				if(result > 0){
//					result = manager.deleteAlarmByForeignId(alarmId);
//					if(result <= 0){
//						LocalLog.e(TAG, "DeleteTask.doInBackground", "delete alarm with foreignId = "+alarmId+" error");
//					}
//					CommonSetting cs = manager.getCommonSettingByAlarmId(alarmId);
//					if(cs != null){
//						String videoPath = cs.getVideoPath();
//						if(videoPath != null && videoPath.length() > 0){
//							if(CheckEnvironment.deleteFile(videoPath)){
//								LocalLog.e(TAG,"DeleteTask.doInBackground","delete video file with the path = "+videoPath+" successfully");
//							}else{
//								LocalLog.e(TAG,"DeleteTask.doInBackground","delete video file with the path = "+videoPath+" error");
//							}
//						}
//						String videoThumbPath = cs.getVideoThumbPath();
//						if(videoThumbPath != null && videoThumbPath.length() > 0){
//							if(CheckEnvironment.deleteFile(videoThumbPath)){
//								LocalLog.e(TAG,"DeleteTask.doInBackground","delete video thumb image with the path = "+videoThumbPath+" successfully");
//							}else{
//								LocalLog.e(TAG,"DeleteTask.doInBackground","delete video video thumb image with the path = "+videoThumbPath+" error");
//							}
//						}
//					}else{
//						LocalLog.e(TAG,"DeleteTask.doInBackground","get common setting with alarmId = "+alarmId+" error");
//					}
//					result = manager.deleteCommonSettingByAlarmId(alarmId);
//					if(result <= 0){
//						LocalLog.e(TAG, "DeleteTask.doInBackground", "delete common setting with alarmId = "+alarmId+" error");
//					}
//					return true;
//				}else{
//					LocalLog.e(TAG, "DeleteTask.doInBackground", "delete alarm with alarmId = "+alarmId+" error");
//				}
//			}
//			
//			return false;
//		}
//	}
}
