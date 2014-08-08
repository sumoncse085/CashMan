package com.cashman.physio.v1.android.alarm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.AlarmApplication;
import com.cashman.physio.v1.android.alarm.R;

/**
 * @author DFu
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";
	public static final boolean DEBUG = true;
	private UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandler INSTANCE;

	private Application mContext;
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";

	private static final String CRASH_REPORTER_EXTENSION = ".cr";

	private CrashHandler() {

	}

	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/**
	 * @param ctx
	 */
	public void init(Application ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				LocalLog.e(TAG, "InterruptedException on uncaughtException", e);
			}
//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(10);
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
		
	}

	/**
	 * handle exception
	 * 
	 * @param ex
	 * @return true:processed;or false
	 */
	private boolean handleException(Throwable ex) {
		LocalLog.e(TAG,"uncatch exception",ex);
		if (ex == null) {
			return true;
		}
		final String msg = mContext.getString(R.string.uncaught_exception);
		Thread toastThread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				LocalLog.e(TAG,msg);
				Toast.makeText(mContext, "Error:" + msg, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		};
		toastThread.start();
		// collect crash infos
		collectCrashDeviceInfo(mContext);
		// save crash info to file
		String crashFileName = saveCrashInfoToFile(ex);
		AlarmApplication app = ((AlarmApplication)mContext.getApplicationContext());
		app.saveErrorFlag(true);
		app.saveErrorFileName(crashFileName);
		// 
		//sendCrashReportsToServer(mContext);
		return true;
	}

	/**
	 * when start app, send the report if you want
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}

	/**
	 * sendCrashReportsToServer
	 * 
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));
			for (String fileName : sortedFiles) {
				File cr = new File(ctx.getFilesDir(), fileName);
				postReport(cr);
				cr.delete();//delete sent report
			}
		}
	}

	private void postReport(File file) {
		// TODO HTTP Post file
	}

	/**
	 * get Crash Report file
	 * 
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		File filesDir = ctx.getFilesDir();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(CRASH_REPORTER_EXTENSION);
			}
		};
		return filesDir.list(filter);
	}

	/**
	 * saveCrashInfoToFile
	 * 
	 * @param ex
	 * @return
	 */
	
	private String getCrashInfo(Throwable ex){
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		String result = info.toString();
		printWriter.close();
		return result;
	}
	private String saveCrashInfoToFile(Throwable ex) {
		String result=getCrashInfo(ex);
		mDeviceCrashInfo.put(STACK_TRACE, result);
		try {
			String strDate=getCurrentDate();
			String fileName = "crash-" + strDate + CRASH_REPORTER_EXTENSION;
			FileOutputStream trace = mContext.openFileOutput(fileName,
					Context.MODE_APPEND | Context.MODE_WORLD_READABLE);			
			//trace.write(mDeviceCrashInfo.toString().getBytes());
			//trace.write(String.valueOf("\n").getBytes());
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
					PackageManager.GET_ACTIVITIES);
		    String versionName = pi.versionName;
			String infoStr="Client Phone Model: " + android.os.Build.MODEL + ",Version = "
			                + android.os.Build.VERSION.SDK + ",Android " 
                            + android.os.Build.VERSION.RELEASE
                            + "\r\nClient : "+"Version = "+versionName
                            +"\r\ncrash date:["+strDate+"]"+", crash details:\r\n"+result;
			trace.write(infoStr.getBytes());
//			String mailContent="["+strDate+"]\r\n"+result;
//			sendMailAboutCrashInfo(mContext,mailContent);
			trace.flush();
			trace.close();
			return fileName;
		} catch (Exception e) {
			LocalLog.e(TAG, "an error occured while writing report file...", e);
		}
		return null;
	}
	
	private String getCurrentDate(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = format.format(date);
		return strDate;
	}
	
	/**
	 * collect Crash DeviceInfo
	 * 
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
			}
		} catch (NameNotFoundException e) {
			LocalLog.e(TAG, "Error while collect package info", e);
		}
		//get device infos user reflate.
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get(null));
				//LocalLog.d(TAG, field.getName() + " : " + field.get(null));
				
			} catch (Exception e) {
				LocalLog.e(TAG, "Error while collect crash info", e);
			}
		}
	}

	/**
	 * check network available
	 * 
	 * @param ctx
	 * @return true available; false not available
	 */
	private boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo[] netinfo = cm.getAllNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		for (int i = 0; i < netinfo.length; i++) {
			if (netinfo[i].isConnected()) {
				return true;
			}
		}
		return false;
	}

}
