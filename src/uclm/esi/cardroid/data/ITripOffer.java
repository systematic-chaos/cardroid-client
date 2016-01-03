package uclm.esi.cardroid.data;

/**
 * \interface ITripOffer
 * Public operations interface for the implementation of a TripOffer object
 */
public interface ITripOffer extends ITrip {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param tripOfferObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public ITripOffer newInstance(ITripOffer tripOfferObject);

	/**
	 * @param datetime The new date and time for this ITripOffer
	 */
	public void setDateTime(IDateTime datetime);

	/**
	 * @return The date and time for this ITripOffer
	 */
	public IDateTime getDateTime();

	/**
	 * @param returnDatetime The new return date and time for this ITripOffer
	 */
	public void setReturnDateTime(IDateTime returnDatetime);

	/**
	 * @return The return date and time for this ITripOffer 
	 */
	public IDateTime getReturnDateTime();

	/**
	 * @param driver The new driver for this ITripOffer
	 */
	public void setDriver(IUser driver);

	/**
	 * @return The driver for this ITripOffer
	 */
	public IUser getDriver();

	/**
	 * @param waypoints The new waypoint collection for this ITripOffer
	 */
	public void setWaypoints(IWaypoint[] waypoints);

	/**
	 * @return The waypoint collection for this ITripOffer
	 */
	public IWaypoint[] getWaypoints();

	/**
	 * @param waypoint Place to be added to this ITripOffer's
	 * 					waypoint collection
	 * @return			true
	 */
	public boolean addWaypoint(IPlace waypoint);

	/**
	 * @param pos 	The position in the waypoint collection of the place to be 
	 * 					removed
	 * @return		Whether the waypoint collection was modified
	 */
	public boolean removeWaypoint(int pos);

	/**
	 * Remove every place in this ITripOffer's waypoint collection
	 */
	public void clearWaypoints();

	/**
	 * @return The number of places in this ITripOffer's waypoint collection
	 */
	public int getNWaypoints();

	/**
	 * @param car The new car for this ITripOffer
	 */
	public void setCar(ICar car);

	/**
	 * @return The car for this ITripOffer
	 */
	public ICar getCar();

	/**
	 * @param passengers The new passenger collection for this ITripOffer
	 */
	public void setPassengers(IPassenger[] passengers);

	/**
	 * @return The passenger collection for this ITripOffer
	 */
	public IPassenger[] getPassengers();

	/**
	 * @param passenger Passenger to be added to this ITripOffer 's passenger 
	 * 						collection
	 * @return 			true
	 */
	public boolean addPassenger(IPassenger passenger);

	/**
	 * @param passenger User to be removed from this ITripOffer 's passenger 
	 * 						collection
	 * @return Whether the passenger collection was modified
	 */
	public boolean removePassenger(IUser passenger);

	/**
	 * Remove every passenger in this ITripOffer's passenger collection
	 */
	public void clearPassengers();

	/**
	 * @return The number of passengers in this ITripOffer's passenger collection
	 */
	public int getNPassengers();

	/**
	 * @param price The new price for this ITripOffer
	 */
	public void setPrice(double price);

	/**
	 * @return The price for this ITripOffer
	 */
	public double getPrice();

	/**
	 * @param allowed The new set of allowed features for this ITripOffer
	 */
	public void setAllowed(boolean[] allowed);

	/**
	 * @return The set of allowed features for this ITripOffer
	 */
	public boolean[] getAllowed();
}
