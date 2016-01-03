package uclm.esi.cardroid.data;

/**
 * \interface IDateTimePrefs
 * Public operations interface for the implementation of a DateTimePrefs object
 */
public interface IDateTimePrefs extends IDate {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param dateTimePrefsObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public IDateTimePrefs newInstance(IDateTimePrefs dateTimePrefsObject);

	/**
	 * @param toleranceDays The new tolerance days in the date for this
	 * 							IDateTimePrefs
	 */
	public void setToleranceDays(int toleranceDays);

	/**
	 * @return The tolerance days in the date for this IDateTimePrefs
	 */
	public int getToleranceDays();

	/**
	 * @param timePreferences The new time preferences in the date for this 
	 * 							IDateTimePrefs
	 */
	public void setTimePreferences(TimePreferences timePreferences);

	/**
	 * @return The time preferences in the date for this IDateTimePrefs
	 */
	public TimePreferences getTimePreferences();

	/**
	 * \enum TimePreferences
	 * enum type depicting the values available to
	 * express time preferences
	 */
	public static enum TimePreferences {
		ANY, MORNING, AFTERNOON, NIGHT;
	}
}
