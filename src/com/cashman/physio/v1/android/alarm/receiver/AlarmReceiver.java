package com.cashman.physio.v1.android.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.activity.exercise.ExerciseReachActivity;
import com.cashman.physio.v1.android.alarm.activity.profile.AppointmentAlarmActivity;
import com.cashman.physio.v1.android.alarm.data.AlarmItem;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

public class AlarmReceiver extends BroadcastReceiver
{
  private static final String TAG = "AlarmReceiver";

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    LocalLog.i("AlarmReceiver", "onReceive", "******");
//    AlarmItem localAlarmItem = (AlarmItem)paramIntent.getExtras().getSerializable("alarm_item");
//    LocalLog.i("AlarmReceiver", "onReceive", "alarm = " + localAlarmItem.getName());
    
    boolean appoinmentalarm=false;
    appoinmentalarm=paramIntent.getExtras().getBoolean("appoinmentalarm");
//    Toast.makeText(paramContext, appoinmentalarm+"", 2000).show();
    if(appoinmentalarm){
    	 Bundle localBundle = paramIntent.getExtras();
 	    Intent localIntent = new Intent(paramContext, AppointmentAlarmActivity.class);
 	    localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 	    localIntent.putExtras(localBundle);
 	    paramContext.startActivity(localIntent);
    }
    else{
    	 Bundle localBundle = paramIntent.getExtras();
    	    Intent localIntent = new Intent(paramContext, ExerciseReachActivity.class);
    	    localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	    localIntent.putExtras(localBundle);
    	    paramContext.startActivity(localIntent);
    }
   
  }
}