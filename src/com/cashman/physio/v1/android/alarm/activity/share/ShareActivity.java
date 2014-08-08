package com.cashman.physio.v1.android.alarm.activity.share;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.share.email.EmailShareActivity;
import com.cashman.physio.v1.android.alarm.activity.share.facebook.FacebookShareActivity;
import com.cashman.physio.v1.android.alarm.activity.share.text.TextShareActivity;
import com.cashman.physio.v1.android.alarm.activity.share.twitter.TwitterShareActivity;
import com.cashman.physio.v1.android.alarm.util.AlertDialogTool;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.NetworkTool;
import com.cashman.physio.v1.android.alarm.util.PhoneAndEmailTool;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;

public class ShareActivity extends Activity implements View.OnClickListener {
	private static final int SHARE_EMAIL = 3;
	private static final int SHARE_FACEBOOK = 1;
	private static final int SHARE_TWITTER = 2;
	private static final String TAG = "ShareActivity";
	private TextView mTextView_Email;
	private TextView mTextView_Facebook;
	private TextView mTextView_Sms;
	private TextView mTextView_Twitter;

	private void SmsShare() {
		if (((TelephonyManager) getSystemService("phone")).getSimState() == 5)
			smsMessage();
//			startActivity(new Intent(this, TextShareActivity.class));
		else
			showAlert(R.string.sim_state_title, R.string.sim_state_message);
	}
	
	private  void smsMessage(){
		String number = "";
		String message = "";
		String str2 = PreferencesTool.get(ShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_TEXT_SHARE_INFO);
		if(str2 == null || str2.length() <= 0){
			str2 = getString(R.string.text_share_info);
			PreferencesTool.save(ShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_TEXT_SHARE_INFO,str2);
		}
		if(message == null || message.length() <= 0){
			message =  str2;
		}else{
			message += "."+ str2;
		}
		
		LocalLog.i(TAG,"### message = "+message);
				
		try {
			PhoneAndEmailTool.message(ShareActivity.this, number,
					message);
			return;
		} catch (Exception localException) {
			Resources localResources = ShareActivity.this
					.getResources();
			String titleAlert = localResources.getString(R.string.send_fail);
			
			String messageAlert = localResources.getString(R.string.send_fail_message,
					number);
			AlertDialogTool.showPositive(ShareActivity.this,
					titleAlert, messageAlert, null);
		}
	}
	

	private void checkNetwork(int paramInt) {
		if (NetworkTool.isNetworkAvailable(this))
			share(paramInt);
		else
			showAlert(R.string.network_title, R.string.network_message);
	}

	private void initViews() {
		 TextView headTxt = (TextView)findViewById(R.id.head_nab_txt_title);
		  headTxt.setText("Share App");
		this.mTextView_Facebook = ((TextView) findViewById(R.id.tv_facebook));
		this.mTextView_Twitter = ((TextView) findViewById(R.id.tv_twitter));
		this.mTextView_Email = ((TextView) findViewById(R.id.tv_email));
		this.mTextView_Sms = ((TextView) findViewById(R.id.tv_sms));
		this.mTextView_Facebook.setOnClickListener(this);
		this.mTextView_Twitter.setOnClickListener(this);
		this.mTextView_Email.setOnClickListener(this);
		this.mTextView_Sms.setOnClickListener(this);
	}

	private void share(int paramInt) {
		switch (paramInt) {
		default:
		case SHARE_EMAIL:
			shareEmail();
			break;
		case SHARE_FACEBOOK:
			shareFacebook();
			break;
		case SHARE_TWITTER:
			shareTwitter();
			break;
		}
	}

	private void shareEmail() {
//		LocalLog.i("ShareActivity", "shareEmail");
//		startActivity(new Intent(this, EmailShareActivity.class));
		sendEmail();
	}

	private void sendEmail(){
	
			String subject = "";
			if ((subject == null) || (subject.length() <= 0))
				subject = getString(R.string.email_subject_default);
			String str2 = PreferencesTool.get(ShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_EMAIL_SHARE_INFO);
			if(str2 == null || str2.length() <= 0){
//				str2 = getString(R.string.email_share_info);;
				str2 = "Cashman Physiotherapy Ph 04 4723698 gave me this free App, it reminds me to do my rehab exercise. Download it for free just search for ¡°Cashman Physio¡± on Google play or App store" + "\n";
				str2  += "Cashman Physiotherapy is at Level 1 50 The Terrace, which can be found behind The Vector Building, 44 The Terrace, Wellington. We are open Monday to Friday with appointments available from 7.30 am to 4.45 pm." + "\n";  
				str2+="We also have a satellite clinic that operates out of 52 Northland Rd Tuesday and Thursday afternoons from 2.30 to 6 pm " + "\n";
				str2 +="http://www.cashmanphysio.co.nz/";
				PreferencesTool.save(ShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_EMAIL_SHARE_INFO,str2);
			}
			String body = "";
			if (body == null || body.length() <= 0)
				body = str2;
			else 
				body += "\n" + str2;
			
			
			String address = "";
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
	private void shareFacebook() {
		LocalLog.i("ShareActivity", "shareFacebook");
		startActivity(new Intent(this, FacebookShareActivity.class));
	}

	private void shareTwitter() {
		LocalLog.i("ShareActivity", "shareTwitter");
		startActivity(new Intent(this, TwitterShareActivity.class));
	}

	private void showAlert(int paramInt1, int paramInt2) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setTitle(paramInt1);
		localBuilder.setMessage(paramInt2);
		localBuilder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						paramDialogInterface.dismiss();
					}
				});
		localBuilder.create().show();
	}

	public void onClick(View paramView) {
		if (paramView == this.mTextView_Twitter) {
			checkNetwork(SHARE_TWITTER);
		} else if (paramView == this.mTextView_Facebook) {
			checkNetwork(SHARE_FACEBOOK);
		} else if (paramView == this.mTextView_Email) {
			checkNetwork(SHARE_EMAIL);
		} else if (paramView == this.mTextView_Sms) {
			SmsShare();
		}
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.share);
//		new Thread(mRun).start();
		initViews();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onStart() {
		super.onStart();

	}

	private Runnable mRun = new Runnable() {

		@Override
		public void run() {
			copyImage();
		}

		public void copyImage() {

			String imageName = CONST.SHARE_IMAGE_NAME;
			File file = getFileStreamPath(imageName);
			if (file == null || file.length() <= 0) {
				try {
					InputStream input = getResources().openRawResource(
							R.drawable.home_page);
					// getResources().
					OutputStream output = openFileOutput(imageName,
							MODE_WORLD_WRITEABLE);
					byte[] bytes = new byte[1024];
					int length = -1;
					while ((length = input.read(bytes)) != -1) {
						output.write(bytes, 0, length);
						output.flush();
					}

					output.close();
					input.close();
				} catch (Exception ex) {
					LocalLog.e(TAG, "copyImage", "exception", ex);
				}
			} else {
				LocalLog.d(TAG, "file has exists");
			}
		}
	};
}