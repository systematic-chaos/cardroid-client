package uclm.esi.cardroid.data;

/**
 * \interface IDate
 * Public operations interface for the implementation of a Date object
 */
public interface IDate {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param dateObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public IDate newInstance(IDate dateObject);

    /**
     * @param time The new time in milliseconds for this date, counting from 
     * 				"the date"
     */
	public void setTimeInMillis(long time);

    /**
     * @return The time in milliseconds for this date, counting from "the date"
     */
	public long getTimeInMillis();
}
