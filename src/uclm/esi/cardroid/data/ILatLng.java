package uclm.esi.cardroid.data;

/**
 * \interface ILatLng
 * Public operations interface for the implementation of a LatLng object
 */
public interface ILatLng {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param latLngObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public ILatLng newInstance(ILatLng latLngObject);

    /**
     * @param latitude The new latitude for this ILatLng
     */
	public void setLatitude(double latitude);

    /**
     * @return The latitude for this ILatLng
     */
	public double getLatitude();

    /**
     * @param longitude The new longitude for this ILatLng
     */
	public void setLongitude(double longitude);

    /**
     * @return The longitude for this ILatLng
     */
	public double getLongitude();
}
