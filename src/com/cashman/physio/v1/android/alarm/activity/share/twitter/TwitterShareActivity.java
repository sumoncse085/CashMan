package com.cashman.physio.v1.android.alarm.activity.share.twitter;

import java.io.InputStream;

import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.share.CONST;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;
import com.cashman.physio.v1.android.twitter.TwitterConnect;

public class TwitterShareActivity extends Activity {
	private static final String TAG = "TwitterShareActivity";
	private Button mButton_Back;
	private Button mButton_Login;
	private Button mButton_Share;
	private Context mContext;
	private EditText mEditText_Caption;
	private ProgressDialog mProgressDialog = null;
	private TextView mTextView_State;
	private TextView mTextView_Title;
	

	private void authorizeFail() {
		Resources localResources = getResources();
		showAlert(localResources.getString(R.string.authorize_fail_title),
				localResources.getString(R.string.authorize_fail_message),
				CONST.FLAG_FAIL);
	}

	private void changeButtonState() {
		if (TwitterConnect.isSessionValid(this.mContext)) {
			this.mButton_Login.setText("Logout");
			this.mTextView_State.setText(R.string.log_in);
			setStateText();
		} else {
			this.mButton_Login.setText("Login");
			this.mTextView_State.setText(R.string.not_log_in);
		}
	}

	private ProgressDialog getProgressDialogInstance(String paramString1,
			String paramString2) {
		ProgressDialog localProgressDialog = new ProgressDialog(this.mContext);
		localProgressDialog.setMessage(paramString2);
		return localProgressDialog;
	}

