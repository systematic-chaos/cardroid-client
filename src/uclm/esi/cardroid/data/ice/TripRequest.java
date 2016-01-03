package uclm.esi.cardroid.data.ice;

import Ice.Current;
import Ice.Identity;
import Ice.ObjectFactory;
import uclm.esi.cardroid.data.IDate;
import uclm.esi.cardroid.data.IDateTimePrefs;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.ITrip;
import uclm.esi.cardroid.data.ITripRequest;
import uclm.esi.cardroid.data.IUser;
import uclm.esi.cardroid.data.zerocice.DateTimePrefsTyp;
import uclm.esi.cardroid.data.zerocice.DateTyp;
import uclm.esi.cardroid.data.zerocice.PlaceTyp;
import uclm.esi.cardroid.data.zerocice.TripRequestTyp;
import uclm.esi.cardroid.data.zerocice.TripRequestTypPrx;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.network.client.SessionController;

/**
 * \class TripRequest
 * Domain class implementing a TripRequest for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class TripRequest extends TripRequestTyp implements ITripRequest,
		ObjectFactory {
	protected static SessionController _sessionController;

	protected static final int TRIP_TYPE = 0, TRIP_OFFER_TYP = 1,
			TRIP_REQUEST_TYPE = 2;

	private static final long serialVersionUID = 4965726405855943632L;

	public TripRequest(SessionController sessionController) {
		_sessionController = sessionController;
	}

	/**
	 * Short constructor
	 */
	public TripRequest(PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTimePrefsTyp datetimeprefs, int nSeats, UserTypPrx requester) {
		this(-1, fromPlace, toPlace, datetimeprefs, nSeats, requester);
	}

	public TripRequest(PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTimePrefsTyp datetimeprefs, int nSeats,
			DateTimePrefsTyp returnDatetimeprefs, String[] weekdays,
			uclm.esi.cardroid.data.zerocice.Periodicity tripPeriodicity,
			int distance, String characteristics, UserTypPrx requester) {
		this(-1, fromPlace, toPlace, datetimeprefs, nSeats,
				returnDatetimeprefs, weekdays, tripPeriodicity, distance,
				characteristics, requester);
	}

	public TripRequest(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTimePrefsTyp datetimeprefs, int nSeats, UserTypPrx requester) {
		super(tripId, fromPlace, toPlace, datetimeprefs, nSeats,
				TRIP_REQUEST_TYPE, requester, datetimeprefs);
	}

	public TripRequest(int tripId, PlaceTyp fromPlace, PlaceTyp toPlace,
			DateTimePrefsTyp datetimeprefs, int nSeats,
			DateTimePrefsTyp returnDatetimeprefs, String[] weekDays,
			uclm.esi.cardroid.data.zerocice.Periodicity tripPeriodicity,
			int distance, String characteristics, UserTypPrx requester) {
		super(tripId, fromPlace, toPlace, datetimeprefs, nSeats,
				returnDatetimeprefs, weekDays, tripPeriodicity, distance,
				characteristics, TRIP_REQUEST_TYPE, requester, datetimeprefs,
				returnDatetimeprefs);
	}

	public TripRequest(TripRequestTyp tripRequest) {
		this(tripRequest.tripId, tripRequest.fromPlace, tripRequest.toPlace,
				(DateTimePrefsTyp) tripRequest.tDateTimePrefs,
				tripRequest.nSeats, tripRequest.requester);
		if (tripRequest.hasTripReturnDate())
			setTripReturnDateTimePrefs(tripRequest.getTripReturnDateTimePrefs());
		if (tripRequest.hasTWeekDays() && tripRequest.hasTPeriodicity())
			setTripWeekDaysPeriodicity(tripRequest.getTWeekDays(),
					tripRequest.getTPeriodicity());
		if (tripRequest.hasTripDistance())
			setTripDistance(tripRequest.getTripDistance());
		if (tripRequest.hasTripCharacteristics())
			setTripCharacteristics(tripRequest.getTripCharacteristics());
	}

	/**
	 *  @return An Ice Identity for this datatype category and the data provided
	 */
	public static Identity createIdentity(int tripId) {
		Identity id = new Identity();
		id.category = "trip_request";
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
	 * @param proxy A proxy to a remote object implementing a TripRequest
	 * @return A local TripRequest object containing the data of the remote 
	 * 			object referenced by proxy
	 */
	public static TripRequest extractObject(TripRequestTypPrx proxy) {
		TripRequest tripRequest = null;
		if (!(proxy.hasTripReturnDate() && proxy.hasWeekDaysPeriodicity()
				&& proxy.hasDistance() && proxy.hasCharacteristics())) {
			tripRequest = new TripRequest(proxy.getTripId(), proxy.getPlace1(),
					proxy.getPlace2(), proxy.getTripDateTimePrefs(),
					proxy.getNSeats(), proxy.getTripRequester());
			if (!proxy.hasTripReturnDate() && !proxy.hasWeekDaysPeriodicity()
					&& !proxy.hasDistance() && !proxy.hasCharacteristics())
				return tripRequest;
		}

		DateTimePrefsTyp returnDateTimePrefs = proxy
				.getTripReturnDateTimePrefs();
		String[] weekDays = proxy.getTripWeekDays();
		uclm.esi.cardroid.data.zerocice.Periodicity periodicity = proxy
				.getTripPeriodicity();
		int distance = proxy.getDistance();
		String characteristics = proxy.getCharacteristics();

		if (!(proxy.hasTripReturnDate() && proxy.hasWeekDaysPeriodicity()
				&& proxy.hasDistance() && proxy.hasCharacteristics())) {
			if (proxy.hasDistance())
				tripRequest.setTripReturnDateTimePrefs(returnDateTimePrefs);
			if (proxy.hasWeekDaysPeriodicity())
				tripRequest.setTripWeekDaysPeriodicity(weekDays, periodicity);
			if (proxy.hasDistance())
				tripRequest.setDistance(distance);
			if (proxy.hasCharacteristics())
				tripRequest.setCharacteristics(characteristics);
		} else
			tripRequest = new TripRequest(proxy.getTripId(), proxy.getPlace1(),
					proxy.getPlace2(), proxy.getTripDateTimePrefs(),
					proxy.getNSeats(), returnDateTimePrefs, weekDays,
					periodicity, distance, characteristics,
					proxy.getTripRequester());
		return tripRequest;
	}

	/* ITripRequest interface */

	public Trip newInstance(ITrip tripObject) {
		Trip t = new Trip();
		return t.newInstance(tripObject);
	}

	public TripRequest newInstance(ITripRequest tripRequestObject) {
		if (tripRequestObject == null)
			return null;
		if (tripRequestObject instanceof TripRequest)
			return (TripRequest) tripRequestObject;

		TripRequest tripRequest = null;
		Place fromPlace = new Place().newInstance(tripRequestObject
				.getFromPlace());
		Place toPlace = new Place().newInstance(tripRequestObject.getToPlace());
		DateTimePrefs datetime = new DateTimePrefs()
				.newInstance(tripRequestObject.getDateTime());
		UserTypPrx requester = _sessionController
				.getUserFromEmail(tripRequestObject.getRequester().getEmail());
		if (!(tripRequestObject.hasReturnDateTime()
				&& tripRequestObject.hasWeekDaysPeriodicity()
				&& tripRequestObject.hasDistance() && tripRequestObject
					.hasCharacteristics())) {
			tripRequest = new TripRequest(tripRequestObject.getTripId(),
					fromPlace, toPlace, datetime,
					tripRequestObject.getNSeats(), requester);
			if (!tripRequestObject.hasReturnDateTime()
					&& !tripRequestObject.hasWeekDaysPeriodicity()
					&& !tripRequestObject.hasDistance()
					&& !tripRequestObject.hasCharacteristics())
				return tripRequest;
		}

		DateTimePrefs returnDatetime = null;
		if (tripRequestObject.hasReturnDateTime())
			returnDatetime = new DateTimePrefs().newInstance(tripRequestObject
					.getReturnDateTime());
		uclm.esi.cardroid.data.zerocice.Periodicity periodicity = null;
		String[] weekDays = null;
		if (tripRequestObject.hasWeekDaysPeriodicity()) {
			periodicity = uclm.esi.cardroid.data.zerocice.Periodicity
					.valueOf(tripRequestObject.getPeriodicity().name());
			char[] wd = tripRequestObject.getWeekDays();
			weekDays = new String[wd.length];
			for (int n = 0; n < wd.length; n++)
				weekDays[n] = String.valueOf(wd[n]);
		}
		int distance = tripRequestObject.getDistance();
		String characteristics = tripRequestObject.getCharacteristics();

		if (!(tripRequestObject.hasReturnDateTime()
				&& tripRequestObject.hasWeekDaysPeriodicity()
				&& tripRequestObject.hasDistance() && tripRequestObject
					.hasCharacteristics())) {
			if (tripRequestObject.hasReturnDateTime())
				tripRequest.setReturnDateTime(returnDatetime);
			if (tripRequestObject.hasWeekDaysPeriodicity())
				tripRequest.setTripWeekDaysPeriodicity(weekDays, periodicity);
			if (tripRequestObject.hasDistance())
				tripRequest.setDistance(distance);
			if (tripRequestObject.hasCharacteristics())
				tripRequest.setCharacteristics(characteristics);
		} else
			tripRequest = new TripRequest(tripRequestObject.getTripId(),
					fromPlace, toPlace, datetime,
					tripRequestObject.getNSeats(), returnDatetime, weekDays,
					periodicity, distance, characteristics, requester);
		return tripRequest;
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
		tDate = new DateTimePrefs().newInstance(datetime);
	}

	public void setReturnDateTime(IDate returnDatetime) {
		setTripReturnDate(new DateTimePrefs().newInstance(returnDatetime));
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

	public void setRequester(IUser requester) {
		setTripRequester(_sessionController.getUserFromEmail(requester
				.getEmail()));
	}

	public User getRequester() {
		return User.extractObject(requester);
	}

	public void setDateTime(IDateTimePrefs dateTimePrefsObject) {
		tDate = tDateTimePrefs = new DateTimePrefs()
				.newInstance(dateTimePrefsObject);
	}

	public DateTimePrefs getDateTime() {
		return tDateTimePrefs instanceof DateTimePrefs ? (DateTimePrefs) tDateTimePrefs
				: new DateTimePrefs(tDateTimePrefs);
	}

	public void setReturnDateTime(IDateTimePrefs dateTimePrefsObject) {
		DateTimePrefs dtp = new DateTimePrefs()
				.newInstance(dateTimePrefsObject);
		setTripReturnDate(dtp);
		setTripReturnDateTimePrefs(dtp);
	}

	public DateTimePrefs getReturnDateTime() {
		if (!hasTripReturnDate())
			return null;
		DateTimePrefsTyp rdt = getTripReturnDateTimePrefs();
		return rdt instanceof DateTimePrefs ? (DateTimePrefs) rdt
				: new DateTimePrefs(rdt);
	}

	/* Overriding superclass */

	@Override
	public DateTimePrefsTyp getTripDateTimePrefs(Current __current) {
		return tDateTimePrefs;
	}

	@Override
	public void setTripDateTimePrefs(DateTimePrefsTyp dtp, Current __current) {
		tDate = tDateTimePrefs = dtp;
	}

	@Override
	public DateTimePrefsTyp getTripReturnDateTimePrefs(Current __current) {
		return hasTReturnDateTimePrefs() ? getTReturnDateTimePrefs() : null;
	}

	@Override
	public void setTripReturnDateTimePrefs(DateTimePrefsTyp dtp,
			Current __current) {
		setTReturnDateTimePrefs(dtp);
	}

	@Override
	public UserTypPrx getTripRequester(Current __current) {
		return requester;
	}

	@Override
	public void setTripRequester(UserTypPrx requester, Current __current) {
		this.requester = requester;
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
		return tDateTimePrefs;
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
		return getTripReturnDateTimePrefs();
	}

	@Override
	public void setTripReturnDate(DateTyp tripReturnDate, Current __current) {
		setTReturnDate(tripReturnDate);
	}

	@Override
	public boolean hasTripReturnDate(Current __current) {
		return hasTReturnDateTimePrefs();
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
		return hasDistance() ? getTripDistance() : 0;
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
		builder.append(" [" + tDateTimePrefs._toString() + "]");
		return builder.toString();
	}

	/* ObjectFactory interface */

	@Override
	public TripRequest create(String type) {
		if (type.equals(ice_staticId())) {
			return new TripRequest(_sessionController);
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
