package uclm.esi.cardroid.data.android;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import uclm.esi.cardroid.data.ICar;
import uclm.esi.cardroid.data.IDateTime;
import uclm.esi.cardroid.data.IPassenger;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.ITripOffer;
import uclm.esi.cardroid.data.IUser;
import uclm.esi.cardroid.data.IWaypoint;

/**
 * \class TripOffer
 * Persistence class that represents a trip offer data
 */
public class TripOffer extends Trip implements ITripOffer {

	private User driver;
	private List<Waypoint> waypoints;
	private Car car;
	private List<Passenger> passengers;
	private double price;
	private boolean[] allowed;

	/**
	 * Simple constructor.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date and time
	 * @param driver
	 *            The trip's driver user
	 * @param car
	 *            The car to be used for the trip
	 * @param availableSeats
	 *            The number of seats offered
	 * @param price
	 *            The price the trip will cost to each of its participants
	 * @param allow
	 *            The trip features to be allowed
	 */
	public TripOffer(Place from, Place to, DateTime datetime, User driver,
			Car car, int availableSeats, double price, boolean[] allow) {
		this(-1, from, to, datetime, driver, car, availableSeats, price, allow);
	}

	public TripOffer(int tripId, Place from, Place to, DateTime datetime,
			User driver, Car car, int availableSeats, double price,
			boolean[] allow) {
		super(tripId, from, to, datetime, availableSeats, TRIP_OFFER_TYPE);
		this.driver = driver;
		this.car = car;
		this.price = price;
		this.allowed = allow;
		waypoints = new ArrayList<Waypoint>();
		passengers = new ArrayList<Passenger>(availableSeats);
	}

	/**
	 * Constructor for a two-way trip.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date and time
	 * @param returnDatetime
	 *            The trip's return date and time
	 * @param driver
	 *            The trip's driver user
	 * @param car
	 *            The car to be used for the trip
	 * @param availableSeats
	 *            The number of seats offered
	 * @param price
	 *            The price the trip will cost to each of its participants
	 * @param allow
	 *            The trip features to be allowed
	 */
	public TripOffer(Place from, Place to, DateTime datetime,
			DateTime returnDatetime, User driver, Car car, int availableSeats,
			double price, boolean[] allow) {
		this(-1, from, to, datetime, returnDatetime, driver, car,
				availableSeats, price, allow);
	}

	public TripOffer(int tripId, Place from, Place to, DateTime datetime,
			DateTime returnDatetime, User driver, Car car, int availableSeats,
			double price, boolean[] allow) {
		super(tripId, from, to, datetime, returnDatetime, availableSeats,
				TRIP_OFFER_TYPE);
		this.driver = driver;
		this.car = car;
		this.price = price;
		this.allowed = allow;
		waypoints = new ArrayList<Waypoint>();
		passengers = new ArrayList<Passenger>(availableSeats);
	}

	/**
	 * Constructor for a periodic trip.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date and time
	 * @param driver
	 *            The trip's driver user
	 * @param car
	 *            The car to be used for the trip
	 * @param availableSeats
	 *            The number of seats offered
	 * @param price
	 *            The price the trip will cost to each of its participants
	 * @param allow
	 *            The trip features to be allowed
	 * @param weekDays
	 *            Those days of the week on which the trip will take place
	 * @param periodicity
	 *            Those weeks on which the trip will take place
	 */
	public TripOffer(Place from, Place to, DateTime datetime, User driver,
			Car car, int availableSeats, double price, boolean[] allow,
			char[] weekDays, Periodicity periodicity) {
		this(-1, from, to, datetime, driver, car, availableSeats, price, allow,
				weekDays, periodicity);
	}

	public TripOffer(int tripId, Place from, Place to, DateTime datetime,
			User driver, Car car, int availableSeats, double price,
			boolean[] allow, char[] weekDays, Periodicity periodicity) {
		super(tripId, from, to, datetime, availableSeats, TRIP_OFFER_TYPE,
				weekDays, periodicity);
		this.driver = driver;
		this.car = car;
		this.price = price;
		this.allowed = allow;
		waypoints = new ArrayList<Waypoint>();
		passengers = new ArrayList<Passenger>(availableSeats);
	}

