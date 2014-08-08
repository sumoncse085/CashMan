package com.cashman.physio.v1.android.alarm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Staff;

public class StaffExpandableAdapter extends BaseExpandableListAdapter {
	private List<String> groupList;
	private List<List<Staff>> itemList;
	private Context mContext;
	private LayoutInflater mInflater;

	public StaffExpandableAdapter(List<String> paramList,
			List<List<Staff>> paramList1, Context paramContext) {
		this.groupList = paramList;
		this.itemList = paramList1;
		this.mContext = paramContext;
		this.mInflater = LayoutInflater.from(paramContext);
	}

	public Object getChild(int paramInt1, int paramInt2) {
		return ((List) this.itemList.get(paramInt1)).get(paramInt2);
	}

	public int getChildIconId(int paramInt1, int paramInt2) {
		return ((Staff) ((List) this.itemList.get(paramInt1)).get(paramInt2))
				.getIconId();
	}

	public long getChildId(int paramInt1, int paramInt2) {
		return paramInt2;
	}

	public View getChildView(int paramInt1, int paramInt2,
			boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
		ItemViewHolder localItemViewHolder;
		if (paramView == null) {
			paramView = this.mInflater.inflate(R.layout.staff_item_layout, null);
			localItemViewHolder = new ItemViewHolder();
			localItemViewHolder.img_staff = ((ImageView) paramView
					.findViewById(R.id.img_staff));
			localItemViewHolder.tv_staff = ((TextView) paramView
					.findViewById(R.id.tv_staff));
			paramView.setTag(localItemViewHolder);
		}else{
			localItemViewHolder = (ItemViewHolder) paramView.getTag();
		}
		Staff localStaff = (Staff) ((List) this.itemList.get(paramInt1))
				.get(paramInt2);
		localItemViewHolder.img_staff.setImageResource(localStaff
				.getIconId());
		localItemViewHolder.tv_staff.setText(localStaff.getName());
		return paramView;
	}

	public int getChildrenCount(int paramInt) {
		return ((List) this.itemList.get(paramInt)).size();
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

	public View getGroupView(int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
  {
    GroupViewHolder localGroupViewHolder;
    if (paramView == null)
    {
      localGroupViewHolder = new GroupViewHolder();
      paramView = this.mInflater.inflate(R.layout.name_group_layout, null);
      localGroupViewHolder.tv_group = ((TextView)paramView.findViewById(R.id.tv_group));
      localGroupViewHolder.img = ((ImageView)paramView.findViewById(R.id.img));
      paramView.setTag(localGroupViewHolder);
      
    }else{
    	 localGroupViewHolder = (GroupViewHolder)paramView.getTag();
    }
    localGroupViewHolder.tv_group.setText((CharSequence)this.groupList.get(paramInt));
    if(paramBoolean){
    	localGroupViewHolder.img.setImageResource(R.drawable.expander_down);
    }else{
    	localGroupViewHolder.img.setImageResource(R.drawable.expander);
    }
    return paramView;
  }

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int paramInt1, int paramInt2) {
		return true;
	}

	private class GroupViewHolder {
		ImageView img;
		TextView tv_group;

		private GroupViewHolder() {
		}
	}

	private class ItemViewHolder {
		ImageView img_staff;
		TextView tv_staff;

		private ItemViewHolder() {
		}
	}
}