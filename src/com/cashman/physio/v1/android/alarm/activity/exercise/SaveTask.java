package com.cashman.physio.v1.android.alarm.activity.exercise;

import com.cashman.physio.v1.android.alarm.data.Alarm;
import com.cashman.physio.v1.android.alarm.data.CommonSetting;
import com.cashman.physio.v1.android.alarm.db.DbManager;
import com.cashman.physio.v1.android.alarm.util.CheckEnvironment;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

import android.content.Context;
import android.os.AsyncTask;

public class SaveTask extends AsyncTask<String, String, Boolean>{
	
	public static final String FLAG_EDIT = "flag_edit";
	public static final String FLAG_CONFIGURE = "flag_configure";
	
	private int alarmId = -1;
	private String startTime ;
	private String weekday;
	private String name ;
	private String instruction;
	private String times;
	private String every;
	private String videoPath;
	private boolean activate;
	private String videoThumPath;
	private String createTime;
	private String ringtonePath;
	private boolean ringtoneEnable;
	private boolean vibrateEnable;
	private DbManager mDbmanager;
	private Context mContext;
	
	private static final String TAG = "SaveTask";
	
	protected SaveTask(Context context,String startTime,String weekday,String name,String instruction,String times,String every,String videoPath,String videoThumPath,boolean activate,String createTime,String ringtonePath,boolean ringtoneEnable,boolean vibrateEnable){
		this(context,-1,startTime,weekday,name,instruction,times,every,videoPath,videoThumPath,activate,createTime,ringtonePath,ringtoneEnable,vibrateEnable);
	}
	
	protected SaveTask(Context context,int alarmId,String startTime,String weekday,String name,String instruction,String times,String every,String videoPath,String videoThumPath,boolean activate,String createTime,String ringtonePath,boolean ringtoneEnable,boolean vibrateEnable){
		this.alarmId = alarmId;
		this.startTime = startTime;
		this.weekday = weekday;
		this.name = name;
		this.instruction = instruction;
		this.times = times;
		this.every = every;
		this.videoPath = videoPath;
		this.videoThumPath = videoThumPath;
		this.activate = activate;
		this.createTime = createTime;
		this.ringtonePath = ringtonePath;
		this.ringtoneEnable = ringtoneEnable;
		this.vibrateEnable = vibrateEnable;
		mDbmanager = new DbManager(context);
		mContext = context;
	}
	@Override
	protected Boolean doInBackground(String... params) {
		long result = 0L;
		if(params == null || params.length <= 0){
			Alarm alarm = new Alarm(startTime,-1);
			int intEvery = Integer.parseInt(every.substring(0,1));
			result = mDbmanager.insertAlarm(alarm);
			if(result != -1){
				alarm = mDbmanager.getLastAlarm();
				if(alarm != null){
					int alarmId = alarm.getId();
					CommonSetting common = new CommonSetting(
							alarmId,
							name,
							instruction,
							createTime,
							weekday,
							ringtonePath,
							ringtoneEnable,
							vibrateEnable,
							videoPath,
							videoThumPath,
							activate,intEvery);
					result = mDbmanager.insertCommonSetting(common);
					if(result != -1){
						if(addAlarmByForeignId(alarmId,startTime,every,times)){
							return true;
						}else{
							LocalLog.e(TAG,"SaveTask.doInbackground","add alarm by foreignId = "+alarmId+" error");
						}
					}else{
						LocalLog.e(TAG, "SaveTask.doInbackground", "insert common setting error");
					}
				
				}else{
					LocalLog.e(TAG, "SaveTask.doInbackground", "get last alarm error" );
				}
			}else{
				LocalLog.e(TAG, "SaveTask.doInbackground","insert alarm error");
			}
		}else{
			int alarmId = Integer.parseInt(params[0]);
			Alarm alarm = mDbmanager.getAlarmById(alarmId);
			if(alarm == null){
				LocalLog.e(TAG,"SaveTask.doInbackground","get alarm with alarmId = "+alarmId+" null");
				return false;
			}
			alarm.setForeignId(-1);
			alarm.setStartTime(startTime);
			result = mDbmanager.updateAlarm(alarm);
			if(result > 0){
				CommonSetting common = mDbmanager.getCommonSettingByAlarmId(alarmId);
				if(!common.getVideoPath().equals(videoPath)){
					CheckEnvironment.deleteFile(common.getVideoPath());
					CheckEnvironment.deleteFile(common.getVideoThumbPath());
				}
				
				if(ringtonePath == null || ringtonePath.length() <= 0){
					ringtonePath = common.getRingtonePath();
				}
				common.setActivate(activate);
				common.setName(name);
				common.setInstruction(instruction);
				common.setCreateTime(createTime);
				common.setWeekday(weekday);
				common.setRingtonePath(ringtonePath);
				common.setRingtoneEnable(ringtoneEnable);
				common.setVideoPath(videoPath);
				common.setVideoThumbPath(videoThumPath);
				common.setActivate(activate);
				int intEvery = Integer.parseInt(every.substring(0,1));
				common.setEvery(intEvery);
				
				result = mDbmanager.updateCommonSetting(common);
				if(result > 0){
					int count = mDbmanager.getAlarmCountByForeignId(alarmId);
					if(count > 0){
						result = mDbmanager.deleteAlarmByForeignId(alarmId);
					}
					if(result > 0){
						if(addAlarmByForeignId(alarmId,startTime,every,times)){
							return true;
						}else{
							LocalLog.e(TAG,"SaveTask.doInbackground","add alarm by foreignId = "+alarmId+" error");
						}
					}else{
						LocalLog.e(TAG,"SaveTask.doInbackground", "delete alarm with foreignId = "+alarmId+" error");
					}
				}else{
					LocalLog.e(TAG,"SaveTask.doInbackground", "update the common setting with alarmId = "+alarmId+" error");
				}
			}else{
				LocalLog.e(TAG,"SaveTask.doInbackground", "update the alarm with alarmId = "+alarmId+" error");
			}
		}
		return false;
	}
	
	private boolean addAlarmByForeignId(int foreignId,String startTime,String every,String times){
		long result = 0L;
		String[] startTimeSplit = startTime.split(":");
		int hour = Integer.parseInt(startTimeSplit[0]);
		int everyAlarm = Integer.parseInt(every.substring(0,every.indexOf(" ")));
		int timesPerday = Integer.parseInt(times);
		
		for(int i = 1; i < timesPerday; i++){
			int startHour =  (hour + everyAlarm * i) % 24;
			String startTimei = String.valueOf(startHour < 10 ? "0"+startHour : startHour)+":"+startTimeSplit[1];
			Alarm alarm = new Alarm(startTimei,foreignId);
			result = mDbmanager.insertAlarm(alarm);
			if(result == -1){
				LocalLog.e(TAG, "addAlarmByForeignId","add alarm with foreignId = "+foreignId+" error");
			}
		}
		return true;
	}
}
