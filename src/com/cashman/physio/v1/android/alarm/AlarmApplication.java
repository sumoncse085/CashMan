package com.cashman.physio.v1.android.alarm;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.AlarmTool;
import com.cashman.physio.v1.android.alarm.util.CrashHandler;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

public class AlarmApplication extends Application
{
  private static final String APP_NAME = "alarm_app";
  private static final String KEY_ERROR = "error_app";
  private static final String KEY_ERROR_FILE_NAME = "key_error_file_name";
  public static AlarmApplication appInstance = null;
  private AlarmTool mAlarmTool;

  private void crashProcess()
  {
    CrashHandler.getInstance().init(appInstance);
  }

  public String getErrorFileName()
  {
    return getSharedPreferences(APP_NAME, 0).getString(KEY_ERROR_FILE_NAME, "");
  }

  public boolean getErrorFlag()
  {
    return getSharedPreferences(APP_NAME, 0).getBoolean(KEY_ERROR, false);
  }
  
  public static String getInternalFileDirectory(){
	  String  dir = "/data/data/" +  appInstance.getPackageName() + "/file/";
	  
	  return dir;
  }
  
  public static String getSdFileDirectory(){
	  return Constant.Path.SD_PATH;
  }

  public void onCreate()
  {
    super.onCreate();
    LocalLog.setEnabled(true);
    appInstance = this;
    crashProcess();
  }

  public void onLowMemory()
  {
    super.onLowMemory();
  }

  public void saveErrorFileName(String paramString)
  {
    SharedPreferences.Editor localEditor = getSharedPreferences(APP_NAME, 0).edit();
    localEditor.putString(KEY_ERROR_FILE_NAME, paramString);
    localEditor.commit();
  }

  public void saveErrorFlag(boolean paramBoolean)
  {
    SharedPreferences.Editor localEditor = getSharedPreferences(APP_NAME, 0).edit();
    localEditor.putBoolean(KEY_ERROR, paramBoolean);
    localEditor.commit();
  }

  public void startAlarm()
  {
    if (this.mAlarmTool == null)
      this.mAlarmTool = new AlarmTool(getApplicationContext());
    AlarmTool localAlarmTool = this.mAlarmTool;
    localAlarmTool.setClosestAlarm(null);
    localAlarmTool.start();
  }
}