package uclm.esi.cardroid.data.android;

import android.os.Parcel;
import android.os.Parcelable;
import uclm.esi.cardroid.data.IDateTimePrefs;
import uclm.esi.cardroid.data.ITripRequest;
import uclm.esi.cardroid.data.IUser;

/**
 * \class TripRequest
 * Persistence class that represents a trip request data
 */
public class TripRequest extends Trip implements ITripRequest {

	private User requester;

	/**
	 * Simple constructor.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date and time preferences
	 * @param requester
	 *            The user who launched this trip request
	 * @param requestedSeats
	 *            The number of seats requested
	 */
	public TripRequest(Place from, Place to, DateTimePrefs datetime,
			User requester, int requestedSeats) {
		this(-1, from, to, datetime, requester, requestedSeats);
	}

	public TripRequest(int tripId, Place from, Place to,
			DateTimePrefs datetime, User requester, int requestedSeats) {
		super(tripId, from, to, datetime, requestedSeats, TRIP_REQUEST_TYPE);
		this.requester = requester;
	}

	/**
	 * Constructor for a two-way trip.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date and time preferences
	 * @param returnDatetime
	 *            The trip's return date and time preferences
	 * @param requester
	 *            The user who launched this trip request
	 * @param requestedSeats
	 *            The number of seats requested
	 */
	public TripRequest(Place from, Place to, DateTimePrefs datetime,
			DateTimePrefs returnDatetime, User requester, int requestedSeats) {
		this(-1, from, to, datetime, returnDatetime, requester, requestedSeats);
	}

	public TripRequest(int tripId, Place from, Place to,
			DateTimePrefs datetime, DateTimePrefs returnDatetime,
			User requester, int requestedSeats) {
		super(tripId, from, to, datetime, returnDatetime, requestedSeats,
				TRIP_REQUEST_TYPE);
		this.requester = requester;
	}

	/**
	 * Constructor for a periodic trip.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date and time preferences
	 * @param requester
	 *            The user who launched this trip request
	 * @param requestedSeats
	 *            The number of seats requested
	 * @param weekDays
	 *            Those days of the week on which the trip will take place
	 * @param periodicity
	 *            Those weeks on which the trip will take place
	 */
	public TripRequest(Place from, Place to, DateTimePrefs datetime,
			User requester, int requestedSeats, char[] weekDays,
			Periodicity periodicity) {
		this(-1, from, to, datetime, requester, requestedSeats, weekDays,
				periodicity);
	}

	public TripRequest(int tripId, Place from, Place to,
			DateTimePrefs datetime, User requester, int requestedSeats,
			char[] weekDays, Periodicity periodicity) {
		super(tripId, from, to, datetime, requestedSeats, TRIP_REQUEST_TYPE,
				weekDays, periodicity);
		this.requester = requester;
	}

	/**
	 * Constructor for a two-way periodic trip.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date and time preferences
	 * @param returnDatetime
	 *            The trip's return date and time preferences
	 * @param requester
	 *            The user who launched this trip request
	 * @param requestedSeats
	 *            The number of seats requested
	 * @param weekDays
	 *            Those days of the week on which the trip will take place
	 * @param periodicity
	 *            Those weeks on which the trip will take place
	 */
	public TripRequest(Place from, Place to, DateTimePrefs datetime,
			DateTimePrefs returnDatetime, User requester, int requestedSeats,
			char[] weekDays, Periodicity periodicity) {
		this(-1, from, to, datetime, returnDatetime, requester, requestedSeats,
				weekDays, periodicity);
	}

	public TripRequest(int tripId, Place from, Place to,
			DateTimePrefs datetime, DateTimePrefs returnDatetime,
			User requester, int requestedSeats, char[] weekDays,
			Periodicity periodicity) {
		super(tripId, from, to, datetime, returnDatetime, requestedSeats,
				TRIP_REQUEST_TYPE, weekDays, periodicity);
		this.requester = requester;
	}

	public TripRequest(Place from, Place to, DateTimePrefs datetime,
			DateTimePrefs returnDatetime, int requestedSeats, char[] weekDays,
			Periodicity periodicity, User requester, int distance,
			String characteristics) {
		this(-1, from, to, datetime, returnDatetime, requestedSeats, weekDays,
				periodicity, requester, distance, characteristics);
	}

	public TripRequest(int tripId, Place from, Place to,
			DateTimePrefs datetime, DateTimePrefs returnDatetime,
			int requestedSeats, char[] weekDays, Periodicity periodicity,
			User requester, int distance, String characteristics) {
		this(tripId, from, to, datetime, returnDatetime, requester,
				requestedSeats, weekDays, periodicity);
		this.distance = distance;
		this.characteristics = characteristics;
	}

