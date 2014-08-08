package com.cashman.physio.v1.android.alarm.activity.about;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.about.TypeData.Data;
import com.cashman.physio.v1.android.alarm.adapter.StaffExpandableAdapter;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.data.Staff;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

public class StaffProfilesActivity extends Activity
{
  private static final String TAG = "StaffProfilesActivity";
  private Button mButton_Back;
  private Button mButton_Save;
  private ExpandableListView mExpandleListView_Profiles;
  private List<String> mGroupList;
  private List<List<Staff>> mStaffList;
  private TextView mTextView_Title;

  private void initListView()
  {
    this.mGroupList = new ArrayList<String>();
    this.mStaffList = new ArrayList<List<Staff>>();
    List<Data> dataList = TypeData.getIntance().personData;
    for(TypeData.Data data : dataList){
    	this.mGroupList.add(data.name);
    	 ArrayList<Staff> localArrayList = new ArrayList<Staff>();
    	for(TypeData.Person person : data.personList){
    		Staff temp = new Staff(person.name, person.img);
    		localArrayList.add(temp);
    	}
    	 this.mStaffList.add(localArrayList);
    }
//    add(arrayOfString1[0], arrayOfString2, arrayOfInt1);
//    add(arrayOfString1[1], arrayOfString3, arrayOfInt2);
//    add(arrayOfString1[2], arrayOfString4, arrayOfInt3);
//    add(arrayOfString1[3], arrayOfString5, arrayOfInt4);
    
//   print();
  }
  
  private void initViews()
  {
    this.mButton_Back = ((Button)findViewById(R.id.head_btn_back));
    this.mButton_Save = ((Button)findViewById(R.id.head_btn_save));
    this.mButton_Save.setVisibility(4);
    this.mTextView_Title = ((TextView)findViewById(R.id.head_txt_title));
    this.mTextView_Title.setText(R.string.staff_list);
    this.mButton_Back.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
    	 if(index ==5){
    		 setResult(2);
       	     finish(); 
    	 }else{
    		 Intent intent = new Intent();
    		 intent.putExtra("index", index);
    		 setResult(3, intent);
    		 finish();
    	 }
    	  
      }
    });
    this.mExpandleListView_Profiles = ((ExpandableListView)findViewById(R.id.lv_profile_list));
    this.mExpandleListView_Profiles.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
    {
      public boolean onChildClick(ExpandableListView paramExpandableListView, View paramView, int paramInt1, int paramInt2, long paramLong)
      {
        Intent localIntent = new Intent();
         
        localIntent.putExtra(Constant.AboutUs.KEY_GROUP_ID, paramInt1);
        localIntent.putExtra(Constant.AboutUs.KEY_ITEM_ID, paramInt2);
        localIntent.putExtra("index", index);
//        Intent data = new  
          setResult(1, localIntent);
          finish();
       // StaffProfilesActivity.this.startActivity(localIntent);
        return true;
      }
    });
    initListView();
  }

  private void setAdapter()
  {
    ExpandableListView localExpandableListView = this.mExpandleListView_Profiles;
    StaffExpandableAdapter localStaffExpandableAdapter = new StaffExpandableAdapter(this.mGroupList, this.mStaffList, this);
    localExpandableListView.setAdapter(localStaffExpandableAdapter);
    int size = localStaffExpandableAdapter.getGroupCount();
    for (int j = 0; j < size ; ++j)
    {
    	 localExpandableListView.expandGroup(j);
    }
  }

  private int index ;
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.staff_profile_list);
    index  = getIntent().getIntExtra("index", 5);
    System.out.println("index:" + index );
    initViews();
  }

  protected void onStart()
  {
    super.onStart();
    setAdapter();
  }
}