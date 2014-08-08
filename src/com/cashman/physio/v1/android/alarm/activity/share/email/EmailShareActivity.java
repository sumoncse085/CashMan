package com.cashman.physio.v1.android.alarm.activity.share.email;

import java.io.File;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.share.CONST;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PhoneAndEmailTool;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;

public class EmailShareActivity extends Activity {
	private Button mButton_Back;
	private Button mButton_Share;
	private EditText mEditText_Caption;
	private EditText mEditText_Email;
	private EditText mEditText_Subject;
	private TextView mTextView_Title;
	
	private static final String TAG = "EmailShareActivity";

	private void checkInputAndPost() {
		String address = this.mEditText_Email.getText().toString().trim();
		if (PhoneAndEmailTool.checkEmail(address)) {
			Resources res = getResources();
			String  title = res.getString(R.string.share_email_title);
			String message = res.getString(R.string.share_message,res.getString(R.string.email));
			showAlert(title,message,CONST.FLAG_DO_SHARE);
		}else{
			showAlert(R.string.email_alert_title,R.string.email_alert_message,CONST.FLAG_FAIL);
		}

	}

	private void initViews() {
		this.mEditText_Email = ((EditText) findViewById(R.id.edit_email));
		this.mEditText_Caption = ((EditText) findViewById(R.id.edit_caption));
		this.mButton_Back = ((Button) findViewById(R.id.head_btn_back));
		this.mTextView_Title = (TextView)findViewById(R.id.head_txt_title);
		this.mTextView_Title.setVisibility(View.INVISIBLE);
		this.mButton_Share = ((Button) findViewById(R.id.head_btn_save));
		this.mEditText_Subject = ((EditText) findViewById(R.id.edit_subject));
		this.mButton_Share.setText(R.string.share);
		this.mButton_Back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				EmailShareActivity.this.finish();
			}
		});
		this.mButton_Share.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				EmailShareActivity.this.checkInputAndPost();
			}
		});
	}

	private boolean isEmailAddress(String paramString) {
		if (Pattern
				.compile(
						"[a-zA-Z0-9]([\\w-]+\\.)*[\\w-]*[a-zA-Z0-9]@[a-zA-Z0-9]+\\.([a-zA-Z0-9-]+\\.)*(?i)(com|net|cn|org|ir|it)")
				.matcher(paramString).matches())
			return true;

		return false;
	}

	private void share() {
		String subject = this.mEditText_Subject.getText().toString().trim();
		if ((subject == null) || (subject.length() <= 0))
			subject = getString(R.string.email_subject_default);
		String str2 = PreferencesTool.get(EmailShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_EMAIL_SHARE_INFO);
		if(str2 == null || str2.length() <= 0){
//			str2 = getString(R.string.email_share_info);
			str2 = "Cashman Physiotherapy Ph 04 4723698 gave me this free App, it reminds me to do my rehab exercise. Download it for free just search for ¡°Cashman Physio¡± on Google play or App store" + "\n";
str2  += "Cashman Physiotherapy is at Level 1 50 The Terrace, which can be found behind The Vector Building, 44 The Terrace, Wellington. We are open Monday to Friday with appointments available from 7.30 am to 4.45 pm." + "\n";  
str2+="We also have a satellite clinic that operates out of 52 Northland Rd Tuesday and Thursday afternoons from 2.30 to 6 pm " + "\n";
str2 +="http://www.cashmanphysio.co.nz/";
			PreferencesTool.save(EmailShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_EMAIL_SHARE_INFO,str2);
		}
		String body = this.mEditText_Caption.getText().toString().trim();
		if (body == null || body.length() <= 0)
			body = str2;
		else 
			body += "\n" + str2;
		
		
		String address = mEditText_Email.getText().toString().trim();
		Intent localIntent = new Intent("android.intent.action.SEND");
		String[] arrayOfString = new String[1];
		arrayOfString[0] = address;
		localIntent.putExtra("android.intent.extra.EMAIL", arrayOfString);
		localIntent.putExtra("android.intent.extra.SUBJECT", subject);
		localIntent.putExtra("android.intent.extra.TEXT", body);
		localIntent.putExtra("android.intent.extra.STREAM", getPhotoUri());
		localIntent.setType("application/octet-stream");
		startActivity(Intent.createChooser(localIntent, getString(R.string.email_app_title)));
	}
	
	private Uri getPhotoUri() {
//		File file = getFileStreamPath(CONST.SHARE_IMAGE_NAME);
//		Uri uri = Uri.fromFile(file);
//		LocalLog.d(TAG, "####  file.path = "+file.getAbsolutePath()+"; file.size = "+file.length());
//		if(file == null || file.length() <= 0){
//			Resources localResources = getResources();
//			String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
//					+ localResources.getResourcePackageName(R.drawable.home_page) + "/"
//					+ localResources.getResourceTypeName(R.drawable.home_page)
//					+ "/"
//					+ localResources.getResourceEntryName(R.drawable.home_page);
//
//			uri = Uri.parse(path);
//		}
		
		Resources localResources = getResources();
		String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
				+ localResources.getResourcePackageName(R.drawable.home_page) + "/"
				+ localResources.getResourceTypeName(R.drawable.home_page)
				+ "/"
				+ localResources.getResourceEntryName(R.drawable.home_page);

		Uri uri = Uri.parse(path);
		
		LocalLog.i(TAG, "uri = " + uri.toString());
		return uri;
	}
	private void showAlert(int titleId, int messageId,int flag) {
		Resources res= getResources();
		String title = res.getString(titleId);
		String message = res.getString(messageId);
		showAlert(title,message,flag);
	}
	
	private void showAlert(String title,String message,final int flag){
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setTitle(title);
		localBuilder.setMessage(message);
		localBuilder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						if(flag > 0){
							finish();
						}else if(flag == 0){
							share();
						}
					}
				});
		localBuilder.create().show();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.email_share);
		initViews();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onStart() {
		super.onStart();
	}
}