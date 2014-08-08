package com.cashman.physio.v1.android.alarm.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Alarm;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlarmStartTimeAdapter extends BaseAdapter{

	private Context mContext;
	private List<Alarm> mAlarmList;
	private LayoutInflater mInflater;
	
	private static final String TAG = "AlarmStartTimeAdapter";
	
	public AlarmStartTimeAdapter(Context context,List<Alarm> alarmList){
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mAlarmList = alarmList;
		if(alarmList == null){
			this.mAlarmList = new ArrayList<Alarm>();
		}
	}
	
	@Override
	public int getCount() {
		
		return mAlarmList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mAlarmList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public boolean remove(int position){
		if(position == 0){
			LocalLog.i(TAG,"remove", "the delete position = "+position+",that is not permission");
			return false;
		}
		mAlarmList.remove(position);
		return  true;
	}
	
	public void setStartTime(int position,String startTime){
		mAlarmList.get(position).setStartTime(startTime);
	}
	
	public List<Alarm> getList(){
		return this.mAlarmList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.sub_alarm_view, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_sub_title);
			holder.tv_startTime = (TextView) convertView.findViewById(R.id.tv_sub_start_time);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == 0){
			holder.tv_title.setText(R.string.start_time_colon);
		}
		Alarm alarm = mAlarmList.get(position);
		holder.tv_startTime.setText(alarm.getStartTime());
		return convertView;
	}

	private class ViewHolder{
		TextView tv_startTime;
		TextView tv_title;
	}
}
