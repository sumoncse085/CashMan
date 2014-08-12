package com.cashman.physio.v1.android.alarm.activity.profile;

import java.sql.Driver;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.receiver.AlarmReceiver;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;
import com.cashman.physio.v1.android.alarm.util.ProfileNotify;

public class ProfileActivity extends Activity implements View.OnClickListener, OnCheckedChangeListener {
	private static final int FLAG_DUE = Constant.Profile.FLAG_DUE;
	private static final int FLAG_GOALS = Constant.Profile.FLAG_GOALS;
	private static final int FLAG_NEXT_APPOINT = Constant.Profile.FLAG_NEXT_APPOINT;
	private static final int FLAG_LOCATION = 0;
	private static final int FLAG_NAME_1 = 12;
	private static final int FLAG_NAME_2 = 22;
	private static final int FLAG_NAME_3 = 32;
	private static final int FLAG_NAME_4 = 42;
	private static final int FLAG_ROLE_1 = 11;
	private static final int FLAG_ROLE_2 = 21;
	private static final int FLAG_ROLE_3 = 31;
	private static final int FLAG_ROLE_4 = 41;
	private static final String TAG = "ProfileActivity";
	private String[] mArray_Location;
	private String[] mArray_Role;
	private LinearLayout mLinear_Due;
	private LinearLayout mLinear_Goals;
	private TextView mTextView_Due;
	private TextView mTextView_Goals;
	private TextView mTvList_Name_1;
	private TextView mTvList_Name_2;
	private TextView mTvList_Name_3;
	private TextView mTvList_Name_4;
	private TextView mTvList_Role_1;
	private TextView mTvList_Role_2;
	private TextView mTvList_Role_3;
	private TextView mTvList_Role_4;
	private CheckBox isCheck_before_notify;
	private ProfileNotify notify;
	Thread notifyThread = null;
	private Button mButton_NextAppointmentDate;
	private Button mButton_NextAppointmentTime;

	private String getPreferences(String paramString) {
		return getSharedPreferences(
				Constant.SharePreferences.KEY_PROFILE_PREFERENCES_NAME, 0)
				.getString(paramString, "");
	}

	private void initData() {
		Resources localResources = getResources();
		this.mArray_Location = localResources
				.getStringArray(R.array.location_list);
		this.mArray_Role = localResources.getStringArray(R.array.role_list);
		//		this.mTvList_Location.setText(initTextView(FLAG_LOCATION));
		this.mTvList_Role_1.setText(initTextView(FLAG_ROLE_1));
		this.mTvList_Role_2.setText(initTextView(FLAG_ROLE_2));
		this.mTvList_Role_3.setText(initTextView(FLAG_ROLE_3));
		this.mTvList_Role_4.setText(initTextView(FLAG_ROLE_4));
		this.mTvList_Name_1.setText(initTextView(FLAG_NAME_1));
		this.mTvList_Name_2.setText(initTextView(FLAG_NAME_2));
		this.mTvList_Name_3.setText(initTextView(FLAG_NAME_3));
		this.mTvList_Name_4.setText(initTextView(FLAG_NAME_4));
		this.mTextView_Due.setText(initTextView(FLAG_DUE));
		this.mTextView_Goals.setText(initTextView(FLAG_GOALS));

		//		this.mTextView_NextAppoint.setText(initTextView(FLAG_NEXT_APPOINT));

		String str8 = getPreferences(Constant.Profile.KEY_FIXED_NEXT_APPOINT);
		if (str8.length() > 0){
			int i = str8.indexOf(Constant.Profile.DATE_AND_TIME_SEPARATOR);
			mButton_NextAppointmentDate.setText(str8.substring(0,i));
			mButton_NextAppointmentTime.setText(str8.substring(i + 1, str8.length()));
		}else{
			Date current = new Date();
			String str = DateFormat.format(Constant.Profile.DATE_TIME_PATTERN, current).toString() ;
			int i = str.indexOf(Constant.Profile.DATE_AND_TIME_SEPARATOR);
			mButton_NextAppointmentDate.setText(str.substring(0,i));
			mButton_NextAppointmentTime.setText(str.substring(i + 1));
		}

		boolean is_check  = getSharedPreferences(
				Constant.SharePreferences.KEY_PROFILE_PREFERENCES_NAME, 0).getBoolean(Constant.SharePreferences.KEY_NOTIFY_IS_CHECK, false);
		this.isCheck_before_notify.setChecked(is_check);
		//  SimpleDateFormat df2 = new SimpleDateFormat(Constant.Profile.DATE_TIME_PATTERN );
		//saveToPreferences(Constant.SharePreferences.KEY_FIXED_NEXT_APPOINT, df2.format(new Date()));

	}

