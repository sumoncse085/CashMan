package com.cashman.physio.v1.android.alarm.push;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.provider.Settings.Secure;

import com.cashman.physio.v1.android.alarm.data.Constant;

public class PushService extends Service {

	public static final String NAME = "com.cashman.physio.v1.android.alarm.push.PushService";
	private static int SLEEP = 15000;
	private boolean isRun = true;
	public Thread pushThread;
	private String mDeviceID;
	private static String URL = "http://physiosecrets.com/push/andriodCashmanPush/push.php";
	public Context ctx;
 

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		mDeviceID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		if (mDeviceID != null && !mDeviceID.equals("")) {
			pushThread = new Thread(pushRun);
			pushThread.start();
		}
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		pushThread.destroy();
		super.onDestroy();
	}

	private Runnable pushRun = new Runnable() {

		@Override
		public void run() {
			while (isRun) {
				try {
					DefaultHttpClient http = new DefaultHttpClient();
					HttpGet get = new HttpGet(URL + "?deviceId=" + mDeviceID);
					try {
						HttpResponse response = http.execute(get);
						HttpEntity entity = response.getEntity();
						InputStream content = entity.getContent();
						String returnConnection = convertStreamToString(content);
						if (returnConnection != null && !returnConnection.equals("")) {
							System.out.println(returnConnection);
							JSONObject obj;
							try {
								obj = new JSONObject(returnConnection);
								if (!obj.getString("content").equals("")) {
									
									SharedPreferences  sharedPrefs = getSharedPreferences(Constant.Notification.NOTIFICATION_NAME, Context.MODE_PRIVATE);
									Editor edit = sharedPrefs.edit();
									edit.putString(Constant.Notification.NOTIFICATION_CONTENT, obj.getString("content"));
									SimpleDateFormat format = new SimpleDateFormat(Constant.Profile.DATE_TIME_PATTERN);
									edit.putString(Constant.Notification.NOTIFICATION_DATE, format.format(new Date()));
									edit.commit();
									
									Intent it =new Intent(PushService.this, PushAlertActivity.class); 
									 it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									 it.putExtra("message", obj.getString("content"));
									 PushService.this.startActivity(it);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Thread.sleep(SLEEP);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {

				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
