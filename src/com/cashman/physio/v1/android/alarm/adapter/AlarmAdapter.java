package com.cashman.physio.v1.android.alarm.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.exercise.VideoViewActivity;
import com.cashman.physio.v1.android.alarm.data.AlarmItem;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<AlarmItem> mItemList;
	private Context mContext;
	private String[] mWeekdayArr;

	private static final String TAG = "AlarmAdapter";

	public AlarmAdapter(Context context, List<AlarmItem> itemList) {
		mInflater = LayoutInflater.from(context);
		mItemList = itemList;
		if (itemList == null) {
			mItemList = new ArrayList<AlarmItem>();
		}
		mContext = context;
		mWeekdayArr = context.getResources().getStringArray(
				R.array.weekday_selection);
	}

	public int getCount() {

		return mItemList.size();
	}

	public Object getItem(int position) {

		return mItemList.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public void remove(int position) {
		mItemList.remove(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.alarmlist_item, null);
			holder = new ViewHolder();
			holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_instruction = (TextView) convertView
					.findViewById(R.id.tv_instruction);
			holder.tv_ringTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			holder.iv_thumb = (ImageView) convertView
					.findViewById(R.id.iv_video_thumb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AlarmItem item = mItemList.get(position);
		holder.tv_id.setText(String.valueOf(item.getAlarmId()));
		holder.tv_name.setText(item.getName());
		holder.tv_instruction.setText(item.getInstruction());
		holder.tv_ringTime.setText(getRingTime(item.getRingTime(),
				item.getWeekday()));
		String path = item.getThumbPath();
		if (path == null || path.length() <= 0) {
			holder.iv_thumb.setImageResource(R.drawable.video);
			
		} else {
			holder.iv_thumb.setImageURI(Uri.parse(new File(path).toString()));
		}
		
		holder.iv_thumb.setOnClickListener(new VideoImageClickListener(item.getVideoPath()));
//		if (item.isActivate()) {
			convertView
					.setBackgroundResource(R.drawable.round_item_activate_bkg);
//		} else {
//			convertView
//					.setBackgroundResource(R.drawable.round_item_unactivate_bkg);
//		}
//		if (position == 0) {
//			holder.tv_ringTime.setTextColor(mContext.getResources().getColor(
//					R.color.red));
//		}
		return convertView;
	}

	private String getRingTime(String ringTime, String weekday) {
		if (weekday == null || weekday.length() != 7) {
			LocalLog.e(TAG, "getRingTimeLong", "weekday is null: " + weekday);
			return "0111110";
		}
		String result = "";
		Date currentDate = new Date();
		long currentTime = currentDate.getTime();

		String[] split = ringTime.split(":");
		Date startDate = currentDate;
		startDate.setHours(Integer.parseInt(split[0]));
		startDate.setMinutes(Integer.parseInt(split[1]));
		long startTime = startDate.getTime();

		int currentWeekday = currentDate.getDay();
		char[] weekdaySelectedArr = weekday.toCharArray();
		int temp = currentWeekday;
		if (startTime < currentTime) {
			temp++;
		}
		if (temp <= 6) {
			while (weekdaySelectedArr[temp] != '1') {
				temp++;
				if (temp > 6) {
					break;
				}
			}
		}
		if (temp == currentWeekday) {
			result = "Today ";
		} else if (temp > currentWeekday && temp <= 6) {
			result = "this " + mWeekdayArr[temp] + " ";
		} else if (temp > 6) {
			temp = 0;
			while (weekdaySelectedArr[temp] != '1') {
				temp++;
				if (temp >= currentWeekday) {
					// result = "next "+ mWeekdayArr[temp - 1]+" ";
					break;
				}
			}
			if (temp == 0) {
				result = "this Sunday ";
			} else if (temp < currentWeekday) {
				result = "next " + mWeekdayArr[temp] + " ";
			}
		}
		if (temp - currentWeekday == 1 || temp + 7 - currentWeekday == 1) {
			result = "Tomorrow ";
		}
		result += ringTime;
		return result;
	}

	private class VideoImageClickListener implements View.OnClickListener {
		private String videoPath = "";

		public VideoImageClickListener(String arg2) {
			this.videoPath = arg2;
		}

		public void onClick(View paramView) {
			Intent localIntent = new Intent(AlarmAdapter.this.mContext,
					VideoViewActivity.class);
			localIntent.putExtra("video_path", this.videoPath);
			AlarmAdapter.this.mContext.startActivity(localIntent);
		}
	}

	private class ViewHolder {
		TextView tv_id;
		TextView tv_name;
		TextView tv_instruction;
		TextView tv_ringTime;
		ImageView iv_thumb;
	}
}
