package uclm.esi.cardroid.util;

import android.location.Location;

/**
 * \interface Geolocator
 * Make a class to implement some geolocation services
 * related functions.
 */
public interface Geolocator {
	/**
	 * Check the enabled state of the location services on the device.
	 * 
	 * @return Whether location services are enabled
	 */
	public boolean areLocationServicesEnabled();

	/**
	 * Query the geolocation services to obtain the device's geographical
	 * location.
	 * 
	 * @return The device's geographical location
	 */
	public Location geolocate();
}
