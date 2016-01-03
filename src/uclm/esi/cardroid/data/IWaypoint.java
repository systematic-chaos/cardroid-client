package uclm.esi.cardroid.data;

/**
 * \interface IWaypoint
 * Public operations interface for the implementation of a IWaypoint object
 */
public interface IWaypoint extends IPlace {
	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param waypointObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public IWaypoint newInstance(IWaypoint waypointObject);

	/**
	 * @param nOrder The new order of this IWaypoint in the waypoint
	 * 					collection that contains it
	 */
	public void setNOrder(int nOrder);

	/**
	 * @return The order of this IWaypoint in the waypoint collection that 
	 * 			contains it
	 */
	public int getNOrder();

	/**
	 * @param placeWaypoint The new place this IWaypoint refers to
	 */
	public void setPlaceWaypoint(IPlace placeWaypoint);

	/**
	 * @return The place this IWaypoint refers to
	 */
	public IPlace getPlaceWaypoint();
}
