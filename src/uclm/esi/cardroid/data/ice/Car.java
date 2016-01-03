package uclm.esi.cardroid.data.ice;

import Ice.Current;
import Ice.Identity;
import Ice.ObjectFactory;
import uclm.esi.cardroid.data.ICar;
import uclm.esi.cardroid.data.zerocice.CarTyp;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.network.client.SessionController;

/**
 * \class Car
 * Domain class implementing a Car for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class Car extends CarTyp implements ICar, ObjectFactory {
	private static final long serialVersionUID = -3328992844832771026L;

	public Car() {
	}

	/**
	 * Default constructor
	 */
	public Car(String brand, String model,
			uclm.esi.cardroid.data.zerocice.Fuel carFuel,
			double consumptionPerKm, int nSeats, String color, String plate) {
		super(brand, model, carFuel, consumptionPerKm, nSeats, color, plate);
	}

	public Car(CarTyp car) {
		this(car.brand, car.model, car.carFuel, car.consumptionPerKm,
				car.nSeats, car.color, car.plate);
	}

	/**
	 *  @return An Ice Identity for this datatype category and the data provided
	 */
	public static Identity createIdentity(String plate, String ownerEmail) {
		Identity id = new Identity();
		id.category = "car";
		id.name = plate + "@" + ownerEmail;
		return id;
	}

	/**
	 * @return A proxy to an Ice Object incarnating the provided data, whose 
	 * 			servant is added to adapter 's active servant map
	 */
	public static CarTypPrx createProxy(SessionController sessionController,
			String plate, UserTypPrx owner) {
		return sessionController.getCarFromPlate(plate, owner);
	}

	/**
	 * @return A proxy to an Ice Object incarnating the provided data, whose 
	 * 			servant is added to adapter 's active servant map
	 */
	public static CarTypPrx createProxy(SessionController sessionController,
			String plate, String ownerEmail) {
		return sessionController.getCarFromPlateEmail(plate, ownerEmail);
	}

	/**
	 * @param proxy A proxy to a remote object implementing a Car
	 * @return A local Car object containing the data of the remote object
	 * 			referenced by proxy
	 */
	public static Car extractObject(CarTypPrx proxy) {
		return new Car(proxy.getBrand(), proxy.getModel(), proxy.getCarFuel(),
				proxy.getConsumptionPerKm(), proxy.getNSeats(),
				proxy.getColor(), proxy.getPlate());
	}

	/* ICar interface */

	public Car newInstance(ICar carObject) {
		if (carObject == null)
			return null;
		if (carObject instanceof Car)
			return (Car) carObject;
		String brand = carObject.getBrand();
		String model = carObject.getModel();
		uclm.esi.cardroid.data.zerocice.Fuel fuel = uclm.esi.cardroid.data.zerocice.Fuel
				.valueOf(carObject.getFuel().name());
		double consumptionPerKm = carObject.getConsumptionPerKm();
		int nSeats = carObject.getNSeats();
		String color = carObject.getColor();
		String plate = carObject.getPlate();
		return new Car(brand, model, fuel, consumptionPerKm, nSeats, color,
				plate);
	}

	public void setFuel(Fuel fuel) {
		setCarFuel(uclm.esi.cardroid.data.zerocice.Fuel.valueOf(fuel.name()));
	}

	public Fuel getFuel() {
		return ICar.Fuel.valueOf(getCarFuel().name());
	}

	/* Overriding superclass */

	@Override
	public String getBrand(Current __current) {
		return brand;
	}

	@Override
	public void setBrand(String brand, Current __current) {
		this.brand = brand;
	}

	@Override
	public String getModel(Current __current) {
		return model;
	}

	@Override
	public void setModel(String model, Current __current) {
		this.model = model;
	}

	@Override
	public uclm.esi.cardroid.data.zerocice.Fuel getCarFuel(Current __current) {
		return carFuel;
	}

	@Override
	public void setCarFuel(uclm.esi.cardroid.data.zerocice.Fuel carFuel,
			Current __current) {
		this.carFuel = carFuel;
	}

	@Override
	public double getConsumptionPerKm(Current __current) {
		return consumptionPerKm;
	}

	@Override
	public void setConsumptionPerKm(double consumptionPerKm, Current __current) {
		this.consumptionPerKm = consumptionPerKm;
	}

	@Override
	public int getNSeats(Current __current) {
		return nSeats;
	}

	@Override
	public void setNSeats(int nSeats, Current __current) {
		this.nSeats = nSeats;
	}

	@Override
	public String getColor(Current __current) {
		return color;
	}

	@Override
	public void setColor(String color, Current __current) {
		this.color = color;
	}

	@Override
	public String getPlate(Current __current) {
		return plate;
	}

	@Override
	public void setPlate(String plate, Current __current) {
		this.plate = plate;
	}

	@Override
	public String _toString(Current __current) {
		StringBuilder builder = new StringBuilder();
		builder.append(brand + " ");
		builder.append(model + " - ");
		builder.append(plate);
		return builder.toString();
	}

	/* ObjectFactory interface */

	@Override
	public Car create(String type) {
		if (type.equals(ice_staticId())) {
			return new Car();
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
