package uclm.esi.cardroid.data.ice;

import java.sql.Blob;
import java.sql.SQLException;

import uclm.esi.cardroid.util.SerialBlob;

import android.util.Log;

import Ice.Current;
import Ice.Identity;
import Ice.ObjectAdapter;
import Ice.ObjectFactory;

import uclm.esi.cardroid.data.ILatLng;
import uclm.esi.cardroid.data.IPlace;
import uclm.esi.cardroid.data.zerocice.LatLngTyp;
import uclm.esi.cardroid.data.zerocice.PlaceTyp;
import uclm.esi.cardroid.data.zerocice.PlaceTypPrx;
import uclm.esi.cardroid.data.zerocice.PlaceTypPrxHelper;

/**
 * \class Place
 * Domain class implementing a Place for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class Place extends PlaceTyp implements IPlace, ObjectFactory {
	private static final long serialVersionUID = -3644730415012574777L;

	public Place() {
	}

	/**
	 * Short constructor
	 */
	public Place(String name, LatLngTyp coords) {
		super(name, coords);
	}

	/**
	 * Long constructor
	 */
	public Place(String name, LatLngTyp coords, String description,
			byte[] snapshot) {
		super(name, coords, description, snapshot);
	}

	public Place(PlaceTyp place) {
		this(place.name, place.coords);
		assert place != null;
		if (place.hasPlaceDescription())
			this.setPlaceDescription(place.getPlaceDescription());
		if (place.hasPlaceSnapshotBytes())
			this.setPlaceSnapshotBytes(place.getPlaceSnapshotBytes());
	}

	/**
	 *  @return An Ice Identity for this datatype category and the data provided
	 */
	public static Identity createIdentity(String name, double latitude,
			double longitude) {
		Identity id = new Identity();
		id.category = "place";
		id.name = name + " [" + latitude + ", " + longitude + "]";
		return id;
	}

	/**
	 * @return A proxy to an Ice Object incarnating the provided data, whose 
	 * 			servant is added to adapter 's active servant map
	 */
	public static PlaceTypPrx createProxy(PlaceTyp placeObject,
			ObjectAdapter adapter) {
		PlaceTypPrx placePrx = PlaceTypPrxHelper.uncheckedCast(adapter
				.createProxy(createIdentity(placeObject.getName(), placeObject
						.getCoords().getLatitude(), placeObject.getCoords()
						.getLongitude())));
		fillProxyData(placeObject, placePrx, adapter);
		return placePrx;
	}

	/**
	 * @return A proxy to an Ice Object incarnating the provided data, whose 
	 * 			servant is added to adapter 's active servant map
	 */
	public static PlaceTypPrx createProxy(IPlace placeObject,
			ObjectAdapter adapter) {
		PlaceTypPrx placePrx = PlaceTypPrxHelper.uncheckedCast(adapter
				.createProxy(createIdentity(placeObject.getName(), placeObject
						.getCoordinates().getLatitude(), placeObject
						.getCoordinates().getLongitude())));
		fillProxyData(placeObject, placePrx, adapter);
		return placePrx;
	}

	/**
	 * Fill the servant referenced by the given proxy with the data contained 
	 * in the given object
	 */
	public static void fillProxyData(PlaceTyp object, PlaceTypPrx proxy,
			ObjectAdapter adapter) {
		proxy.setName(object.getName());
		proxy.setCoords(object.getCoords());
		if (object.hasDescription())
			proxy.setDescription(object.getDescription());
		if (object.hasSnapshot())
			proxy.setSnapshotBytes(object.getSnapshotBytes());
	}

	/**
	 * Fill the servant referenced by the given proxy with the data contained 
	 * in the given object
	 */
	public static void fillProxyData(IPlace object, PlaceTypPrx proxy,
			ObjectAdapter adapter) {
		proxy.setName(object.getName());
		proxy.setCoords(new LatLng().newInstance(object.getCoordinates()));
		if (object.hasDescription())
			proxy.setDescription(object.getDescription());
		if (object.hasSnapshot()) {
			try {
				Blob snapshot = object.getSnapshot();
				proxy.setSnapshotBytes(snapshot.getBytes(1,
						(int) snapshot.length()));
			} catch (SQLException sqle) {
				Log.e("CARDROID", sqle.getMessage());
			}
		}
	}

	/**
	 * @param proxy A proxy to a remote object implementing a Place
	 * @return A local Place object containing the data of the remote object
	 * 			referenced by proxy
	 */
	public static Place extractObject(PlaceTypPrx proxy) {
		Place place = null;

		if (!(proxy.hasDescription() && proxy.hasSnapshot())) {
			place = new Place(proxy.getName(), proxy.getCoords());
			if (!proxy.hasDescription() && !proxy.hasSnapshot())
				return place;
		}

		if (!(proxy.hasDescription() && proxy.hasSnapshot())) {
			if (proxy.hasDescription())
				place.setDescription(proxy.getDescription());
			if (proxy.hasSnapshot())
				place.setSnapshotBytes(proxy.getSnapshotBytes());
		} else
			place = new Place(proxy.getName(), proxy.getCoords(),
					proxy.getDescription(), proxy.getSnapshotBytes());
		return place;
	}

	/* IPlace interface */

	public Place newInstance(IPlace placeObject) {
		if (placeObject == null)
			return null;
		if (placeObject instanceof Place)
			return (Place) placeObject;

		Place place = null;
		String name = placeObject.getName();
		LatLng coords = new LatLng().newInstance(placeObject.getCoordinates());
		if (!(placeObject.hasDescription() && placeObject.hasSnapshot())) {
			place = new Place(name, coords);
			if (!placeObject.hasDescription() && !placeObject.hasSnapshot())
				return place;
		}

		String description = placeObject.getDescription();
		byte[] snapshot = null;
		if (placeObject.hasSnapshot()) {
			try {
				snapshot = placeObject.getSnapshot().getBytes(1,
						(int) placeObject.getSnapshot().length());
			} catch (SQLException sqle) {
				System.err.println("SQLException: " + sqle.getMessage());
			}
		}

		if (!(placeObject.hasDescription() && placeObject.hasSnapshot())) {
			if (placeObject.hasDescription())
				place.setDescription(description);
			if (placeObject.hasSnapshot())
				place.setSnapshotBytes(snapshot);
		} else
			place = new Place(name, coords, description, snapshot);
		return place;
	}

	public void setCoordinates(ILatLng coords) {
		this.coords = new LatLng().newInstance(coords);
	}

	public LatLng getCoordinates() {
		return coords instanceof LatLng ? (LatLng) coords : new LatLng(coords);
	}

	public void setSnapshot(Blob bitmapObject) {
		try {
			setSnapshotBytes(bitmapObject.getBytes(1,
					(int) bitmapObject.length()));
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle.getMessage());
		}
	}

	public Blob getSnapshot() {
		Blob snapshot = null;
		if (hasSnapshot()) {
			try {
				snapshot = new SerialBlob(getSnapshotBytes());
			} catch (SQLException sqle) {
				System.err.println("SQLException: " + sqle.getMessage());
			}
		}
		return snapshot;
	}

	/* Overriding superclass */

	@Override
	public String getName(Current __current) {
		return name;
	}

	@Override
	public void setName(String name, Current __current) {
		this.name = name;
	}

	@Override
	public LatLngTyp getCoords(Current __current) {
		return coords;
	}

	@Override
	public void setCoords(LatLngTyp coords, Current __current) {
		this.coords = coords;
	}

	@Override
	public String getDescription(Current __current) {
		return hasPlaceDescription() ? getPlaceDescription() : null;
	}

	@Override
	public void setDescription(String description, Current __current) {
		setPlaceDescription(description);
	}

	@Override
	public boolean hasDescription(Current __current) {
		return hasPlaceDescription();
	}

	@Override
	public byte[] getSnapshotBytes(Current __current) {
		return hasPlaceSnapshotBytes() ? getPlaceSnapshotBytes() : null;
	}

	@Override
	public void setSnapshotBytes(byte[] snapshotBytes, Current __current) {
		setPlaceSnapshotBytes(snapshotBytes);
	}

	@Override
	public boolean hasSnapshot(Current __current) {
		return hasPlaceSnapshotBytes() && getPlaceSnapshotBytes().length > 0;
	}

	@Override
	public String _toString(Current __current) {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(" [" + coords.latitude);
		builder.append(", " + coords.longitude + "]");
		return builder.toString();
	}

	/* ObjectFactory interface */

	@Override
	public Place create(String type) {
		if (type.equals(ice_staticId())) {
			return new Place();
		}

		return null;
	}

	@Override
	public void destroy() {
	}
}
