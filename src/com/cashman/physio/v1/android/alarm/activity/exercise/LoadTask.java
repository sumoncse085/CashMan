package com.cashman.physio.v1.android.alarm.activity.exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.cashman.physio.v1.android.alarm.data.Alarm;
import com.cashman.physio.v1.android.alarm.data.AlarmItem;
import com.cashman.physio.v1.android.alarm.data.CommonSetting;
import com.cashman.physio.v1.android.alarm.db.DbManager;
import com.cashman.physio.v1.android.alarm.util.AlarmComparator;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

import android.content.Context;
import android.os.AsyncTask;

public class LoadTask extends AsyncTask<String, String, List<AlarmItem>> {
	
	private Context mContext;

	private final Date CURRENT_DATE = new Date();
	
	private static final String TAG = "LoadTask";
	
	public LoadTask(Context context){
		mContext = context;
	}
	
	@Override
	protected List<AlarmItem> doInBackground(String... params) {
		DbManager dbManager = new DbManager(mContext);
		List<Alarm> alarmList = dbManager.getAlarmListByForeignId(-1);
		List<AlarmItem> itemList = null;
		int alarmSize = 0;
		if(alarmList != null && alarmList.size() > 0){
			itemList = new ArrayList<AlarmItem>();
			List<String> startTimeList=null;
			alarmSize = alarmList.size();
			
			for(int i = 0; i < alarmSize; i++){
				startTimeList = new ArrayList<String>();
				Alarm alarm = alarmList.get(i);
				int alarmId = alarm.getId();
				CommonSetting common = dbManager.getCommonSettingByAlarmId(alarmId);
				AlarmItem item = new AlarmItem();
				item.setAlarmId(alarmId);
				item.setName(common.getName());
				item.setInstruction(common.getInstruction());
				item.setActivate(common.isActivate());
				item.setThumbPath(common.getVideoThumbPath());
				item.setVideoPath(common.getVideoPath());
				item.setWeekday(common.getWeekday());
				item.setRingtoneEnable(common.isRingtoneEnable());
				item.setRingtoneUri(common.getRingtonePath());
				item.setVibrateEnable(common.isVibrateEnable());
				startTimeList.add(alarm.getStartTime());
				List<Alarm> subAlarmList = dbManager.getAlarmListByForeignId(alarmId);
				if(subAlarmList != null && subAlarmList.size() > 0){
					int subAlarmSize = subAlarmList.size();
					for(int j = 0; j < subAlarmSize; j++){
						startTimeList.add(subAlarmList.get(j).getStartTime());
					}
				}
				
				item.setStartTimeList(startTimeList);
				
				item.setRingTime(getClosestTime(startTimeList,common.getWeekday()));
				itemList.add(item);
			}
			
			Collections.sort(itemList, new AlarmComparator());
			
//			for(int i = 0; i< alarmSize; i++){
//				AlarmItem item1 = itemList.get(i);
//				
//				LocalLog.i(TAG, "alarm: id = "+item1.getAlarmId()+"; name = "+item1.getName()
//						+"; instruction = "+item1.getInstruction()
//						+"; ringtime = "+item1.getRingTime()
//						+"; startTimeList = "+item1.getStartTimeList().toString());
//			}
		}
		
		return itemList;
	}
	
	private String getClosestTime(List<String> startTimeList,String weekdaySelected){
		if(weekdaySelected == null || weekdaySelected.length() != 7){
			LocalLog.e(TAG, "getClosestTime", "weekday is null : "+weekdaySelected);
			return "0111110";
		}
		Date currentDate = CURRENT_DATE;
		long currentTime = currentDate.getTime();
		int currentWeekday = currentDate.getDay();
		char[] weekdaySelectedArr = weekdaySelected.toCharArray();
		if(weekdaySelectedArr[currentWeekday] != '1'){
			return startTimeList.get(0);
		}
		long delta = 0L;
		String result = "";
		int size = startTimeList.size();
		if(size == 1){
			return startTimeList.get(0);
		}
		for(int i = 0; i < size; i++){
			String startTimeStr = startTimeList.get(i);
			String[] split = startTimeStr.split(":");
			Date startTime = new Date();
			startTime.setHours(Integer.parseInt(split[0]));
			startTime.setMinutes(Integer.parseInt(split[1]));
			startTime.setSeconds(0);
			long time = startTime.getTime();
			if(time == currentTime){
				return startTimeStr;
			}else if(time > currentTime){
				if(delta == 0 || delta > time - currentTime){
					delta = time - currentTime;
					result = startTimeStr;
				}
			}else{
				if(delta == 0 || delta > time + 24 * 3600 * 1000 - currentTime){
					delta = time + 24 * 3600 * 1000 - currentTime;
					result = startTimeStr;
				}
			}
		}
		if(result.length() <= 0){
			result = startTimeList.get(0);
		}
		LocalLog.i(TAG, "getClosestTime()","result = "+result);
		return result;
	}

}
