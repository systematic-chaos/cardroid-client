package uclm.esi.cardroid.data.ice;

import java.sql.Blob;
import java.sql.SQLException;

import uclm.esi.cardroid.data.IBitmap;
import uclm.esi.cardroid.data.zerocice.BitmapTyp;
import uclm.esi.cardroid.util.SerialBlob;

/**
 * \class Bitmap
 * Domain class implementing a Bitmap for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class Bitmap extends BitmapTyp implements IBitmap {

	private static final long serialVersionUID = -54799676768736432L;

	public Bitmap() {
	}

	/**
	 * Default constructor
	 */
	public Bitmap(byte[] bitmapBitmap,
			uclm.esi.cardroid.data.zerocice.CompressFormat bitmapCompressFormat,
			uclm.esi.cardroid.data.zerocice.Config bitmapConfig, int density) {
		super(bitmapBitmap, bitmapCompressFormat, bitmapConfig, density);
	}

	public Bitmap(BitmapTyp bitmap) {
		this(bitmap.bitmapBitmap, bitmap.bitmapCompressFormat,
				bitmap.bitmapConfig, bitmap.density);
	}

	/* IBitmap interface */

	public Bitmap newInstance(IBitmap bitmapObject) {
		if (bitmapObject == null)
			return null;
		if (bitmapObject instanceof BitmapTyp)
			return (Bitmap) bitmapObject;
		byte[] bitmap = null;
		try {
			bitmap = bitmapObject.getBitmap().getBytes(1,
					(int) bitmapObject.getBitmap().length());
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
		}
		uclm.esi.cardroid.data.zerocice.CompressFormat compressFormat = uclm.esi.cardroid.data.zerocice.CompressFormat
				.valueOf(bitmapObject.getCompressFormat().name());
		uclm.esi.cardroid.data.zerocice.Config config = uclm.esi.cardroid.data.zerocice.Config
				.valueOf(bitmapObject.getConfig().name());
		int density = bitmapObject.getDensity();
		return new Bitmap(bitmap, compressFormat, config, density);
	}

	public void setBitmap(Blob bitmap) {
		try {
			bitmapBitmap = bitmap.getBytes(1, (int) bitmap.length());
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
		}
	}

	public Blob getBitmap() {
		Blob bitmap = null;
		try {
			bitmap = new SerialBlob(bitmapBitmap);
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
		}
		return bitmap;
	}

	public void setCompressFormat(CompressFormat compressFormat) {
		this.bitmapCompressFormat = uclm.esi.cardroid.data.zerocice.CompressFormat
				.valueOf(compressFormat.name());
	}

	public CompressFormat getCompressFormat() {
		return uclm.esi.cardroid.data.IBitmap.CompressFormat
				.valueOf(bitmapCompressFormat.name());
	}

	public void setConfig(Config config) {
		this.bitmapConfig = uclm.esi.cardroid.data.zerocice.Config
				.valueOf(config.name());
	}

	public Config getConfig() {
		return uclm.esi.cardroid.data.IBitmap.Config.valueOf(bitmapConfig
				.name());
	}

	public void setDensity(int density) {
		this.density = density;
	}

	public int getDensity() {
		return density;
	}

}
