package uclm.esi.cardroid.data;

/**
 * \interface operations interface for the implementation of an Place object
 */
public interface IPlace {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param placeObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public IPlace newInstance(IPlace placeObject);

	/**
	 * @param name The new name for this IPlace
	 */
	public void setName(String name);

	/**
	 * @return The name for this IPlace
	 */
	public String getName();

	/**
	 * @param coords The new coordinates for this IPlace
	 */
	public void setCoordinates(ILatLng coords);

	/**
	 * @return The coordinates for this IPlace
	 */
	public ILatLng getCoordinates();

	/**
	 * @param description The new description for this IPlace
	 */
	public void setDescription(String description);

	/**
	 * @return The description for this IPlace
	 */
	public String getDescription();

	/**
	 * @return Whether this IPlace has a description
	 */
	public boolean hasDescription();

	/**
	 * @param bitmapObject The new snapshot picture for this IPlace, in the  
	 * 						form of a Blob, which might need to be read by 
	 * 						means of a binary stream
	 */
	public void setSnapshot(java.sql.Blob bitmapObject);

	/**
	 * @return The snapshot picture for this IPlace, in the form of a Blob, 
	 * 			which might need to be read by means of a binary stream
	 */
	public java.sql.Blob getSnapshot();

	/**
	 * @return Whether this IPlace has a snapshot picture
	 */
	public boolean hasSnapshot();
}
