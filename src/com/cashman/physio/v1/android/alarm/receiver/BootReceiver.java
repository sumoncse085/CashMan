package com.cashman.physio.v1.android.alarm.receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.cashman.physio.v1.android.alarm.AlarmApplication;
import com.cashman.physio.v1.android.alarm.activity.profile.ProfileActivity;
import com.cashman.physio.v1.android.alarm.data.Constant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
	Context arg0;
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		this.arg0=arg0;
		AlarmApplication.appInstance.startAlarm();
		String str8 = getPreferences(Constant.Profile.KEY_FIXED_NEXT_APPOINT);
		if(str8.length() > 0){

			AddAlarm();

		}
		else{
			Toast.makeText(arg0, "str8", 2000).show();
		}
	}

	private String getPreferences(String paramString) {
		return arg0.getSharedPreferences(
				Constant.SharePreferences.KEY_PROFILE_PREFERENCES_NAME, 0)
				.getString(paramString, "");
	}


	public void AddAlarm(){
		Calendar calendar = Calendar.getInstance();
		long currenttime=calendar.getTimeInMillis();
		Log.e("before", ""+calendar.getTimeInMillis());
		String date = getPreferences(Constant.Profile.KEY_FIXED_NEXT_APPOINT);
		date=date.replace(" ", "");
		//08-03 00:02:10.443: E/Time(15800): 2014/08/02Sat,12:03am  201/0/0 1:0

		int i = date.indexOf(Constant.Profile.DATE_AND_TIME_SEPARATOR);
		String time=date.substring(i + 1, date.length());
		int year=Integer.parseInt(date.substring(0, 4));
		int month=Integer.parseInt(date.substring(5, 7));
		int dayofmonth=Integer.parseInt(date.substring(8, 10));
		int hour=Integer.parseInt(time.substring(0, 2));
		int min=Integer.parseInt(time.substring(3, 5));
		time=time.replace("P", "p");
		time=time.replace("M", "m");
		if(time.contains("pm")){
			if(hour!=12){
				hour+=12;	
			}

		}


		Log.e("Time ",date+"  " +year+"/"+month+"/"+dayofmonth+" "+hour+":"+min);

		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, dayofmonth);

		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);


		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(year, month, dayofmonth, 
				hour, Calendar.MINUTE, 0);
		long startTime = calendar1.getTimeInMillis();

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
			formatter.setLenient(false);

			String oldTime =dayofmonth+"." +month+"."+year+", "+hour+":"+min;//"05.01.2011, 12:45";
			Date oldDate = formatter.parse(oldTime);
			startTime= oldDate.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Log.e("after", ""+startTime);
		if(startTime-currenttime>60*60*1000){
			startTime-=60*60*1000;
			Intent myIntent = new Intent(arg0, AlarmReceiver.class);
			myIntent.putExtra("appoinmentalarm", true);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(arg0, 1, myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

			AlarmManager alarmManager = (AlarmManager)arg0.getSystemService(arg0.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP,startTime, pendingIntent);
		//	alarmManager.se
			
			
			
//			AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//			Intent intent = new Intent(mContext,AlarmReceiver.class);
////			intent.putExtra(Constant.Alarm.Intent.KEY_RING_TIME, ringTime);
////			intent.putExtra(Constant.Alarm.Intent.KEY_ALARM_NAME, alarm.getName());
////			intent.putExtra(Constant.Alarm.Intent.KEY_ALARM_INSTRUCTION, alarm.getInstruction());
////			intent.putExtra(Constant.Alarm.Intent.KEY_RINGTONE_ENABLE, alarm.isActivate());
////			intent.putExtra(Constant.Alarm.Intent.KEY_RINGTONE_URI, alarm.getRingtoneUri());
////			intent.putExtra(Constant.Alarm.Intent.KEY_VIBRATE_ENABLE, alarm.isVibrateEnable());
//			Bundle bundle = new Bundle();
//			bundle.putSerializable(Constant.Alarm.Intent.KEY_ALARM_ITEM, alarm);
//			intent.putExtras(bundle);
//			pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//			
//			manager.set(AlarmManager.RTC_WAKEUP, ringTime, pIntent);
			
		}
	}

}
