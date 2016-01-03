package uclm.esi.cardroid.data.ice;

import Ice.Current;
import Ice.Identity;
import Ice.ObjectFactory;
import uclm.esi.cardroid.data.IDate;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.ITrip;
import uclm.esi.cardroid.data.zerocice.DateTyp;
import uclm.esi.cardroid.data.zerocice.PlaceTyp;
import uclm.esi.cardroid.data.zerocice.TripTyp;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.network.client.SessionController;

/**
 * \class Trip
 * Domain class implementing a Trip for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class Trip extends TripTyp implements ITrip, ObjectFactory {

	protected static final int TRIP_TYPE = 0, TRIP_OFFER_TYP = 1,
			TRIP_REQUEST_TYPE = 2;

	private static final long serialVersionUID = 1900796714995036513L;

	public Trip() {
	}

	/**
	 * Short constructor
	 */
	public Trip(PlaceTyp fromPlace, PlaceTyp toPlace, DateTyp tripDate,
			int nSeats, int tripType) {
		this(-1, fromPlace, toPlace, tripDate, nSeats, tripType);
	}

	public Trip(PlaceTyp fromPlace, PlaceTyp toPlace, DateTyp tripDate,
			int nSeats, DateTyp returnDatetime, String[] weekDays,
			uclm.esi.cardroid.data.zerocice.Periodicity tripPeriodicity,
			int distance, String characteristics, int tripType) {
		this(-1, fromPlace, toPlace, tripDate, nSeats, returnDatetime,
				weekDays, tripPeriodicity, distance, characteristics, tripType);
	}

	public Trip(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTyp tripDate, int nSeats, int tripType) {
		super(tripId, fromPlace, toPlace, tripDate, nSeats, tripType);
	}

	public Trip(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTyp tripDate, int nSeats, DateTyp returnDatetime,
			String[] weekDays,
			uclm.esi.cardroid.data.zerocice.Periodicity tripPeriodicity,
			int distance, String characteristics, int tripType) {
		super(tripId, fromPlace, toPlace, tripDate, nSeats, returnDatetime,
				weekDays, tripPeriodicity, distance, characteristics, tripType);
	}

	public Trip(TripTyp trip) {
		this(trip.tripId, trip.fromPlace, trip.toPlace, trip.tDate,
				trip.nSeats, trip.tripType);
		if (trip.hasTripReturnDate())
			setTripReturnDate(trip.getTripReturnDate());
		if (trip.hasTWeekDays() && trip.hasTPeriodicity())
			setTripWeekDaysPeriodicity(trip.getTWeekDays(),
					trip.getTPeriodicity());
		if (trip.hasTripDistance())
			setTripDistance(trip.getTripDistance());
		if (trip.hasTripCharacteristics())
			setTripCharacteristics(trip.getTripCharacteristics());
	}

	/**
	 *  @return An Ice Identity for this datatype category and the data provided
	 */
	public static Identity createIdentity(int tripId) {
		Identity id = new Identity();
		id.category = "trip";
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
	 * @param proxy A proxy to a remote object implementing a Trip
	 * @return A local Trip object containing the data of the remote object
	 * 			referenced by proxy
	 */
	public static Trip extractObject(TripTypPrx proxy) {
		Trip trip = null;

		if (!(proxy.hasTripReturnDate() && proxy.hasWeekDaysPeriodicity()
				&& proxy.hasDistance() && proxy.hasCharacteristics())) {
			trip = new Trip(proxy.getTripId(), proxy.getPlace1(),
					proxy.getPlace2(), proxy.getTripDate(), proxy.getNSeats(),
					proxy.getTripType());
			if (!proxy.hasTripReturnDate() && !proxy.hasWeekDaysPeriodicity()
					&& !proxy.hasDistance() && !proxy.hasCharacteristics())
				return trip;
		}

		DateTyp returnDate = proxy.getTripReturnDate();
		String[] weekDays = proxy.getTripWeekDays();
		uclm.esi.cardroid.data.zerocice.Periodicity periodicity = proxy
				.getTripPeriodicity();
		int distance = proxy.getDistance();
		String characteristics = proxy.getCharacteristics();

		if (!(proxy.hasTripReturnDate() && proxy.hasWeekDaysPeriodicity()
				&& proxy.hasDistance() && proxy.hasCharacteristics())) {
			if (proxy.hasTripReturnDate())
				trip.setTripReturnDate(returnDate);
			if (proxy.hasWeekDaysPeriodicity())
				trip.setTripWeekDaysPeriodicity(weekDays, periodicity);
			if (proxy.hasDistance())
				trip.setDistance(distance);
			if (proxy.hasCharacteristics())
				trip.setCharacteristics(characteristics);
		} else
			trip = new Trip(proxy.getTripId(), proxy.getPlace1(),
					proxy.getPlace2(), proxy.getTripDate(), proxy.getNSeats(),
					returnDate, weekDays, periodicity, distance,
					characteristics, proxy.getTripType());
		return trip;
	}

	/* ITrip interface */

	public Trip newInstance(ITrip tripObject) {
		if (tripObject == null)
			return null;
		if (tripObject instanceof Trip)
			return (Trip) tripObject;

		Trip trip = null;
		Place fromPlace = new Place().newInstance(tripObject.getFromPlace());
		Place toPlace = new Place().newInstance(tripObject.getToPlace());
		Date datetime = new Date().newInstance(tripObject.getDateTime());

		if (!(tripObject.hasReturnDateTime()
				&& tripObject.hasWeekDaysPeriodicity()
				&& tripObject.hasDistance() && tripObject.hasCharacteristics())) {
			trip = new Trip(tripObject.getTripId(), fromPlace, toPlace,
					datetime, tripObject.getNSeats(), tripObject.getTripType());
			if (!tripObject.hasReturnDateTime()
					&& !tripObject.hasWeekDaysPeriodicity()
					&& !tripObject.hasDistance()
					&& !tripObject.hasCharacteristics())
				return trip;
		}

		Date returnDatetime = null;
		if (tripObject.hasReturnDateTime())
			returnDatetime = new Date().newInstance(tripObject
					.getReturnDateTime());
		uclm.esi.cardroid.data.zerocice.Periodicity periodicity = null;
		if (tripObject.getPeriodicity() != null) {
			periodicity = uclm.esi.cardroid.data.zerocice.Periodicity
					.valueOf(tripObject.getPeriodicity().name());
		}
		String[] weekDays = null;
		if (tripObject.getWeekDays() != null) {
			char[] wd = tripObject.getWeekDays();
			weekDays = new String[wd.length];
			for (int n = 0; n < wd.length; n++)
				weekDays[n] = String.valueOf(wd[n]);
		}
		int distance = tripObject.getDistance();
		String characteristics = tripObject.getCharacteristics();

		if (!(tripObject.hasReturnDateTime()
				&& tripObject.hasWeekDaysPeriodicity()
				&& tripObject.hasDistance() && tripObject.hasCharacteristics())) {
			if (tripObject.hasReturnDateTime())
				trip.setTripReturnDate(returnDatetime);
			if (tripObject.hasWeekDaysPeriodicity())
				trip.setTripWeekDaysPeriodicity(weekDays, periodicity);
			if (tripObject.hasDistance())
				trip.setDistance(distance);
			if (tripObject.hasCharacteristics())
				trip.setCharacteristics(characteristics);
		} else
			trip = new Trip(tripObject.getTripId(), fromPlace, toPlace,
					datetime, tripObject.getNSeats(), returnDatetime, weekDays,
					periodicity, distance, characteristics,
					tripObject.getTripType());
		return trip;
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
		tDate = new Date().newInstance(datetime);
	}

	public Date getDateTime() {
		return tDate instanceof Date ? (Date) tDate : new Date(tDate);
	}

	public void setReturnDateTime(IDate returnDatetime) {
		setTripReturnDate(new Date().newInstance(returnDatetime));
	}

	public Date getReturnDateTime() {
		DateTyp rdt = getTripReturnDate();
		return rdt instanceof Date ? (Date) rdt : new Date(rdt);
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

	/* Overriding superclass */

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
		return tDate;
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
		return hasTReturnDate() ? getTReturnDate() : null;
	}

	@Override
	public void setTripReturnDate(DateTyp tripReturnDate, Current __current) {
		setTReturnDate(tripReturnDate);
	}

	@Override
	public boolean hasTripReturnDate(Current __current) {
		return hasTReturnDate();
	}

	@Override
	public String[] getTripWeekDays(Current __current) {
		return hasWeekDaysPeriodicity() ? getTWeekDays() : null;
	}

	@Override
	public uclm.esi.cardroid.data.zerocice.Periodicity getTripPeriodicity(
			Current __current) {
		return hasWeekDaysPeriodicity() ? getTPeriodicity() : null;
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
		return hasCharacteristics() ? getTripCharacteristics() : null;
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
		builder.append(" [" + tDate._toString() + "]");
		return builder.toString();
	}

	/* ObjectFactory interface */

	@Override
	public Trip create(String type) {
		if (type.equals(ice_staticId())) {
			return new Trip();
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
