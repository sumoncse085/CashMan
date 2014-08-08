package com.cashman.physio.v1.android.alarm.util;

import java.util.Date;

import com.cashman.physio.v1.android.alarm.activity.profile.ProfileNotifyActivity;
import com.cashman.physio.v1.android.alarm.data.Constant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;

public class ProfileNotify implements Runnable {

	private Context ctx;
	private Date date;
	private boolean isFlag =false;
	private static ProfileNotify notify; 

	
	public void setDate(Date date) {
		this.date = date;
	}
	public static ProfileNotify getInstance(Context ctx, Date date){
		if(notify == null){
			notify = new ProfileNotify(ctx, date);
			return notify;
		}
		return notify;
	}
	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}
	private ProfileNotify(Context ctx, Date date){
		this.ctx = ctx;
		this.date = date;
	}
	@Override
	public void run() {
		 while(isFlag){
			 Log.i("notify", "-----------run------------"+ (System.currentTimeMillis() +"||"+ date.getTime()));
			 if(System.currentTimeMillis()  > date.getTime() -60 *60*1000 ){
				 isFlag = false;
				 showDialog();
				 saveBooleanPreferences(Constant.SharePreferences.KEY_NOTIFY_IS_CHECK, false);
				 break;
			 }else{
				 try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 }
	}
	
	private void showDialog(){
		 Intent it =new Intent(ctx, ProfileNotifyActivity.class); 
		 it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 it.putExtra("message", "You have an appointment at<br/>     " +DateFormat.format(Constant.Profile.DATE_TIME_PATTERN ,date));
		 ctx.startActivity(it);
	}
	
	private void saveBooleanPreferences(String key, boolean value){
		SharedPreferences.Editor localEditor = ctx.getSharedPreferences(
				Constant.SharePreferences.KEY_PROFILE_PREFERENCES_NAME, 0).edit();
		localEditor.putBoolean(key, value);
		localEditor.commit();
	}
	
	 
}