	/**
	 * Constructor for a two-way periodic trip.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date and time
	 * @param returnDatetime
	 *            The trip's return date and time
	 * @param driver
	 *            The trip's driver user
	 * @param car
	 *            The car to be used for the trip
	 * @param availableSeats
	 *            The number of seats offered
	 * @param price
	 *            The price the trip will cost to each of its participants
	 * @param allow
	 *            The trip features to be allowed
	 * @param weekDays
	 *            Those days of the week on which the trip will take place
	 * @param periodicity
	 *            Those weeks on which the trip will take place
	 */
	public TripOffer(Place from, Place to, DateTime datetime,
			DateTime returnDatetime, User driver, Car car, int availableSeats,
			double price, boolean[] allow, char[] weekDays,
			Periodicity periodicity) {
		this(-1, from, to, datetime, returnDatetime, driver, car,
				availableSeats, price, allow, weekDays, periodicity);
	}

	public TripOffer(int tripId, Place from, Place to, DateTime datetime,
			DateTime returnDatetime, User driver, Car car, int availableSeats,
			double price, boolean[] allow, char[] weekDays,
			Periodicity periodicity) {
		super(tripId, from, to, datetime, returnDatetime, availableSeats,
				TRIP_OFFER_TYPE, weekDays, periodicity);
		this.driver = driver;
		this.car = car;
		this.price = price;
		this.allowed = allow;
		waypoints = new ArrayList<Waypoint>();
		passengers = new ArrayList<Passenger>(availableSeats);
	}

	public TripOffer(Place from, Place to, DateTime datetime,
			DateTime returnDatetime, int availableSeats, char[] weekDays,
			Periodicity periodicity, User driver, Car car, double price,
			boolean[] allow, int distance, String characteristics,
			List<Waypoint> waypoints, List<Passenger> passengers) {
		this(-1, from, to, datetime, returnDatetime, availableSeats, weekDays,
				periodicity, driver, car, price, allow, distance,
				characteristics, waypoints, passengers);
	}

	public TripOffer(int tripId, Place from, Place to, DateTime datetime,
			DateTime returnDatetime, int availableSeats, char[] weekDays,
			Periodicity periodicity, User driver, Car car, double price,
			boolean[] allow, int distance, String characteristics,
			List<Waypoint> waypoints, List<Passenger> passengers) {
		this(tripId, from, to, datetime, returnDatetime, driver, car,
				availableSeats, price, allow, weekDays, periodicity);
		this.distance = distance;
		this.characteristics = characteristics;
		this.waypoints = waypoints;
		this.passengers = passengers;
	}

	public TripOffer() {
	}

