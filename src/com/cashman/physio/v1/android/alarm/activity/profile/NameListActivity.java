package com.cashman.physio.v1.android.alarm.activity.profile;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.about.TypeData;
import com.cashman.physio.v1.android.alarm.adapter.NameExpandableAdapter;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

public class NameListActivity extends Activity
{
  private static final String TAG = "NameListActivity";
  private Button mButton_Back;
  private Button mButton_Save;
  private EditText mEditText_Name;
  private ExpandableListView mExpandableListView;
  private List<String> mList_NameGroup;
  private List<List<String>> mList_NameItem;
  private String mSelectName;
  private TextView mTextView_Title;

  private void checkInputAndSave()
  {
    String str = this.mEditText_Name.getText().toString().trim();
    if ((str == null) || (str.length() <= 0))
      if ((this.mSelectName == null) || (this.mSelectName.length() <= 0))
    	  showAlert();
      else
    	  startActivity(this.mSelectName);
    else
    	startActivity(str);
   
  }

  private void initNameList()
  {
    this.mList_NameGroup = new ArrayList<String>();
    this.mList_NameItem = new ArrayList<List<String>>();
    
//    this.mList_NameGroup.add(paramString);
//    ArrayList<String> localArrayList = new ArrayList<String>();
//    int length = paramArrayOfString.length;
   List<TypeData.Data> listData = TypeData.getIntance().personData;
   for (TypeData.Data data : listData) {
	   this.mList_NameGroup.add(data.name);
	   List<String> temp = new ArrayList<String>();
	   for(TypeData.Person person : data.personList){
		   temp.add(person.name);
	   }
	   this.mList_NameItem.add(temp);
    }
//    for (int i = 0; i < length ; ++i)
//    {
//      localArrayList.add(paramArrayOfString[i]);
//    }
//    mList_NameItem.add(localArrayList);
    this.mSelectName = getIntent().getStringExtra(Constant.Profile.KEY_NAME);
  }

  private void initViews()
  {
    this.mExpandableListView = ((ExpandableListView)findViewById(R.id.lv_namelist));
    this.mEditText_Name = ((EditText)findViewById(R.id.edit_name));
    this.mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
    {
      public boolean onChildClick(ExpandableListView paramExpandableListView, View paramView, int paramInt1, int paramInt2, long paramLong)
      {
        NameListActivity.this.mSelectName = ((String)((List<String>)NameListActivity.this.mList_NameItem.get(paramInt1)).get(paramInt2));
        NameExpandableAdapter localNameExpandableAdapter = (NameExpandableAdapter)NameListActivity.this.mExpandableListView.getExpandableListAdapter();
        localNameExpandableAdapter.setSelectItem(NameListActivity.this.mSelectName);
        localNameExpandableAdapter.notifyDataSetChanged();
        LocalLog.i("NameListActivity", "onChildClick", "groupPosition = " + paramInt1 + ", childPosition = " + paramInt2);
        NameListActivity.this.mEditText_Name.setText(mSelectName);
        return true;
      }
    });
    this.mButton_Back = ((Button)findViewById(R.id.head_btn_back));
    this.mButton_Save = ((Button)findViewById(R.id.head_btn_save));
    this.mButton_Back.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        NameListActivity.this.finish();
      }
    });
    this.mButton_Save.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        NameListActivity.this.checkInputAndSave();
      }
    });
    this.mTextView_Title = ((TextView)findViewById(R.id.head_txt_title));
    this.mTextView_Title.setText(R.string.select_name);
    
  }

  private void setAdapter()
  {
    ExpandableListView localExpandableListView = this.mExpandableListView;
    localExpandableListView.setAdapter(new NameExpandableAdapter(this.mList_NameGroup, this.mList_NameItem, this, this.mSelectName));
    int i = localExpandableListView.getCount();
    for (int j = 0; j < i ; ++j)
    {
    	localExpandableListView.expandGroup(j);
    }
  }


  private void showAlert()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.name_alert_title);
    localBuilder.setMessage(R.string.name_alert_content);
    localBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        NameListActivity.this.finish();
      }
    });
    localBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
      }
    });
    localBuilder.create().show();
  }

  private void startActivity(String paramString)
  {
    Intent localIntent = getIntent();
    localIntent.putExtra(Constant.Profile.KEY_NAME, paramString);
    setResult(Constant.Profile.RESULT_SUCCESS, localIntent);
    finish();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.name_select_layout);
    initNameList();
    initViews();
  }

  protected void onStart()
  {
    super.onStart();
    setAdapter();
  }
}