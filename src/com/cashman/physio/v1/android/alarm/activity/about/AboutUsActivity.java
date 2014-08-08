package com.cashman.physio.v1.android.alarm.activity.about;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.R.color;
import com.cashman.physio.v1.android.alarm.activity.about.TypeData.Data;
import com.cashman.physio.v1.android.alarm.data.Constant;



public class AboutUsActivity  extends Activity implements View.OnClickListener {
	private static final String TAG = "AboutActivity";
	private LinearLayout linearPhysiotherapy;
	private LinearLayout linearAccupunture;
	private LinearLayout linearWorkplacetesting;
	private TextView about_content;
	private void initViews() {
		initViewStaffProfiles();
		linearPhysiotherapy = (LinearLayout) findViewById(R.id.linear_doctor_info);
		linearAccupunture = (LinearLayout) findViewById(R.id.linear_physio_info);
		linearWorkplacetesting = (LinearLayout) findViewById(R.id.linear_sport_message);

		linearPhysiotherapy.setOnClickListener(this);
		linearAccupunture.setOnClickListener(this);
		linearWorkplacetesting.setOnClickListener(this);
		
		about_content = (TextView)findViewById(R.id.about_content);
		about_content.setText(Html.fromHtml(getResources().getString(R.string.about_us_content)));
		
		TextView head_nab_txt_title = (TextView)findViewById(R.id.head_nab_txt_title);
		head_nab_txt_title.setText("About Us");

	}

	private void initData(final int index) {
		Log.i(TAG, "-----index-------" + index);
		AboutUsActivity.this.setContentView(R.layout.about_data_info);
		Button mButton_ViewStaffProfiles = ((Button) findViewById(R.id.btn_view_staff_profile));
		mButton_ViewStaffProfiles.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramView) {
						Intent localIntent = new Intent(AboutUsActivity.this,StaffProfilesActivity.class);
						localIntent.putExtra("index", index);
						AboutUsActivity.this.startActivityForResult(localIntent, 0);
					}
				});
		Data data = TypeData.getIntance().groupData.get(index);
 
		TextView head_txt_title = (TextView)findViewById(R.id.head_txt_title);
		initBackBtn();
		head_txt_title.setText(data.name);
		head_txt_title.setVisibility(View.VISIBLE);
		TextView dataContent = (TextView) AboutUsActivity.this.findViewById(R.id.data_content);
		LinearLayout linearPersonList = (LinearLayout) AboutUsActivity.this.findViewById(R.id.linearPersonList);
 
		ImageView imageView = (ImageView)findViewById(R.id.about_group_image);
		imageView.setImageDrawable(getResources().getDrawable(data.image));
		dataContent.setText(Html.fromHtml(data.content));
		for (final TypeData.Person person : data.personList) {
			TextView tv = new TextView(AboutUsActivity.this);
			tv.setText(Html.fromHtml("<u>" + person.name + "</u>"));
			tv.setTextSize(20f);
			tv.setTextColor(color.blue);
			linearPersonList.addView(tv);
			tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
					initPersonProfile(person, index );
				}
			});
		}
		 initBackBtn();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.about_main);
			initViews();
		 
	}

	private void initPersonProfile(TypeData.Person person,final int index ) {
		AboutUsActivity.this.setContentView(R.layout.about_person_profile);
//		initBackBtn();
		AboutUsActivity.this.findViewById(R.id.head_btn_save).setVisibility(View.GONE);
		Button btnBack = (Button) AboutUsActivity.this.findViewById(R.id.head_btn_back);
		btnBack.setVisibility(View.VISIBLE);
	  if(index < 4){	
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent intent  = new Intent();
				 intent.setClass(AboutUsActivity.this, StaffProfilesActivity.class);
				 intent.putExtra("index", index);
				 AboutUsActivity.this.startActivityForResult(intent, 2);
				 
			}
		});
		} else{
			btnBack.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					 Intent intent  = new Intent();
					 intent.setClass(AboutUsActivity.this, StaffProfilesActivity.class);
					 intent.putExtra("index", index);
					 AboutUsActivity.this.startActivityForResult(intent, 2);
					 
				}
			});
		}
		TextView personName = (TextView) AboutUsActivity.this
				.findViewById(R.id.person_name);
//		TextView personRemark = (TextView) AboutUsActivity.this
//				.findViewById(R.id.person_remark);
		TextView personFirstContent = (TextView)AboutUsActivity.this.findViewById(R.id.person_frist);
		TextView personContent = (TextView) AboutUsActivity.this
				.findViewById(R.id.person_content);
//		personRemark.setText(person.remark);
		personFirstContent.setText(Html.fromHtml(person.frist_content));
		personContent.setText(Html.fromHtml(person.content));
		
		personName.setText(person.name);
		if (person.img != 0) {
			ImageView personImage = (ImageView) AboutUsActivity.this
					.findViewById(R.id.person_img);
			personImage
					.setImageDrawable(getResources().getDrawable(person.big_img));
		}
		

	}

	private void initBackBtn() {
		AboutUsActivity.this.findViewById(R.id.head_btn_save).setVisibility(View.INVISIBLE);
//		AboutUsActivity.this.findViewById(R.id.head_txt_title).setVisibility(View.GONE);
		Button btnBack = (Button) AboutUsActivity.this
				.findViewById(R.id.head_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		//btnBack.setText(Html.fromHtml("<u><< Back</u>"));
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AboutUsActivity.this.setContentView(R.layout.about_main);
				initViews();
			}
		});
	}

	private void initViewStaffProfiles() {
		Button mButton_ViewStaffProfiles = ((Button) findViewById(R.id.btn_view_staff_profile));
		mButton_ViewStaffProfiles.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramView) {
						Intent localIntent = new Intent(AboutUsActivity.this,
								StaffProfilesActivity.class);
						AboutUsActivity.this.startActivityForResult(localIntent, 0);
					}
				});
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onStart() {
		super.onStart();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 1){
			 int i = data.getIntExtra(Constant.AboutUs.KEY_GROUP_ID, 0);
	    	 int j = data.getIntExtra(Constant.AboutUs.KEY_ITEM_ID, 0);
	    	 TypeData.Person  person = TypeData.getIntance().personData.get(i).personList.get(j);
	    	 int index  = data.getIntExtra("index", 5);
	    	 System.out.println("index:" + index);
	    	 initPersonProfile(person, index); 
			 
		  }else if(resultCode == 2){
			    System.out.println("list finish");
			    setContentView(R.layout.about_main);
				initViews();
		  }else if(resultCode == 3){
			 int index = data.getIntExtra("index", 0);
			 initData(index);
		  }
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linear_doctor_info:
			initData(0);
			break;
		case R.id.linear_physio_info:
			initData(1);
			break;
		case R.id.linear_sport_message:
			initData(2);
			break;
		default:
			break;
		}

	}
}