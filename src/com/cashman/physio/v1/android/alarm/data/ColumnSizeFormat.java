package com.cashman.physio.v1.android.alarm.data;

import com.cashman.physio.v1.android.alarm.util.LocalLog;

public class ColumnSizeFormat {
	
	private static final String TAG = "ColumnSizeFormat";

	public static class ColumnSize{}
	
	protected String getColumn(String src,int size){
		if(src == null){
			LocalLog.e(TAG, "src = null");
		}
		if(src.length() > size){
			return src.substring(0,size);
		}else{
			return src;
		}
	}
}
