package com.cashman.physio.v1.android.alarm.util;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class PreferencesTool {

	private static final String TAG = "PreferencesTool";
	
	public static void save(Context context, String name, String key,
			String value) {
		if(key == null) {
			Log.e(TAG, "save();key is null");
			return;
		}
		if(value == null) {
			Log.e(TAG, "value is null");
			value = "";
		}
		
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String get(Context context,String name,String key) {
		if(key == null) {
			Log.e(TAG, "get();key is null");
			return null;
		}
		SharedPreferences preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
		String value = preferences.getString(key, "");
		return value;
	}
	
	public static void clean(Context context,String name) {
		SharedPreferences preferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor edit = preferences.edit();
		edit.clear();
		edit.commit();
	}
	
	public static final class Purchase{
		public static final String NAME = "purchase";
		public static final String KEY_ITEM_ID = "item_id";
		
	}
}
