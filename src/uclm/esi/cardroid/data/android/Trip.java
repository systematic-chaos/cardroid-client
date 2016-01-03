package uclm.esi.cardroid.data.android;

import android.os.Parcel;
import android.os.Parcelable;
import uclm.esi.cardroid.data.IDate;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.ITrip;

/**
 * \class Trip
 * Persistence class that represent a trip essential data
 */
public class Trip implements Parcelable, ITrip {

	protected int tripId;
	protected Place from, to;
	protected Date datetime, returnDatetime;
	protected int nSeats;
	protected int tripType;
	protected char[] weekDays;
	protected Periodicity periodicity;
	protected int distance = 0;
	protected String characteristics;

	public static final int TRIP_TYPE = 0, TRIP_OFFER_TYPE = 1,
			TRIP_REQUEST_TYPE = 2;

	/**
	 * Simple constructor.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date
	 * @param availableSeats
	 *            The number of seats offered/requested
	 */
	public Trip(Place from, Place to, Date datetime, int availableSeats,
			int tripType) {
		this(-1, from, to, datetime, availableSeats, tripType);
	}

	public Trip(int tripId, Place from, Place to, Date datetime,
			int availableSeats, int tripType) {
		this.tripId = tripId;
		this.from = from;
		this.to = to;
		this.datetime = datetime;
		this.nSeats = availableSeats;
		this.tripType = tripType;
		characteristics = "";
	}

	/**
	 * Constructor for a two-way trip.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date
	 * @param returnDatetime
	 *            The trip's return date
	 * @param nSeats
	 *            The number of seats offered/requested
	 */
	public Trip(Place from, Place to, Date datetime, Date returnDatetime,
			int nSeats, int tripType) {
		this(-1, from, to, datetime, returnDatetime, nSeats, tripType);
	}

	public Trip(int tripId, Place from, Place to, Date datetime,
			Date returnDatetime, int nSeats, int tripType) {
		this(tripId, from, to, datetime, nSeats, tripType);
		this.returnDatetime = returnDatetime;
	}

	/**
	 * Constructor for a periodic trip.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date
	 * @param nSeats
	 *            The number of seats offered/requested
	 * @param weekDays
	 *            Those days of the week on which the trip will take place
	 * @param periodicity
	 *            Those weeks on which the trip will take place (an instance of
	 *            Periodicity)
	 */
	public Trip(Place from, Place to, Date datetime, int nSeats, int tripType,
			char[] weekDays, Periodicity periodicity) {
		this(-1, from, to, datetime, nSeats, tripType, weekDays, periodicity);
	}

	public Trip(int tripId, Place from, Place to, Date datetime, int nSeats,
			int tripType, char[] weekDays, Periodicity periodicity) {
		this(tripId, from, to, datetime, nSeats, tripType);
		this.weekDays = weekDays;
		this.periodicity = periodicity;
	}

	/**
	 * Constructor for a two-way periodic trip.
	 * 
	 * @param from
	 *            The trip's origin place
	 * @param to
	 *            The trip's destination place
	 * @param datetime
	 *            The trip's date
	 * @param returnDatetime
	 *            The trip's return date
	 * @param nSeats
	 *            The number of seats offered/requested
	 * @param weekDays
	 *            Those days of the week on which the trip will take place
	 * @param periodicity
	 *            Those weeks on which the trip will take place (an instance of
	 *            Periodicity)
	 */
	public Trip(Place from, Place to, Date datetime, Date returnDatetime,
			int nSeats, int tripType, char[] weekDays, Periodicity periodicity) {
		this(-1, from, to, datetime, returnDatetime, nSeats, tripType,
				weekDays, periodicity);
	}

	public Trip(int tripId, Place from, Place to, Date datetime,
			Date returnDatetime, int nSeats, int tripType, char[] weekDays,
			Periodicity periodicity) {
		this(tripId, from, to, datetime, nSeats, tripType);
		this.returnDatetime = returnDatetime;
		this.weekDays = weekDays;
		this.periodicity = periodicity;
	}

	public Trip(Place from, Place to, Date datetime, Date returnDatetime,
			int nSeats, int tripType, char[] weekDays, Periodicity periodicity,
			int distance, String characteristics) {
		this(-1, from, to, datetime, returnDatetime, nSeats, tripType,
				weekDays, periodicity, distance, characteristics);
	}