	public TripRequest() {
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
		DateTimePrefs tripDatetime = new DateTimePrefs()
				.newInstance(tripRequestObject.getDateTime());
		DateTimePrefs tripReturnDatetime = tripRequestObject
				.hasReturnDateTime() ? new DateTimePrefs()
				.newInstance(tripRequestObject.getReturnDateTime()) : null;
		User requesterUser = new User().newInstance(tripRequestObject
				.getRequester());

		if (!tripRequestObject.hasReturnDateTime()
				&& !tripRequestObject.hasWeekDaysPeriodicity()
				&& !tripRequestObject.hasDistance()
				&& !tripRequestObject.hasCharacteristics()) {
			tripRequest = new TripRequest(tripRequestObject.getTripId(),
					fromPlace, toPlace, tripDatetime, requesterUser,
					tripRequestObject.getNSeats());
			return tripRequest;
		}
		if (tripRequestObject.hasReturnDateTime()
				&& !tripRequestObject.hasWeekDaysPeriodicity()
				&& !tripRequestObject.hasCharacteristics()) {
			tripRequest = new TripRequest(tripRequestObject.getTripId(),
					fromPlace, toPlace, tripDatetime, tripReturnDatetime,
					requesterUser, tripRequestObject.getNSeats());
			return tripRequest;
		}
		if (!tripRequestObject.hasReturnDateTime()
				&& tripRequestObject.hasWeekDaysPeriodicity()
				&& !tripRequestObject.hasDistance()
				&& !tripRequestObject.hasCharacteristics()) {
			tripRequest = new TripRequest(tripRequestObject.getTripId(),
					fromPlace, toPlace, tripDatetime, requesterUser,
					tripRequestObject.getNSeats(),
					tripRequestObject.getWeekDays(),
					tripRequestObject.getPeriodicity());
			return tripRequest;
		}
		if (tripRequestObject.hasReturnDateTime()
				&& tripRequestObject.hasWeekDaysPeriodicity()
				&& !tripRequestObject.hasDistance()
				&& !tripRequestObject.hasCharacteristics()) {
			tripRequest = new TripRequest(tripRequestObject.getTripId(),
					fromPlace, toPlace, tripDatetime, tripReturnDatetime,
					requesterUser, tripRequestObject.getNSeats(),
					tripRequestObject.getWeekDays(),
					tripRequestObject.getPeriodicity());
			return tripRequest;
		}
		tripRequest = new TripRequest(tripRequestObject.getTripId(), fromPlace,
				toPlace, tripDatetime, tripReturnDatetime,
				tripRequestObject.getNSeats(), tripRequestObject.getWeekDays(),
				tripRequestObject.getPeriodicity(), requesterUser,
				tripRequestObject.getDistance(),
				tripRequestObject.getCharacteristics());
		return tripRequest;
	}

	/**
	 * @return The trip's date and time preferences
	 */
	@Override
	public DateTimePrefs getDateTime() {
		return (DateTimePrefs) datetime;
	}

	/**
	 * @param dateTimePrefsObject
	 *            The trip's new date and time preferences
	 */
	public void setDateTime(IDateTimePrefs dateTimePrefsObject) {
		this.datetime = new DateTimePrefs().newInstance(dateTimePrefsObject);
	}

	/**
	 * @return The trip's return date and time preferences
	 */
	@Override
	public DateTimePrefs getReturnDateTime() {
		return hasReturnDateTime() ? (DateTimePrefs) returnDatetime : null;
	}

	/**
	 * @param dateTimePrefsObject
	 *            The trip's new date and time preferences
	 */
	public void setReturnDateTime(IDateTimePrefs dateTimePrefsObject) {
		this.returnDatetime = new DateTimePrefs()
				.newInstance(dateTimePrefsObject);
	}

	/**
	 * @return The user who requested the trip
	 */
	public User getRequester() {
		return requester;
	}

	/**
	 * @param requester
	 *            The updated user who requested the trip
	 */
	public void setRequester(IUser requester) {
		this.requester = new User().newInstance(requester);
	}

	/**
	 * @return The number of seats requested for the trip by its requester
	 */
	public int getRequestedSeats() {
		return nSeats;
	}

	/**
	 * @param requestedSeats
	 *            The new number of seats requested for the trip by its
	 *            requester
	 */
	public void setRequestedSeats(int requestedSeats) {
		this.nSeats = requestedSeats;
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof TripRequest) {
			TripRequest t = (TripRequest) o;
			/**
			 * Two trips are compared in terms of their origin and destination
			 * places, their date and time respective preferences and their
			 * respective requesters
			 */
			eq = from.equals(t.getFromPlace()) && to.equals(t.getToPlace())
					&& datetime.equals(t.getDateTime())
					&& requester.equals(t.getRequester());
		}
		return eq;
	}

	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder(super.getDescription());
		sb.append(requester.toString() + '\n');
		return sb.toString();
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeParcelable(requester, flags);
	}

	public static final Parcelable.Creator<TripRequest> CREATOR = new Parcelable.Creator<TripRequest>() {

		public TripRequest createFromParcel(Parcel in) {
			return new TripRequest(in);
		}

		public TripRequest[] newArray(int size) {
			return new TripRequest[size];
		}
	};

	protected TripRequest(Parcel in) {
		from = in.readParcelable(Place.class.getClassLoader());
		to = in.readParcelable(Place.class.getClassLoader());
		datetime = in.readParcelable(DateTimePrefs.class.getClassLoader());
		returnDatetime = in.readByte() != 0 ? (DateTimePrefs) in
				.readParcelable(DateTimePrefs.class.getClassLoader()) : null;
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

		requester = in.readParcelable(User.class.getClassLoader());
	}
}
