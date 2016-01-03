package uclm.esi.cardroid.data.ice;

import uclm.esi.cardroid.data.ILatLng;
import uclm.esi.cardroid.data.zerocice.LatLngTyp;

/**
 * \class LatLng
 * Domain class implementing a LatLng for its transmission between systems
 * communicating across an Ice network infrastructure
 */
public class LatLng extends LatLngTyp implements ILatLng {

	private static final long serialVersionUID = 1665309767560539369L;

	public LatLng() {
	}

	/**
	 * Default constructor
	 */
	public LatLng(double latitude, double longitude) {
		super(latitude, longitude);
	}

	public LatLng(LatLngTyp latLng) {
		this(latLng.latitude, latLng.longitude);
	}

	/* ILatLng interface */

	public LatLng newInstance(ILatLng latLngObject) {
		if (latLngObject == null)
			return null;
		if (latLngObject instanceof LatLng)
			return (LatLng) latLngObject;
		double latitude = latLngObject.getLatitude();
		double longitude = latLngObject.getLongitude();
		return new LatLng(latitude, longitude);
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

}
