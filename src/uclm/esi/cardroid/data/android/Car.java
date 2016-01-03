package uclm.esi.cardroid.data.android;

import android.os.Parcel;
import android.os.Parcelable;
import uclm.esi.cardroid.data.ICar;

/**
 * \class Car
 * Persistence class that represents a car data
 */
public class Car implements Parcelable, ICar {

	private String brand;
	private String model;
	private Fuel fuel;
	private double consumptionPerKm;
	private int nSeats;
	private String color;
	private String plate;

	/**
	 * Default constructor.
	 */
	public Car(String brand, String model, String color, int nSeats,
			String plate, Fuel fuel, double consumptionPerKm) {
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.nSeats = nSeats;
		this.plate = plate;
		this.fuel = fuel;
		this.consumptionPerKm = consumptionPerKm;
	}

	public Car() {
	}

	public Car newInstance(ICar carObject) {
		if (carObject == null)
			return null;
		if (carObject instanceof Car)
			return (Car) carObject;

		return new Car(carObject.getBrand(), carObject.getModel(),
				carObject.getColor(), carObject.getNSeats(),
				carObject.getPlate(), carObject.getFuel(),
				carObject.getConsumptionPerKm());
	}

	/**
	 * @return The car's brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand
	 *            The car's new brand
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return The car's model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model
	 *            The car's new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return The car's number of seats
	 */
	public int getNSeats() {
		return nSeats;
	}

	/**
	 * @param nSeats
	 *            The car's new number of seats
	 */
	public void setNSeats(int nSeats) {
		this.nSeats = nSeats;
	}

	/**
	 * @return The car's color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            The car's new color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return The car's color
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @param plate
	 *            The car's new color
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

	/**
	 * @return The car's fuel type
	 */
	public Fuel getFuel() {
		return fuel;
	}

	/**
	 * @param fuel
	 *            The car's new fuel type
	 */
	public void setFuel(Fuel fuel) {
		this.fuel = fuel;
	}

	/**
	 * @return The car's consumption per kilometer rate
	 */
	public double getConsumptionPerKm() {
		return consumptionPerKm;
	}

	/**
	 * @param consumptionPerKm
	 *            The car's new consumption per kilometer rate
	 */
	public void setConsumptionPerKm(double consumptionPerKm) {
		this.consumptionPerKm = consumptionPerKm;
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof Car) {
			// / Two cars comparison is done in terms of their plates
			eq = ((Car) o).getPlate().equals(plate);
		}
		return eq;
	}

	@Override
	public String toString() {
		return brand + " " + model + "\t" + plate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(brand);
		out.writeString(model);
		out.writeInt(nSeats);
		out.writeString(color);
		out.writeString(plate);
		out.writeString(fuel.name());
		out.writeDouble(consumptionPerKm);
	}

	public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {

		public Car createFromParcel(Parcel in) {
			return new Car(in);
		}

		public Car[] newArray(int size) {
			return new Car[size];
		}
	};

	private Car(Parcel in) {
		brand = in.readString();
		model = in.readString();
		nSeats = in.readInt();
		color = in.readString();
		plate = in.readString();
		fuel = Fuel.valueOf(in.readString());
		consumptionPerKm = in.readDouble();
	}
}
