package com.cashman.physio.v1.android.alarm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.List;

import com.cashman.physio.v1.android.alarm.R;

public class NameExpandableAdapter extends BaseExpandableListAdapter {
	private static final String TAG = "NameExpandableAdapter";
	private List<String> groupList;
	private List<List<String>> itemsList;
	private Context mContext;
	private LayoutInflater mInflater;
	private String mSelectName;

	public NameExpandableAdapter(List<String> paramList,
			List<List<String>> paramList1, Context paramContext,
			String paramString) {
		this.groupList = paramList;
		this.itemsList = paramList1;
		this.mContext = paramContext;
		this.mInflater = LayoutInflater.from(paramContext);
		this.mSelectName = paramString;
	}

	public Object getChild(int paramInt1, int paramInt2) {
		return ((List) this.itemsList.get(paramInt1)).get(paramInt2);
	}

	public long getChildId(int paramInt1, int paramInt2) {
		return paramInt2;
	}

	public View getChildView(int paramInt1, int paramInt2,
			boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
		ChildViewHolder localChildViewHolder;
		if (paramView == null) {
			localChildViewHolder = new ChildViewHolder();
			paramView = this.mInflater.inflate(R.layout.name_item_layout, null);
			localChildViewHolder.textView_Name = ((TextView) paramView
					.findViewById(R.id.tv_name));
			localChildViewHolder.radio = ((RadioButton) paramView
					.findViewById(R.id.radio));
			paramView.setTag(localChildViewHolder);

		} else {
			localChildViewHolder = (ChildViewHolder) paramView.getTag();
		}

		String str = (String) ((List) this.itemsList.get(paramInt1))
				.get(paramInt2);
		localChildViewHolder.textView_Name.setText(str);
		if (this.mSelectName.trim().equals(str.trim()))
			localChildViewHolder.radio.setChecked(true);
		else
			localChildViewHolder.radio.setChecked(false);

		return paramView;
	}

	public int getChildrenCount(int paramInt) {
		return ((List) this.itemsList.get(paramInt)).size();
	}

	public Object getGroup(int paramInt) {
		return this.groupList.get(paramInt);
	}

	public int getGroupCount() {
		return this.groupList.size();
	}

	public long getGroupId(int paramInt) {
		return paramInt;
	}

	public View getGroupView(int paramInt, boolean paramBoolean,
			View paramView, ViewGroup paramViewGroup) {
		GroupViewHolder localGroupViewHolder;
		if (paramView == null) {
			localGroupViewHolder = new GroupViewHolder();
			paramView = this.mInflater
					.inflate(R.layout.name_group_layout, null);
			localGroupViewHolder.textView_group = ((TextView) paramView
					.findViewById(R.id.tv_group));
			localGroupViewHolder.img = ((ImageView) paramView
					.findViewById(R.id.img));
			paramView.setTag(localGroupViewHolder);

		} else {
			localGroupViewHolder = (GroupViewHolder) paramView.getTag();
		}

		localGroupViewHolder.textView_group
				.setText((CharSequence) this.groupList.get(paramInt));
		if (paramBoolean)
			localGroupViewHolder.img.setImageResource(R.drawable.expander_down);
		else
			localGroupViewHolder.img.setImageResource(R.drawable.expander);
		return paramView;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int paramInt1, int paramInt2) {
		return true;
	}

	public void setSelectItem(String paramString) {
		this.mSelectName = paramString;
	}

	private class ChildViewHolder {
		RadioButton radio;
		TextView textView_Name;

		private ChildViewHolder() {
		}
	}

	private class GroupViewHolder {
		ImageView img;
		TextView textView_group;

		private GroupViewHolder() {
		}
	}
}