	private String initTextView(int flag)
	{
		String str = "";
		switch (flag)
		{
		default:

		case FLAG_ROLE_1:
			str = getPreferences(Constant.SharePreferences.KEY_ROLE_1);
			break;
		case FLAG_ROLE_2:
			str = getPreferences(Constant.SharePreferences.KEY_ROLE_2);
			break;
		case FLAG_ROLE_3:
			str = getPreferences(Constant.SharePreferences.KEY_ROLE_3);
			break;
		case FLAG_ROLE_4:
			str = getPreferences(Constant.SharePreferences.KEY_ROLE_4);
			break;
		case FLAG_LOCATION:
			str = getPreferences(Constant.SharePreferences.KEY_LOCATION);
			break;
		case FLAG_NAME_1:
			str = getPreferences(Constant.SharePreferences.KEY_NAME_1);
			break;
		case FLAG_NAME_2:
			str = getPreferences(Constant.SharePreferences.KEY_NAME_2);
			break;
		case FLAG_NAME_3:
			str = getPreferences(Constant.SharePreferences.KEY_NAME_3);
			break;
		case FLAG_NAME_4:
			str = getPreferences(Constant.SharePreferences.KEY_NAME_4);
			break;
		case FLAG_NEXT_APPOINT:
			str = getPreferences(Constant.SharePreferences.KEY_NEXT_APPOINT);
			break;
		case FLAG_GOALS:
			str = getPreferences(Constant.SharePreferences.KEY_GOALS);
			break;
		case FLAG_DUE:
			str = getPreferences(Constant.SharePreferences.KEY_DUE);
			break;
		}
		if(str == null || str.length() <= 0){
			if(flag != FLAG_DUE && flag != FLAG_GOALS && flag != FLAG_NEXT_APPOINT){
				str = getString(R.string.please_select);
			}else{
				str = getString(R.string.none);
			}
		}


		return str;
	}

