package com.cashman.physio.v1.android.alarm.db;

import java.util.Date;
import java.util.List;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Alarm;
import com.cashman.physio.v1.android.alarm.data.CommonSetting;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DbTestActivity extends Activity implements OnClickListener{

	private Button mButton_Insert;
	private Button mButton_Find;
	private Button mButton_Update;
	private Button mButton_Select;
	private Button mButton_Delete;
	private Button mButton_Last;
	
	private DbManager dbManager = new DbManager(DbTestActivity.this);
	
	private static final String TAG = "DbTestActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbtest);
		initViews();
	}
	
	private void initViews(){
		mButton_Insert = (Button) findViewById(R.id.btn_insert);
		mButton_Find   = (Button) findViewById(R.id.btn_find);
		mButton_Update = (Button) findViewById(R.id.btn_update);
		mButton_Select = (Button) findViewById(R.id.btn_select);
		mButton_Delete = (Button) findViewById(R.id.btn_delete);
		mButton_Last = (Button) findViewById(R.id.btn_last);
		
		mButton_Insert.setOnClickListener(this);
		mButton_Find.setOnClickListener(this);
		mButton_Update.setOnClickListener(this);
		mButton_Select.setOnClickListener(this);
		mButton_Delete.setOnClickListener(this);
		mButton_Last.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	public void onClick(View v) {
		if(v == mButton_Insert){
			insert();
		}else if(v == mButton_Find){
			find();
		}else if(v == mButton_Update){
			update();
		}else if(v == mButton_Select){
			select();
		}else if(v == mButton_Delete){
			delete();
		}else if(v == mButton_Last){
			getLastAlarm();
		}
	}
	
	private void insert(){
		Alarm alarm = new Alarm("08:23");
		dbManager.insertAlarm(alarm);
		alarm = new Alarm("10:23",1);
		dbManager.insertAlarm(alarm);
		alarm = new Alarm("12:00",1);
		dbManager.insertAlarm(alarm);
		
		alarm = new Alarm("22:22");
		dbManager.insertAlarm(alarm);
		
		CommonSetting cs = new CommonSetting(
				1,
				"alarm0",
				"2 relative alarms",
				DateFormat.format("yyyy/MM/dd-hh:mm", new Date()).toString(),
				"0111100",
				"/sd/audio/ringtone.mp3",
				true,
				false,
				"",
				"",
				true,
				0
				);
		dbManager.insertCommonSetting(cs);
		cs = new CommonSetting(
				4,
				"alarm3",
				"2 relative alarms",
				DateFormat.format("yyyy/MM/dd-hh:mm", new Date()).toString(),
				"0110000",
				"/sd/audio/ringtone2.mp3",
				false,
				false,
				"",
				"",
				true,1
				);
		dbManager.insertCommonSetting(cs);
		
		
	}
	
	private void select(){
		Alarm alarm = dbManager.getAlarmById(4);
		LocalLog.i(TAG, "select","alarm: id = "+alarm.getId()+", startTime = "+alarm.getStartTime()+", foreignId = "+alarm.getForeignId());
		List<Alarm> alarmList = dbManager.getAlarmListByForeignId(-1);
		for(Alarm a : alarmList){
			LocalLog.i(TAG, "select","alarm: id = "+a.getId()+", startTime = "+a.getStartTime()+", foreignId = "+a.getForeignId());
		}
		
		CommonSetting cs = dbManager.getCommonSettingByAlarmId(1);
		LocalLog.i(TAG, "select","CommonSetting: id = "+cs.getId()+", alarmId = "+cs.getAlarmId()+", name = "+cs.getName()+", instruction = "+cs.getInstruction());
	}
	
	private void delete(){
		dbManager.deleteCommonSettingByAlarmId(4);
		dbManager.deleteAlarmById(4);
	}
	
	private void update(){
		CommonSetting cs = new CommonSetting(
				1,
				1,
				"alarm0,updateed",
				"2 relative alarms",
				DateFormat.format("yyyy/MM/dd-hh:mm", new Date()).toString(),
				"0111100",
				"/sd/audio/ringtone.mp3",
				true,
				true,
				"",
				"",
				true,3
				);
		dbManager.updateCommonSetting(cs);
		
		Alarm alarm = dbManager.getAlarmById(1);
		alarm.setStartTime("08:20");
		dbManager.updateAlarm(alarm);
		
	}
	
	private void find(){
		boolean find = dbManager.findAlarmByForeignId(1);
		LocalLog.i(TAG,"find", "findAlarmByForeignId(1); find = "+find);
		find = dbManager.findAlarmById(1);
		LocalLog.i(TAG,"find", "findAlarmById(1); find = "+find);
		
		find = dbManager.findCommonSettingByAlarmId(1);
		LocalLog.i(TAG,"find", "findCommonSettingByAlarmId, find = "+find);
	}
	
	private void getLastAlarm(){
		Alarm alarm = dbManager.getLastAlarm();
		LocalLog.i(TAG, "getLastAlarm","alarm: id = "+alarm.getId()+", startTime = "+alarm.getStartTime()+", foreignId = "+alarm.getForeignId());
	}
}
