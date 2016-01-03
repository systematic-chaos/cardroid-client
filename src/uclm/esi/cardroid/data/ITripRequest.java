package uclm.esi.cardroid.data;

/**
 * \interface ITripRequest
 * Public operations interface for the implementation of a TripRequest object
 */
public interface ITripRequest extends ITrip {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param tripRequestObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
    public ITripRequest newInstance(ITripRequest tripRequestObject);

    /**
     * @param requester The new requester for this ITripRequest
     */
    public void setRequester(IUser requester);

    /**
     * @return The request for this ITripRequest
     */
    public IUser getRequester();

    /**
     * @param dateTimePrefsObject The new date and time preferences for this 
     * 								ITripRequest
     */
    public void setDateTime(IDateTimePrefs dateTimePrefsObject);

    /**
     * @return the date and time preferences for this ITripRequest
     */
    public IDateTimePrefs getDateTime();

    /**
     * @param dateTimePrefsObject The new return date and time preferences for 
     * 								this ITripRequest
     */
    public void setReturnDateTime(IDateTimePrefs dateTimePrefsObject);

    /**
     * @return the return date and time preferences for this ITripRequest
     */
    public IDateTimePrefs getReturnDateTime();
}