	private void initViews() {
		//		this.mTvList_Location = ((TextView) findViewById(R.id.tv_location_list));
		this.mTvList_Role_1 = ((TextView) findViewById(R.id.tv_role_list_1));
		this.mTvList_Name_1 = ((TextView) findViewById(R.id.tv_name_list_1));
		this.mTvList_Role_2 = ((TextView) findViewById(R.id.tv_role_list_2));
		this.mTvList_Name_2 = ((TextView) findViewById(R.id.tv_name_list_2));
		this.mTvList_Role_3 = ((TextView) findViewById(R.id.tv_role_list_3));
		this.mTvList_Name_3 = ((TextView) findViewById(R.id.tv_name_list_3));
		this.mTvList_Role_4 = ((TextView) findViewById(R.id.tv_role_list_4));
		this.mTvList_Name_4 = ((TextView) findViewById(R.id.tv_name_list_4));
		//		this.mLinear_NextAppoint = ((LinearLayout) findViewById(R.id.linear_next_appoint));
		this.mLinear_Goals = ((LinearLayout) findViewById(R.id.linear_goals));
		this.mLinear_Due = ((LinearLayout) findViewById(R.id.linear_due_time));
		//		this.mTextView_NextAppoint = ((TextView) findViewById(R.id.tv_next_appoint));
		this.mTextView_Goals = ((TextView) findViewById(R.id.tv_goals));
		this.mTextView_Due = ((TextView) findViewById(R.id.tv_due));
		mButton_NextAppointmentDate = (Button) findViewById(R.id.btn_next_appoint_date);
		mButton_NextAppointmentTime = (Button) findViewById(R.id.btn_next_appoint_time);
		this.isCheck_before_notify = (CheckBox)ProfileActivity.this.findViewById(R.id.profile_is_before_notify);
		//		this.mTvList_Location.setOnClickListener(this);
		this.mTvList_Role_1.setOnClickListener(this);
		this.mTvList_Name_1.setOnClickListener(this);
		this.mTvList_Role_2.setOnClickListener(this);
		this.mTvList_Name_2.setOnClickListener(this);
		this.mTvList_Role_3.setOnClickListener(this);
		this.mTvList_Name_3.setOnClickListener(this);
		this.mTvList_Role_4.setOnClickListener(this);
		this.mTvList_Name_4.setOnClickListener(this);
		this.mLinear_Goals.setOnClickListener(this);
		//		this.mLinear_NextAppoint.setOnClickListener(this);
		this.mLinear_Due.setOnClickListener(this);
		mButton_NextAppointmentDate.setOnClickListener(this);
		mButton_NextAppointmentTime.setOnClickListener(this);
		this.isCheck_before_notify.setOnCheckedChangeListener(this);
		TextView headTxt = (TextView)findViewById(R.id.head_nab_txt_title);
		headTxt.setText("My Profile");
		initData();
	}

	private void saveAndSet(TextView paramTextView, String key, String value) {
		saveToPreferences(key, value);
		paramTextView.setText(value);
	}

	private void saveToPreferences(String paramString1, String paramString2) {
		SharedPreferences.Editor localEditor = getSharedPreferences(
				Constant.SharePreferences.KEY_PROFILE_PREFERENCES_NAME, 0).edit();
		localEditor.putString(paramString1, paramString2);

		localEditor.commit();
	}



