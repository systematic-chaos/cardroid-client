package uclm.esi.cardroid.data.ice;

import java.util.ArrayList;
import java.util.List;

import Ice.Current;
import Ice.Identity;
import Ice.ObjectFactory;
import uclm.esi.cardroid.data.ICar;
import uclm.esi.cardroid.data.IDate;
import uclm.esi.cardroid.data.IDateTime;
import uclm.esi.cardroid.data.IPassenger;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.ITrip;
import uclm.esi.cardroid.data.ITripOffer;
import uclm.esi.cardroid.data.IUser;
import uclm.esi.cardroid.data.IWaypoint;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.DateTimeTyp;
import uclm.esi.cardroid.data.zerocice.DateTyp;
import uclm.esi.cardroid.data.zerocice.PassengerTyp;
import uclm.esi.cardroid.data.zerocice.PlaceTyp;
import uclm.esi.cardroid.data.zerocice.TripOfferTyp;
import uclm.esi.cardroid.data.zerocice.TripOfferTypPrx;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.data.zerocice.WaypointTyp;
import uclm.esi.cardroid.network.client.SessionController;

/**
 * \class TripOffer
 * Domain class implementing a TripOffer for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class TripOffer extends TripOfferTyp implements ITripOffer,
		ObjectFactory {
	protected static SessionController _sessionController;

	protected static final int TRIP_TYPE = 0, TRIP_OFFER_TYP = 1,
			TRIP_REQUEST_TYPE = 2;

	private static final long serialVersionUID = 6393694705313903949L;

	public TripOffer(SessionController sessionController) {
		_sessionController = sessionController;
	}

	/**
	 * Short constructor
	 */
	public TripOffer(PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTimeTyp datetime, int nSeats, UserTypPrx driver,
			List<WaypointTyp> waypoints, CarTypPrx tripOfferCar,
			List<PassengerTyp> passengers, double price, String[] allowed) {
		this(-1, fromPlace, toPlace, datetime, nSeats, driver, waypoints,
				tripOfferCar, passengers, price, allowed);
	}

	public TripOffer(PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTimeTyp datetime, int nSeats, DateTimeTyp returnDatetime,
			String[] weekDays,
			uclm.esi.cardroid.data.zerocice.Periodicity tripPeriodicity,
			int distance, String characteristics, UserTypPrx driver,
			List<WaypointTyp> waypoints, CarTypPrx tripOfferCar,
			List<PassengerTyp> passengers, double price, String[] allowed) {
		this(-1, fromPlace, toPlace, datetime, nSeats, returnDatetime,
				weekDays, tripPeriodicity, distance, characteristics, driver,
				waypoints, tripOfferCar, passengers, price, allowed);
	}

	public TripOffer(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTimeTyp datetime, int nSeats, UserTypPrx driver,
			List<WaypointTyp> waypoints, CarTypPrx tripOfferCar,
			List<PassengerTyp> passengers, double price, String[] allowed) {
		super(tripId, fromPlace, toPlace, datetime, nSeats, TRIP_OFFER_TYP,
				driver, waypoints, tripOfferCar, passengers, price, allowed,
				datetime);
	}

	public TripOffer(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTimeTyp datetime, int nSeats, DateTimeTyp returnDatetime,
			String[] weekDays,
			uclm.esi.cardroid.data.zerocice.Periodicity tripPeriodicity,
			int distance, String characteristics, UserTypPrx driver,
			List<WaypointTyp> waypoints, CarTypPrx tripOfferCar,
			List<PassengerTyp> passengers, double price, String[] allowed) {
		super(tripId, fromPlace, toPlace, datetime, nSeats, returnDatetime,
				weekDays, tripPeriodicity, distance, characteristics,
				TRIP_OFFER_TYP, driver, waypoints, tripOfferCar, passengers,
				price, allowed, datetime, returnDatetime);
	}

	public TripOffer(TripOfferTyp tripOffer) {
		this(tripOffer.tripId, tripOffer.fromPlace, tripOffer.toPlace,
				(DateTimeTyp) tripOffer.tDateTime, tripOffer.nSeats,
				tripOffer.driver, tripOffer.waypoints, tripOffer.tripOfferCar,
				tripOffer.passengers, tripOffer.price, tripOffer.allowed);
		if (tripOffer.hasTripReturnDate())
			setTripReturnDateTime(tripOffer.getTripReturnDateTime());
		if (tripOffer.hasTWeekDays() && tripOffer.hasTPeriodicity())
			setTripWeekDaysPeriodicity(tripOffer.getTWeekDays(),
					tripOffer.getTPeriodicity());
		if (tripOffer.hasTripDistance())
			setTripDistance(tripOffer.getTripDistance());
		if (tripOffer.hasTripCharacteristics())
			setTripCharacteristics(tripOffer.getTripCharacteristics());
	}

	/**
	 * @return An Ice Identity for this datatype category and the data provided
	 */
	public static Identity createIdentity(int tripId) {
		Identity id = new Identity();
		id.category = "trip_offer";
		id.name = String.valueOf(tripId);
		return id;
	}

	/**
	 * @return A proxy to an Ice Object incarnating the provided data, whose 
	 * 			servant is added to adapter 's active servant map
	 */
	public static TripTypPrx createProxy(SessionController sessionController,
			int tripId) {
		return sessionController.getTripFromId(tripId);
	}

	/**
	 * @param proxy A proxy to a remote object implementing a TripOffer
	 * @return A local TripOffer object containing the data of the remote 
	 * 			object referenced by proxy
	 */
	public static TripOffer extractObject(TripOfferTypPrx proxy) {
		TripOffer tripOffer = null;
		if (!(proxy.hasTripReturnDate() && proxy.hasWeekDaysPeriodicity()
				&& proxy.hasDistance() && proxy.hasCharacteristics())) {
			tripOffer = new TripOffer(proxy.getTripId(), proxy.getPlace1(),
					proxy.getPlace2(), proxy.getTripDateTime(),
					proxy.getNSeats(), proxy.getTripDriver(),
					proxy.getTripWaypoints(), proxy.getTripCar(),
					proxy.getTripPassengers(), proxy.getPrice(),
					proxy.getAllowedFeatures());
			if (!proxy.hasTripReturnDate() && !proxy.hasWeekDaysPeriodicity()
					&& !proxy.hasDistance() && !proxy.hasCharacteristics())
				return tripOffer;
		}

		DateTimeTyp returnDateTime = proxy.getTripReturnDateTime();
		String[] weekDays = proxy.getTripWeekDays();
		uclm.esi.cardroid.data.zerocice.Periodicity periodicity = proxy
				.getTripPeriodicity();
		int distance = proxy.getDistance();
		String characteristics = proxy.getCharacteristics();

		if (!(proxy.hasTripReturnDate() && proxy.hasWeekDaysPeriodicity()
				&& proxy.hasDistance() && proxy.hasCharacteristics())) {
			if (proxy.hasTripReturnDate())
				tripOffer.setTripReturnDateTime(returnDateTime);
			if (proxy.hasWeekDaysPeriodicity())
				tripOffer.setTripWeekDaysPeriodicity(weekDays, periodicity);
			if (proxy.hasDistance())
				tripOffer.setDistance(distance);
			if (proxy.hasCharacteristics())
				tripOffer.setCharacteristics(characteristics);
		} else
			tripOffer = new TripOffer(proxy.getTripId(), proxy.getPlace1(),
					proxy.getPlace2(), proxy.getTripDateTime(),
					proxy.getNSeats(), returnDateTime, weekDays, periodicity,
					distance, characteristics, proxy.getTripDriver(),
					proxy.getTripWaypoints(), proxy.getTripCar(),
					proxy.getTripPassengers(), proxy.getPrice(),
					proxy.getAllowedFeatures());
		return tripOffer;
	}

	/* ITripOffer interface */

	public Trip newInstance(ITrip tripObject) {
		Trip t = new Trip();
		return t.newInstance(tripObject);
	}

	public TripOffer newInstance(ITripOffer tripOfferObject) {
		if (tripOfferObject == null)
			return null;
		if (tripOfferObject instanceof TripOffer)
			return (TripOffer) tripOfferObject;

		TripOffer tripOffer = null;
		Place fromPlace = new Place().newInstance(tripOfferObject
				.getFromPlace());
		Place toPlace = new Place().newInstance(tripOfferObject.getToPlace());
		DateTime datetime = new DateTime().newInstance(tripOfferObject
				.getDateTime());
		UserTypPrx driver = _sessionController.getUserFromEmail(tripOfferObject
				.getDriver().getEmail());
		IWaypoint[] w = tripOfferObject.getWaypoints();
		ArrayList<WaypointTyp> waypoints = new ArrayList<WaypointTyp>(w.length);
		for (int n = 0; n < w.length; n++) {
			waypoints.add(new Waypoint().newInstance(w[n]));
		}
		CarTypPrx car = _sessionController.getCarFromPlateEmail(tripOfferObject
				.getCar().getPlate(), tripOfferObject.getDriver().getEmail());
		IPassenger[] p = tripOfferObject.getPassengers();
		ArrayList<PassengerTyp> passengers = new ArrayList<PassengerTyp>(
				p.length);
		Passenger passenger = new Passenger(_sessionController);
		for (int n = 0; n < p.length; n++) {
			passengers.add(passenger.newInstance(p[n]));
		}
		double price = tripOfferObject.getPrice();
		String[] allowed = null;
		boolean[] a = tripOfferObject.getAllowed();
		allowed = new String[a.length];
		for (int n = 0; n < a.length; n++)
			allowed[n] = String.valueOf(a[n]);
		if (!(tripOfferObject.hasReturnDateTime()
				&& tripOfferObject.hasWeekDaysPeriodicity()
				&& tripOfferObject.hasDistance() && tripOfferObject
					.hasCharacteristics())) {

			tripOffer = new TripOffer(tripOfferObject.getTripId(), fromPlace,
					toPlace, datetime, tripOfferObject.getNSeats(), driver,
					waypoints, car, passengers, price, allowed);
			if (!tripOfferObject.hasReturnDateTime()
					&& !tripOfferObject.hasWeekDaysPeriodicity()
					&& !tripOfferObject.hasDistance()
					&& !tripOfferObject.hasCharacteristics())
				return tripOffer;
		}

		DateTime returnDatetime = null;
		if (tripOfferObject.hasReturnDateTime())
			returnDatetime = new DateTime().newInstance(tripOfferObject
					.getReturnDateTime());
		String[] weekDays = null;
		uclm.esi.cardroid.data.zerocice.Periodicity periodicity = null;
		if (tripOfferObject.hasWeekDaysPeriodicity()) {
			char[] wd = tripOfferObject.getWeekDays();
			weekDays = new String[wd.length];
			for (int n = 0; n < wd.length; n++)
				weekDays[n] = String.valueOf(wd[n]);
			periodicity = uclm.esi.cardroid.data.zerocice.Periodicity
					.valueOf(tripOfferObject.getPeriodicity().name());
		}
		int distance = tripOfferObject.getDistance();
		String characteristics = tripOfferObject.getCharacteristics();

		if (!(tripOfferObject.hasReturnDateTime()
				&& tripOfferObject.hasWeekDaysPeriodicity()
				&& tripOfferObject.hasDistance() && tripOfferObject
					.hasCharacteristics())) {
			if (tripOfferObject.hasReturnDateTime())
				tripOffer.setReturnDateTime(returnDatetime);
			if (tripOfferObject.hasWeekDaysPeriodicity())
				tripOffer.setTripWeekDaysPeriodicity(weekDays, periodicity);
			if (tripOfferObject.hasDistance())
				tripOffer.setDistance(distance);
			if (tripOfferObject.hasCharacteristics())
				tripOffer.setCharacteristics(characteristics);
		} else
			tripOffer = new TripOffer(tripOfferObject.getTripId(), fromPlace,
					toPlace, datetime, tripOfferObject.getNSeats(),
					returnDatetime, weekDays, periodicity, distance,
					characteristics, driver, waypoints, car, passengers, price,
					allowed);
		return tripOffer;
	}

	public void setFromPlace(IPlace from) {
		fromPlace = new Place().newInstance(from);
	}

	public Place getFromPlace() {
		return fromPlace instanceof Place ? (Place) fromPlace : new Place(
				fromPlace);
	}

	public void setToPlace(IPlace to) {
		toPlace = new Place().newInstance(to);
	}

	public Place getToPlace() {
		return toPlace instanceof Place ? (Place) toPlace : new Place(toPlace);
	}

	public void setDateTime(IDate datetime) {
		tDate = new DateTime().newInstance(datetime);
	}

	public void setReturnDateTime(IDate returnDatetime) {
		setTripReturnDate(new DateTime().newInstance(returnDatetime));
	}

	public boolean hasReturnDateTime() {
		return hasTripReturnDate();
	}

	public void setWeekDaysPeriodicity(char[] weekDays, Periodicity periodicity) {
		String[] wd = new String[weekDays.length];
		for (int n = 0; n < weekDays.length; n++)
			wd[n] = String.valueOf(weekDays[n]);
		uclm.esi.cardroid.data.zerocice.Periodicity p = uclm.esi.cardroid.data.zerocice.Periodicity
				.valueOf(periodicity.name());
		setTripWeekDaysPeriodicity(wd, p);
	}

	public char[] getWeekDays() {
		String[] weekDays = getTripWeekDays();
		char[] wd = new char[weekDays.length];
		for (int n = 0; n < weekDays.length; n++)
			wd[n] = weekDays[n].charAt(0);
		return wd;
	}

	public Periodicity getPeriodicity() {
		return Periodicity.valueOf(getTripPeriodicity().name());
	}

	public void setDateTime(IDateTime datetime) {
		tDate = tDateTime = new DateTime().newInstance(datetime);
	}

	public DateTime getDateTime() {
		return tDateTime instanceof DateTime ? (DateTime) tDateTime
				: new DateTime(tDateTime);
	}

	public void setReturnDateTime(IDateTime returnDatetime) {
		DateTime dt = new DateTime().newInstance(returnDatetime);
		setTripReturnDate(dt);
		setTReturnDateTime(dt);
	}

	public DateTime getReturnDateTime() {
		if (!hasTripReturnDate())
			return null;
		DateTimeTyp returnDateTime = getTripReturnDateTime();
		return returnDateTime instanceof DateTime ? (DateTime) returnDateTime
				: new DateTime(returnDateTime);
	}

	public void setDriver(IUser driver) {
		setTripDriver(_sessionController.getUserFromEmail(driver.getEmail()));
	}

	public User getDriver() {
		return User.extractObject(getTripDriver());
	}

	public void setWaypoints(IWaypoint[] waypoints) {
		ArrayList<WaypointTyp> wp = new ArrayList<WaypointTyp>(waypoints.length);
		for (int n = 0; n < waypoints.length; n++)
			wp.add(new Waypoint().newInstance(waypoints[n]));
		setTripWaypoints(wp);
	}

	public Waypoint[] getWaypoints() {
		List<WaypointTyp> waypoints = getTripWaypoints();
		Waypoint[] wp = new Waypoint[waypoints.size()];
		for (int n = 0; n < wp.length; n++)
			wp[n] = new Waypoint(waypoints.get(n));
		return wp;
	}

	public int getNWaypoints() {
		return getNTripWaypoints();
	}

	public boolean addWaypoint(IPlace waypoint) {
		return addTripWaypoint(new Place().newInstance(waypoint));
	}

	public boolean removeWaypoint(int pos) {
		return removeTripWaypoint(pos);
	}

	public void clearWaypoints() {
		clearTripWaypoints();
	}

	public void setCar(ICar car) {
		setTripCar(_sessionController.getCarFromPlate(car.getPlate(), driver));
	}

	public Car getCar() {
		return Car.extractObject(tripOfferCar);
	}

	public void setPassengers(IPassenger[] passengers) {
		ArrayList<PassengerTyp> p = new ArrayList<PassengerTyp>(
				passengers.length);
		for (IPassenger ip : passengers)
			p.add(new Passenger(_sessionController.getUserFromEmail(ip
					.getEmail()), ip.getNSeats()));
	}

	public Passenger[] getPassengers() {
		List<PassengerTyp> passengers = getTripPassengers();
		Passenger[] p = new Passenger[passengers.size()];
		for (int n = 0; n < p.length; n++)
			p[n] = new Passenger(passengers.get(n));
		return p;
	}

	public int getNPassengers() {
		return getNTripPassengers();
	}

	public boolean addPassenger(IPassenger passenger) {
		return addTripPassenger(
				_sessionController.getUserFromEmail(passenger.getEmail()),
				passenger.getNSeats());
	}

	public boolean removePassenger(IUser passenger) {
		return removeTripPassenger(_sessionController
				.getUserFromEmail(passenger.getEmail()));
	}

	public void clearPassengers() {
		clearTripPassengers();
	}

	public void setAllowed(boolean[] allowed) {
		String[] a = new String[allowed.length];
		for (int n = 0; n < allowed.length; n++)
			a[n] = String.valueOf(allowed[n]);
		setAllowedFeatures(a);
	}

	public boolean[] getAllowed() {
		boolean[] a = new boolean[allowed.length];
		for (int n = 0; n < allowed.length; n++)
			a[n] = Boolean.parseBoolean(allowed[n]);
		return a;
	}

	/* Overriding superclass */

	@Override
	public DateTimeTyp getTripDateTime(Current __current) {
		return tDateTime;
	}

	@Override
	public void setTripDateTime(DateTimeTyp dt, Current __current) {
		tDate = tDateTime = dt;
	}

	@Override
	public DateTimeTyp getTripReturnDateTime(Current __current) {
		return hasTReturnDateTime() ? getTReturnDateTime() : null;
	}

	@Override
	public void setTripReturnDateTime(DateTimeTyp dt, Current __current) {
		setTReturnDate(dt);
		setTReturnDateTime(dt);
	}

	@Override
	public UserTypPrx getTripDriver(Current __current) {
		return driver;
	}

	@Override
	public void setTripDriver(UserTypPrx driver, Current __current) {
		this.driver = driver;
	}

	@Override
	public List<WaypointTyp> getTripWaypoints(Current __current) {
		return waypoints;
	}

	@Override
	public void setTripWaypoints(List<WaypointTyp> waypoints, Current __current) {
		this.waypoints = waypoints;
	}

	@Override
	public CarTypPrx getTripCar(Current __current) {
		return tripOfferCar;
	}

	@Override
	public void setTripCar(CarTypPrx tripCar, Current __current) {
		this.tripOfferCar = tripCar;
	}

	@Override
	public List<PassengerTyp> getTripPassengers(Current __current) {
		return passengers;
	}

	@Override
	public void setTripPassengers(List<PassengerTyp> passengers,
			Current __current) {
		this.passengers = passengers;
	}

	@Override
	public double getPrice(Current __current) {
		return price;
	}

	@Override
	public void setPrice(double price, Current __current) {
		this.price = price;
	}

	@Override
	public String[] getAllowedFeatures(Current __current) {
		return allowed;
	}

	@Override
	public void setAllowedFeatures(String[] allowed, Current __current) {
		this.allowed = allowed;
	}

	@Override
	public boolean addTripWaypoint(PlaceTyp waypoint, Current __current) {
		return waypoints.add(new Waypoint(waypoints.size() + 1, waypoint));
	}

	@Override
	public boolean removeTripWaypoint(int pos, Current __current) {
		return waypoints.remove(pos) != null;
	}

	@Override
	public int getNTripWaypoints(Current __current) {
		return waypoints.size();
	}

	@Override
	public void clearTripWaypoints(Current __current) {
		waypoints.clear();
	}

	@Override
	public boolean addTripPassenger(UserTypPrx passenger, int seats,
			Current __current) {
		return passengers.add(new Passenger(passenger, seats));
	}

	@Override
	public boolean removeTripPassenger(UserTypPrx passenger, Current __current) {
		int pos = -1;
		for (int n = 0; n < passengers.size(); n++) {
			if (((Passenger) passengers.get(n)).getEmail().equals(
					passenger.getEmail())) {
				pos = n;
				break;
			}
		}
		if (pos >= 0) {
			passengers.remove(pos);
		}
		return pos >= 0;
	}

	@Override
	public int getNTripPassengers(Current __current) {
		return passengers.size();
	}

	@Override
	public void clearTripPassengers(Current __current) {
		passengers.clear();
	}

	@Override
	public int getTripId(Current __current) {
		return tripId;
	}

	@Override
	public void setTripId(int tripId, Current __current) {
		this.tripId = tripId;
	}

	@Override
	public PlaceTyp getPlace1(Current __current) {
		return fromPlace;
	}

	@Override
	public void setPlace1(PlaceTyp fromPlace, Current __current) {
		this.fromPlace = fromPlace;
	}

	@Override
	public PlaceTyp getPlace2(Current __current) {
		return toPlace;
	}

	@Override
	public void setPlace2(PlaceTyp toPlace, Current __current) {
		this.toPlace = toPlace;
	}

	@Override
	public DateTyp getTripDate(Current __current) {
		return tDateTime;
	}

	@Override
	public void setTripDate(DateTyp tripDate, Current __current) {
		this.tDate = tripDate;
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
	public DateTyp getTripReturnDate(Current __current) {
		return getTripReturnDateTime();
	}

	@Override
	public void setTripReturnDate(DateTyp tripReturnDate, Current __current) {
		setTReturnDate(tripReturnDate);
	}

	@Override
	public boolean hasTripReturnDate(Current __current) {
		return hasTReturnDateTime();
	}

	@Override
	public String[] getTripWeekDays(Current __current) {
		return getTWeekDays();
	}

	@Override
	public uclm.esi.cardroid.data.zerocice.Periodicity getTripPeriodicity(
			Current __current) {
		return getTPeriodicity();
	}

	@Override
	public void setTripWeekDaysPeriodicity(String[] tripWeekDays,
			uclm.esi.cardroid.data.zerocice.Periodicity tripPeriodicity,
			Current __current) {
		setTWeekDays(tripWeekDays);
		setTPeriodicity(tripPeriodicity);
	}

	@Override
	public boolean hasWeekDaysPeriodicity(Current __current) {
		return hasTWeekDays() && hasTPeriodicity();
	}

	@Override
	public int getDistance(Current __current) {
		return hasTripDistance() ? getTripDistance() : 0;
	}

	@Override
	public void setDistance(int distance, Current __current) {
		setTripDistance(distance);
	}

	@Override
	public boolean hasDistance(Current __current) {
		return hasTripDistance();
	}

	@Override
	public String getCharacteristics(Current __current) {
		return hasTripCharacteristics() ? getTripCharacteristics() : null;
	}

	@Override
	public void setCharacteristics(String characteristics, Current __current) {
		setTripCharacteristics(characteristics);
	}

	@Override
	public boolean hasCharacteristics(Current __current) {
		return hasTripCharacteristics();
	}

	@Override
	public void setTripType(int type, Current __current) {
		this.tripType = type;
	}

	@Override
	public int getTripType(Current __current) {
		return tripType;
	}

	@Override
	public String _toString(Current __current) {
		StringBuilder builder = new StringBuilder();
		builder.append(fromPlace.getName() + " - ");
		builder.append(toPlace.getName());
		builder.append(" [" + tDateTime._toString() + "]");
		return builder.toString();
	}

	/* ObjectFactory interface */

	@Override
	public TripOffer create(String type) {
		if (type.equals(ice_staticId())) {
			return new TripOffer(_sessionController);
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
