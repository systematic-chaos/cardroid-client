package uclm.esi.cardroid.data;

/**
 * \interface ITrip
 * Public operations interface for the implementation of a Trip object
 */
public interface ITrip {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param tripObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public ITrip newInstance(ITrip tripObject);

	/**
	 * @param tripId The new identifier for this ITrip
	 */
	public void setTripId(int tripId);

	/**
	 * @return The identifier for this ITrip
	 */
	public int getTripId();

	/**
	 * @param from The new origin place for this ITrip
	 */
	public void setFromPlace(IPlace from);

	/**
	 * @return The origin place for this ITrip
	 */
	public IPlace getFromPlace();

	/**
	 * @param to The new destination place for this ITrip
	 */
	public void setToPlace(IPlace to);

	/**
	 * @return The destination place for this ITrip
	 */
	public IPlace getToPlace();

	/**
	 * @param datetime The new date and time for this ITrip
	 */
	public void setDateTime(IDate datetime);

	/**
	 * @return The date and time for this ITrip
	 */
	public IDate getDateTime();

	/**
	 * @param nSeats The new number of seats for this ITrip (offered or 
	 * 					requested, depending on its nature)
	 */
	public void setNSeats(int nSeats);

	/**
	 * @return The number of seats for this ITrip (offered or requested, 
	 * 			depending on its nature)
	 */
	public int getNSeats();

	/**
	 * @param returnDatetime The new return date and time for this ITrip
	 */
	public void setReturnDateTime(IDate returnDatetime);

	/**
	 * @return The return date and time for this ITrip
	 */
	public IDate getReturnDateTime();

	/**
	 * @return Whether this Trip has a return date and time
	 */
	public boolean hasReturnDateTime();

	/**
	 * @param weekDays 		The week days on which this ITrip will be  
	 * 							periodically performed  
	 * @param periodicity 	The weekly periodicity for this ITrip
	 */
	public void setWeekDaysPeriodicity(char[] weekDays, Periodicity periodicity);

	/**
	 * @return The week days on which this ITrip will be periodically performed
	 */
	public char[] getWeekDays();

	/**
	 * @return The weekly periodicity for this ITrip
	 */
	public Periodicity getPeriodicity();

	/**
	 * @return Whether this ITrip will be performed on a periodic fashion
	 */
	public boolean hasWeekDaysPeriodicity();

	/**
	 * @param distance The new distance covered by this ITrip, 
	 * 					expressed in kilometers
	 */
	public void setDistance(int distance);

	/**
	 * @return The distance covered by this ITrip, expressed in kilometers
	 */
	public int getDistance();

	/**
	 * @return Whether this ITrip has a value for this distance it covers
	 */
	public boolean hasDistance();

	/**
	 * @param characteristics The new characteristics for this ITrip
	 */
	public void setCharacteristics(String characteristics);

	/**
	 * @return The additional characteristics for this ITrip
	 */
	public String getCharacteristics();

	/**
	 * @return Whether this ITrip has additional characteristics
	 */
	public boolean hasCharacteristics();
	
	public void setTripType(int tripType);
	
	public int getTripType();

	/**
	 * \enum Periodicity
	 * enum type depicting the possible week values for
	 * scheduling a periodic trip
	 */
	public static enum Periodicity {
		EVERYWEEK, EVENWEEKS, ODDWEEKS;
	}
}