	private void initViews() {
		this.mEditText_Caption = ((EditText) findViewById(R.id.edit_caption));
		this.mButton_Back = ((Button) findViewById(R.id.head_btn_back));
		this.mButton_Share = ((Button) findViewById(R.id.head_btn_save));
		this.mTextView_Title = (TextView)findViewById(R.id.head_txt_title);
		this.mTextView_Title.setVisibility(View.INVISIBLE);
		this.mButton_Share.setText(R.string.share);
		this.mButton_Login = ((Button) findViewById(R.id.twitter_login));
		this.mTextView_State = ((TextView) findViewById(R.id.tv_twitter_status));
		this.mButton_Back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				TwitterShareActivity.this.finish();
			}
		});
		this.mButton_Share.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				Resources localResources = TwitterShareActivity.this
						.getResources();
				if (TwitterConnect
						.isSessionValid(TwitterShareActivity.this.mContext)) {
					TwitterShareActivity localTwitterShareActivity = TwitterShareActivity.this;
					String str = localResources.getString(R.string.share_twitter_title);
					String message = localResources.getString(R.string.twitter);
					localTwitterShareActivity.showAlert(
							str,
							localResources.getString(R.string.share_message, message),CONST.FLAG_DO_SHARE
							);
				} else {
					TwitterShareActivity.this.showAlert(
							localResources.getString(R.string.facebook_not_login_title),
							localResources.getString(R.string.twitter_not_login_message), CONST.FLAG_FAIL);
				}

			}
		});
		this.mButton_Login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				if (TwitterConnect.isSessionValid(TwitterShareActivity.this.mContext))
					TwitterShareActivity.this.logout();
				else
					TwitterShareActivity.this.login();
			}
		});
		changeButtonState();
	}

	private void logout() {
		PreferencesTool.clean(this.mContext,
				CONST.Twitter.TWITTER_LOGIN_PREFERENCES);
		changeButtonState();
	}

	private void share() {
		final ShareTask localShareTask = new ShareTask();
		Resources localResources = getResources();
		this.mProgressDialog = getProgressDialogInstance(
				localResources.getString(R.string.sharing),
				localResources.getString(R.string.pls_wait));
		this.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface paramDialogInterface) {
						if (!localShareTask.isCancelled())
							localShareTask.cancel(true);
					}
				});
		this.mProgressDialog.show();
		localShareTask.execute();
	}

	private void showAlert(String paramString1, String paramString2,
			final int flag) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		localBuilder.setTitle(paramString1);
		localBuilder.setMessage(paramString2);
		localBuilder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						paramDialogInterface.dismiss();
						if (flag > 0)
							TwitterShareActivity.this.finish();
						else if(flag == 0)
							share();
					}
				});
		localBuilder.create().show();
	}

	private void showToast(String paramString) {
		Toast.makeText(this.mContext, paramString, 0).show();
	}

	public void login() {
		startActivityForResult(new Intent(this.mContext,TwitterLoginBrowserActivity.class), 1);
	}

	protected void onActivityResult(int paramInt1, int paramInt2,
			Intent paramIntent) {
		super.onActivityResult(paramInt1, paramInt2, paramIntent);
		
		if (TwitterConnect.isSessionValid(this.mContext)){
			setStateText();
		}else{
			authorizeFail();
		}
		changeButtonState();
	}
	
	private void setStateText(){
		String name = PreferencesTool.get(this.mContext,CONST.Twitter.TWITTER_LOGIN_PREFERENCES, CONST.Twitter.KEY_USER_NAME);
		if(name != null && name.length() > 0){
			this.mTextView_State.setText("Welcome "+name+" !");
		}
		
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.twitter_share);
		this.mContext = this;
		initViews();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onStart() {
		super.onStart();
	}

	private class ShareTask extends AsyncTask<String, String, Boolean> {
		private ShareTask() {
		}

		private String getTweet() {
			String str1 = TwitterShareActivity.this.mEditText_Caption.getText()
					.toString().trim();
			String str2 = PreferencesTool.get(mContext, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_TWITTER_SHARE_INFO);
			if(str2 == null || str2.length() <= 0){
				str2 = TwitterShareActivity.this.getString(R.string.twitter_share_info);
				PreferencesTool.save(mContext, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_TWITTER_SHARE_INFO,str2);
			}
			String tweet = null;
			if (str2.length() > 140)
				tweet = str2.substring(0, 139);
			else{
				int left = 139 - str2.length();
				if(str1.length() > left){
					str1 = str1.substring(0,left);
				}
				tweet = str1+ "."+ str2;
			}
			return tweet;
		}

		protected Boolean doInBackground(String[] paramArrayOfString) {
			boolean result = false;
			TwitterConnect localTwitterConnect = new TwitterConnect(
					TwitterShareActivity.this.mContext);
			ConfigurationBuilder localConfigurationBuilder = new ConfigurationBuilder();
			localConfigurationBuilder
					.setOAuthConsumerKey(CONST.Twitter.CONSUMER_KEY);
			localConfigurationBuilder
					.setOAuthConsumerSecret(CONST.Twitter.CONSUMER_SECRET);
			localConfigurationBuilder.setOAuthAccessToken(PreferencesTool.get(
					TwitterShareActivity.this.mContext,
					CONST.Twitter.TWITTER_LOGIN_PREFERENCES, CONST.Twitter.KEY_ACCESS_TOKEN));
			localConfigurationBuilder
					.setOAuthAccessTokenSecret(PreferencesTool.get(
							TwitterShareActivity.this.mContext,
							CONST.Twitter.TWITTER_LOGIN_PREFERENCES, CONST.Twitter.KEY_ACCESS_TOKEN_SECRET));
			TwitterConnect.twitter = new TwitterFactory(
					localConfigurationBuilder.build()).getInstance();
			try {
				TwitterConnect.accessToken = TwitterConnect.twitter
						.getOAuthAccessToken();
				InputStream localInputStream = TwitterShareActivity.this
						.getResources().openRawResource(R.drawable.home_page);
				String str = getTweet();
				LocalLog.d(TAG, "###   tweet : "+ str);
				result = localTwitterConnect.TwitterContribute(str,
						localInputStream, str);
				
				return result;
			} catch (Exception localException) {
				LocalLog.e("TwitterShareActivity", "exception in share",
						localException);
			}
			return result;
		}

		protected void onPostExecute(Boolean paramBoolean) {
			super.onPostExecute(paramBoolean);
			if (TwitterShareActivity.this.mProgressDialog.isShowing())
				TwitterShareActivity.this.mProgressDialog.dismiss();
			Resources localResources = TwitterShareActivity.this.getResources();
			if (paramBoolean.booleanValue()) {
				
				showAlert(
						localResources.getString(R.string.share_success_title),
						localResources.getString(R.string.share_twitter_success_message), CONST.FLAG_SUCCESS);
				
			}
			else{
				TwitterShareActivity.this.showAlert(
						localResources.getString(R.string.share_fail_title),
						localResources.getString(R.string.share_twitter_fail_message), CONST.FLAG_FAIL);
			}
		}
	}
}