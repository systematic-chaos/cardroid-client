package uclm.esi.cardroid.data;

/**
 * \interface IUser
 * Public operations interface for the implementation of an User object
 */
public interface IUser {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param userObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public IUser newInstance(IUser userObject);

	/**
	 * @param name The new name for this IUser
	 */
	public void setName(String name);

	/**
	 * @return The name for this IUser
	 */
	public String getName();

	/**
	 * @param surname The new surname for this IUser object
	 */
	public void setSurname(String surname);

	/**
	 * @return The surname for this IUser object
	 */
	public String getSurname();

	/**
	 * @param avatar The new avatar picture for this IUser, in the form of a 
	 * 					Blob, so it might need to be read by means of a binary 
	 * 					stream
	 */
	public void setAvatar(java.sql.Blob avatar);

	/**
	 * @return Whether this IUser has an avatar picture
	 */
	public boolean hasAvatar();

	/**
	 * @return The avatar picture for this IUser, in the form of a Blob, so it 
	 * 			might need to be read by means of a binary stream
	 */
	public java.sql.Blob getAvatar();

	/**
	 * @param home The new home place for this user
	 */
	public void setHome(IPlace home);

	/**
	 * @return The home place for this user
	 */
	public IPlace getHome();

	/**
	 * @param telephone The new telephone number for this IUser
	 */
	public void setTelephoneNumber(int telephone);

	/**
	 * @return The telephone number for this IUser
	 */
	public int getTelephoneNumber();

	/**
	 * @param email The new email for this IUser
	 */
	public void setEmail(String email);

	/**
	 * @return The email for this IUser
	 */
	public String getEmail();
	
	/*
	 * @return The new reputation scale value for this IUser
	 */
	public void setReputation(int reputation);

	/**
	 * @return The reputation scale value for this IUser
	 */
	public int getReputation();

	/**
	 * Increase the reputation scale value for this IUser in one unit
	 */
	public void increaseReputation();

	/**
	 * Increase the reputation scale value for this IUser in the specified 
	 * number of units
	 * @param increase The number of units on which this IUser 's reputation
	 * 					scale value will be increased 
	 */
	public void increaseReputation(int increase);

	/**
	 * Decrease the reputation scale value for this IUser in one unit
	 */
	public void decreaseReputation();

	/**
	 * Decrease the reputation scale value for this IUser in the specified 
	 * number of units
	 * @param decrease The number of units on which this User's reputation 
	 * 					scale value wil be decreased
	 */
	public void decreaseReputation(int decrease);

	/**
	 * @return Whether this IUser has a value in the reputation scale
	 */
	public boolean hasReputation();

	/**
	 * @param cars The new car collection for this IUser
	 */
	public void setCars(ICar[] cars);

	/**
	 * @return The car collection for this IUSer
	 */
	public ICar[] getCars();

	/**
	 * @param car 	The car to be added to this IUser' s car collection
	 * @return		true
	 */
	public boolean addCar(ICar car);

	/**
	 * @param car The car to be removed from this IUser's car collection
	 * @return Whether this IUser's car collection was modified
	 */
	public boolean removeCar(ICar car);

	/**
	 * Remove every car from this IUser's car collection
	 */
	public void clearCars();

	/**
	 * @return The number of cars in this Iuser's car collection
	 */
	public int getNCars();
}
