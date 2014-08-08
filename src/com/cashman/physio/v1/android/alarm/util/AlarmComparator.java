package com.cashman.physio.v1.android.alarm.util;

import java.util.Comparator;
import java.util.Date;

import com.cashman.physio.v1.android.alarm.data.AlarmItem;

public class AlarmComparator implements Comparator<AlarmItem> {
	
	private static final String TAG = "AlarmComparator";
	
	public AlarmComparator(){
		super();
	}
	
	public int compare(AlarmItem object1, AlarmItem object2) {
		
		return compareRingTime(object1,object2);
	}
	
	private int compareRingTime(AlarmItem lhs,AlarmItem rhs){
		
		String lhsRingTime = lhs.getRingTime();
		String rhsRingTime = rhs.getRingTime();
		String lhsWeekdaySelected = lhs.getWeekday();
		String rhsWeekdaySelected = rhs.getWeekday();
		boolean lhsActivate = lhs.isActivate();
		boolean rhsActivate = rhs.isActivate();
		
		if(!lhsActivate){
			if(!rhsActivate){
				return 0;
			}else{
				return 1;
			}
		}else if(!rhsActivate){
			return -1;
		}
		
		if(lhsRingTime.length() <= 0){
			if(rhsRingTime.length() <= 0){
				return 0;
			}else{
				return 1;
			}
		}else if(rhsRingTime.length() <= 0){
			return -1;
		}
		if(lhsWeekdaySelected.equals("0000000")){
			if(rhsWeekdaySelected.equals("0000000")){
				return 0;
			}else{
				return 1;
			}
		}else if(rhsWeekdaySelected.equals("0000000")){
			return -1;
		}
		
		long lhsRingTimeLong = getRingTimeLong(lhsRingTime,lhsWeekdaySelected);
		long rhsRingTimeLong = getRingTimeLong(rhsRingTime,rhsWeekdaySelected);
		
		if(lhsRingTimeLong > rhsRingTimeLong){
			return 1;
		}else if(lhsRingTimeLong == rhsRingTimeLong){
			return 0;
		}else{
			return -1;
		}
	}
	
	public static long getRingTimeLong(String dateStr,String weekdaySelectedStr){
		if(weekdaySelectedStr == null || weekdaySelectedStr.length() != 7){
			LocalLog.e(TAG, "getRingTimeLong","weekday is null: "+ weekdaySelectedStr);
			return 0;
		}
		Date currentDate = new Date();
		char[] weekdaySelectedArr = weekdaySelectedStr.toCharArray();
		String[] split = dateStr.split(":");
		Date startTime = currentDate;
		long currentTime = currentDate.getTime();
		startTime.setHours(Integer.parseInt(split[0]));
		startTime.setMinutes(Integer.parseInt(split[1]));
		startTime.setSeconds(0);
		long time = startTime.getTime();
		int currentWeekday = currentDate.getDay();
		int weekday = currentWeekday;
		if(time < currentTime){
			time += 24 * 3600 * 1000;
			weekday++;
		}
		if(weekday <= 6){
			while(weekdaySelectedArr[weekday] != '1'){
				time += 24 * 3600 * 1000;
				weekday++;
				if(weekday > 6){
					break;
				}
			}
		}
		if(weekday > 6){
			weekday = 0;
			while(weekdaySelectedArr[weekday] != '1'){
				time += 24 * 3600 * 1000;
				weekday++;
				if(weekday >= currentWeekday - 1){
					break;
				}
			}
			if(weekday >= currentWeekday){
				time = 0;
			}
		}
		return time;
	}
}
