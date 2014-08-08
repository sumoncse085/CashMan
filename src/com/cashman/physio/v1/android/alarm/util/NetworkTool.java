package com.cashman.physio.v1.android.alarm.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkTool {

	/**
	 *  status is available
	 * @param con
	 * @return
	 */
    public static boolean isNetworkAvailable(Context con){
    	 /**
    	  * 
    	 if(null == connMgr){  
            connMgr = (ConnectivityManager)con.getSystemService(Context.CONNECTIVITY_SERVICE);  
        }  
       
         NetworkInfo wifiInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
        NetworkInfo mobileInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
        if(wifiInfo.isAvailable()){  
            return true;  
        }else if(mobileInfo.isAvailable()){
            return true;  
        }else{  
            return false;  
        }     
        **/
        ConnectivityManager connMgr = (ConnectivityManager)con.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if(connMgr != null){
        	 NetworkInfo[] netinfo = connMgr.getAllNetworkInfo();
             if (netinfo == null) {
             	return false;
             }
             for (int i = 0; i < netinfo.length; i++) {
             	if (netinfo[i].isConnected()) {
             		return true;
             	}
             }
             return false;
        }else{
        	return false;
        }
       
    }
}