	public Trip(int tripId, Place from, Place to, Date datetime,
			Date returnDatetime, int nSeats, int tripType, char[] weekDays,
			Periodicity periodicity, int distance, String characteristics) {
		this(tripId, from, to, datetime, returnDatetime, nSeats, tripType,
				weekDays, periodicity);
		this.distance = distance;
		this.characteristics = characteristics;
	}

	public Trip() {
	}

	public Trip newInstance(ITrip tripObject) {
		if (tripObject == null)
			return null;
		if (tripObject instanceof Trip)
			return (Trip) tripObject;

		Trip trip = null;
		Place fromPlace = new Place().newInstance(tripObject.getFromPlace());
		Place toPlace = new Place().newInstance(tripObject.getToPlace());
		Date tripDatetime = new Date().newInstance(tripObject.getDateTime());
		Date tripReturnDatetime = tripObject.hasReturnDateTime() ? new Date()
				.newInstance(tripObject.getReturnDateTime()) : null;

		if (!tripObject.hasReturnDateTime()
				&& !tripObject.hasWeekDaysPeriodicity()
				&& !tripObject.hasDistance()
				&& !tripObject.hasCharacteristics()) {
			trip = new Trip(tripObject.getTripId(), fromPlace, toPlace,
					tripDatetime, tripObject.getNSeats(),
					tripObject.getTripType());
			return trip;
		}
		if (tripObject.hasReturnDateTime()
				&& !tripObject.hasWeekDaysPeriodicity()
				&& !tripObject.hasDistance()
				&& !tripObject.hasCharacteristics()) {
			trip = new Trip(tripObject.getTripId(), fromPlace, toPlace,
					tripDatetime, tripReturnDatetime, tripObject.getNSeats(),
					tripObject.getTripType());
			return trip;
		}
		if (!tripObject.hasReturnDateTime()
				&& tripObject.hasWeekDaysPeriodicity()
				&& !tripObject.hasDistance()
				&& !tripObject.hasCharacteristics()) {
			trip = new Trip(tripObject.getTripId(), fromPlace, toPlace,
					tripDatetime, tripObject.getNSeats(),
					tripObject.getTripType(), tripObject.getWeekDays(),
					tripObject.getPeriodicity());
			return trip;
		}
		if (tripObject.hasReturnDateTime()
				&& tripObject.hasWeekDaysPeriodicity()
				&& !tripObject.hasDistance()
				&& !tripObject.hasCharacteristics()) {
			trip = new Trip(tripObject.getTripId(), fromPlace, toPlace,
					tripDatetime, tripReturnDatetime, tripObject.getNSeats(),
					tripObject.getTripType(), tripObject.getWeekDays(),
					tripObject.getPeriodicity());
			return trip;
		}
		trip = new Trip(tripObject.getTripId(), fromPlace, toPlace,
				tripDatetime, tripReturnDatetime, tripObject.getNSeats(),
				tripObject.getTripType(), tripObject.getWeekDays(),
				tripObject.getPeriodicity(), tripObject.getDistance(),
				tripObject.getCharacteristics());
		return trip;
	}

	public int getTripId() {
		return tripId;
	}

	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

	public boolean hasTripId() {
		return tripId > 0;
	}

	/**
	 * @return The trip's origin place
	 */
	public Place getFromPlace() {
		return from;
	}

	/**
	 * @param from
	 *            The trip's new origin place
	 */
	public void setFromPlace(IPlace from) {
		this.from = new Place().newInstance(from);
	}

	/**
	 * @return The trip's destination place
	 */
	public Place getToPlace() {
		return to;
	}

	/**
	 * @param to
	 *            The trip's new destination place
	 */
	public void setToPlace(IPlace to) {
		this.to = new Place().newInstance(to);
	}

	/**
	 * @return The trip's date
	 */
	public Date getDateTime() {
		return datetime;
	}

	/**
	 * @param datetime
	 *            The trip's new date
	 */
	public void setDateTime(IDate datetime) {
		this.datetime = new Date().newInstance(datetime);
	}

	/**
	 * @return The number of seats offered/requested
	 */
	public int getNSeats() {
		return nSeats;
	}

	/**
	 * @param nSeats
	 *            The new number of seats to be offered/requested
	 */
	public void setNSeats(int nSeats) {
		this.nSeats = nSeats;
	}

	public int getTripType() {
		return tripType;
	}

	public void setTripType(int tripType) {
		this.tripType = tripType;
	}

	/**
	 * @return The trip's return date, if this is a two-way trip (otherwise,
	 *         null will be returned)
	 */
	public Date getReturnDateTime() {
		return hasReturnDateTime() ? returnDatetime : null;
	}

