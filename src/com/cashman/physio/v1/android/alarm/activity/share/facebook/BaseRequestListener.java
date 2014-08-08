package com.cashman.physio.v1.android.alarm.activity.share.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.util.Log;

import com.cashman.physio.v1.android.facebook.FacebookError;
import com.cashman.physio.v1.android.facebook.AsyncFacebookRunner.RequestListener;


/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 * 
 */
public abstract class BaseRequestListener implements RequestListener {

	@Override
	public void onFacebookError(final FacebookError e, final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	@Override
	public void onFileNotFoundException(final FileNotFoundException e,
			final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	@Override
	public void onIOException(final IOException e, final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	@Override
	public void onMalformedURLException(final MalformedURLException e,
			final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

}
