package com.cashman.physio.v1.android.alarm.activity.exercise;

import java.io.File;

import com.cashman.physio.v1.android.alarm.R;

import android.media.MediaMetadataEditor;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.accounts.OnAccountsUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.VideoView;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class PlayVedioActivity extends Activity {

	String vediopath="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_vedio);
		vediopath=getIntent().getExtras().getString("vediopath");
		Log.e("path", "vediopath "+vediopath);
		final VideoView vv_show=(VideoView) findViewById(R.id.vv_show);
	Button btn_ok=(Button) findViewById(R.id.btn_ok);
	vv_show
	.setVideoPath(vediopath);
	vv_show.start();
	btn_ok.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	});
		
	}
	

}
