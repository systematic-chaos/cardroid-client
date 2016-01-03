package uclm.esi.cardroid.data.ice;

import java.sql.Blob;
import java.sql.SQLException;

import uclm.esi.cardroid.util.SerialBlob;
import uclm.esi.cardroid.util.SerialException;

import uclm.esi.cardroid.data.ILatLng;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.IWaypoint;
import uclm.esi.cardroid.data.zerocice.PlaceTyp;
import uclm.esi.cardroid.data.zerocice.WaypointTyp;
import Ice.Current;
import Ice.ObjectFactory;

/**
 * \class Waypoint
 * Domain class implementing a waypoint for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class Waypoint extends WaypointTyp implements IWaypoint, ObjectFactory {
	private static final long serialVersionUID = -939224739039674266L;

	public Waypoint() {
	}

	/**
	 * Default constructor
	 */
	public Waypoint(int nOrder, PlaceTyp waypointPlace) {
		super(nOrder, waypointPlace);
	}

	public Waypoint(WaypointTyp waypoint) {
		this(waypoint.nOrder, waypoint.waypointPlace);
	}

	/* IWaypoint interface */

	public Place newInstance(IPlace placeObject) {
		return new Place().newInstance(placeObject);
	}

	public void setName(String name) {
		waypointPlace.setName(name);
	}

	public String getName() {
		return waypointPlace.getName();
	}

	public void setCoordinates(ILatLng coords) {
		waypointPlace.setCoords(new LatLng().newInstance(coords));
	}

	public LatLng getCoordinates() {
		return new LatLng(waypointPlace.getCoords());
	}

	public void setDescription(String description) {
		waypointPlace.setDescription(description);
	}

	public String getDescription() {
		return waypointPlace.getDescription();
	}

	public boolean hasDescription() {
		return waypointPlace.hasDescription();
	}

	public void setSnapshot(Blob bitmapObject) {
		byte[] snapshot = null;
		try {
			snapshot = bitmapObject.getBytes(1, (int) bitmapObject.length());
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
		}
		waypointPlace.setSnapshotBytes(snapshot);
	}

	public Blob getSnapshot() {
		Blob snapshot = null;
		if (waypointPlace.hasSnapshot()) {
			try {
				snapshot = new SerialBlob(waypointPlace.getSnapshotBytes());
			} catch (SerialException se) {
				System.err.println("SerialException: " + se.getMessage());
			} catch (SQLException sqle) {
				System.err.println("SQLException: " + sqle.getMessage());
			}
		}
		return snapshot;
	}

	public boolean hasSnapshot() {
		return waypointPlace.hasSnapshot();
	}

	public Waypoint newInstance(IWaypoint waypointObject) {
		return new Waypoint(waypointObject.getNOrder(),
				new Waypoint().newInstance(waypointObject.getPlaceWaypoint()));
	}

	public void setPlaceWaypoint(IPlace placeWaypoint) {
		setWaypointPlace(new Place().newInstance(placeWaypoint));
	}

	public Place getPlaceWaypoint() {
		return waypointPlace instanceof Place ? (Place) waypointPlace
				: new Place(waypointPlace);
	}

	/* Overriding superclass */

	@Override
	public int getNOrder(Current __current) {
		return nOrder;
	}

	@Override
	public void setNOrder(int nOrder, Current __current) {
		this.nOrder = nOrder;
	}

	@Override
	public PlaceTyp getWaypointPlace(Current __current) {
		return waypointPlace;
	}

	@Override
	public void setWaypointPlace(PlaceTyp waypointPlace, Current __current) {
		this.waypointPlace = waypointPlace;
	}

	@Override
	public String _toString(Current __current) {
		StringBuilder builder = new StringBuilder();
		builder.append(nOrder);
		builder.append(" " + waypointPlace.getName());
		return builder.toString();
	}

	/* ObjectFactory interface */

	@Override
	public Waypoint create(String type) {
		if (type.equals(ice_staticId())) {
			return new Waypoint();
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