	/**
	 * @param returnDatetime
	 *            The trip's new return date, making this a two-way trip
	 */
	public void setReturnDateTime(IDate returnDatetime) {
		this.returnDatetime = new Date().newInstance(returnDatetime);
	}

	public boolean hasReturnDateTime() {
		return returnDatetime != null;
	}

	/**
	 * @return Those days of the week on which the trip will take place, if this
	 *         is a periodic trip
	 */
	public char[] getWeekDays() {
		return hasWeekDaysPeriodicity() ? weekDays : null;
	}

	/**
	 * @return Those days of the week on which the trip will now take place,
	 *         making this a periodic trip
	 */
	public void setWeekDays(char[] weekDays) {
		this.weekDays = weekDays;
	}

	/**
	 * @return Those weeks on which the trip will take place
	 */
	public Periodicity getPeriodicity() {
		return hasWeekDaysPeriodicity() ? periodicity : null;
	}

	/**
	 * @param weekDays
	 *            Those days of the week on which the trip will now take place,
	 *            making this a periodic trip
	 * @param periodicity
	 *            Those weeks on which this periodic trip will take place
	 */
	public void setWeekDaysPeriodicity(char[] weekDays, Periodicity periodicity) {
		this.weekDays = weekDays;
		this.periodicity = periodicity;
	}

	public boolean hasWeekDaysPeriodicity() {
		return weekDays != null && periodicity != null;
	}

	/**
	 * @return The distance estimated to exist from% from to% to
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            The distance estimated to exist from% from to% to
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	public boolean hasDistance() {
		return distance > 0;
	}

	/**
	 * @return The trip's additional characteristics and commentaries
	 */
	public String getCharacteristics() {
		return hasCharacteristics() ? characteristics : null;
	}

	/**
	 * @param comments
	 *            The trip's additional characteristics and commentaries
	 */
	public void setCharacteristics(String comments) {
		this.characteristics = comments;
	}

	public boolean hasCharacteristics() {
		return characteristics != null && !"".equals(characteristics);
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o != null && o instanceof Trip) {
			Trip t = (Trip) o;
			/**
			 * Two trips are compared in terms of their origin and destination
			 * places, and their respective dates
			 */
			eq = from.equals(t.getFromPlace()) && to.equals(t.getToPlace())
					&& datetime.equals(t.getDateTime());
		}
		return eq;
	}

	@Override
	public String toString() {
		return from + " - " + to + "\t" + datetime;
	}

	/**
	 * Returns a legible textual representation of the information stored in the
	 * trip data
	 * 
	 * @return A legible textual representation of the trip data
	 */
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(from.getName() + '\n');
		sb.append(to.getName() + '\n');
		sb.append(datetime.toString() + '\n');
		sb.append(nSeats + '\n');
		if (returnDatetime != null) {
			sb.append(returnDatetime.toString() + '\n');
		}
		if (weekDays != null) {
			sb.append(String.valueOf(weekDays) + '\n');
			sb.append(periodicity.name() + '\n');
		}
		if (distance > 0) {
			sb.append(distance + '\n');
		}
		return sb.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(from, flags);
		out.writeParcelable(to, flags);
		out.writeParcelable(datetime, flags);
		out.writeByte((byte) (hasReturnDateTime() ? 1 : 0));
		if (hasReturnDateTime())
			out.writeParcelable(returnDatetime, flags);
		out.writeInt(nSeats);
		out.writeByte((byte) (hasWeekDaysPeriodicity() ? 1 : 0));
		if (hasWeekDaysPeriodicity()) {
			out.writeCharArray(weekDays);
			out.writeString(periodicity.name());
		}
		out.writeByte((byte) (hasDistance() ? 1 : 0));
		out.writeInt(distance);
		out.writeByte((byte) (hasCharacteristics() ? 1 : 0));
		if (hasCharacteristics())
			out.writeString(characteristics);
	}

	public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {

		public Trip createFromParcel(Parcel in) {
			return new Trip(in);
		}

		public Trip[] newArray(int size) {
			return new Trip[size];
		}
	};

	protected Trip(Parcel in) {
		from = in.readParcelable(Place.class.getClassLoader());
		to = in.readParcelable(Place.class.getClassLoader());
		datetime = in.readParcelable(Date.class.getClassLoader());
		returnDatetime = in.readByte() != 0 ? (Date) in
				.readParcelable(Date.class.getClassLoader()) : null;
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
	}
}
