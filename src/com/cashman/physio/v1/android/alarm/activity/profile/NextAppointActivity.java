package com.cashman.physio.v1.android.alarm.activity.profile;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.widget.NumericWheelAdapter;
import com.cashman.physio.v1.android.alarm.widget.OnWheelChangedListener;
import com.cashman.physio.v1.android.alarm.widget.WheelView;

public class NextAppointActivity extends Activity {
	private static final String KEY_FLAG = Constant.Profile.KEY_FLAG;
	private static final int FLAG_NEXT_APPOINT = Constant.Profile.FLAG_NEXT_APPOINT;
	private static final int FLAG_DUE = Constant.Profile.FLAG_DUE;
	private static final String KEY_NEXT_APPOINT = Constant.Profile.KEY_NEXT_APPOINT;
	private static final String KEY_DUE = Constant.Profile.KEY_DUE;
	
	private static final String TAG = "NextAppointActivity";
	private Button mButton_Back;
	private Button mButton_Save;
	private TextView mTextView_OriginalTime;
	private TextView mTextView_SetTime;
	private TextView mTextView_Title;
	private WheelView mWheel_Day;
	private WheelView mWheel_Hour;
	private WheelView mWheel_Minute;
	private WheelView mWheel_Month;

	private String getTimeFromWheel() {
		int i = 1 + this.mWheel_Month.getCurrentItem();
		int j = 1 + this.mWheel_Day.getCurrentItem();
		int k = this.mWheel_Hour.getCurrentItem();
		int l = this.mWheel_Minute.getCurrentItem();
		return i + "/" + j + " " + k + ":" + l;
	}

	private void initTextView() {
		Intent localIntent = getIntent();
		String str = "";
		int flag = localIntent.getIntExtra(KEY_FLAG, -1);
		
		if (flag == FLAG_NEXT_APPOINT) {
			str = localIntent.getStringExtra(KEY_NEXT_APPOINT);
			mTextView_Title.setText(R.string.next_appoint);
		} else if (flag == FLAG_DUE) {
			str = localIntent.getStringExtra(KEY_DUE);
			mTextView_Title.setText(R.string.complete_date);
		}
		LocalLog.i(TAG, "flag = "+flag+"; str = "+str);
		if(str == null  || str.length() <= 0){
			str = DateFormat.format("M/d k:m", new Date()).toString();
		}
		this.mTextView_OriginalTime.setText(str);
		this.mTextView_SetTime.setText(getTimeFromWheel());
		
		
	}

	private void initViews(){
    this.mButton_Back = ((Button)findViewById(R.id.head_btn_back));
    this.mButton_Save = ((Button)findViewById(R.id.head_btn_save));
    this.mWheel_Month = ((WheelView)findViewById(R.id.wheel_month));
    this.mWheel_Day = ((WheelView)findViewById(R.id.wheel_day));
    this.mWheel_Hour = ((WheelView)findViewById(R.id.wheel_hour));
    this.mWheel_Minute = ((WheelView)findViewById(R.id.wheel_minute));
    this.mTextView_OriginalTime = ((TextView)findViewById(R.id.tv_original_time));
    this.mTextView_SetTime = ((TextView)findViewById(R.id.tv_set_time));
    this.mTextView_Title = (TextView)findViewById(R.id.head_txt_title);
	
    initWheelViews();
    this.mButton_Back.setOnClickListener(new View.OnClickListener(){
      public void onClick(View paramView) {
        NextAppointActivity.this.finish();
      }
    });
    this.mButton_Save.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        String str = NextAppointActivity.this.getTimeFromWheel();
        Intent localIntent = NextAppointActivity.this.getIntent();
        if (localIntent.getIntExtra(KEY_FLAG, -1) == FLAG_NEXT_APPOINT)
            localIntent.putExtra(KEY_NEXT_APPOINT, str);
        else
        	localIntent.putExtra(KEY_DUE, str);
        NextAppointActivity.this.setResult(1, localIntent);
        NextAppointActivity.this.finish();
      }
    });
    initTextView();
  }

	private void initWheelViews() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int textSize = (int) (14 * density);
		int textSizeValue = (int) (16 * density);
		Resources localResources = getResources();
		this.mWheel_Month.setAdapter(new NumericWheelAdapter(1, 12));
		this.mWheel_Month.setLabel(localResources.getString(R.string.month));
		this.mWheel_Month.setTextSize(textSize, textSizeValue);
		this.mWheel_Day.setAdapter(new NumericWheelAdapter(1, 31));
		this.mWheel_Day.setLabel(localResources.getString(R.string.day));
		this.mWheel_Day.setTextSize(textSize, textSizeValue);
		this.mWheel_Month.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView paramWheelView, int paramInt1,
					int paramInt2) {
				LocalLog.i("NextAppointActivity", "oldValue = " + paramInt1
						+ "; newValue = " + paramInt2);
				
				NumericWheelAdapter localNumericWheelAdapter = new NumericWheelAdapter(1, getMonthDays(paramInt2 + 1));
				NextAppointActivity.this.mWheel_Day.setAdapter(localNumericWheelAdapter);
				NextAppointActivity.this.mWheel_Day.setCurrentItem(0);
				NextAppointActivity.this.mTextView_SetTime.setText(getTimeFromWheel());
			}
		});
		this.mWheel_Day.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView paramWheelView, int paramInt1,
					int paramInt2) {
				mTextView_SetTime.setText(getTimeFromWheel());
			}
		});
		this.mWheel_Hour.setAdapter(new NumericWheelAdapter(0, 23));
		this.mWheel_Hour.setLabel(localResources.getString(R.string.hour));
		this.mWheel_Hour.setTextSize(textSize, textSizeValue);
		this.mWheel_Minute.setAdapter(new NumericWheelAdapter(0, 59));
		this.mWheel_Minute.setLabel(localResources.getString(R.string.minute));
		this.mWheel_Minute.setTextSize(textSize, textSizeValue);
		this.mWheel_Hour.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView paramWheelView, int paramInt1,
					int paramInt2) {
				mTextView_SetTime.setText(NextAppointActivity.this.getTimeFromWheel());
			}
		});
		this.mWheel_Minute.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView paramWheelView, int paramInt1,
					int paramInt2) {
				mTextView_SetTime.setText(getTimeFromWheel());
			}
		});
		Date localDate = new Date();
		int i = localDate.getMonth();
		int j = localDate.getDate();
		int k = localDate.getHours();
		int l = localDate.getMinutes();
		this.mWheel_Month.setCurrentItem(i);
		this.mWheel_Day.setCurrentItem(j - 1);
		this.mWheel_Hour.setCurrentItem(k);
		this.mWheel_Minute.setCurrentItem(l);
	}

	private int getMonthDays(int month) {
		if(month == 2){
			if(isLeapYear()){
				return 29;
			}else {
				return 28;
			}
		}else if(month == 4 || month == 6 || month == 9 || month == 11){
			return 30;
		}else 
			return 31;
	}
	
	boolean isLeapYear() {
		int year = new Date().getYear();
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			return true;
		else
			return false;
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.next_appoint_layout);
		initViews();
	}
}