	public TripOffer newInstance(ITripOffer tripOfferObject) {
		if (tripOfferObject == null)
			return null;
		if (tripOfferObject instanceof Trip)
			return (TripOffer) tripOfferObject;

		TripOffer tripOffer = null;
		Place fromPlace = new Place().newInstance(tripOfferObject
				.getFromPlace());
		Place toPlace = new Place().newInstance(tripOfferObject.getToPlace());
		DateTime tripDatetime = new DateTime()
				.newInstance((IDateTime) tripOfferObject.getDateTime());
		DateTime tripReturnDatetime = tripOfferObject.hasReturnDateTime() ? new DateTime()
				.newInstance((IDateTime) tripOfferObject.getReturnDateTime())
				: null;
		User driverUser = new User().newInstance(tripOfferObject.getDriver());
		Car tripCar = new Car().newInstance(tripOfferObject.getCar());

		if (!tripOfferObject.hasReturnDateTime()
				&& !tripOfferObject.hasWeekDaysPeriodicity()
				&& !tripOfferObject.hasDistance()
				&& tripOfferObject.hasCharacteristics()) {
			tripOffer = new TripOffer(tripOfferObject.getTripId(), fromPlace,
					toPlace, tripDatetime, driverUser, tripCar,
					tripOfferObject.getNSeats(), tripOfferObject.getPrice(),
					tripOfferObject.getAllowed());
			return tripOffer;
		}
		if (tripOfferObject.hasReturnDateTime()
				&& !tripOfferObject.hasWeekDaysPeriodicity()
				&& !tripOfferObject.hasDistance()
				&& !tripOfferObject.hasCharacteristics()) {
			tripOffer = new TripOffer(tripOfferObject.getTripId(), fromPlace,
					toPlace, tripDatetime, tripReturnDatetime, driverUser,
					tripCar, tripOfferObject.getNSeats(),
					tripOfferObject.getPrice(), tripOfferObject.getAllowed());
			return tripOffer;
		}
		if (!tripOfferObject.hasReturnDateTime()
				&& tripOfferObject.hasWeekDaysPeriodicity()
				&& !tripOfferObject.hasDistance()
				&& !tripOfferObject.hasCharacteristics()) {
			tripOffer = new TripOffer(tripOfferObject.getTripId(), fromPlace,
					toPlace, tripDatetime, driverUser, tripCar,
					tripOfferObject.getNSeats(), tripOfferObject.getPrice(),
					tripOfferObject.getAllowed(),
					tripOfferObject.getWeekDays(),
					tripOfferObject.getPeriodicity());
			return tripOffer;
		}
		if (tripOfferObject.hasReturnDateTime()
				&& tripOfferObject.hasWeekDaysPeriodicity()
				&& !tripOfferObject.hasDistance()
				&& !tripOfferObject.hasCharacteristics()) {
			tripOffer = new TripOffer(tripOfferObject.getTripId(), fromPlace,
					toPlace, tripDatetime, tripReturnDatetime, driverUser,
					tripCar, tripOfferObject.getNSeats(),
					tripOfferObject.getPrice(), tripOfferObject.getAllowed(),
					tripOfferObject.getWeekDays(),
					tripOfferObject.getPeriodicity());
			return tripOffer;
		}
		IWaypoint[] wp = tripOfferObject.getWaypoints();
		List<Waypoint> waypoints = new ArrayList<Waypoint>(wp.length);
		for (IWaypoint waypoint : wp)
			waypoints.add(new Waypoint().newInstance(waypoint));
		IPassenger[] p = tripOfferObject.getPassengers();
		List<Passenger> passengers = new ArrayList<Passenger>(p.length);
		for (IPassenger passenger : p)
			passengers.add(new Passenger().newInstance(passenger));
		tripOffer = new TripOffer(tripOfferObject.getTripId(), fromPlace,
				toPlace, tripDatetime, tripReturnDatetime,
				tripOfferObject.getNSeats(), tripOfferObject.getWeekDays(),
				tripOfferObject.getPeriodicity(), driverUser, tripCar,
				tripOfferObject.getPrice(), tripOfferObject.getAllowed(),
				tripOfferObject.getDistance(),
				tripOfferObject.getCharacteristics(), waypoints, passengers);
		return tripOffer;
	}

	/**
	 * @return The trip's date and time
	 */
	@Override
	public DateTime getDateTime() {
		return (DateTime) datetime;
	}

	/**
	 * @param datetimeObject
	 *            The trip's new date and time
	 */
	public void setDateTime(IDateTime datetimeObject) {
		this.datetime = new DateTime().newInstance(datetimeObject);
	}

	/**
	 * @param datetime
	 *            The trip's return date and time
	 */
	@Override
	public DateTime getReturnDateTime() {
		return hasReturnDateTime() ? (DateTime) returnDatetime : null;
	}

	/**
	 * @param datetime
	 *            The trip's return new date and time
	 */
	public void setReturnDateTime(IDateTime datetimeObject) {
		this.returnDatetime = new DateTime().newInstance(datetimeObject);
	}

	public boolean hasReturnDateTime() {
		return returnDatetime != null;
	}

	/**
	 * @return The trip's driver user
	 */
	public User getDriver() {
		return driver;
	}

	/**
	 * @param driver
	 *            The trip's new driver user
	 */
	public void setDriver(User driver) {
		this.driver = driver;
	}

	/**
	 * @param driver
	 *            The trip's new driver user
	 */
	public void setDriver(IUser driver) {
		driver = new User().newInstance(driver);
	}

	/**
	 * @return The car to be used for the trip
	 */
	public Car getCar() {
		return car;
	}

	/**
	 * @param car
	 *            The new car to be used for the trip
	 */
	public void setCar(Car car) {
		this.car = car;
	}

	/**
	 * @param car
	 *            The new car to be used for the trip
	 */
	public void setCar(ICar car) {
		this.car = new Car().newInstance(car);
	}

