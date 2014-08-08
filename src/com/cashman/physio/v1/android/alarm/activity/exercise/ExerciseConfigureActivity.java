package com.cashman.physio.v1.android.alarm.activity.exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.adapter.AlarmStartTimeAdapter;
import com.cashman.physio.v1.android.alarm.data.Alarm;
import com.cashman.physio.v1.android.alarm.data.CommonSetting;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.db.DbManager;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.RingtoneTool;

public class ExerciseConfigureActivity extends Activity {

	private LinearLayout mLinear_Weekday;
	private ListView mListView_Alarm;
	private Spinner mSpinner_Ringtone;
	private CheckBox mCheckBox_RingtoneEnable;
	private CheckBox mCheckBox_VibrateEnable;
	private CheckBox mCheckBox_Activate;
	private Button mButton_Back;
	private Button mButton_Save;
	private ProgressDialog mProgress;
	
	private CommonSetting mCommonSetting;
	private Ringtone mRingtone;
	private int mRingtoneSelectedPosition;
	private String mRingtoneUri;
	
	private boolean[] mWeekday_SelectedState = Constant.Alarm.WEEKDAY_SELECTION_DEFAULT;
	
	private static final String TAG = "ExerciseConfigureActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_configure);
		
		initViews();
	}
	
	private void initViews(){
		mLinear_Weekday = (LinearLayout) findViewById(R.id.linear_weekday);
		mListView_Alarm = (ListView) findViewById(R.id.lv_alarm_start_time);
		mSpinner_Ringtone = (Spinner) findViewById(R.id.spinner_ringtone);
		mCheckBox_RingtoneEnable = (CheckBox) findViewById(R.id.chk_ringtone_enable);
		mCheckBox_VibrateEnable = (CheckBox) findViewById(R.id.chk_vibrate_enable);
		mCheckBox_Activate = (CheckBox) findViewById(R.id.chk_activate);
		mButton_Back = (Button) findViewById(R.id.btn_back);
		mButton_Save = (Button) findViewById(R.id.btn_save);
		
		mButton_Back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mButton_Save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				save();
			}
		});
		
		mLinear_Weekday.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showWeekdayDialog();
			}
		});
		
		mListView_Alarm.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showStartTimeDialog(position);
			}
		});
		
		
		initShows();
	}
	
	private void initShows(){
		Intent intent = getIntent();
		if(intent != null){
			int alarmId = intent.getIntExtra(Constant.Alarm.Intent.KEY_ALARM_ID, 0);
			if(alarmId > 0){
				DbManager dbManager = new DbManager(ExerciseConfigureActivity.this);
				Alarm alarm = dbManager.getAlarmById(alarmId);
				CommonSetting common = dbManager.getCommonSettingByAlarmId(alarmId);
				mCommonSetting = common;
				List<Alarm> alarmList = new ArrayList<Alarm>();
				List<Alarm> subAlarmList = dbManager.getAlarmListByForeignId(alarmId);
				alarmList.add(alarm);
				alarmList.addAll(subAlarmList);
				showAlarms(alarmList);
				mWeekday_SelectedState = ExerciseAddActivity.getWeekdayState(common.getWeekday());
				mCheckBox_RingtoneEnable.setChecked(common.isRingtoneEnable());
				mCheckBox_VibrateEnable.setChecked(common.isVibrateEnable());
				mCheckBox_Activate.setChecked(common.isActivate());
				initSpinner(common.getRingtonePath());
			}else{
				LocalLog.e(TAG,"initShows","alarmId is < 0");
				showToast(R.string.load_error,0);
			}
		}else{
			LocalLog.e(TAG,"initShows","intent is null");
			showToast(R.string.load_error,0);
		}
	}
	
	private void showWeekdayDialog(){
		final boolean[] weekday_CurrentState = mWeekday_SelectedState.clone();
		Resources res = getResources();
		AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseConfigureActivity.this);
		builder.setIcon(android.R.drawable.btn_star);
		builder.setTitle(res.getString(R.string.dialog_title_weekday));
		builder.setMultiChoiceItems(R.array.weekday_selection, weekday_CurrentState, new OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				weekday_CurrentState[which] = isChecked;
			}
		});
		
		builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean success = false;
				for(int i =0; i < weekday_CurrentState.length ; i++){
					if(weekday_CurrentState[i]){
						success = true;
						break;
					}
				}
				if(success){
					mWeekday_SelectedState = weekday_CurrentState;
				}else{
					showToast(R.string.weekday_selected_null_wraning,0);
				}
			}
		});
		
		builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
			}
		});
		
		builder.create().show();
	}
	
