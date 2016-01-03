package uclm.esi.cardroid.data;

/**
 * \interface IBitmap
 * Public operations interface for the implementation of a Bitmap object
 */
public interface IBitmap {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param bitmapObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public IBitmap newInstance(IBitmap bitmapObject);

	/**
	 * @param bitmap The new bitmap data, in the form of a Blob, which
	 * 					might need to be read by means of a binary stream
	 */
	public void setBitmap(java.sql.Blob bitmap);

	/**
	 * @return The bitmap data, in the form of a Blob, which might need to be 
	 * 			read by means of a binary stream
	 */
	public java.sql.Blob getBitmap();

	/**
	 * @param compressFormat The new compress format for this IBitmap
	 */
	public void setCompressFormat(CompressFormat compressFormat);

	/**
	 * @return The compress format for this IBitmap
	 */
	public CompressFormat getCompressFormat();

	/**
	 * @param config The new color configuration for this IBitmap
	 */
	public void setConfig(Config config);

	/**
	 * @return The color configuration for this IBitmap
	 */
	public Config getConfig();

	/**
	 * @param density The new image's density for this IBitmap
	 */
	public void setDensity(int density);

	/**
	 * @return The image's density for this IBitmap
	 */
	public int getDensity();

	/**
	 * \enum CompressFormat
	 * The compress formats available for a IBitmap implementation
	 */
	public static enum CompressFormat {
		JPEG, PNG, WEBP;
	}

	/**
	 * \enum Config
	 * The color configurations available for a IBitmap implementation
	 */
	public static enum Config {
		ALPHA8, ARGB4444, ARGB8888, RGB565;
	}
}