	/**
	 * @return A boolean array containing whether each trip feature is or not
	 *         allowed
	 */
	public boolean[] getAllowed() {
		return allowed;
	}

	/**
	 * @param allowed
	 *            A boolean array containing wheter each trip feature is or not
	 *            allowed
	 */
	public void setAllowed(boolean[] allowed) {
		this.allowed = allowed;
	}

	/**
	 * @return The waypoints on the trip's route
	 */
	public List<Waypoint> getWaypointsList() {
		return waypoints;
	}

	public Waypoint[] getWaypoints() {
		return waypoints.toArray(new Waypoint[0]);
	}

	public void setWaypoints(IWaypoint[] waypoints) {
		Waypoint[] wp = new Waypoint[waypoints.length];
		for (int n = 0; n < waypoints.length; n++) {
			wp[n] = new Waypoint().newInstance(waypoints[n]);
		}
		this.waypoints = new ArrayList<Waypoint>(Arrays.asList(wp));
	}

	/**
	 * @param waypoint
	 *            A new waypoint to be added to the trip's route
	 * @return true
	 */
	public boolean addWaypoint(Waypoint waypoint) {
		return waypoints.add(waypoint);
	}

	/**
	 * @param waypoint
	 *            A new waypoint to be added to the trip's route
	 * @return true
	 */
	public boolean addWaypoint(IWaypoint waypoint) {
		return addWaypoint(new Waypoint().newInstance(waypoint));
	}

	public boolean addWaypoint(IPlace waypoint) {
		return addWaypoint(new Waypoint(new Place().newInstance(waypoint),
				waypoints.size() + 1));
	}

	/**
	 * 
	 * @param pos
	 *            The position in the waypoint list of the waypoint to be
	 *            removed
	 * @return true if this list contained the specified element
	 */
	public boolean removeWaypoint(int pos) {
		return waypoints.remove(pos) != null;
	}

	/**
	 * 
	 * @param waypoint
	 *            A waypoint to be removed from the trip's route
	 * @return true if this list contained the specified element
	 */
	public boolean removeWaypoint(IPlace waypoint) {
		ArrayList<Place> waypointPlaces = new ArrayList<Place>(waypoints.size());
		for (Waypoint wp : waypoints)
			waypointPlaces.add(wp.getPlaceWaypoint());
		int pos = waypointPlaces.indexOf(new Place().newInstance(waypoint));
		if (pos > 0)
			return removeWaypoint(pos);
		else
			return false;
	}

	/**
	 * @return The number of intermediate waypoints on the trip's route
	 */
	public int getNWaypoints() {
		return waypoints != null ? waypoints.size() : 0;
	}

	/**
	 * Remove all the waypoints from the trip's route
	 */
	public void clearWaypoints() {
		if (waypoints != null) {
			waypoints.clear();
		}
	}

	public Passenger[] getPassengers() {
		return passengers.toArray(new Passenger[0]);
	}

	/**
	 * @return The users participating in the trip
	 */
	public List<Passenger> getPassengersList() {
		return passengers;
	}

	public void setPassengers(IPassenger[] passengers) {
		Passenger[] p = new Passenger[passengers.length];
		for (int n = 0; n < passengers.length; n++) {
			p[n] = new Passenger().newInstance(passengers[n]);
		}
		this.passengers = new ArrayList<Passenger>(Arrays.asList(p));
	}

	/**
	 * @param passenger
	 *            A new passenger to be added to the trip, if there is room for
	 *            her
	 * @return true
	 */
	public boolean addPassenger(Passenger passenger) {
		boolean add = false;
		if (nSeats >= passenger.getNSeats()) {
			if (passengers.add(passenger)) {
				nSeats -= passenger.getNSeats();
				add = true;
			}
		}
		return add;
	}

	/**
	 * @param passenger
	 *            A new passenger to be added to the trip, if there is room for
	 *            her
	 * @return true
	 */
	public boolean addPassenger(IUser passenger, int nSeats) {
		return addPassenger(new Passenger(new User().newInstance(passenger),
				nSeats));
	}

	public boolean addPassenger(IPassenger passenger) {
		return addPassenger(new Passenger().newInstance(passenger));
	}

