package uclm.esi.cardroid.data;

/**
 * \interface IPassenger
 * Public operations interface for the implementation of a Passenger object
 */
public interface IPassenger extends IUser {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param passengerObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public IPassenger newInstance(IPassenger passengerObject);

	/**
	 * @param userPassenger This IPassenger's new user
	 */
	public void setUserPassenger(IUser userPassenger);

	/**
	 * @return This IPassenger's user
	 */
	public IUser getUserPassenger();

	/**
	 * @param nSeats The new number of seats reserved by this IPassenger
	 */
	public void setNSeats(int nSeats);

	/**
	 * @return The number of seats reserved by this IPassenger
	 */
	public int getNSeats();
}
