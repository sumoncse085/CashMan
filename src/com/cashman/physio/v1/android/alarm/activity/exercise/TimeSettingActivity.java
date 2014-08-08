package com.cashman.physio.v1.android.alarm.activity.exercise;

/**
 * not in use
 */

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.widget.NumericWheelAdapter;
import com.cashman.physio.v1.android.alarm.widget.OnWheelChangedListener;
import com.cashman.physio.v1.android.alarm.widget.WheelView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeSettingActivity extends Activity {

	
	private WheelView mWheelView_Times;
	private WheelView mWheelView_Every;
	private WheelView mWheelView_Hours;
	private WheelView mWheelView_Minutes;
	private LinearLayout mLinear_Times;
	private LinearLayout mLinear_Every;
	private LinearLayout mLinear_StartTime;
	
	private Button mButton_Back;
	private Button mButton_Save;
	private TextView mTextView_Title;
	
	private int mWhich_View = TIMES_VIEW;
	
	private static final int TIMES_VIEW = 1;;
	private static final int EVERY_VIEW = 2;
	private static final int START_TIME_VIEW = 3;
	
	private static final String TAG = "TimeSettingActivity";
	
	private void initViews(){
		
		mLinear_Times = (LinearLayout) findViewById(R.id.linear_times_per_day);
		mLinear_Every = (LinearLayout) findViewById(R.id.linear_every);
		mLinear_StartTime = (LinearLayout) findViewById(R.id.linear_start_time);
		
		mWheelView_Times = (WheelView)findViewById(R.id.wheel_times_perday);
		mWheelView_Every = (WheelView) findViewById(R.id.wheel_every);
		mWheelView_Hours = (WheelView) findViewById(R.id.wheel_start_time_hour);
		mWheelView_Minutes = (WheelView) findViewById(R.id.wheel_start_time_minute);
		
		mWheelView_Times.setAdapter(new NumericWheelAdapter(0, 24));
		mWheelView_Times.setCyclic(false);
		
		mWheelView_Every.setAdapter(new NumericWheelAdapter(0, 24));
		mWheelView_Every.setLabel("hours");
		mWheelView_Every.setCyclic(false);
		
		
		mWheelView_Hours.setAdapter(new NumericWheelAdapter(0, 23));
		
		mWheelView_Hours.setCyclic(false);
		
		mWheelView_Minutes.setAdapter(new NumericWheelAdapter(0, 59));
		mWheelView_Minutes.setCyclic(false);
		
		addChangingListener(mWheelView_Every,"hour");
		
//		mTextView_Title.setText();
		mButton_Back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}
		});
		
		mButton_Save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Intent i = getIntent();
		int which = TIMES_VIEW;
		if(i.getBooleanExtra(Constant.Alarm.Intent.INTENT_FOR_START_TIME, false)){
			which = START_TIME_VIEW;
		}else if(i.getBooleanExtra(Constant.Alarm.Intent.INTENT_FOR_EVERY, false)){
			which = EVERY_VIEW;
		}
		setViewsVisibility(which);
	}
	
	private void setViewsVisibility(int which){
		mWhich_View = which;
		int titleId = R.string.times_per_day;
		switch(which){
		case TIMES_VIEW:
			mLinear_Times.setVisibility(View.VISIBLE);
			break;
		case EVERY_VIEW:
			mLinear_Every.setVisibility(View.VISIBLE);
			titleId = R.string.every;
			break;
		case START_TIME_VIEW:
			mLinear_StartTime.setVisibility(View.VISIBLE);
			titleId = R.string.start_time;
			break;
		default:
			LocalLog.e(TAG, "setViewsVisibility","which = "+which+", and this is a error value");
			break;
		}
		mTextView_Title.setText(titleId);
	}
	
	/**
	 * Adds changing listener for wheel that updates the wheel label
	 * @param wheel the wheel
	 * @param label the wheel label
	 */
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				LocalLog.i(TAG, "current = "+wheel.getCurrentItem());
//				LocalLog.i(TAG, "addChangingListener.onChanged","wheel = "+wheel.getId()+"oldValue = "+oldValue+"; newValue = "+newValue);
				
				wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_setting);
		initViews();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			mButton_Back.performClick();
			return true;
		}
		return false;
	}
}
