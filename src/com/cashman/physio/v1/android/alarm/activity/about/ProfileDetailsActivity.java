package com.cashman.physio.v1.android.alarm.activity.about;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileDetailsActivity extends Activity
{
  private Button mButton_Back;
  private Button mButton_Save;
  private ImageView mImageView_Staff;
  private TextView mTextView_Details;
  private TextView mTextView_Title;

  private void init()
  {
    Intent localIntent = getIntent();
    if (localIntent != null){
//    	 int i = localIntent.getIntExtra(Constant.AboutUs.KEY_GROUP_ID, 0);
//    	    int j = localIntent.getIntExtra(Constant.AboutUs.KEY_ITEM_ID, 0);
//    	    this.mImageView_Staff.setImageResource(Constant.AboutUs.STAFF_DRAWABLE_BIG[i][j]);
//    	    this.mTextView_Details.setText(Html.fromHtml(getString(Constant.AboutUs.STAFF_PROFILE[i][j])));
    }
  }

  private void initViews()
  {
    this.mButton_Back = ((Button)findViewById(R.id.head_btn_back));
    this.mButton_Save = ((Button)findViewById(R.id.head_btn_save));
    this.mButton_Save.setVisibility(4);
    this.mTextView_Title = ((TextView)findViewById(R.id.head_txt_title));
    this.mTextView_Title.setText(R.string.profile);
    this.mButton_Back.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        ProfileDetailsActivity.this.finish();
      }
    });
    this.mImageView_Staff = ((ImageView)findViewById(R.id.img_staff));
    this.mTextView_Details = ((TextView)findViewById(R.id.tv_detail));
    init();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.profile_detail);
    initViews();
  }

  protected void onStart()
  {
    super.onStart();
  }
}