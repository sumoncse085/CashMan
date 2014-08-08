package com.cashman.physio.v1.android.alarm.activity.share.facebook;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

import org.json.JSONObject;

import com.cashman.physio.v1.android.facebook.AsyncFacebookRunner;
import com.cashman.physio.v1.android.facebook.Facebook;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.provider.MediaStore;



public class Utility extends Application {

	public static Facebook mFacebook;
	public static AsyncFacebookRunner mAsyncRunner;
	public static JSONObject mFriendsList;
	public static String userUID = null;
	public static String objectID = null;

	public static AndroidHttpClient httpclient = null;
	public static Hashtable<String, String> currentPermissions = new Hashtable<String, String>();

	private static int MAX_IMAGE_DIMENSION = 720;
	public static final String HACK_ICON_URL = "http://www.facebookmobileweb.com/hackbook/img/facebook_icon_large.png";

	public static Bitmap getBitmap(final String url) {
		Bitmap bm = null;
		try {
			final URL aURL = new URL(url);
			final URLConnection conn = aURL.openConnection();
			conn.connect();
			final InputStream is = conn.getInputStream();
			final BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
			bis.close();
			is.close();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (httpclient != null) {
				httpclient.close();
			}
		}
		return bm;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(final InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(final long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					final int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	public static Bitmap resizeMatrix(final Bitmap BitmapOrg, int x, int y,
			final boolean flag) {

		final int origHeight = BitmapOrg.getHeight();
		final int origWidth = BitmapOrg.getWidth();
		float scaleWidth;
		float scaleHeight;
		if (origWidth >= origHeight) {
			scaleWidth = (float) x / origWidth;
			scaleHeight = scaleWidth;
		} else {
			scaleHeight = (float) y / origHeight;
			scaleWidth = scaleHeight;
		}
		x = Math.round(origWidth * scaleWidth);
		y = Math.round(origHeight * scaleWidth);
		;

		// create a matrix for the manipulation
		final Matrix matrix = new Matrix();
		// resize the Bitmap
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);

		return Bitmap.createBitmap(BitmapOrg, 0, 0, x, y, matrix, flag);

	}

	public static byte[] scaleImage(final Context context, final Uri photoUri)
			throws IOException {
		InputStream is = context.getContentResolver().openInputStream(photoUri);
		final BitmapFactory.Options dbo = new BitmapFactory.Options();
		dbo.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, dbo);
		is.close();

		int rotatedWidth, rotatedHeight;
		final int orientation = getOrientation(context, photoUri);

		if (orientation == 90 || orientation == 270) {
			rotatedWidth = dbo.outHeight;
			rotatedHeight = dbo.outWidth;
		} else {
			rotatedWidth = dbo.outWidth;
			rotatedHeight = dbo.outHeight;
		}

		Bitmap srcBitmap;
		is = context.getContentResolver().openInputStream(photoUri);
		if (rotatedWidth > MAX_IMAGE_DIMENSION
				|| rotatedHeight > MAX_IMAGE_DIMENSION) {
			final float widthRatio = (float) rotatedWidth
					/ (float) MAX_IMAGE_DIMENSION;
			final float heightRatio = (float) rotatedHeight
					/ (float) MAX_IMAGE_DIMENSION;
			final float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = (int) maxRatio;
			srcBitmap = BitmapFactory.decodeStream(is, null, options);
		} else {
			srcBitmap = BitmapFactory.decodeStream(is);
		}
		is.close();

		/*
		 * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
		if (orientation > 0) {
			final Matrix matrix = new Matrix();
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
					srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
		}

		String type = context.getContentResolver().getType(photoUri);
		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(type == null){
			type = "image/png";
		}
		if (type.equals("image/png")) {
			srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		} else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
			srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		}
		final byte[] bMapArray = baos.toByteArray();
		baos.close();
		return bMapArray;
	}

	public static int getOrientation(final Context context, final Uri photoUri) {
		/* it's on the external media. */
		final Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);
		
		if (cursor == null || cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}
}