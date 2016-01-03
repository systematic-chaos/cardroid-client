package uclm.esi.cardroid.data.android;

import java.sql.Timestamp;
import java.util.Hashtable;

import android.os.Parcel;
import android.os.Parcelable;
import uclm.esi.cardroid.data.ITripOffer;
import uclm.esi.cardroid.data.IUser;
import uclm.esi.cardroid.data.IUserActivity;

/**
 * \class UserActivity
 * Persistence class that represents the data detailing an
 * activity carried out by an user
 */
public class UserActivity implements Parcelable, IUserActivity {

	private User user;
	private TripOffer trip;
	private ActivityType type;
	private Timestamp timestamp;
	private static Hashtable<ActivityType, String> activityDescription;
	private static final String[] TYPE_DESCRIPTION = {
			" se ha apuntado a tu viaje ",
			" ha publicado un viaje en respuesta a tu petición ",
			" ha aceptado tu incorporación a su viaje "
					+ " ha rechazado tu incorporación a su viaje " };

	/**
	 * Constructor. An user activity is characterized by the user who carried it
	 * out, the trip it refers to and the type of the user activity itself.
	 */
	public UserActivity(User user, TripOffer trip, ActivityType type) {
		this(user, trip, type, new java.util.Date().getTime());
	}

	public UserActivity(User user, TripOffer trip, ActivityType type,
			long timestamp) {
		this.user = user;
		this.trip = trip;
		this.type = type;
		this.timestamp = new Timestamp(timestamp);

		if (activityDescription == null) {
			activityDescription = new Hashtable<UserActivity.ActivityType, String>(
					ActivityType.values().length, 1);
			ActivityType[] types = ActivityType.values();
			for (int n = 0; n < types.length; n++) {
				activityDescription.put(types[n], TYPE_DESCRIPTION[n]);
			}
		}
	}

	public UserActivity newInstance(IUserActivity userActivityObject) {
		if (userActivityObject == null)
			return null;
		if (userActivityObject instanceof UserActivity)
			return (UserActivity) userActivityObject;

		User activityUser = new User()
				.newInstance(userActivityObject.getUser());
		TripOffer activityTrip = new TripOffer().newInstance(userActivityObject
				.getTrip());
		return new UserActivity(activityUser, activityTrip,
				userActivityObject.getType(), userActivityObject.getTimeStamp()
						.getTime());
	}

	/**
	 * @return The user who carried out the activity
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            The updated user who carried out the activity
	 */
	public void setUser(IUser user) {
		this.user = new User().newInstance(user);
	}

	/**
	 * @return The trip referenced by this activity instance
	 */
	public TripOffer getTrip() {
		return trip;
	}

	/**
	 * @param trip
	 *            The trip referenced by this activity instance
	 */
	public void setTrip(ITripOffer trip) {
		this.trip = new TripOffer().newInstance(trip);
	}

	/**
	 * @return The type of the user activity carried out.
	 */
	public ActivityType getType() {
		return type;
	}

	/**
	 * @param type
	 *            The updated type of the user activity carried out.
	 */
	public void setType(ActivityType type) {
		this.type = type;
	}

	public Timestamp getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o instanceof UserActivity) {
			UserActivity ua = (UserActivity) o;
			eq = user.equals(ua.getUser()) && trip.equals(ua.getTrip())
					&& type.equals(ua.getType())
					&& timestamp.equals(ua.getTimeStamp());
		}
		return eq;
	}

	@Override
	public String toString() {
		return user.toString() + activityDescription.get(type)
				+ trip.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(getUser(), flags);
		out.writeParcelable(trip, flags);
		out.writeString(type.name());
		out.writeLong(timestamp.getTime());
	}

	public static final Parcelable.Creator<UserActivity> CREATOR = new Parcelable.Creator<UserActivity>() {

		public UserActivity createFromParcel(Parcel in) {
			return new UserActivity(in);
		}

		public UserActivity[] newArray(int size) {
			return new UserActivity[size];
		}
	};

	private UserActivity(Parcel in) {
		user = in.readParcelable(User.class.getClassLoader());
		trip = in.readParcelable(Trip.class.getClassLoader());
		type = ActivityType.valueOf(in.readString());
		timestamp = new Timestamp(in.readLong());
	}
}
