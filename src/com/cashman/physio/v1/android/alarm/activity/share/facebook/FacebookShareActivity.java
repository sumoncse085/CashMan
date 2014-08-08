package com.cashman.physio.v1.android.alarm.activity.share.facebook;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.share.CONST;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;
import com.cashman.physio.v1.android.facebook.AsyncFacebookRunner;
import com.cashman.physio.v1.android.facebook.Facebook;
import com.cashman.physio.v1.android.facebook.FacebookError;
import com.cashman.physio.v1.android.facebook.Util;

public class FacebookShareActivity extends Activity {

	private Button mButton_Back;
	private Button mButton_Send;
	private LoginButton mLoginButton;
	private TextView mTextView_FbStatus,mTextView_Title;
	private EditText mEditText_Comment;
	private Handler mHandler;
	private ProgressDialog mProgressDialog;

	private static final String TAG = "FacebookShareActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.facebook_share);

		if (CONST.Fackbook.APP_ID == null)
			Util.showAlert(
					this,
					"Warning",
					"Facebook Applicaton ID must be specified before running this example: see FbAPIs.java");
		else
			initViews();
	}

	private void initViews() {
		this.mButton_Back = ((Button) findViewById(R.id.head_btn_back));
		this.mButton_Send = ((Button) findViewById(R.id.head_btn_save));
		this.mButton_Send.setText(R.string.share);
		this.mTextView_Title = (TextView)findViewById(R.id.head_txt_title);
		this.mTextView_Title.setVisibility(View.INVISIBLE);
		this.mLoginButton = ((LoginButton) findViewById(R.id.login));
		this.mTextView_FbStatus = ((TextView) findViewById(R.id.tv_fb_status));
		this.mEditText_Comment = ((EditText) findViewById(R.id.edit_comment));
		this.mButton_Back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				FacebookShareActivity.this.finish();
			}
		});
		this.mButton_Send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				if (Utility.mFacebook.isSessionValid()){
					Resources res = getResources();
					String title = res.getString(R.string.share_facebook_title);
					String message = res.getString((R.string.share_message),res.getString(R.string.facebook));
					showAlert(title,message,CONST.FLAG_DO_SHARE);
				}else {
					showAlert(R.string.facebook_not_login_title, R.string.facebook_not_login_message, CONST.FLAG_FAIL);
				}

			}
		});
		this.mHandler = new Handler();
		Utility.mFacebook = new Facebook(CONST.Fackbook.APP_ID);
		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
		SessionStore.restore(Utility.mFacebook, this);
		SessionEvents.addAuthListener(new FbAuthListener());
		SessionEvents.addLogoutListener(new FbLogoutListener());
		this.mLoginButton.init(FacebookShareActivity.this,
				CONST.Fackbook.ACTIVITY_INDEPENDENT, Utility.mFacebook,
				CONST.Fackbook.PERMISSIONS);
		if (Utility.mFacebook.isSessionValid())
			requestUserData();
		else {
			this.mTextView_FbStatus.setText(R.string.not_log_in);
		}
	}

	private void showAlert(int titleId, int messageId, int flag) {
		Resources res = getResources();
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
						paramDialogInterface.dismiss();
						if (flag > 0) {
							finish();
						}else if(flag == 0){
							upload();
						}
					}
				});
		localBuilder.create().show();
	}

	private void upload() {
		Bundle localBundle = new Bundle();
		try {
			localBundle.putByteArray("photo",
					Utility.scaleImage(getApplicationContext(), getPhotoUri()));
			String str2 = PreferencesTool.get(FacebookShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_FACEBOOK_SHARE_INFO);
			if(str2 == null || str2.length() <= 0){
				str2 = getString(R.string.facebook_share_info);
				PreferencesTool.save(FacebookShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_FACEBOOK_SHARE_INFO,str2);
			}
			LocalLog.d(TAG,"###  str = "+str2);
			localBundle.putString("caption", this.mEditText_Comment.getText()
					.toString().trim()
					+ "\n" + str2);
			Resources localResources = getResources();
			this.mProgressDialog = ProgressDialog.show(this,
					localResources.getString(R.string.sharing),
					localResources.getString(R.string.pls_wait), true, true);
			Utility.mAsyncRunner.request("me/photos", localBundle, "POST",
					new PhotoUploadListener(), null);
			return;
		} catch (IOException localIOException) {
			Toast.makeText(this, "Upload Exception", CONST.FLAG_FAIL).show();
		}
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

	public void requestUserData() {
		this.mTextView_FbStatus.setText(R.string.not_log_in);
		Bundle localBundle = new Bundle();
		localBundle.putString("fields", "name, picture");
		Utility.mAsyncRunner.request("me", localBundle,
				new UserRequestListener());
	}

	public class FbAuthListener implements SessionEvents.AuthListener {
		public FbAuthListener() {
		}

		public void onAuthFail(String paramString) {
			FacebookShareActivity.this.mTextView_FbStatus
					.setText(FacebookShareActivity.this.getString(R.string.facebook_error)
							+ paramString);
		}

		public void onAuthSucceed() {
			FacebookShareActivity.this.mTextView_FbStatus.setText(R.string.log_in);
			FacebookShareActivity.this.requestUserData();
		}
	}

	public class FbLogoutListener implements SessionEvents.LogoutListener {
		public FbLogoutListener() {
		}

		public void onLogoutBegin() {
		}

		public void onLogoutFinish() {
			FacebookShareActivity.this.mTextView_FbStatus.setText(R.string.not_log_in);
		}
	}

	public class PhotoUploadListener extends BaseRequestListener {
		public PhotoUploadListener() {
		}

		public void onComplete(String paramString, Object paramObject) {
			FacebookShareActivity.this.mProgressDialog.dismiss();
			FacebookShareActivity.this.mHandler.post(new Runnable() {
				public void run() {
					FacebookShareActivity.this.showAlert(R.string.share_success_title,
							R.string.share_facebook_success_message, CONST.FLAG_SUCCESS);
				}
			});
		}

		public void onFacebookError(FacebookError paramFacebookError) {
			FacebookShareActivity.this.mProgressDialog.dismiss();
			LocalLog.e("FacebookShareActivity", "Share app error : "
					+ paramFacebookError.getMessage());
			FacebookShareActivity.this.showAlert(R.string.share_fail_title, R.string.share_facebook_fail_message, CONST.FLAG_FAIL);
		}
	}

	public class UserRequestListener extends BaseRequestListener {
		public UserRequestListener() {
		}

		public void onComplete(String paramString, Object paramObject) {
			try {
				final String user_name = new JSONObject(paramString).getString("name");
				FacebookShareActivity.this.mHandler.post(new Runnable() {
					public void run() {
						mTextView_FbStatus
								.setText("Welcome " + user_name + "!");
					}
				});
				return;
			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
				
			}
		}
		
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

}
