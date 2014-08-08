package com.cashman.physio.v1.android.alarm.activity.share.text;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.share.CONST;
import com.cashman.physio.v1.android.alarm.util.AlertDialogTool;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.cashman.physio.v1.android.alarm.util.PhoneAndEmailTool;
import com.cashman.physio.v1.android.alarm.util.PreferencesTool;


public class TextShareActivity extends Activity {
	private Button mButton_Back;
	private Button mButton_Send;
	private TextView mTextView_Title;
	private EditText mEditText_Message;
	private EditText mEditText_PhoneNumber;
	
	private static final String TAG = "TextShareActivity";

	private void initViews() {
		this.mEditText_PhoneNumber = ((EditText) findViewById(R.id.edit_number));
		this.mEditText_Message = ((EditText) findViewById(R.id.edit_message));
		this.mButton_Send = ((Button) findViewById(R.id.head_btn_save));
		this.mButton_Send.setText(R.string.send);
		this.mTextView_Title = (TextView) findViewById(R.id.head_txt_title);
		mTextView_Title.setVisibility(View.INVISIBLE);
		this.mButton_Back = ((Button) findViewById(R.id.head_btn_back));
		this.mButton_Back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				TextShareActivity.this.finish();
			}
		});
		this.mButton_Send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				String number = TextShareActivity.this.mEditText_PhoneNumber
						.getText().toString().trim();
				String message = TextShareActivity.this.mEditText_Message
						.getText().toString().trim();
				String str2 = PreferencesTool.get(TextShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_TEXT_SHARE_INFO);
				if(str2 == null || str2.length() <= 0){
					str2 = getString(R.string.text_share_info);
					PreferencesTool.save(TextShareActivity.this, CONST.SHARE_TEXT_PREFERENCES_NAME, CONST.KEY_TEXT_SHARE_INFO,str2);
				}
				if(message == null || message.length() <= 0){
					message =  str2;
				}else{
					message += "."+ str2;
				}
				
				LocalLog.i(TAG,"### message = "+message);
						
				try {
					PhoneAndEmailTool.message(TextShareActivity.this, number,
							message);
					return;
				} catch (Exception localException) {
					Resources localResources = TextShareActivity.this
							.getResources();
					String titleAlert = localResources.getString(R.string.send_fail);
					
					String messageAlert = localResources.getString(R.string.send_fail_message,
							number);
					AlertDialogTool.showPositive(TextShareActivity.this,
							titleAlert, messageAlert, null);
				}
			}
		});
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.text_share);
		initViews();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onStart() {
		super.onStart();
	}
}