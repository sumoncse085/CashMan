package com.cashman.physio.v1.android.alarm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cashman.physio.v1.android.alarm.data.Constant;

import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class RingtoneTool {

	private Context mContext;
	
	public RingtoneTool(Context context){
		this.mContext = context;
	}
	
	public Ringtone getDefaultRingtone(int type){ 
	    return RingtoneManager.getRingtone(mContext, RingtoneManager.getActualDefaultRingtoneUri(mContext, type)); 
	} 
	 
	public Uri getDefaultRingtoneUri(int type){ 
	    return RingtoneManager.getActualDefaultRingtoneUri(mContext, type); 
	} 
	 
	public List<Ringtone> getRingtoneList(int type){ 
		
	    List<Ringtone> resArr = new ArrayList<Ringtone>(); 
	    RingtoneManager manager = new RingtoneManager(mContext); 
	    manager.setType(type); 
	    Cursor cursor = manager.getCursor(); 
	    int count = cursor.getCount(); 
	    for(int i = 0 ; i < count ; i ++){ 
	        resArr.add(manager.getRingtone(i)); 
	    } 
	    cursor.close();
	    return resArr; 
	} 
	 
	public Ringtone getRingtone(int type,int pos){ 
	    RingtoneManager manager = new RingtoneManager(mContext); 
	    manager.setType(type); 
	    return manager.getRingtone(pos); 
	} 
	 
	public List<String> getRingtoneTitleList(int type){ 
	    List<String> resArr = new ArrayList<String>(); 
	    RingtoneManager manager = new RingtoneManager(mContext); 
	    manager.setType(type); 
	    Cursor cursor = manager.getCursor(); 
	    if(cursor.moveToFirst()){ 
	        do{ 
	            resArr.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)); 
	        }while(cursor.moveToNext()); 
	    } 
	    cursor.close();
	    return resArr; 
	}  
	
	public List<Map<String,String>> getRingtoneInfoList(int type){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		RingtoneManager  manager = new RingtoneManager(mContext);
		manager.setType(type);
		Cursor c = manager.getCursor();
		int i = 0;
		String defaultUri = getDefaultRingtoneUri(type).toString();
		while(c.moveToNext()){
			Map<String,String> info = new HashMap<String,String>();
			info.put(Constant.Ringtone.KEY_RINGTONE_TITLE, c.getString(RingtoneManager.TITLE_COLUMN_INDEX));
			Uri uri = manager.getRingtoneUri(i);
			String uriPath = uri == null ? defaultUri : uri.toString();
			info.put(Constant.Ringtone.KEY_RINGTONE_URI,uriPath);
			list.add(info);
			i++;
		}
		return list;
	}
	 
	 
	public String getRingtoneUriPath(int type,int pos,String def){ 
	    RingtoneManager manager = new RingtoneManager(mContext); 
	    manager.setType(type); 
	    Uri uri = manager.getRingtoneUri(pos); 
	    return uri==null?def:uri.toString(); 
	} 
	 
	public Ringtone getRingtoneByUriPath(int type ,String uriPath){ 
	    RingtoneManager manager = new RingtoneManager(mContext); 
	    manager.setType(type); 
	    Uri uri = Uri.parse(uriPath); 
	    return RingtoneManager.getRingtone(mContext, uri); 
	} 
}
