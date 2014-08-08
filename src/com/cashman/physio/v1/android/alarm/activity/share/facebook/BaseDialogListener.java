package com.cashman.physio.v1.android.alarm.activity.share.facebook;

import com.cashman.physio.v1.android.facebook.DialogError;
import com.cashman.physio.v1.android.facebook.FacebookError;
import com.cashman.physio.v1.android.facebook.Facebook.DialogListener;


/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 * 
 */
public abstract class BaseDialogListener implements DialogListener {

	@Override
	public void onFacebookError(final FacebookError e) {
		e.printStackTrace();
	}

	@Override
	public void onError(final DialogError e) {
		e.printStackTrace();
	}

	@Override
	public void onCancel() {
	}

}
