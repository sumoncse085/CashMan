package com.cashman.physio.v1.android.alarm.activity.profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PhoneAndEmailTool;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;


public class Profile2Activity extends Activity implements OnClickListener {

	private static final String TAG = "ProfileActivity";
	private static final String PROFILE_NAME = Constant.Profile.KEY_PROFILE_NAME;
	private static final String KEY_BUSINESS_NAME = Constant.Profile.KEY_BUSINESS_NAME;
	private static final String KEY_NAME = Constant.Profile.KEY_NAME;
	private static final String KEY_PHONE_NUMBER = Constant.Profile.KEY_PHONE_NUMBER;
	private static final String KEY_EMAIL = Constant.Profile.KEY_EMAIL_ADDRESS;
	private static final String KEY_NEXT_APPOINT = Constant.Profile.KEY_NEXT_APPOINT;
	private static final String KEY_GOALS = Constant.Profile.KEY_GOALS;
	private static final String KEY_FLAG = Constant.Profile.KEY_FLAG;
	private static final String KEY_DUE = Constant.Profile.KEY_DUE;
	private static final String KEY_FIXED_NEXT_APPOINT = Constant.Profile.KEY_FIXED_NEXT_APPOINT;
	private static final String DATE_AND_TIME_SEPARATOR = Constant.Profile.DATE_AND_TIME_SEPARATOR;
	private static final String DATE_PATTERN = Constant.Profile.DATE_PATTERN;
	private static final String TIME_PATTERN = Constant.Profile.TIME_PATTERN;
	private static final String DATE_TIME_PATTERN = Constant.Profile.DATE_TIME_PATTERN;

