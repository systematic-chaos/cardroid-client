package uclm.esi.cardroid.data.android;

import java.sql.Blob;

import android.os.Parcel;
import android.os.Parcelable;

import uclm.esi.cardroid.data.ILatLng;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.IWaypoint;
import uclm.esi.cardroid.data.android.Place.Coordinates;

/**
 * \class Waypoint
 * Persistence class that represents a Waypoint in a Trip 's route
 */
public class Waypoint implements Parcelable, IWaypoint {
	private Place placeWaypoint;
	private int nOrder;

	/**
	 * Default constructor
	 * @param waypointPlace The Waypoint place
	 * @param nOrder The order of waypointPlace in the Trip 's 
	 * 					waypoint collection
	 */
	public Waypoint(Place waypointPlace, int nOrder) {
		this.placeWaypoint = waypointPlace;
		this.nOrder = nOrder;
	}

	public Waypoint() {
	}

	@Override
	public Place newInstance(IPlace placeObject) {
		return new Place().newInstance(placeObject);
	}

	@Override
	public void setName(String name) {
		placeWaypoint.setName(name);
	}

	@Override
	public String getName() {
		return placeWaypoint.getName();
	}

	@Override
	public void setCoordinates(ILatLng coords) {
		placeWaypoint.setCoordinates(coords);
	}

	@Override
	public Coordinates getCoordinates() {
		return placeWaypoint.getCoordinates();
	}

	@Override
	public void setDescription(String description) {
		placeWaypoint.setDescription(description);
	}

	@Override
	public String getDescription() {
		return placeWaypoint.getDescription();
	}

	@Override
	public boolean hasDescription() {
		return placeWaypoint.hasDescription();
	}

	@Override
	public void setSnapshot(Blob bitmapObject) {
		placeWaypoint.setSnapshot(bitmapObject);
	}

	@Override
	public Blob getSnapshot() {
		return placeWaypoint.getSnapshot();
	}

	@Override
	public boolean hasSnapshot() {
		return placeWaypoint.hasSnapshot();
	}

	@Override
	public Waypoint newInstance(IWaypoint waypointObject) {
		if (waypointObject == null)
			return null;
		if (waypointObject instanceof Waypoint)
			return (Waypoint) waypointObject;

		Waypoint waypoint = null;
		Place waypointPlace = new Place().newInstance(waypointObject
				.getPlaceWaypoint());
		waypoint = new Waypoint(waypointPlace, waypointObject.getNOrder());
		return waypoint;
	}

	@Override
	public void setNOrder(int nOrder) {
		this.nOrder = nOrder;
	}

	@Override
	public int getNOrder() {
		return nOrder;
	}

	@Override
	public void setPlaceWaypoint(IPlace placeWaypoint) {
		this.placeWaypoint = new Place().newInstance(placeWaypoint);
	}

	@Override
	public Place getPlaceWaypoint() {
		return placeWaypoint;
	}

	@Override
	public boolean equals(Object o) {
		boolean eq = false;
		if (o != null && o instanceof IPlace)
			eq = placeWaypoint.equals((IPlace) o);
		return eq;
	}

	@Override
	public String toString() {
		return nOrder + '\t' + placeWaypoint.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(nOrder);
		out.writeParcelable(placeWaypoint, flags);
	}

	public static Parcelable.Creator<Waypoint> CREATOR = new Parcelable.Creator<Waypoint>() {

		public Waypoint createFromParcel(Parcel in) {
			return new Waypoint(in);
		}

		public Waypoint[] newArray(int size) {
			return new Waypoint[size];
		}
	};

	private Waypoint(Parcel in) {
		nOrder = in.readInt();
		placeWaypoint = in.readParcelable(Place.class.getClassLoader());
	}
}
