package com.cashman.physio.v1.android.alarm.util;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogTool {

	public static final void showPositive(Context context, String title,String message,DialogInterface.OnClickListener positive){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		if(positive == null){
			positive = new DefaultListener();
		}
		builder.setPositiveButton(R.string.ok, positive);
		builder.create().show();
	}
	
	public static final void showPositiveAndNegative(Context context, String title,String message,DialogInterface.OnClickListener positive,DialogInterface.OnClickListener negative){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		if(positive == null){
			positive = new DefaultListener();
		}
		builder.setPositiveButton(R.string.yes, positive);
		
		if(negative == null){
			negative = new DefaultListener();
		}
		builder.setNegativeButton(R.string.no, positive);
		builder.create().show();
	}
	
	public static class DefaultListener implements DialogInterface.OnClickListener{

		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
		
	}
}
