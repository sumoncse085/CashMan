package com.cashman.physio.v1.android.alarm.activity.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;

public class GoalsActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "GoalsActivity";
	private Button mButton_Back;
	private Button mButton_Save;
	private EditText mEditText_Goals;
	private TextView mTextView_Title;

	private static final String KEY_GOALS = Constant.Profile.KEY_GOALS;

	private void initViews() {
		this.mButton_Back = ((Button) findViewById(R.id.head_btn_back));
		this.mButton_Save = ((Button) findViewById(R.id.head_btn_save));
		this.mTextView_Title = ((TextView) findViewById(R.id.head_txt_title));
		 this.mTextView_Title.setText(R.string.goals);
		this.mEditText_Goals = ((EditText) findViewById(R.id.edit_alarm_instruction));
		this.mEditText_Goals.setHint(R.string.input_goals);
		String str = getIntent().getStringExtra(KEY_GOALS);
		
		if ((str != null) && (str.length() > 0))
			this.mEditText_Goals.setText(str);
		this.mButton_Back.setOnClickListener(this);
		this.mButton_Save.setOnClickListener(this);
	}

	private void saveAndBack() {
		String str = this.mEditText_Goals.getText().toString().trim();
		if ((str == null) || (str.length() <= 0))
			str = getString(R.string.goal);
		Intent localIntent = getIntent();
		localIntent.putExtra(KEY_GOALS, str);
		setResult(Constant.Profile.RESULT_SUCCESS, localIntent);
		finish();
	}

	public void onClick(View paramView) {
		if (paramView == this.mButton_Back)
			finish();
		else if (paramView == this.mButton_Save)
			saveAndBack();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.instruction_setting);
		initViews();
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

}
