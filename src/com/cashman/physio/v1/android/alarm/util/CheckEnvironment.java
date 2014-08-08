package com.cashman.physio.v1.android.alarm.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

import com.cashman.physio.v1.android.alarm.AlarmApplication;
import com.cashman.physio.v1.android.alarm.data.Constant;

public class CheckEnvironment {
	
	private static String  sPath = Constant.Path.SD_PATH;
	
	private static final String TAG = "CheckEnvironment";
	
	public static boolean isSdAvailable(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			 sPath = Constant.Path.SD_PATH;
			return true;
		}else{
			sPath = AlarmApplication.getInternalFileDirectory();
			return false;
		}
	}
	
	public static void createFile(String filePath){
		File file = new File(filePath);
		File parentFile = file.getParentFile();
		if(file.exists()){
			file.delete();
		}
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			LocalLog.e(TAG, "createFile", "create the new file with path = "+filePath + " catches exception",e);
		}
	}
	
	public static boolean deleteFile(String path){
		if(path!=null && path.length() > 0){
			File file = new File(path);
			if(file.exists()){
				 return file.delete();
			}else{
				LocalLog.e(TAG,"deleteFile","the file to delete does not exist");
				
			}
		}else{
			LocalLog.d(TAG,"deleteFile","the path of file to delete is null");
		}
		return false;
	}

	public static String getPath() {
		return sPath;
	}

	public static void setPath(String path) {
		CheckEnvironment.sPath = path;
	}
}
