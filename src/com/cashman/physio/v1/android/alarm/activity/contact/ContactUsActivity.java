package com.cashman.physio.v1.android.alarm.activity.contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.MainActivity;
import com.cashman.physio.v1.android.alarm.activity.home.HomeActivity;
import com.cashman.physio.v1.android.alarm.activity.share.ShareActivity;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

public class ContactUsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		initViews();
		setTextAndSave();
	}
	
	 private static final String TAG = "ContactActivity";

	  private TextView mTextView_Phone;
	  private TextView mTextView_tvinfo;
	  private TextView mTextView_ViewMap;
	  private TextView mTextView_ViewMap2;
	  private TextView mTextView_shareApp;
	  
	  private TextView mTextView_tvinfo2;
	  private TextView mTextView_website;
	  private TextView mTextView_email;

	  private void call(String paramString)
	  {
	    startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + paramString)));
	  }

	  private String getPreferences(String paramString)
	  {
	    return getSharedPreferences(Constant.Contact.PREFERENCES_CONTACT, 0).getString(paramString, "");
	  }

	  private void initViews()
	  {
	    
	    this.mTextView_ViewMap = ((TextView)findViewById(R.id.tv_view_map));
	    this.mTextView_ViewMap2 = ((TextView)findViewById(R.id.tv_view_map2));
	    this.mTextView_Phone = ((TextView)findViewById(R.id.tv_phone));
	    this.mTextView_email = ((TextView)findViewById(R.id.tv_email));
	    this.mTextView_website = ((TextView)findViewById(R.id.tv_Website));
	    this.mTextView_tvinfo = (TextView)findViewById(R.id.contact_us_time);
	    this.mTextView_tvinfo2 = (TextView)findViewById(R.id.contact_us_time2);
	    this.mTextView_shareApp =(TextView)findViewById(R.id.shareApp);
	    TextView headTxt = (TextView)findViewById(R.id.head_nab_txt_title);
	    TextView contact_address = (TextView)findViewById(R.id.contact_address);
	    TextView contact_address_two = (TextView)findViewById(R.id.contact_address2);
	    contact_address.setText(Html.fromHtml(getResources().getString(R.string.contact_address)));
	    
	    contact_address_two.setText(Html.fromHtml(getResources().getString(R.string.contact_address_two)));
	    headTxt.setText("Contact Us");
	    mTextView_tvinfo.setText(Html.fromHtml(getResources().getString(R.string.contact_info)));
	    mTextView_tvinfo2.setText(Html.fromHtml(getResources().getString(R.string.contact_info2)));
	    this.mTextView_ViewMap.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View paramView)
	      {
	        Intent localIntent = new Intent(ContactUsActivity.this, ViewMapActivity.class);
	        localIntent.putExtra(Constant.Contact.KEY_LOCATION_LATITUDE,  "-41280528");
	        localIntent.putExtra(Constant.Contact.KEY_LOCATION_LONGITUDE, "174774917");
	        localIntent.putExtra(Constant.Contact.KEY_LOCATION_TITLE, "50 The Terrace Wellington (Behind 44 The Terrace)");
	        localIntent.putExtra(Constant.Contact.KEY_LOCATION_SNIPPET, "-41.280528,174.774917");
	        ContactUsActivity.this.startActivity(localIntent);
	      }
	    });
	    
	    this.mTextView_ViewMap2.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View paramView)
	      {
	        Intent localIntent = new Intent(ContactUsActivity.this, ViewMapActivity.class);
	        localIntent.putExtra(Constant.Contact.KEY_LOCATION_LATITUDE,  "-41283415");
	        localIntent.putExtra(Constant.Contact.KEY_LOCATION_LONGITUDE, "174757192");
	        localIntent.putExtra(Constant.Contact.KEY_LOCATION_TITLE, "52 Northland Rd  Northland");
	        localIntent.putExtra(Constant.Contact.KEY_LOCATION_SNIPPET, "-41.283415,174.757192 ");
	        ContactUsActivity.this.startActivity(localIntent);
	      }
	    });
	    
	    this.mTextView_Phone.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View paramView)
	      {
	        ContactUsActivity.this.showAlert(R.string.phone_call_title, R.string.phone_call_message, " 04 4723698");
	      }
	    });
	    
	    this.mTextView_email.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View paramView)
	      {
	  		Intent localIntent = new Intent("android.intent.action.SEND");
	  		String[] arrayOfString = new String[1];
	  		arrayOfString[0] = "info@www.cashmanphysio.co.nz";
	  		localIntent.putExtra("android.intent.extra.EMAIL", arrayOfString);
	  		localIntent.putExtra("android.intent.extra.SUBJECT", "Cashman Physiotherapy");
	  		localIntent.putExtra("android.intent.extra.TEXT", "");
	  		localIntent.setType("application/octet-stream");
	  		startActivity(Intent.createChooser(localIntent, getString(R.string.email_app_title)));
	      }
	    });
	    
	    this.mTextView_website.setOnClickListener(new View.OnClickListener()
	    {
	      public void onClick(View paramView)
	      {
	        openBrowser(ContactUsActivity.this, "http://www.cashmanphysio.co.nz");
	      }
	    });
	    
	    this.mTextView_shareApp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity parent = (MainActivity) ContactUsActivity.this.getParent();
				parent.setCurrentTabByTag(MainActivity.SHARE_TAB_TAG);
//				ContactUsActivity.this.startActivity(new Intent(ContactUsActivity.this, ShareActivity.class));
				
			}
		});
	  }
 
	  private void savePreferences(String paramString1, String paramString2)
	  {
	    SharedPreferences.Editor localEditor = getSharedPreferences(Constant.Contact.PREFERENCES_CONTACT, 0).edit();
	    localEditor.putString(paramString1, paramString2);
	    localEditor.commit();
	  }

	  private void setTextAndSave()
	  {

	    this.mTextView_Phone.setText(Html.fromHtml("Call"));

	    savePreferences(Constant.Contact.KEY_LOCATION, "contact_us");
	    savePreferences(Constant.Contact.KEY_LOCATION_LATITUDE, "-36.708743");
	    savePreferences(Constant.Contact.KEY_LOCATION_LONGITUDE, "174.727354");

	  }

	  private void showAlert(int paramInt1, int paramInt2,final String phoneNumber)
	  {
	    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
	    localBuilder.setTitle(paramInt1);
	    localBuilder.setMessage(getString(paramInt2, phoneNumber));
	    localBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
	    {
	      public void onClick(DialogInterface paramDialogInterface, int paramInt)
	      {
	    	  ContactUsActivity.this.call(phoneNumber);
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

	 

	  protected void onDestroy()
	  {
	    super.onDestroy();
	  }

	  protected void onStart()
	  {
	    super.onStart();
	  }

	  
	  private void openBrowser(Context ctx, String url) {
			Uri uri = Uri.parse(url);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			ctx.startActivity(it);
		}
}