//	private void addAlarmViews(List<Alarm> subAlarmList){
//		if(subAlarmList == null || subAlarmList.size() <= 0){
//			return;
//		}
//		LayoutInflater inflater = LayoutInflater.from(ExerciseConfigureActivity.this);
//		int size = subAlarmList.size();
//		for(int i = 0 ; i < size; i++){
//			Alarm alarm = subAlarmList.get(i);
//			View convertView = inflater.inflate(R.layout.sub_alarm_view, null);
//			TextView tv_startTime = (TextView) convertView.findViewById(R.id.tv_sub_start_time);
//			tv_startTime.setText(alarm.getStartTime());
//			tv_startTime.setText(Constant.Alarm.SUB_ALARM_PREFIX+i);
//			mLinear_Alarms.addView(tv_startTime);
//		}
//	}
	
	private void showAlarms(List<Alarm> alarmList){
		AlarmStartTimeAdapter adapter = new AlarmStartTimeAdapter(ExerciseConfigureActivity.this,alarmList);
		mListView_Alarm.setAdapter(adapter);
		setListViewHeightBasedOnChildren(mListView_Alarm);
	}
	
    public void setListViewHeightBasedOnChildren(ListView listView) {  
    	AlarmStartTimeAdapter listAdapter = (AlarmStartTimeAdapter) listView.getAdapter();   
        if (listAdapter == null) {  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        params.height += 5;//if without this statement,the listview will be a little short  
        listView.setLayoutParams(params);  
    }  
	
	private void showStartTimeDialog(final int position){
		final AlarmStartTimeAdapter adapter = (AlarmStartTimeAdapter) mListView_Alarm.getAdapter();
		String startTime = ((Alarm)adapter.getItem(position)).getStartTime();
		String[] split = startTime.split(":");
		AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseConfigureActivity.this);
		builder.setTitle(startTime);
		final TimePicker timePicker = new TimePicker(ExerciseConfigureActivity.this);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(Integer.parseInt(split[0]));
		timePicker.setCurrentMinute(Integer.parseInt(split[1]));
//		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		builder.setView(timePicker);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int hour = timePicker.getCurrentHour();
				int minute = timePicker.getCurrentMinute();
				String result = String.valueOf(hour < 10 ? "0"+hour : hour)+":"+(minute < 10 ? "0"+minute: minute);
				adapter.setStartTime(position, result);
				adapter.notifyDataSetChanged();
			}
		});
		if(position != 0){
			builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					boolean success = adapter.remove(position);
					if(success){
						showToast(R.string.delete_parent_alarm_fail, 0);
					}else{
						adapter.notifyDataSetChanged();
						setListViewHeightBasedOnChildren(mListView_Alarm);
					}
				}
			});
		}
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	private void initSpinner(String selectedUri){
		int selectedPosition = 0;
		RingtoneTool ringtoneTool = new RingtoneTool(ExerciseConfigureActivity.this);
		List<Map<String,String>> infoList = ringtoneTool.getRingtoneInfoList(RingtoneManager.TYPE_ALL);
		int size = infoList.size();
		if(selectedUri == null || selectedUri.length() <= 0){
			selectedPosition = 0;
		}else{
			for(int i = 0; i < size; i++){
				String uri = infoList.get(i).get(Constant.Ringtone.KEY_RINGTONE_URI);
				if(uri.equals(selectedUri)){
					selectedPosition = i;
					break;
				}
			}
		}
		setSpinnerAdapter(infoList,selectedPosition);
	}
	
	private void setSpinnerAdapter(final List<Map<String,String>> list, int position){
//		RingtoneTool ringtoneTool = new RingtoneTool(ExerciseConfigureActivity.this);
//		final List<Map<String,String>> infoList = ringtoneTool.getRingtoneInfoList(RingtoneManager.TYPE_ALL);
		SimpleAdapter adapter = new SimpleAdapter(ExerciseConfigureActivity.this,list,
								android.R.layout.simple_spinner_item,
								new String[]{Constant.Ringtone.KEY_RINGTONE_TITLE},
								new int[]{android.R.id.text1});
		mSpinner_Ringtone.setAdapter(adapter);
		mSpinner_Ringtone.setSelection(position);
		mRingtoneUri = list.get(position).get(Constant.Ringtone.KEY_RINGTONE_URI);
		mSpinner_Ringtone.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					int position = mSpinner_Ringtone.getSelectedItemPosition();
					showRingtoneSelectDialog(list,position);
				}
				return true;
			}
		});
	}
	
	private void showRingtoneSelectDialog(final List<Map<String,String>> list,int position){
		int size = list.size();
		String[] titleArr = new String[size];
		for(int i = 0;i<size;i++){
			titleArr[i] = list.get(i).get(Constant.Ringtone.KEY_RINGTONE_TITLE);
		}
		final RingtoneTool ringtoneTool = new RingtoneTool(ExerciseConfigureActivity.this);
		mRingtone = ringtoneTool.getRingtone(RingtoneManager.TYPE_ALL, 0);
		AlertDialog.Builder builder  = new AlertDialog.Builder(ExerciseConfigureActivity.this);
		builder.setTitle(R.string.ringtone_selection_title);
		builder.setSingleChoiceItems(titleArr, position, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mRingtone != null && mRingtone.isPlaying()){
					mRingtone.stop();
				}
				mRingtoneSelectedPosition = which;
				String uriPath = list.get(which).get(Constant.Ringtone.KEY_RINGTONE_URI);
				
				mRingtone = ringtoneTool.getRingtoneByUriPath(RingtoneManager.TYPE_ALL, uriPath);
				mRingtone.play();
			}
		});
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mRingtone != null && mRingtone.isPlaying()){
					mRingtone.stop();
				}
				mSpinner_Ringtone.setSelection(mRingtoneSelectedPosition);
				mRingtoneUri = list.get(mRingtoneSelectedPosition).get(Constant.Ringtone.KEY_RINGTONE_URI);
				LocalLog.i(TAG, "which = "+mRingtoneSelectedPosition+"; spinner.getSelection = "+mSpinner_Ringtone.getSelectedItemPosition());
			}
		});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mRingtone != null && mRingtone.isPlaying()){
					mRingtone.stop();
				}
			}
		});
		
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				if(mRingtone != null && mRingtone.isPlaying()){
					mRingtone.stop();
				}
			}
		});
		
		builder.create().show();
	}
	
	private void showToast(int textId,int duration){
		if(duration != 1){
			Toast.makeText(ExerciseConfigureActivity.this, textId, Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(ExerciseConfigureActivity.this, textId, Toast.LENGTH_SHORT).show();
		}
	}

	private void save(){
		AlarmStartTimeAdapter startTimeAdapter = (AlarmStartTimeAdapter) mListView_Alarm.getAdapter();
		List<Alarm> alarmList = startTimeAdapter.getList();
		
		CommonSetting comm = mCommonSetting;
		comm.setActivate(mCheckBox_Activate.isChecked());
		comm.setRingtoneEnable(mCheckBox_RingtoneEnable.isChecked());
		comm.setVibrateEnable(mCheckBox_VibrateEnable.isChecked());
		comm.setWeekday(ExerciseAddActivity.getWeekdaySelecedStr(mWeekday_SelectedState));
		comm.setRingtonePath(mRingtoneUri);
		
		final SaveTask task = new SaveTask(alarmList,comm){
			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
					mHandler.sendEmptyMessage(Constant.Handler.SAVE_SUCCESS);
				}else{
					mHandler.sendEmptyMessage(Constant.Handler.SAVE_FAIL);
				}
			}
		};
		
		mProgress = getProgressDialogInstance(R.string.saving);
		mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if(task.isCancelled()){
					task.cancel(true);
				}
			}
		});
		
		task.execute();
	}
	
	private ProgressDialog getProgressDialogInstance(int textId){
		ProgressDialog progress = null;
		if(ExerciseConfigureActivity.this.isChild()){
			progress = new ProgressDialog(ExerciseConfigureActivity.this.getParent());
		}else{
			progress = new ProgressDialog(ExerciseConfigureActivity.this);
		}
		progress.setMessage(getResources().getString(textId));
		return progress;
	}
	
	@Override
	protected void onStart() {
	
		super.onStart();
	}
	
	private class SaveTask extends AsyncTask<String, String, Boolean>{
		
		private List<Alarm> mAlarmList;
		private CommonSetting mCommonSetting;
		
		public SaveTask(List<Alarm> alarmList,CommonSetting common){
			this.mAlarmList = alarmList;
			this.mCommonSetting = common;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = true;
			List<Alarm> alarmList = mAlarmList;
			CommonSetting common = mCommonSetting;
			long affectedRows = 0;
			int alarmListSize = alarmList.size();
			if(alarmList == null || alarmListSize <= 0){
				LocalLog.e(TAG, "SaveTask.doInbackground","the alarm to save is null");
				return false;
			}
			if(common == null){
				LocalLog.e(TAG, "SaveTask.doInbackground","the common setting to save is null");
				return false;
			}
			DbManager manager = new DbManager(ExerciseConfigureActivity.this);
			Alarm parentAlarm = mAlarmList.get(0);
			affectedRows = manager.updateAlarm(parentAlarm);
			if(affectedRows <= 0){
				LocalLog.e(TAG, "SaveTask.doInbackground","update base alarm error");
				result = false;
			}
			
			affectedRows = manager.deleteAlarmByForeignId(parentAlarm.getId());
			if(manager.findAlarmByForeignId(parentAlarm.getId()) && affectedRows <= 0){
				LocalLog.e(TAG, "SaveTask.doInbackground","delete sub alarms error");
				result = false;
			}
			for(int i = 1; i < alarmListSize; i++){
				affectedRows = manager.insertAlarm(alarmList.get(i));
				if(affectedRows <= 0){
					result = false;
					LocalLog.e(TAG, "SaveTask.doInbackground","insert sub alarms error");
					continue;
				}
			}
			affectedRows = manager.updateCommonSetting(common);
			if(affectedRows <= 0){
				result = false;
				LocalLog.e(TAG, "SaveTask.doInbackground","save common setting error");
			}
			return result;
		}
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Constant.Handler.SAVE_FAIL:
				showToast(R.string.save_fail,0);
				finish();
				break;
			case Constant.Handler.SAVE_SUCCESS:
				showToast(R.string.save_successfully,1);
				finish();
				break;
			default:
				break;
			}
		}
	};
}
