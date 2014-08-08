package com.cashman.physio.v1.android.alarm.util;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cashman.physio.v1.android.alarm.activity.exercise.LoadTask;
import com.cashman.physio.v1.android.alarm.data.AlarmItem;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.receiver.AlarmReceiver;

public class AlarmTool {
	private AlarmItem mAlarm;
	private Context mContext;
	private PendingIntent pIntent;
	
	private static final String TAG = "AlarmTool";
	
	public AlarmTool(Context context){
		mContext = context;
	}
	
	public void setClosestAlarm(AlarmItem alarmItem){
		mAlarm = alarmItem;
	}
	public AlarmItem getClosestAlarm(){
		return mAlarm;
	}
	
	public void start(){
		if(mAlarm == null){
			loadAndStart();
		}else{
			AlarmItem alarm = mAlarm;
			if(!alarm.isActivate()){
				return;
			}
			long ringTime = AlarmComparator.getRingTimeLong(alarm.getRingTime(),alarm.getWeekday());
			LocalLog.i(TAG, "start","ringTime = "+alarm.getRingTime());
			AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
			
			Intent intent = new Intent(mContext,AlarmReceiver.class);
//			intent.putExtra(Constant.Alarm.Intent.KEY_RING_TIME, ringTime);
//			intent.putExtra(Constant.Alarm.Intent.KEY_ALARM_NAME, alarm.getName());
//			intent.putExtra(Constant.Alarm.Intent.KEY_ALARM_INSTRUCTION, alarm.getInstruction());
//			intent.putExtra(Constant.Alarm.Intent.KEY_RINGTONE_ENABLE, alarm.isActivate());
//			intent.putExtra(Constant.Alarm.Intent.KEY_RINGTONE_URI, alarm.getRingtoneUri());
//			intent.putExtra(Constant.Alarm.Intent.KEY_VIBRATE_ENABLE, alarm.isVibrateEnable());
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constant.Alarm.Intent.KEY_ALARM_ITEM, alarm);
			intent.putExtras(bundle);
			pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			manager.set(AlarmManager.RTC_WAKEUP, ringTime, pIntent);
		}
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Constant.Handler.LOAD_FINISH:
				List<AlarmItem> itemList = (List<AlarmItem>) msg.obj;
				mAlarm = getClosestAlarmBySelf(itemList);
				if(mAlarm != null){
					start();
				}else{
					cancelAlarm();
				}
				
				break;
			default:
				break;
			}
		}
	};
	
	private void cancelAlarm(){
		AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(pIntent);
	}
	
	private AlarmItem getClosestAlarmBySelf(List<AlarmItem> alarmList){
		AlarmItem alarm = null;
		if(alarmList != null){
			int size = alarmList.size();
			if(size > 0){
				alarm = alarmList.get(0);
			}
		}
		
		return alarm;
	}
	
	private void loadAndStart(){
		final LoadTask task = new LoadTask(mContext){

			@Override
			protected void onPostExecute(List<AlarmItem> result) {
				Message msg = mHandler.obtainMessage(Constant.Handler.LOAD_FINISH);
				msg.obj = result;
				mHandler.dispatchMessage(msg);
			}
			
		};
		task.execute();
	}
}
