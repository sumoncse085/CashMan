package com.cashman.physio.v1.android.alarm.activity.exercise;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InstructionSettingActivity extends Activity {

	private EditText mEditText_Instruction;
	private Button mButton_Back;
	private Button mButton_Save;
	private TextView mTextView_Title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instruction_setting);
		
		initViews();
	}
	
	private void initViews(){
		mEditText_Instruction = (EditText) findViewById(R.id.edit_alarm_instruction);
		mButton_Back = (Button) findViewById(R.id.head_btn_back);
		mButton_Save = (Button) findViewById(R.id.head_btn_save);
		mTextView_Title = (TextView) findViewById(R.id.head_txt_title);
		
		mTextView_Title.setText(R.string.alarm_instruction);
		mButton_Save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String name = mEditText_Instruction.getText().toString().trim();
				if (name.length() <= 0) {
					showInputWarningDialog(R.string.input_warning_title,
							R.string.instruction_empty_wraning);
					return;
				}
				Intent  i = InstructionSettingActivity.this.getIntent();
				
				Bundle bundle = new Bundle();
				bundle.putString(Constant.Alarm.Intent.KEY_ALARM_INSTRUCTION,name);
				i.putExtra(Constant.Alarm.Intent.BUNDLE_FOR_INSTRUCTION, bundle);
				setResult(Constant.Alarm.Intent.RESULT_CODE,i);
				finish();
		}});
		mButton_Back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent  i = InstructionSettingActivity.this.getIntent();
				setResult(Constant.Alarm.Intent.RESULT_CODE_CANCEL,i);
				finish();
			}
		});
		
		initShows();
	}
	
	private void initShows(){
		Intent intent = getIntent();
		Bundle b = intent.getBundleExtra(Constant.Alarm.Intent.BUNDLE_FOR_INSTRUCTION);
		String instruction = b.getString(Constant.Alarm.Intent.KEY_ALARM_INSTRUCTION);
		mEditText_Instruction.setText(instruction);
	}
	
	private void showInputWarningDialog(int titleId, int msgId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				InstructionSettingActivity.this);
		builder.setTitle(titleId);
		builder.setMessage(msgId);
		builder.setPositiveButton(R.string.dialog_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
		});
		builder.create().show();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			mButton_Back.performClick();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
