package com.cashman.physio.v1.android.alarm.db;

import java.util.ArrayList;
import java.util.List;

import com.cashman.physio.v1.android.alarm.data.Alarm;
import com.cashman.physio.v1.android.alarm.data.CommonSetting;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager {

	private static final String DB_NAME = "screencast";
	private static final int DB_VERSION = 1;
	private DbHelper mDbHelper;
	
	private static final String TAG = "DbManager";
	
	public class AlarmTable{
		public static final String TABLE_ALARM = "alarm";
		protected static final String ID = "_id";
		protected static final String START_TIME = "start_time";
		protected static final String FOREIGN_ID = "foreign_id";
		
		public static final String CREATE = "create table if not exists "+TABLE_ALARM+" ("
													+ID+" integer primary key autoincrement,"
													+START_TIME+"  nvarchar(5) not null,"
													+FOREIGN_ID + " integer)";
		public static final String DROP = "drop table if exists "+ TABLE_ALARM;
	}
	
	
	public class CommonSettingTable{
		public static final String TABLE_COMMON_SETTING = "common_setting";
		protected static final String ID = "_id";
		protected static final String CREATE_TIME = "create_time";
		protected static final String ALARM_ID = "alarm_id";
		protected static final String RINGTONE_PATH = "ringtone_path";
		protected static final String RINGTONE_ENABLE = "ringtone_enable";
		protected static final String VIBRATE_ENABLE = "vibrate_enable";
		protected static final String VIDEO_PATH = "video_path";
		protected static final String VIDEO_THUMB_PATH = "video_thumb_path";
		protected static final String ACTIVATE = "activate";
		protected static final String WEEKDAY = "weekday";
		protected static final String NAME = "name";
		protected static final String INSTRUCTION = "instruction";
		protected static final String EVERY = "every";
		
		public static final String CREATE = "create table if not exists "+TABLE_COMMON_SETTING+" ("
													+ID + " integer primary key autoincrement,"
													+ALARM_ID+" integer not null,"
													+NAME+" nvarchar(100),"
													+INSTRUCTION + " nvarchar(1000),"
													+CREATE_TIME+" nvarchar(16),"
													+RINGTONE_PATH+" nvarchar(100),"
													+RINGTONE_ENABLE+" integer not null,"
													+VIBRATE_ENABLE+" integer not null,"
													+VIDEO_PATH+" nvarchar(100),"
													+VIDEO_THUMB_PATH+" nvarchar(100),"
													+ACTIVATE+" integer not null,"
													+WEEKDAY+" nchar(7) not null,"
													+EVERY+" integer)";
		
		public static final String DROP = "drop table if exists "+TABLE_COMMON_SETTING;
	}
	
	public DbManager(Context context){
		mDbHelper = new DbHelper(context,DB_NAME,null,DB_VERSION);
	}
	
	/**
	 * 
	 * @param alarm
	 * @return  the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insertAlarm(Alarm alarm){
		if(alarm == null){
			LocalLog.e(TAG,"insertAlarm", "alarm to insert is null");
			return -1;
		}
		long result = 0L;
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			ContentValues cv  = new ContentValues();
			cv.put(AlarmTable.START_TIME, alarm.getStartTime());
			cv.put(AlarmTable.FOREIGN_ID,alarm.getForeignId());
			result = db.insert(AlarmTable.TABLE_ALARM, "null row",cv);
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"insertAlarm", "insert a alarm catches exception",ex);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param commonSetting
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insertCommonSetting(CommonSetting commonSetting){
		if(commonSetting == null){
			LocalLog.e(TAG,"insertCommonSetting" ,"commonSetting to insert is null");
			return -1;
		}
		long result = 0L;
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			ContentValues cv  = new ContentValues();
			cv.put(CommonSettingTable.ALARM_ID, commonSetting.getAlarmId());
			cv.put(CommonSettingTable.ACTIVATE, commonSetting.isActivate());
			cv.put(CommonSettingTable.CREATE_TIME, commonSetting.getCreateTime());
			cv.put(CommonSettingTable.INSTRUCTION, commonSetting.getInstruction());
			cv.put(CommonSettingTable.NAME, commonSetting.getName());
			cv.put(CommonSettingTable.RINGTONE_ENABLE, commonSetting.isRingtoneEnable());
			cv.put(CommonSettingTable.RINGTONE_PATH, commonSetting.getRingtonePath());
			cv.put(CommonSettingTable.VIBRATE_ENABLE, commonSetting.isVibrateEnable());
			cv.put(CommonSettingTable.VIDEO_PATH, commonSetting.getVideoPath());
			cv.put(CommonSettingTable.VIDEO_THUMB_PATH, commonSetting.getVideoThumbPath());
			cv.put(CommonSettingTable.WEEKDAY, commonSetting.getWeekday());
			cv.put(CommonSettingTable.EVERY, commonSetting.getEvery());
			result = db.insert(CommonSettingTable.TABLE_COMMON_SETTING, "null row", cv);
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"insertCommonSetting", "insert a commonSetting catches exception",ex);
		}
		return result;
	}
	
	/**
	 * 
	 * @param alarm
	 * @return the number of rows affected if a alarmId is passed in. return -1 if a alarmId is < 0
	 */
	public int deleteAlarm(Alarm alarm){
		if(alarm == null){
			LocalLog.e(TAG, "deleteAlarm","alarm to delete is null");
			return -1;
		}
		return deleteAlarmById(alarm.getId());
	}
	
	/**
	 * 
	 * @param alarm
	 * @return the number of rows affected if a alarmId is passed in. return -1 if a alarmId is < 0
	 */
	public int deleteAlarmById(int id){
		if(id < 0){
			LocalLog.e(TAG,"deleteAlarmById","the id of alarm to delete is < 0");
			return -1;
		}
		int result = 0;
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			result = db.delete(AlarmTable.TABLE_ALARM, AlarmTable.ID + " = " + id, null);
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"deleteAlarmById","delete alarm with id = "+id+" catches exception", ex);
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param foreignId
	 * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
	 */
	public int deleteAlarmByForeignId(int foreignId){
		if(foreignId < 0){
			LocalLog.e(TAG,"deleteAlarmByForeignId","the foreign_id of alarm to delete is < 0");
			return -1;
		}
		int result = 0;
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			result = db.delete(AlarmTable.TABLE_ALARM, AlarmTable.FOREIGN_ID + " = " + foreignId, null);
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"deleteAlarmById","delete alarm with foreign_id = "+foreignId+" catches exception", ex);
		}
		
		return result;
	}
	
	/**
	 * @param cs
	 * @return the number of rows affected if a commonSettig is passed in. return -1 if a commonSettig is null
	 */
	public int deleteCommonSetting(CommonSetting cs){
		if(cs == null){
			LocalLog.e(TAG,"deleteCommonSetting", "commonSetting to delete is null");
		}
		return deleteCommonSettingById(cs.getId());
	}
	
	/**
	 * @param cs
	 * @return the number of rows affected if a commonSettigId is passed in. return -1 if a commonSettigId is < 0
	 */
	public int deleteCommonSettingById(int id){
		if(id < 0){
			LocalLog.e(TAG,"","the id of commonSetting to delete is < 0");
			return -1;
		}
		int result = 0;
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			result = db.delete(CommonSettingTable.TABLE_COMMON_SETTING, CommonSettingTable.ID + " = " + id, null);
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG, "deleteCommonSettingById", "delete a commonSetting with id = "+id+" catches exception", ex);
		}
		return result;
	}
	
	public int deleteCommonSettingByAlarmId(int alarmId){
		if(alarmId < 0){
			LocalLog.e(TAG,"","the id of commonSetting to delete is < 0");
			return -1;
		}
		int result = 0;
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			result = db.delete(CommonSettingTable.TABLE_COMMON_SETTING, CommonSettingTable.ALARM_ID + " = " + alarmId, null);
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG, "deleteCommonSettingById", "delete a commonSetting with alarm_id = "+alarmId+" catches exception", ex);
		}
		
		return result;
	}
	
	/**
	 * @param cs
	 * @return the number of rows affected if a alarm is passed in. return -1 if a alarm  is null
	 */
	public int updateAlarm(Alarm alarm){
		if(alarm == null){
			LocalLog.e(TAG, "updateAlarm","the alarm to update is null");
			return  -1;
		}
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues cv  = new ContentValues();
		cv.put(AlarmTable.START_TIME, alarm.getStartTime());
		cv.put(AlarmTable.FOREIGN_ID,alarm.getForeignId());
		int result = db.update(AlarmTable.TABLE_ALARM, cv, AlarmTable.ID + " = "+alarm.getId(), null);
		db.close();
		return result;
	}
	
	/**
	 * @param cs
	 * @return the number of rows affected if a commonSettig is passed in. return -1 if a commonSettig is null
	 */
	public int updateCommonSetting(CommonSetting commonSetting){
		if(commonSetting == null){
			LocalLog.e(TAG, "updateCommonSetting","the commonSetting to update is null");
			return -1;
		}
		int result = 0;
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			ContentValues cv  = new ContentValues();
			cv.put(CommonSettingTable.ALARM_ID, commonSetting.getAlarmId());
			cv.put(CommonSettingTable.ACTIVATE, commonSetting.isActivate());
			cv.put(CommonSettingTable.CREATE_TIME, commonSetting.getCreateTime());
			cv.put(CommonSettingTable.INSTRUCTION, commonSetting.getInstruction());
			cv.put(CommonSettingTable.NAME, commonSetting.getName());
			cv.put(CommonSettingTable.RINGTONE_ENABLE, commonSetting.isRingtoneEnable());
			cv.put(CommonSettingTable.RINGTONE_PATH, commonSetting.getRingtonePath());
			cv.put(CommonSettingTable.VIBRATE_ENABLE, commonSetting.isVibrateEnable());
			cv.put(CommonSettingTable.VIDEO_PATH, commonSetting.getVideoPath());
			cv.put(CommonSettingTable.VIDEO_THUMB_PATH, commonSetting.getVideoThumbPath());
			cv.put(CommonSettingTable.WEEKDAY, commonSetting.getWeekday());
			cv.put(CommonSettingTable.EVERY, commonSetting.getEvery());
			result = db.update(CommonSettingTable.TABLE_COMMON_SETTING, cv,CommonSettingTable.ID + " = "+commonSetting.getId(), null);
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"updateCommonSetting", "update commonSetting with id = "+commonSetting.getId()+" catches exception",ex);
		}
		return result;
	}
	
	/**
	 * @param cs
	 * @return the number of rows affected if a commonSettig is passed in. return -1 if a commonSettig is null
	 */
	public int updateCommonSettingByAlarmId(CommonSetting commonSetting){
		if(commonSetting == null){
			LocalLog.e(TAG, "updateCommonSettingByAlarmId","the commonSetting to update is null");
			return -1;
		}
		int result = 0;
		try{
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			ContentValues cv  = new ContentValues();
			cv.put(CommonSettingTable.ALARM_ID, commonSetting.getAlarmId());
			cv.put(CommonSettingTable.ACTIVATE, commonSetting.isActivate());
			cv.put(CommonSettingTable.CREATE_TIME, commonSetting.getCreateTime());
			cv.put(CommonSettingTable.INSTRUCTION, commonSetting.getInstruction());
			cv.put(CommonSettingTable.NAME, commonSetting.getName());
			cv.put(CommonSettingTable.RINGTONE_ENABLE, commonSetting.isRingtoneEnable());
			cv.put(CommonSettingTable.RINGTONE_PATH, commonSetting.getRingtonePath());
			cv.put(CommonSettingTable.VIBRATE_ENABLE, commonSetting.isVibrateEnable());
			cv.put(CommonSettingTable.VIDEO_PATH, commonSetting.getVideoPath());
			cv.put(CommonSettingTable.VIDEO_THUMB_PATH, commonSetting.getVideoThumbPath());
			cv.put(CommonSettingTable.WEEKDAY, commonSetting.getWeekday());
			cv.put(CommonSettingTable.EVERY, commonSetting.getEvery());
			result = db.update(CommonSettingTable.TABLE_COMMON_SETTING, cv,CommonSettingTable.ALARM_ID + " = "+commonSetting.getAlarmId(), null);
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"updateCommonSettingByAlarmId", "update commonSetting with alarm_id = "+commonSetting.getAlarmId()+" catches exception",ex);
		}
		return result;
	}
	
	/**
	 * @param id
	 * @return true if find
	 */
	public boolean findAlarmById(int id){
		if(id < 0){
			LocalLog.e(TAG,"findAlarmById","the id of alarm to find is < 0");
			return false;
		}
		boolean find = false;
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor  c = db.query(AlarmTable.TABLE_ALARM,new String[]{AlarmTable.ID},AlarmTable.ID+" = "+id,null,null,null,null);
			find = c.getCount() > 0 ? true : false;
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"findAlarmById", "find alarm with id = "+id+" catches exception",ex);
		}
		return find;
	}
	
	/**
	 * @param id
	 * @return true if find
	 */
	
	public boolean findAlarmByForeignId(int foreignId){
		if(foreignId < 0){
			LocalLog.e(TAG,"findAlarmByForeignId","the foreignId of alarm to find is < 0");
			return false;
		}
		boolean find = false;
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor  c = db.query(AlarmTable.TABLE_ALARM,new String[]{AlarmTable.ID},AlarmTable.FOREIGN_ID+" = "+foreignId,null,null,null,null);
			find = c.getCount() > 0 ? true : false;
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG, "findAlarmByForeignId","find alarm with foreign_id = "+foreignId+" catches exception", ex);
		}
		return find;
	}
	
	public boolean findCommonSettingById(int id){
		if(id < 0){
			LocalLog.e(TAG,"findAlarmById","the id of commonSetting to find is < 0");
			return false;
		}
		boolean find = false;
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor  c = db.query(CommonSettingTable.TABLE_COMMON_SETTING,new String[]{CommonSettingTable.ID},CommonSettingTable.ID+" = "+id,null,null,null,null);
			find = c.getCount() > 0 ? true:false;
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG, "findCommonSettingById","find commonSetting with id = "+id+" catches exception",ex);
		}
		return find;
	}
	
	public boolean findCommonSettingByAlarmId(int alarmId){
		if(alarmId < 0){
			LocalLog.e(TAG,"findAlarmById","the alarmId of commonSetting to find is < 0");
			return false;
		}
		boolean find = false;
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor  c = db.query(CommonSettingTable.TABLE_COMMON_SETTING,new String[]{CommonSettingTable.ID},CommonSettingTable.ALARM_ID+" = "+alarmId,null,null,null,null);
			find = c.getCount() > 0 ? true : false;
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG, "findCommonSettingById","find commonSetting with alarm_id = "+alarmId+" catches exception",ex);
		}
		return find;
	}
	
	/**
	 * 
	 * @param foreignId if foreignId == -1, the alarm has no relative alarms
	 * @return 
	 */
	public List<Alarm> getAlarmListByForeignId(int foreignId){
		List<Alarm> list = new ArrayList<Alarm>();
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor  c = db.query(AlarmTable.TABLE_ALARM, new String[]{AlarmTable.ID,AlarmTable.FOREIGN_ID,AlarmTable.START_TIME}, AlarmTable.FOREIGN_ID+" = "+foreignId,null, null, null, AlarmTable.ID);
			while(c.moveToNext()){
				Alarm alarm = new Alarm();
				alarm.setId(c.getInt(0));
				alarm.setStartTime(c.getString(2));
				alarm.setForeignId(c.getInt(1));
				list.add(alarm);
			}
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"getAlarmListByForeignId", "get alarm with foreignId = "+foreignId+" catches exception", ex);
		}
		return list;
	}
	
	public Alarm getAlarmById(int id){
		Alarm alarm = new Alarm();
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor  c = db.query(AlarmTable.TABLE_ALARM, new String[]{AlarmTable.ID,AlarmTable.FOREIGN_ID,AlarmTable.START_TIME}, AlarmTable.ID+" = "+id,null, null, null, null);
			while(c.moveToNext()){
				alarm.setId(c.getInt(0));
				alarm.setStartTime(c.getString(2));
				alarm.setForeignId(c.getInt(1));
			}
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"getAlarmById", "get alarm with id = "+id+" catches exception", ex);
		}
		return alarm;
	}
	
	public Alarm getLastAlarm(){
		Alarm alarm = new Alarm();
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor  c = db.query(AlarmTable.TABLE_ALARM, new String[]{AlarmTable.ID,AlarmTable.FOREIGN_ID,AlarmTable.START_TIME},null,null, null, null, AlarmTable.ID);
			if(c.moveToLast()){
				alarm.setId(c.getInt(0));
				alarm.setStartTime(c.getString(2));
				alarm.setForeignId(c.getInt(1));
			}
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"getLastAlarm", "get last alarm catches exception", ex);
		}
		return alarm;
	}
	
	public int getAlarmCountByForeignId(int foreignId){
		int count = 0;
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			
			Cursor  c = db.query(AlarmTable.TABLE_ALARM, new String[]{AlarmTable.ID,AlarmTable.FOREIGN_ID,AlarmTable.START_TIME}, AlarmTable.FOREIGN_ID+" = "+foreignId,null, null, null, null);
			count = c.getCount();
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG, "getAlarmCountByForeignId","get alarm count with foreign_id = "+ foreignId+"catches exception",ex);
		}
		return count;
	}
	
	public CommonSetting getCommonSettingByAlarmId(int alarmId){
		if(alarmId < 0){
			LocalLog.e(TAG,"getCommonSettingByAlarmId","the alarmId of commonSetting to find is < 0");
			return null;
		}
		CommonSetting cs = new  CommonSetting();
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor c = db.query(CommonSettingTable.TABLE_COMMON_SETTING, 
					new String[]{
								CommonSettingTable.ID,CommonSettingTable.ALARM_ID,CommonSettingTable.CREATE_TIME,
								CommonSettingTable.NAME,CommonSettingTable.INSTRUCTION,
								CommonSettingTable.RINGTONE_ENABLE,CommonSettingTable.RINGTONE_PATH,
								CommonSettingTable.VIBRATE_ENABLE,
								CommonSettingTable.VIDEO_PATH,CommonSettingTable.VIDEO_THUMB_PATH,
								CommonSettingTable.WEEKDAY,CommonSettingTable.ACTIVATE,
								CommonSettingTable.EVERY}, 
					CommonSettingTable.ALARM_ID + " = " + alarmId, 
					null, null, null, null);
			while(c.moveToNext()){
				cs.setId(c.getInt(0));
				cs.setAlarmId(c.getInt(1));
				cs.setCreateTime(c.getString(2));
				cs.setName(c.getString(3));
				cs.setInstruction(c.getString(4));
				cs.setRingtoneEnable(c.getInt(5) == 1 ? true :  false);
				cs.setRingtonePath(c.getString(6));
				cs.setVibrateEnable(c.getInt(7) == 1 ? true : false);
				cs.setVideoPath(c.getString(8));
				cs.setVideoThumbPath(c.getString(9));
				cs.setWeekday(c.getString(10));
				cs.setActivate(c.getInt(11) == 1 ? true : false);
				cs.setEvery(c.getInt(12));
			}
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"getCommonSettingByAlarmId","find commonSetting with alarm_id = "+alarmId+" catches exception",ex);
		}
		return cs;
	}
	
	public CommonSetting getCommonSettingById(int id){
		if(id < 0){
			LocalLog.e(TAG,"getCommonSettingByAlarmId","the alarmId of commonSetting to find is < 0");
			return null;
		}
		CommonSetting cs = new  CommonSetting();
		try{
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor c = db.query(CommonSettingTable.TABLE_COMMON_SETTING, 
					new String[]{
								CommonSettingTable.ID,CommonSettingTable.ALARM_ID,CommonSettingTable.CREATE_TIME,
								CommonSettingTable.NAME,CommonSettingTable.INSTRUCTION,
								CommonSettingTable.RINGTONE_ENABLE,CommonSettingTable.RINGTONE_PATH,
								CommonSettingTable.VIBRATE_ENABLE,CommonSettingTable.VIDEO_PATH,
								CommonSettingTable.WEEKDAY,CommonSettingTable.ACTIVATE,
								CommonSettingTable.EVERY}, 
					CommonSettingTable.ID + " = " + id, 
					null, null, null, null);
			while(c.moveToNext()){
				cs.setId(c.getInt(0));
				cs.setAlarmId(c.getInt(1));
				cs.setCreateTime(c.getString(2));
				cs.setName(c.getString(3));
				cs.setInstruction(c.getString(4));
				cs.setRingtoneEnable(c.getInt(5) == 1 ? true :  false);
				cs.setRingtonePath(c.getString(6));
				cs.setVibrateEnable(c.getInt(7) == 1 ? true : false);
				cs.setVideoPath(c.getString(8));
				cs.setVideoThumbPath(c.getString(9));
				cs.setWeekday(c.getString(10));
				cs.setActivate(c.getInt(11) == 1 ? true : false);
				cs.setEvery(c.getInt(12));
			}
			c.close();
			db.close();
		}catch(Exception ex){
			close();
			LocalLog.e(TAG,"getCommonSettingByAlarmId","find commonSetting with id = "+id+" catches exception",ex);
		}
		return cs;
	}
	
	public void close(){
		mDbHelper.close();
	}
	
	private class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(AlarmTable.CREATE);
			db.execSQL(CommonSettingTable.CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(AlarmTable.DROP);
			db.execSQL(CommonSettingTable.DROP);
			onCreate(db);
		}
	}
}
