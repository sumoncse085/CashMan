package com.cashman.physio.v1.android.alarm.activity.exercise;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.AlarmApplication;
import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.MainActivity;
import com.cashman.physio.v1.android.alarm.adapter.AlarmAdapter;
import com.cashman.physio.v1.android.alarm.data.AlarmItem;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.AlarmTool;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

public class ExerciseActivity extends Activity {
	
	private Button mButton_Add;
	private ListView mListView_Exercise;
	private ProgressDialog mProgress;
	private String[] mActions_ItemClick;
	
	private static final String TAG = "ExerciseActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_list);
		initViews();
	}

	private void initViews(){
		mButton_Add = (Button) findViewById(R.id.btn_add);
		  TextView headTxt = (TextView)findViewById(R.id.head_nab_txt_title);
		  headTxt.setText("Rehab Exercise");
		mButton_Add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ExerciseActivity.this,ExerciseAddActivity.class);
				i.putExtra(Constant.Alarm.Intent.KEY_ACTION, Constant.Alarm.Intent.ACTION_ADD_EXERCISE);
				startActivity(i);
			}
		});
		
		mActions_ItemClick = getResources().getStringArray(R.array.action_alarm_item_click);
		mListView_Exercise = (ListView) findViewById(R.id.lv_alarms);
		mListView_Exercise.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this.getParent());
				builder.setItems(mActions_ItemClick,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = null;
						AlarmAdapter adapter = (AlarmAdapter) arg0.getAdapter();
						AlarmItem item = (AlarmItem) adapter.getItem(arg2);
						
						switch(which){
						case Constant.Alarm.AlarmAction.ACTION_CONFIGURE:
							intent = new Intent(ExerciseActivity.this,ExerciseConfigureActivity.class);
							intent.putExtra(Constant.Alarm.Intent.KEY_ALARM_ID,item.getAlarmId());
							startActivity(intent);
							break;
						case Constant.Alarm.AlarmAction.ACTION_VIEW:
							intent = new Intent(ExerciseActivity.this,ExerciseViewActivity.class);
							intent.putExtra(Constant.Alarm.Intent.KEY_ALARM_ID,item.getAlarmId());
							startActivity(intent);
							break;
						case Constant.Alarm.AlarmAction.ACTION_EDIT:
							intent = new Intent(ExerciseActivity.this,ExerciseAddActivity.class);
							intent.putExtra(Constant.Alarm.Intent.KEY_ACTION, Constant.Alarm.Intent.ACTION_EDIT_EXERCISE);
							intent.putExtra(Constant.Alarm.Intent.KEY_ALARM_ID,item.getAlarmId());
							startActivity(intent);
							break;
						case Constant.Alarm.AlarmAction.ACTION_DELETE:
							showDeleteDialog(item, arg2);
						default:
							break;
						}
					}
				});
				builder.create().show();
				return true;
			}
		});
		
		mListView_Exercise.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LocalLog.i(TAG, "item click");
			}
		});
	}
	
	private void showDeleteDialog(final AlarmItem item,final int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseActivity.this);
		builder.setTitle(R.string.delete_alarm_warning_title);
		builder.setMessage(String.format(getResources().getString(R.string.delete_alarm_warning_content2), item.getName()));
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteAlarm(item,position);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.create().show();
	}
	
	private void deleteAlarm(AlarmItem item,final int position){
		final DeleteTask deleteTask = new DeleteTask(ExerciseActivity.this){
			@Override
			protected void onPostExecute(Boolean result) {
				Message msg = mHandler.obtainMessage(Constant.Handler.DELETE_FAIL);
				if(result){
					msg.what = Constant.Handler.DELETE_SUCCESS;
					msg.arg1 = position;
				}
				mHandler.dispatchMessage(msg);
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
		
		deleteTask.execute(String.valueOf(item.getAlarmId()));
	}
	
	private void load(){
		final LoadTask load = new LoadTask(ExerciseActivity.this){
			@Override
			protected void onPostExecute(List<AlarmItem> result) {
				Message msg = mHandler.obtainMessage(Constant.Handler.LOAD_FINISH);
//				if(result != null && result.size() > 0){
//					msg.what = Constant.Handler.LOAD_SUCCESS;
//					msg.obj = result;
//				}else{
//					LocalLog.e(TAG, "load()", "result == null or result.size() <= 0");
//				}
				msg.obj = result;
				mHandler.dispatchMessage(msg);
			}
		};
		mProgress = getProgressInstance(R.string.loading);
		mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if(load.isCancelled()){
					load.cancel(true);
				}
			}
		});
		
		mProgress.show();
		load.execute();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		load();
		
		
	}
	
	private ProgressDialog getProgressInstance(int messageId){
		ProgressDialog progress = null;
		if(ExerciseActivity.this.isChild()){
			progress = new ProgressDialog(ExerciseActivity.this.getParent());
		}else{
			progress = new ProgressDialog(ExerciseActivity.this);
		}
		progress.setMessage(getResources().getString(messageId));
		return progress;
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Constant.Handler.LOAD_FINISH:
				setAdapter(msg.obj);
				mProgress.dismiss();
				break;

			case Constant.Handler.DELETE_FAIL:
				mProgress.dismiss();
				showToast(R.string.delete_fail,0);
				break;
			case Constant.Handler.DELETE_SUCCESS:
				mProgress.dismiss();
				showToast(R.string.delete_successfully,1);
				removeItem(msg.arg1);
				break;
			default:
				break;
			}
		}
		
		private void showToast(int textId,int duration){
			if(duration == 0){
				Toast.makeText(ExerciseActivity.this, textId, Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(ExerciseActivity.this, textId, Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private void setAdapter(Object o){
		List<AlarmItem> itemList = (List<AlarmItem>) o;
		AlarmAdapter adapter = new AlarmAdapter(ExerciseActivity.this,itemList);
		mListView_Exercise.setAdapter(adapter);
	}
	
	private void removeItem(int position){
		AlarmAdapter adapter = (AlarmAdapter) mListView_Exercise.getAdapter();
		adapter.remove(position);
		adapter.notifyDataSetChanged();
		updateAlarm();
	}
	
	private void updateAlarm(){
		AlarmApplication.appInstance.startAlarm();
	}
	
//	private class LoadTask extends AsyncTask<String, String, List<AlarmItem>>{
//		
//		private final Date CURRENT_DATE = new Date();
//		
//		@Override
//		protected List<AlarmItem> doInBackground(String... params) {
//			DbManager dbManager = new DbManager(ExerciseActivity.this);
//			List<Alarm> alarmList = dbManager.getAlarmListByForeignId(-1);
//			List<AlarmItem> itemList = null;
//			int alarmSize = 0;
//			if(alarmList != null && alarmList.size() > 0){
//				itemList = new ArrayList<AlarmItem>();
//				List<String> startTimeList=null;
//				alarmSize = alarmList.size();
//				
//				for(int i = 0; i < alarmSize; i++){
//					startTimeList = new ArrayList<String>();
//					Alarm alarm = alarmList.get(i);
//					int alarmId = alarm.getId();
//					CommonSetting common = dbManager.getCommonSettingByAlarmId(alarmId);
//					AlarmItem item = new AlarmItem();
//					item.setAlarmId(alarmId);
//					item.setName(common.getName());
//					item.setInstruction(common.getInstruction());
//					item.setActivate(common.isActivate());
//					item.setThumbPath(common.getVideoThumbPath());
//					item.setVideoPath(common.getVideoPath());
//					item.setWeekday(common.getWeekday());
//					startTimeList.add(alarm.getStartTime());
//					List<Alarm> subAlarmList = dbManager.getAlarmListByForeignId(alarmId);
//					if(subAlarmList != null && subAlarmList.size() > 0){
//						int subAlarmSize = subAlarmList.size();
//						for(int j = 0; j < subAlarmSize; j++){
//							startTimeList.add(subAlarmList.get(j).getStartTime());
//						}
//					}
//					
//					item.setStartTimeList(startTimeList);
//					
//					item.setRingTime(getClosestTime(startTimeList,common.getWeekday()));
//					itemList.add(item);
//				}
//				
//				Collections.sort(itemList, new AlarmComparator());
//				
//				for(int i = 0; i< alarmSize; i++){
//					AlarmItem item1 = itemList.get(i);
//					
//					LocalLog.i(TAG, "alarm: id = "+item1.getAlarmId()+"; name = "+item1.getName()
//							+"; instruction = "+item1.getInstruction()
//							+"; ringtime = "+item1.getRingTime()
//							+"; startTimeList = "+item1.getStartTimeList().toString());
//				}
//			}
//			
//			return itemList;
//		}
//		
//		private String getClosestTime(List<String> startTimeList,String weekdaySelected){
//			Date currentDate = CURRENT_DATE;
//			long currentTime = currentDate.getTime();
//			int currentWeekday = currentDate.getDay();
//			char[] weekdaySelectedArr = weekdaySelected.toCharArray();
//			if(weekdaySelectedArr[currentWeekday] != '1'){
//				return startTimeList.get(0);
//			}
//			long delta = 0L;
//			String result = "";
//			int size = startTimeList.size();
//			if(size == 1){
//				return startTimeList.get(0);
//			}
//			for(int i = 0; i < size; i++){
//				String startTimeStr = startTimeList.get(i);
//				String[] split = startTimeStr.split(":");
//				Date startTime = new Date();
//				startTime.setHours(Integer.parseInt(split[0]));
//				startTime.setMinutes(Integer.parseInt(split[1]));
//				long time = startTime.getTime();
//				if(time == currentTime){
//					return startTimeStr;
//				}else if(time > currentTime){
//					if(delta == 0 || delta > time - currentTime){
//						delta = time - currentTime;
//						result = startTimeStr;
//					}
//				}else{
//					if(delta == 0 || delta > time + 24 * 3600 * 1000 - currentTime){
//						delta = time + 24 * 3600 * 1000 - currentTime;
//						result = startTimeStr;
//					}
//				}
//			}
//			if(result.length() <= 0){
//				result = startTimeList.get(0);
//			}
//			LocalLog.i(TAG, "getClosestTime()","result = "+result);
//			return result;
//		}
//	}
}
