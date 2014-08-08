/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cashman.physio.v1.android.alarm.activity.share.facebook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.activity.share.facebook.SessionEvents.AuthListener;
import com.cashman.physio.v1.android.alarm.activity.share.facebook.SessionEvents.LogoutListener;
import com.cashman.physio.v1.android.facebook.AsyncFacebookRunner;
import com.cashman.physio.v1.android.facebook.DialogError;
import com.cashman.physio.v1.android.facebook.Facebook;
import com.cashman.physio.v1.android.facebook.FacebookError;
import com.cashman.physio.v1.android.facebook.Facebook.DialogListener;



public class LoginButton extends ImageButton {

	private com.cashman.physio.v1.android.facebook.Facebook mFb;
	private Handler mHandler;
	private final SessionListener mSessionListener = new SessionListener();
	private String[] mPermissions;
	private Activity mActivity;
	private int mActivityCode;

	public LoginButton(final Context context) {
		super(context);
	}

	public LoginButton(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public LoginButton(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(final Activity activity, final int activityCode,
			final Facebook fb) {
		init(activity, activityCode, fb, new String[] {});
	}

	public void init(final Activity activity, final int activityCode,
			final Facebook fb, final String[] permissions) {
		mActivity = activity;
		mActivityCode = activityCode;
		mFb = fb;
		mPermissions = permissions;
		mHandler = new Handler();

		setBackgroundColor(Color.TRANSPARENT);
		setImageResource(fb.isSessionValid() ? R.drawable.logout_button
				: R.drawable.login_button);
		drawableStateChanged();

		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
		setOnClickListener(new ButtonOnClickListener());
	}

	private final class ButtonOnClickListener implements OnClickListener {
		/*
		 * Source Tag: login_tag
		 */
		@Override
		public void onClick(final View arg0) {
			if (mFb.isSessionValid()) {
				SessionEvents.onLogoutBegin();
				final AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
						mFb);
				asyncRunner.logout(getContext(), new LogoutRequestListener());
			} else {
				mFb.authorize(mActivity, mPermissions, mActivityCode,
						new LoginDialogListener());
			}
		}
	}

	private final class LoginDialogListener implements DialogListener {
		@Override
		public void onComplete(final Bundle values) {
			SessionEvents.onLoginSuccess();
		}

		@Override
		public void onFacebookError(final FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onError(final DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class LogoutRequestListener extends BaseRequestListener {
		@Override
		public void onComplete(final String response, final Object state) {
			/*
			 * callback should be run in the original thread, not the background
			 * thread
			 */
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					SessionEvents.onLogoutFinish();
				}
			});
		}
	}

	private class SessionListener implements AuthListener, LogoutListener {

		@Override
		public void onAuthSucceed() {
			setImageResource(R.drawable.logout_button);
			SessionStore.save(mFb, getContext());
		}

		@Override
		public void onAuthFail(final String error) {
		}

		@Override
		public void onLogoutBegin() {
		}

		@Override
		public void onLogoutFinish() {
			SessionStore.clear(getContext());
			setImageResource(R.drawable.login_button);
		}
	}

}