	private static final int FLAG_DUE = Constant.Profile.FLAG_DUE;
	private static final int FLAG_GOALS = Constant.Profile.FLAG_GOALS;
	private static final int FLAG_NEXT_APPOINT = Constant.Profile.FLAG_NEXT_APPOINT;
	private Button mButton_Back;
	private Button mButton_Call;
	private Button mButton_Email;
	private Button mButton_Save;
	private Context mContext;
	private EditText mEditText_Business;
	private EditText mEditText_Email;
	private EditText mEditText_Name;
	private EditText mEditText_Phone;
	private LinearLayout mLinear_Date;
	private LinearLayout mLinear_Goals;
	private LinearLayout mLinear_NextAppoint;
	private TextView mTextView_Date;
	private TextView mTextView_Goals;
	private TextView mTextView_NextAppoint;
	private TextView mTextView_Title;
	private Button mButton_NextAppointmentDate;
	private Button mButton_NextAppointmentTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LocalLog.i(TAG, "onCreate");
		setContentView(R.layout.profile2);
		mContext = this;
		initViews();
	}

	private void initViews() {
		this.mEditText_Business = ((EditText) findViewById(R.id.edit_business));
		this.mEditText_Name = ((EditText) findViewById(R.id.edit_name));
		this.mEditText_Phone = ((EditText) findViewById(R.id.edit_phone));
		this.mEditText_Email = ((EditText) findViewById(R.id.edit_email));
		this.mLinear_NextAppoint = ((LinearLayout) findViewById(R.id.linear_next_appoint));
		this.mLinear_Goals = ((LinearLayout) findViewById(R.id.linear_goals));
		this.mTextView_NextAppoint = ((TextView) findViewById(R.id.tv_next_appoint));
		this.mTextView_Goals = ((TextView) findViewById(R.id.tv_goals));
		this.mLinear_Date = ((LinearLayout) findViewById(R.id.linear_date));
		this.mTextView_Date = ((TextView) findViewById(R.id.tv_date));
		this.mButton_Call = ((Button) findViewById(R.id.btn_call));
		this.mButton_Email = ((Button) findViewById(R.id.btn_email));
		this.mButton_Save = ((Button) findViewById(R.id.head_btn_save));
		this.mButton_Back = ((Button) findViewById(R.id.head_btn_back));
		this.mButton_Back.setVisibility(4);
		this.mTextView_Title = ((TextView) findViewById(R.id.head_txt_title));
		this.mTextView_Title.setVisibility(4);
		mButton_NextAppointmentDate = (Button) findViewById(R.id.btn_next_appoint_date);
		mButton_NextAppointmentTime = (Button) findViewById(R.id.btn_next_appoint_time);
		
		
		this.mButton_Call.setOnClickListener(this);
		this.mButton_Email.setOnClickListener(this);
		//TODO 
		this.mLinear_Date.setOnClickListener(this);
//		this.mLinear_NextAppoint.setOnClickListener(this);
		this.mLinear_Goals.setOnClickListener(this);
		this.mButton_Save.setOnClickListener(this);
		mButton_NextAppointmentDate.setOnClickListener(this);
		mButton_NextAppointmentTime.setOnClickListener(this);
		initShows();
	}

	private void initShows() {
		String str1 = PreferencesTool.get(this.mContext, PROFILE_NAME,
				KEY_BUSINESS_NAME);
		if (str1.length() > 0)
			this.mEditText_Business.setText(str1);
		String str2 = PreferencesTool
				.get(this.mContext, PROFILE_NAME, KEY_NAME);
		if (str2.length() > 0)
			this.mEditText_Name.setText(str2);
		String str3 = PreferencesTool.get(this.mContext, PROFILE_NAME,
				KEY_PHONE_NUMBER);
		if (str3.length() > 0)
			this.mEditText_Phone.setText(str3);
		String str4 = PreferencesTool.get(this.mContext, PROFILE_NAME,
				KEY_EMAIL);
		if (str4.length() > 0)
			this.mEditText_Email.setText(str4);
		String str5 = PreferencesTool.get(this.mContext, PROFILE_NAME,
				KEY_NEXT_APPOINT);
		if (str5.length() > 0)
			this.mTextView_NextAppoint.setText(str5);
		String str6 = PreferencesTool.get(this.mContext, PROFILE_NAME,
				KEY_GOALS);
		if (str6.length() > 0)
			this.mTextView_Goals.setText(str6);
		String str7 = PreferencesTool.get(this.mContext, PROFILE_NAME, KEY_DUE);
		if (str7.length() > 0)
			this.mTextView_Date.setText(str7);
		
		String str8 = PreferencesTool.get(this.mContext, PROFILE_NAME, KEY_FIXED_NEXT_APPOINT);
		if (str8.length() > 0){
			int i = str8.indexOf(DATE_AND_TIME_SEPARATOR);
			mButton_NextAppointmentDate.setText(str8.substring(0,i));
			mButton_NextAppointmentTime.setText(str8.substring(i + 1, str8.length()));
		}else{
			Date current = new Date();
			String str = DateFormat.format(DATE_TIME_PATTERN, current).toString() ;
			int i = str.indexOf(DATE_AND_TIME_SEPARATOR);
			mButton_NextAppointmentDate.setText(str.substring(0,i));
			mButton_NextAppointmentTime.setText(str.substring(i + 1));
		}
	}

	private void save() {
		String str1 = this.mEditText_Business.getText().toString().trim();
		PreferencesTool.save(this.mContext, PROFILE_NAME, KEY_BUSINESS_NAME,
				str1);
		String str2 = this.mEditText_Name.getText().toString().trim();
		PreferencesTool.save(this.mContext, PROFILE_NAME, KEY_NAME, str2);
		String str3 = this.mEditText_Phone.getText().toString().trim();
		PreferencesTool.save(this.mContext, PROFILE_NAME, KEY_PHONE_NUMBER,
				str3);
		String str4 = this.mEditText_Email.getText().toString().trim();
		PreferencesTool.save(this.mContext, PROFILE_NAME, KEY_EMAIL, str4);
		String str5 = this.mTextView_NextAppoint.getText().toString().trim();
		PreferencesTool.save(this.mContext, PROFILE_NAME, KEY_NEXT_APPOINT,
				str5);
		String str6 = this.mTextView_Goals.getText().toString().trim();
		PreferencesTool.save(this.mContext, PROFILE_NAME, KEY_GOALS, str6);
		String str7 = this.mTextView_Date.getText().toString().trim();
		PreferencesTool.save(this.mContext, PROFILE_NAME, KEY_DUE, str7);
		String str8 = this.mButton_NextAppointmentDate.getText().toString() + DATE_AND_TIME_SEPARATOR +this.mButton_NextAppointmentTime.getText().toString();
		PreferencesTool.save(mContext, PROFILE_NAME, KEY_FIXED_NEXT_APPOINT, str8);
		

		Toast.makeText(this.mContext, R.string.save_successfully, 1).show();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		hideKeyboard();
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		imm.hideSoftInputFromWindow(mEditText_Business.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mEditText_Name.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mEditText_Phone.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mEditText_Email.getWindowToken(), 0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == mButton_Save) {
			save();
		} else if (arg0 == mButton_Call) {
			String str2 = this.mEditText_Phone.getText().toString().trim();
			PhoneAndEmailTool.phone(this.mContext, str2);
		} else if (arg0 == mButton_Email) {
			String str1 = this.mEditText_Email.getText().toString().trim();
			PhoneAndEmailTool.email(this.mContext, str1);
		} else if (arg0 == mLinear_Date) {
			startActivityForResult(FLAG_DUE);
		} else if (arg0 == mLinear_Goals) {
			startActivityForResult(FLAG_GOALS);
		} else if (arg0 == mLinear_NextAppoint) {
			startActivityForResult(FLAG_NEXT_APPOINT);
		} else if (arg0 == mButton_NextAppointmentDate){
			showDatePicker(mButton_NextAppointmentDate.getText().toString(),FLAG_NEXT_APPOINT);
		} else if(arg0 == mButton_NextAppointmentTime){
			showTimePicker(mButton_NextAppointmentTime.getText().toString(),FLAG_NEXT_APPOINT);
		}
	}
	
	private void showDatePicker(String originalDateStr,final int flag){
		Date originalDate = null;
		try {
			originalDate = new SimpleDateFormat(DATE_PATTERN).parse(originalDateStr);
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
		
		DatePickerDialog dialog = new DatePickerDialog(mContext,new DatePickerDialog.OnDateSetListener(){

			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				Date date = new Date();
				date.setDate(arg3);
				date.setMonth(arg2);
				date.setYear(arg1 - 1900);
				String str = DateFormat.format(DATE_PATTERN, date).toString();
				if(flag == FLAG_NEXT_APPOINT){
					mButton_NextAppointmentDate.setText(str);
				}
				
				
			}},year
			,month
			,day);
		
		dialog.show();
		
	}
	
	private void showTimePicker(String originalTimeStr,final  int flag){
		Date originalDate = null;
		try {
			originalDate = new SimpleDateFormat(TIME_PATTERN).parse(originalTimeStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(originalDate == null){
			originalDate = new Date();
		}
		int hour = originalDate.getHours();
		int minute = originalDate.getMinutes();
		
		TimePickerDialog dialog = new TimePickerDialog(mContext, new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Date date = new Date();
				date.setHours(hourOfDay);
				date.setMinutes(minute);
				String str = DateFormat.format(TIME_PATTERN, date).toString();
				if(flag == FLAG_NEXT_APPOINT){
					mButton_NextAppointmentTime.setText(str);
				}
			}
		}, hour, minute, false);
		
		dialog.show();
	}

	private void startActivityForResult(int flag) {
		Class<?> targetClass = null;
		Intent i = new Intent();
		switch (flag) {
		case FLAG_GOALS:
			i.putExtra(KEY_GOALS, mTextView_Goals.getText().toString().trim());
			targetClass = GoalsActivity.class;
			break;
		case FLAG_DUE:
			i.putExtra(KEY_DUE, mTextView_Date.getText().toString().trim());
			targetClass = NextAppointActivity.class;
			break;
		case FLAG_NEXT_APPOINT:
			i.putExtra(KEY_NEXT_APPOINT, mTextView_NextAppoint.getText()
					.toString().trim());
			targetClass = NextAppointActivity.class;
			break;
		default:
			break;
		}
		i.putExtra(KEY_FLAG, flag);
		i.setClass(mContext, targetClass);
		startActivityForResult(i, flag);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			String result = "";
			TextView tv = null;
			switch (requestCode) {
			case FLAG_DUE:
				result = data.getStringExtra(KEY_DUE);
				tv = mTextView_Date;
				break;
			case FLAG_GOALS:
				result = data.getStringExtra(KEY_GOALS);
				tv = mTextView_Goals;
				break;
			case FLAG_NEXT_APPOINT:
				result = data.getStringExtra(KEY_NEXT_APPOINT);
				tv = mTextView_NextAppoint;
				break;
			default:
				break;
			}
			if (tv != null) {
				tv.setText(result);
			}

		}

	}
}