	/**
	 * @param passenger
	 *            A passenger to be removed from the trip participants
	 * @return true if this list contained the specified element
	 */
	public boolean removePassenger(User passenger) {
		boolean remove = passengers.remove(passenger);
		if (remove) {
			nSeats++;
		}
		return remove;
	}

	/**
	 * @param passenger
	 *            A passenger to be removed from the trip participants
	 * @return true if this list contained the specified element
	 */
	public boolean removePassenger(IUser passenger) {
		return removePassenger(new User().newInstance(passenger));
	}

	/**
	 * @return The number of users participating in the trip
	 */
	public int getNPassengers() {
		return passengers != null ? passengers.size() : 0;
	}

	/**
	 * Remove all the users from the trip participants
	 */
	public void clearPassengers() {
		if (passengers != null) {
			nSeats += getNPassengers();
			passengers.clear();
		}
	}

	/**
	 * @return The number of car seats left available in the trip
	 */
	public int getAvailableSeats() {
		return nSeats;
	}

	/**
	 * @param availableSeats
	 *            The new number of car seats left available in the trip
	 */
	public void setAvailableSeats(int availableSeats) {
		this.nSeats = availableSeats;
		if (passengers == null) {
			passengers = new ArrayList<Passenger>(availableSeats);
		} else {
			((ArrayList<Passenger>) passengers).ensureCapacity(availableSeats);
		}
	}

	/**
	 * @return The price the trip will cost to each one of its participants
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            The new price the trip will cost to each one of its
	 *            participants
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof TripOffer) {
			TripOffer t = (TripOffer) o;
			/**
			 * Two trips are compared in terms of their origin and destinations
			 * places, their respective date and time, and their respective
			 * driver
			 */
			eq = from.equals(t.getFromPlace()) && to.equals(t.getToPlace())
					&& datetime.equals(t.getDateTime())
					&& driver.equals(t.getDriver());
		}
		return eq;
	}

	/**
	 * Returns a legible textual representation of the information stored in the
	 * trip offer data
	 * 
	 * @return A legible textual representation of the trip offer data
	 */
	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder(super.getDescription());
		// sb.append(driver.toString() + '\n');
		// sb.append(car.toString() + '\n');
		for (Passenger p : passengers) {
			sb.append(p.toString() + '\n');
		}
		for (Waypoint wp : waypoints) {
			sb.append(wp.getName() + '\n');
		}
		sb.append(price + '\n');
		String[] allow = { "Equipaje", "Fumar", "Animales", "Comida" };
		for (int n = 0; n < allowed.length; n++) {
			if (allowed[n]) {
				sb.append(allow[n] + '\n');
			}
		}
		return sb.toString();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeParcelable(driver, flags);
		out.writeTypedList(waypoints);
		out.writeParcelable(car, flags);
		out.writeTypedList(passengers);
		out.writeDouble(price);
		out.writeBooleanArray(allowed);
	}

	public static final Parcelable.Creator<TripOffer> CREATOR = new Parcelable.Creator<TripOffer>() {

		public TripOffer createFromParcel(Parcel in) {
			return new TripOffer(in);
		}

		public TripOffer[] newArray(int size) {
			return new TripOffer[size];
		}
	};

	protected TripOffer(Parcel in) {
		from = in.readParcelable(Place.class.getClassLoader());
		to = in.readParcelable(Place.class.getClassLoader());
		datetime = in.readParcelable(DateTime.class.getClassLoader());
		returnDatetime = in.readByte() != 0 ? (DateTime) in
				.readParcelable(DateTime.class.getClassLoader()) : null;
		nSeats = in.readInt();
		if (in.readByte() != 0) {
			weekDays = in.createCharArray();
			periodicity = Periodicity.valueOf(in.readString());
		} else {
			weekDays = null;
			periodicity = null;
		}
		in.readByte();
		distance = in.readInt();
		if (in.readByte() != 0)
			characteristics = in.readString();

		driver = in.readParcelable(User.class.getClassLoader());
		waypoints = in.createTypedArrayList(Waypoint.CREATOR);
		car = in.readParcelable(Car.class.getClassLoader());
		passengers = in.createTypedArrayList(Passenger.CREATOR);
		price = in.readDouble();
		allowed = in.createBooleanArray();
	}
}
