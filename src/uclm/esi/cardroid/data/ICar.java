package uclm.esi.cardroid.data;

/**
 * \interface ICar
 * Public operations interface for the implementation of a Car object
 */
public interface ICar {

	/**
	 * Create an instance of the class implementing this interface,
	 * from the received parameter, which also implements it.
	 * This method acts as an Abstract Factory, for the sake of the 
	 * implementation of the Abstract Factory pattern in the search of
	 * interoperability among the different implementations of this interface 
	 * which could exist in the different subsystems of the platform
	 * @param carObject An object instance implementing this interface
	 * @return An instance of the class implementing this interface, containing
	 * 			exactly the same data of the received bitmapObject parameter,
	 * 			from the viewpoint of the operations defined in this interface
	 */
	public ICar newInstance(ICar carObject);

	/**
	 * @param brand The new brand for this ICar
	 */
	public void setBrand(String brand);

	/**
	 * @return The brand for this ICar
	 */
	public String getBrand();

	/**
	 * @param model The new model for this ICar
	 */
	public void setModel(String model);

	/**
	 * @return The model for this ICar
	 */
	public String getModel();

	/**
	 * @param fuel The new fuel for this ICar
	 */
	public void setFuel(Fuel fuel);

	/**
	 * @return The fuel for this ICar
	 */
	public Fuel getFuel();

	/**
	 * @param consumptionPerKm The new consumption per kilometer rate 
	 * 							for this ICar
	 */
	public void setConsumptionPerKm(double consumptionPerKm);

	/**
	 * @return The consumption per kilometer rate for this ICar
	 */
	public double getConsumptionPerKm();

	/**
	 * @param nSeats The new number of seats for this ICar
	 */
	public void setNSeats(int nSeats);

	/**
	 * @return The number of seats for this ICar
	 */
	public int getNSeats();

	/**
	 * @param color The new color for this ICar
	 */
	public void setColor(String color);

	/**
	 * @return The color for this ICar
	 */
	public String getColor();

	/**
	 * @param plate The new plate for this ICar
	 */
	public void setPlate(String plate);

	/**
	 * @return The plate for this ICar
	 */
	public String getPlate();

	/**
	 * \enum Fuel
	 * enum type depicting the different types of fuel a car can
	 * consume
	 */
	public static enum Fuel {
		UNLEADED95, UNLEADED98, DIESELA, DIESELAPLUS, BIODIESEL;
	}
}
