package com.cashman.physio.v1.android.alarm.push;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PushAlertActivity extends Activity implements OnClickListener {
	Button sureBtn;
	Button cancelBtn;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.alert_push);
		Intent messageinfo = getIntent();
		String message = messageinfo.getStringExtra("message");
		if (message != null && !message.equals("")) {
			TextView messageView = (TextView) findViewById(R.id.pushMessage);
			messageView.setText(message);
		}
		findViews();
	}

	private void findViews() {
		sureBtn = (Button) findViewById(R.id.pushOpen);
		cancelBtn = (Button) findViewById(R.id.pushClose);

		sureBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.pushOpen) {
			openBrowser(this, Constant.Notification.URL);
			finish();
		} else if (v.getId() == R.id.pushClose) {
			Log.i("sysout", "cancel do something");
			finish();
		}

	}

	private void openBrowser(Context ctx, String url) {
		Uri uri = Uri.parse(url);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		ctx.startActivity(it);
	}
}