	private void showDropdownList(final String[] paramArrayOfString,
			int paramInt, final TextView paramTextView, final String paramString) {
		String str = null;
		if (paramInt == FLAG_LOCATION) {
			str = getString(R.string.location_colon) ;
		} else {
			str = getString(R.string.role_colon) ;
		}
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setTitle(str + paramTextView.getText().toString());
		localBuilder.setItems(paramArrayOfString,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface paramDialogInterface,
					int paramInt) {
				String str = paramArrayOfString[paramInt];
				paramTextView.setText(str);
				ProfileActivity.this
				.saveToPreferences(paramString, str);
			}
		});
		localBuilder.create().show();

	}

	private void showNameDropdownList(int paramInt, String paramString) {
		if (getString(R.string.please_select).equals(paramString))
			paramString = "";
		System.out.println(paramInt + "||"+paramString);
		Intent localIntent = new Intent(this, NameListActivity.class);
		localIntent.putExtra(Constant.Profile.KEY_NAME, paramString);
		startActivityForResult(localIntent, paramInt);
	}

	private void startActivityForResult(int flag) {
		Class<?> targetClass = null;
		Intent i = new Intent();
		String content = null;
		String contentKey = "";
		switch (flag) {
		case FLAG_GOALS:
			contentKey = Constant.Profile.KEY_GOALS;
			content = mTextView_Goals.getText().toString().trim();
			targetClass = GoalsActivity.class;
			break;
		case FLAG_DUE:
			contentKey = Constant.Profile.KEY_DUE;
			content = mTextView_Due.getText().toString().trim();
			targetClass = NextAppointActivity.class;
			break;
		case FLAG_NEXT_APPOINT:
			contentKey = Constant.Profile.KEY_NEXT_APPOINT;
			//			content = mTextView_NextAppoint.getText().toString().trim();
			targetClass = NextAppointActivity.class;
			break;
		default:
			break;
		}
		if(content == null || content.length() <= 0 || content.equals(getString(R.string.none))){
			content = null;
		}
		i.putExtra(contentKey, content);
		i.putExtra(Constant.Profile.KEY_FLAG, flag);
		i.setClass(ProfileActivity.this, targetClass);
		startActivityForResult(i, flag);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String str1 = "";
		String str2 = "";
		TextView localTextView = null;
		if (data != null) {
			switch (requestCode) {
			default:
			case FLAG_NAME_1:
				str1 = data.getStringExtra(Constant.Profile.KEY_NAME);
				localTextView = this.mTvList_Name_1;
				str2 = Constant.SharePreferences.KEY_NAME_1;
				break;
			case FLAG_NAME_2:
				str1 = data.getStringExtra(Constant.Profile.KEY_NAME);
				localTextView = this.mTvList_Name_2;
				str2 = Constant.SharePreferences.KEY_NAME_2;
				break;
			case FLAG_NAME_3:
				str1 = data.getStringExtra(Constant.Profile.KEY_NAME);
				localTextView = this.mTvList_Name_3;
				str2 = Constant.SharePreferences.KEY_NAME_3;
				break;
			case FLAG_NAME_4:
				str1 = data.getStringExtra(Constant.Profile.KEY_NAME);
				localTextView = this.mTvList_Name_4;
				str2 = Constant.SharePreferences.KEY_NAME_4;
				break;
			case FLAG_NEXT_APPOINT:
				str1 = data.getStringExtra(Constant.Profile.KEY_NEXT_APPOINT);
				//				localTextView = this.mTextView_NextAppoint;
				str2 = Constant.SharePreferences.KEY_NEXT_APPOINT;
				break;

			case FLAG_GOALS:
				str1 = data.getStringExtra(Constant.Profile.KEY_GOALS);
				localTextView = this.mTextView_Goals;
				str2 = Constant.SharePreferences.KEY_GOALS;
				break;
			case FLAG_DUE:
				str1 = data.getStringExtra(Constant.Profile.KEY_DUE);
				localTextView = this.mTextView_Due;
				str2 = Constant.SharePreferences.KEY_DUE;
				break;
			}

			saveAndSet(localTextView, str2, str1);
		}
	}

	public void onClick(View paramView) {
		//		if (paramView == this.mTvList_Location)
		//			showDropdownList(this.mArray_Location, FLAG_LOCATION,
		//					this.mTvList_Location,
		//					Constant.SharePreferences.KEY_LOCATION);
		//		else 
		if (paramView == this.mTvList_Name_1)
			showNameDropdownList(FLAG_NAME_1, this.mTvList_Name_1.getText()
					.toString());
		else if (paramView == this.mTvList_Role_1)
			showDropdownList(this.mArray_Role, FLAG_ROLE_1,
					this.mTvList_Role_1, Constant.SharePreferences.KEY_ROLE_1);
		else if (paramView == this.mTvList_Role_2)
			showDropdownList(this.mArray_Role, FLAG_ROLE_2,
					this.mTvList_Role_2, Constant.SharePreferences.KEY_ROLE_2);
		else if (paramView == this.mTvList_Name_2)
			showNameDropdownList(FLAG_NAME_2, this.mTvList_Name_2.getText()
					.toString());
		else if (paramView == this.mTvList_Role_3)
			showDropdownList(this.mArray_Role, FLAG_ROLE_3,
					this.mTvList_Role_3, Constant.SharePreferences.KEY_ROLE_3);
		else if (paramView == this.mTvList_Name_3)
			showNameDropdownList(FLAG_NAME_3, this.mTvList_Name_3.getText()
					.toString());
		else if (paramView == this.mTvList_Role_4)
			showDropdownList(this.mArray_Role, FLAG_ROLE_4,
					this.mTvList_Role_4, Constant.SharePreferences.KEY_ROLE_4);
		else if (paramView == this.mTvList_Name_4)
			showNameDropdownList(FLAG_NAME_4, this.mTvList_Name_4.getText()
					.toString());
		//		else if (paramView == this.mLinear_NextAppoint)
		//			startActivityForResult(FLAG_NEXT_APPOINT);
		else if(paramView == mButton_NextAppointmentDate){
			showDatePicker(mButton_NextAppointmentDate.getText().toString(),FLAG_NEXT_APPOINT);
			this.isCheck_before_notify.setChecked(false);
			saveBooleanPreferences(Constant.SharePreferences.KEY_NOTIFY_IS_CHECK, false);
			this.isCheck_before_notify.setBackgroundDrawable(getResources().getDrawable(R.drawable.none_choose));
			if(this.notify != null)this.notify.setFlag(false);}
		else if(paramView == mButton_NextAppointmentTime)
		{
			showTimePicker(mButton_NextAppointmentTime.getText().toString(),FLAG_NEXT_APPOINT);
			this.isCheck_before_notify.setChecked(false);
			saveBooleanPreferences(Constant.SharePreferences.KEY_NOTIFY_IS_CHECK, false);
			this.isCheck_before_notify.setBackgroundDrawable(getResources().getDrawable(R.drawable.none_choose));
			if(this.notify != null)this.notify.setFlag(false);
		}
		else if (paramView == this.mLinear_Goals)
			startActivityForResult(FLAG_GOALS);
		else if (paramView == this.mLinear_Due)
			startActivityForResult(FLAG_DUE);
	}

	private void showDatePicker(String originalDateStr,final int flag){
		Date originalDate = null;
		try {
			originalDate = new SimpleDateFormat(Constant.Profile.DATE_PATTERN).parse(originalDateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(originalDate == null){
			originalDate = new Date();
		}

		int year = originalDate.getYear() + 1900;
		int month = originalDate.getMonth();
		int day = originalDate.getDate();

		DatePickerDialog dialog = new DatePickerDialog(ProfileActivity.this,new DatePickerDialog.OnDateSetListener(){

			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				Date date = new Date();
				date.setDate(arg3);
				date.setMonth(arg2);
				date.setYear(arg1 - 1900);
				String str = DateFormat.format(Constant.Profile.DATE_PATTERN, date).toString();
				if(flag == FLAG_NEXT_APPOINT){
					mButton_NextAppointmentDate.setText(str);
					String dateTime = str + Constant.Profile.DATE_AND_TIME_SEPARATOR + mButton_NextAppointmentTime.getText().toString();
					saveToPreferences(Constant.SharePreferences.KEY_FIXED_NEXT_APPOINT, dateTime);
					AddAlarm();

				}


			}},year
			,month
			,day);

		dialog.show();

	}


	private void showTimePicker(String originalTimeStr,final  int flag){
		Date originalDate = null;
		try {
			originalDate = new SimpleDateFormat(Constant.Profile.TIME_PATTERN).parse(originalTimeStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(originalDate == null){
			originalDate = new Date();
		}
		int hour = originalDate.getHours();
		int minute = originalDate.getMinutes();

		TimePickerDialog dialog = new TimePickerDialog(ProfileActivity.this, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Date date = new Date();
				date.setHours(hourOfDay);
				date.setMinutes(minute);
				String str = DateFormat.format(Constant.Profile.TIME_PATTERN, date).toString();
				if(flag == FLAG_NEXT_APPOINT){
					mButton_NextAppointmentTime.setText(str);
					String dateTime =  mButton_NextAppointmentDate.getText().toString() + Constant.Profile.DATE_AND_TIME_SEPARATOR +str;
					saveToPreferences(Constant.SharePreferences.KEY_FIXED_NEXT_APPOINT, dateTime);
					AddAlarm();
				}
			}
		}, hour, minute, false);

		dialog.show();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.profile);
		initViews();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			String d1 = getSharedPreferences(Constant.SharePreferences.KEY_PROFILE_PREFERENCES_NAME, 0).getString(Constant.SharePreferences.KEY_FIXED_NEXT_APPOINT, "");
			if(!d1.trim().equals("")){
				SimpleDateFormat df2 = new SimpleDateFormat(Constant.Profile.DATE_TIME_PATTERN );
				Date date =null;
				try {
					date = df2.parse(d1);
					ProfileNotify	notify =	ProfileNotify.getInstance(ProfileActivity.this, date);
					notify.setDate(date);
					notify.setFlag(true);
					notifyThread = new Thread(notify);
					notifyThread.start();
					this.isCheck_before_notify.setBackgroundDrawable(getResources().getDrawable(R.drawable.choose_person_sure_btn_icon));
					saveBooleanPreferences(Constant.SharePreferences.KEY_NOTIFY_IS_CHECK, true);	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.i(TAG, date +"--");
			}
			//			 ProfileNotify.getInstance(ProfileActivity.this, date);
		}else{
			if(notify != null){
				notify.setFlag(false);
				saveBooleanPreferences(Constant.SharePreferences.KEY_NOTIFY_IS_CHECK, false);
			}
			this.isCheck_before_notify.setBackgroundDrawable(getResources().getDrawable(R.drawable.none_choose));
		}
	}

	private void saveBooleanPreferences(String key, boolean value){
		SharedPreferences.Editor localEditor = getSharedPreferences(
				Constant.SharePreferences.KEY_PROFILE_PREFERENCES_NAME, 0).edit();
		localEditor.putBoolean(key, value);
		localEditor.commit();
	}


	public void AddAlarm(){
		Calendar calendar = Calendar.getInstance();
		long currenttime=calendar.getTimeInMillis();
		Log.e("before", ""+calendar.getTimeInMillis());
		String date = getPreferences(Constant.Profile.KEY_FIXED_NEXT_APPOINT);
		date=date.replace(" ", "");
		//08-03 00:02:10.443: E/Time(15800): 2014/08/02Sat,12:03am  201/0/0 1:0

		int i = date.indexOf(Constant.Profile.DATE_AND_TIME_SEPARATOR);
		String time=date.substring(i + 1, date.length());
		int year=Integer.parseInt(date.substring(0, 4));
		int month=Integer.parseInt(date.substring(5, 7));
		int dayofmonth=Integer.parseInt(date.substring(8, 10));
		int hour=Integer.parseInt(time.substring(0, 2));
		int min=Integer.parseInt(time.substring(3, 5));
		time=time.replace("P", "p");
		time=time.replace("M", "m");
		if(time.contains("pm")){
			if(hour!=12){
				hour+=12;	
			}
			
		}
		

		Log.e("Time ",date+"  " +year+"/"+month+"/"+dayofmonth+" "+hour+":"+min);

		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, dayofmonth);

		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);
		
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(year, month, dayofmonth, 
				hour, Calendar.MINUTE, 0);
		long startTime = calendar1.getTimeInMillis();
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
			formatter.setLenient(false);
			
			String oldTime =dayofmonth+"." +month+"."+year+", "+hour+":"+min;//"05.01.2011, 12:45";
			Date oldDate = formatter.parse(oldTime);
			startTime= oldDate.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//calendar.set(Calendar.AM_PM,Calendar.AM);
		Log.e("after", ""+startTime);
		if(startTime-currenttime>60*60*1000){
//			AppListener lisenar=new AppListener(startTime);
//			//lisenar.scheduleAlarms(mgr, pi, ctxt)
//			WakefulIntentService.scheduleAlarms(lisenar,
//                    this, false);
		
			startTime-=60*60*1000;
			Intent myIntent = new Intent(ProfileActivity.this, AlarmReceiver.class);
			myIntent.putExtra("appoinmentalarm", true);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(ProfileActivity.this, 1, myIntent,PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP,startTime, pendingIntent);
	}
		
	}
}