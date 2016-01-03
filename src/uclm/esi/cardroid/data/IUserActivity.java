package uclm.esi.cardroid.data;

import java.sql.Timestamp;

/**
 * \interface IUserActivity
 * Public operations interface for the implementation of an UserActivity object
 */
public interface IUserActivity {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param userActivityObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public IUserActivity newInstance(IUserActivity userActivityObject);

	/**
	 * @param activityUser The new user for this IUserActivity
	 */
	public void setUser(IUser activityUser);

	/**
	 * @return The user for this IUserActivity
	 */
	public IUser getUser();

	/**
	 * @param activityTrip The new trip for this IUserActivity
	 */
	public void setTrip(ITripOffer activityTrip);

	/**
	 * @return The trip for this IUserActivity
	 */
	public ITripOffer getTrip();

	/**
	 * @param activityType The new type for this IUserActivity
	 */
	public void setType(ActivityType activityType);

	/**
	 * @return The type for this IUserActivity
	 */
	public ActivityType getType();

	/**
	 * @return The timestamp for this IUserActivity
	 */
	public Timestamp getTimeStamp();
	
	/**
	 * @param timeStamp The new timestamp for this IUserActivity
	 */
	public void setTimeStamp(Timestamp timeStamp);

	/**
	 * \enum ActivityType
	 * enum type depicting the different types of activity
	 * an user can carry out
	 */
	public static enum ActivityType {
		TRIPJOIN, TRIPREQUESTANSWERED, TRIPACCEPT, TRIPREFUSE;
	}
}
