package uclm.esi.cardroid.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Utilities {
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

	/**
	 * Generate a value suitable for use in {@link #setId(int)}. This value will
	 * not collide with ID values generated at build time by aapt for R.id.
	 * 
	 * @return a generated ID value
	 */
	public static int generateViewId() {
		for (;;) {
			final int result = sNextGeneratedId.get();
			// aapt-generated IDs have the high byte nonzero; clamp to the range
			// under that.
			int newValue = result + 1;
			if (newValue > 0x00FFFFFF)
				newValue = 1; // Roll over to 1, not 0.
			if (sNextGeneratedId.compareAndSet(result, newValue)) {
				return result;
			}
		}
	}

	public static Bitmap blobToBitmap(Blob blob) {
		byte[] bytes = null;
		try {
			bytes = blob.getBytes((long) 1, (int) blob.length());
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getSQLState());
		}
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	public static Blob bitmapToBlob(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] bytes = baos.toByteArray();
		Blob blob = null;
		try {
			blob = new SerialBlob(bytes);
		} catch (SerialException se) {
			Log.e("CARDROID", se.getMessage());
		} catch (SQLException sqle) {
			Log.e("CARDROID", sqle.getMessage());
		} finally {
			try {
				baos.flush();
				baos.close();
			} catch (IOException ioe) {
				Log.e("CARDROID", ioe.getMessage());
			}
		}
		return blob;
	}
}
