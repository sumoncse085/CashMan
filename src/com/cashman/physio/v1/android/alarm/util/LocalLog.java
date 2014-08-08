package com.cashman.physio.v1.android.alarm.util;

import android.util.Log;

public final class LocalLog{

	private static boolean info_enabled = true;
	private static boolean debug_enabled = true;
	private static boolean error_enabled = true;
	
	public static void setEnabled(boolean enable){
		setEnabled(enable,enable,enable);
	}
	
	public static void setEnabled(boolean info_enable,boolean debug_enable,boolean error_enable){
		info_enabled = info_enable;
		debug_enabled = debug_enable;
		error_enabled = error_enable;
	}
	
	public static void i(String tag,String msg){
		if(info_enabled){
			Log.i(tag,msg);
		}
	}
	
	public static void i(String tag,String msg,Throwable th){
		if(info_enabled){
			Log.i(tag,msg,th);
		}
	}
	
	public static void i(String tag,String functionName,String msg){
		if(info_enabled){
			Log.i(tag,"function:"+functionName+"(); \t"+msg);
		}
	}
	
	public static void i(String tag,String functionName,String msg, Throwable th){
		if(info_enabled){
			Log.i(tag,"function:"+functionName+"(); \t"+msg,th);
		}
	}
	
	public static void d(String tag,String msg){
		if(debug_enabled){
			Log.d(tag,msg);
		}
	}
	
	public static void d(String tag,String msg,Throwable th){
		if(debug_enabled){
			Log.d(tag,msg,th);
		}
	}
	
	public static void d(String tag,String functionName,String msg){
		if(debug_enabled){
			Log.d(tag,"function:"+functionName+"(); \t"+msg);
		}
	}
	
	public static void d(String tag,String functionName,String msg, Throwable th){
		if(debug_enabled){
			Log.d(tag,"function:"+functionName+"(); \t"+msg,th);
		}
	}
	
	public static void e(String tag,String msg){
		if(error_enabled){
			Log.e(tag,msg);
		}
	}
	
	public static void e(String tag,String msg,Throwable th){
		if(error_enabled){
			Log.e(tag,msg,th);
		}
	}
	
	public static void e(String tag,String functionName,String msg){
		if(error_enabled){
			Log.e(tag,"function:"+functionName+"(); \t"+msg);
		}
	}
	
	public static void e(String tag,String functionName,String msg, Throwable th){
		if(error_enabled){
			Log.e(tag,"function:"+functionName+"(); \t"+msg,th);
		}
	}
	
}
