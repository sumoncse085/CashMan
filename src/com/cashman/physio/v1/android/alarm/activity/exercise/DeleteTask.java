package com.cashman.physio.v1.android.alarm.activity.exercise;

import com.cashman.physio.v1.android.alarm.data.CommonSetting;
import com.cashman.physio.v1.android.alarm.db.DbManager;
import com.cashman.physio.v1.android.alarm.util.CheckEnvironment;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

import android.content.Context;
import android.os.AsyncTask;

public class DeleteTask extends AsyncTask<String, String, Boolean>{
	
	private static final String TAG = "DeleteTask";
	private Context mContext;
	
	public DeleteTask(Context context){
		mContext = context;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if(params != null && params.length > 0){
			int alarmId = Integer.parseInt(params[0]);
			DbManager manager = new DbManager(mContext);
			int result = 0;
			result = manager.deleteAlarmById(alarmId);
			if(result > 0){
				int count = manager.getAlarmCountByForeignId(alarmId);
				if(count > 0){
					result = manager.deleteAlarmByForeignId(alarmId);
				}
				if(result <= 0){
					LocalLog.e(TAG, "DeleteTask.doInBackground", "delete alarm with foreignId = "+alarmId+" error");
				}
				CommonSetting cs = manager.getCommonSettingByAlarmId(alarmId);
				if(cs != null){
					String videoPath = cs.getVideoPath();
					if(videoPath != null && videoPath.length() > 0){
						if(CheckEnvironment.deleteFile(videoPath)){
							LocalLog.e(TAG,"DeleteTask.doInBackground","delete video file with the path = "+videoPath+" successfully");
						}else{
							LocalLog.e(TAG,"DeleteTask.doInBackground","delete video file with the path = "+videoPath+" error");
						}
					}
					String videoThumbPath = cs.getVideoThumbPath();
					if(videoThumbPath != null && videoThumbPath.length() > 0){
						if(CheckEnvironment.deleteFile(videoThumbPath)){
							LocalLog.e(TAG,"DeleteTask.doInBackground","delete video thumb image with the path = "+videoThumbPath+" successfully");
						}else{
							LocalLog.e(TAG,"DeleteTask.doInBackground","delete video video thumb image with the path = "+videoThumbPath+" error");
						}
					}
				}else{
					LocalLog.e(TAG,"DeleteTask.doInBackground","get common setting with alarmId = "+alarmId+" error");
				}
				result = manager.deleteCommonSettingByAlarmId(alarmId);
				if(result <= 0){
					LocalLog.e(TAG, "DeleteTask.doInBackground", "delete common setting with alarmId = "+alarmId+" error");
				}
				return true;
			}else{
				LocalLog.e(TAG, "DeleteTask.doInBackground", "delete alarm with alarmId = "+alarmId+" error");
			}
		}
		
		return false;
	}
}