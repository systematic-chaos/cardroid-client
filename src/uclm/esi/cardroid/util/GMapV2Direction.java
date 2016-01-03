package uclm.esi.cardroid.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.util.Log;

/**
 * \class GMapV2Direction
 * Class providing features for retrieving routes and
 * directions data from the Google Maps V2 API
 */
public class GMapV2Direction {
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";
	public final static String MODE_BICYCLING = "bicycling";

	private static final String DIRECTIONS_API_BASE = "https://maps.googleapis.com/maps/api/directions";
	private static final String OUT_JSON = "/json?";
	private String LOG_TAG;

	private DirectionSearchDisplayer parentContext;

	/**
	 * Class constructor.
	 * 
	 * @param parent
	 *            The invoking class, which must implement the
	 *            DirectionSearchDisplayer interface, in order to be backcalled
	 *            when the results of asynchronous operations performed by this
	 *            class are ready
	 * @param LT
	 *            Log tag
	 */
	public GMapV2Direction(DirectionSearchDisplayer parent, final String LT) {
		parentContext = parent;
		LOG_TAG = LT;
	}

	/**
	 * Launch a HTTP request to the Google Directions service to obtain a
	 * direction based on the coordinates pair and mode provided. The results to
	 * this query will be returned in the form of a JSONArray routes array via
	 * the DirectionSearchDisplayer.displayRoute of the parentContext, which
	 * will be called when the results to the query (launched in an AsyncTask)
	 * are ready
	 * 
	 * @param start
	 *            The origin place coordinates
	 * @param end
	 *            The destination place coordinates
	 * @param route
	 *            The intermediate points on the route (a.k.a. waypoints)
	 * @param sensor
	 *            Whether the coordinate information provided has been obtained
	 *            via a sensor, such as WiFi location or GPS
	 * @param mode
	 *            The mode on which the route must be calculated. Must be one of
	 *            MODE_DRIVING , MODE_WALKING and MODE_BICYCLING
	 */
	public void retrieveDirection(LatLng start, LatLng end,
			ArrayList<LatLng> route, boolean sensor, String mode) {
		URL url = null;

		StringBuilder sb = new StringBuilder(DIRECTIONS_API_BASE + OUT_JSON);
		sb.append("origin=" + start.latitude + "," + start.longitude);
		sb.append("&destination=" + end.latitude + "," + end.longitude);
		if (route.size() > 0) {
			sb.append("&waypoints=optimize:true|");
			for (int n = 0; n < route.size(); n++) {
				LatLng loc = route.get(n);
				sb.append("via:" + loc.latitude + "," + loc.longitude + "|");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("&sensor=" + sensor);
		sb.append("&avoid=tolls");
		sb.append("&units=metric");
		sb.append("&mode=" + mode);

		try {
			url = new URL(sb.toString());
		} catch (MalformedURLException murle) {
			Log.e(LOG_TAG, "Error processing Directions API URL", murle);
		}

		SearchDirectionTask searchDirection = new SearchDirectionTask();
		searchDirection.execute(url);
	}

	/**
	 * Convenience method for retrieveDirection(LatLng, LatLng,
	 * ArrayList<LatLng>, boolean, String) without providing intermediate route
	 * points (a.k.a. waypoints)
	 * 
	 * @param start
	 *            The origin place coordinates
	 * @param end
	 *            The destination place coordinates
	 * @param sensor
	 *            Whether the coordinate information provided has been obtained
	 *            via a sensor, such as WiFi location or GPS
	 * @param mode
	 *            The mode on which the route must be calculated. Must be one of
	 *            MODE_DRIVING , MODE_WALKING and MODE_BICYCLING
	 */
	public void retrieveDirection(LatLng start, LatLng end, boolean sensor,
			String mode) {
		retrieveDirection(start, end, new ArrayList<LatLng>(), sensor, mode);
	}

	/**
	 * Return the duration text contained on the JSONObject provided (which must
	 * an element of the JSONArray of type legs)
	 * 
	 * @return The duration text contained on the JSONObject provided
	 */
	public String getDurationText(JSONObject jsono) {
		String durationText = null;

		try {
			durationText = jsono.getJSONObject("duration").getString("text");
		} catch (JSONException jsone) {
			Log.e(LOG_TAG, "Cannot process JSON results", jsone);
		}

		return durationText;
	}

	/**
	 * Return the duration value contained on the JSONObject provided (which
	 * must an element of the JSONArray of type legs)
	 * 
	 * @return The duration value contained on the JSONObject provided
	 */
	public int getDurationValue(JSONObject jsono) {
		int durationValue = -1;

		try {
			durationValue = jsono.getJSONObject("duration").getInt("value");
		} catch (JSONException jsone) {
			Log.e(LOG_TAG, "Cannot process JSON results", jsone);
		}

		return durationValue;
	}

	/**
	 * Return the distance text contained on the JSONObject provided (which must
	 * an element of the JSONArray of type legs)
	 * 
	 * @return The distance text contained on the JSONObject provided
	 */
	public String getDistanceText(JSONObject jsono) {
		String distanceText = null;

		try {
			distanceText = jsono.getJSONObject("distance").getString("text");
		} catch (JSONException jsone) {
			Log.e(LOG_TAG, "Cannot process JSON results", jsone);
		}

		return distanceText;
	}

	/**
	 * Return the distance value contained on the JSONObject provided (which
	 * must an element of the JSONArray of type legs)
	 * 
	 * @return The distance value contained on the JSONObject provided
	 */
	public int getDistanceValue(JSONObject jsono) {
		int distanceValue = -1;

		try {
			distanceValue = jsono.getJSONObject("distance").getInt("value");
		} catch (JSONException jsone) {
			Log.e(LOG_TAG, "Cannot process JSON results", jsone);
		}

		return distanceValue;
	}

	/**
	 * Return the start location coordinates pair contained on the JSONObject
	 * provided (which must an element of the JSONArray of type legs)
	 * 
	 * @return The start location coordinates pair contained on the JSONObject
	 *         provided
	 */
	public LatLng getStartLocation(JSONObject jsono) {
		LatLng startLocation = null;

		try {
			JSONObject location = jsono.getJSONObject("start_location");
			startLocation = new LatLng(location.getDouble("lat"),
					location.getDouble("lng"));
		} catch (JSONException jsone) {
			Log.e(LOG_TAG, "Cannot process JSON results", jsone);
		}

		return startLocation;
	}

	/**
	 * Return the end location coordinates contained on the JSONObject provided
	 * (which must an element of the JSONArray of type legs)
	 * 
	 * @return The end location coordinates contained on the JSONObject provided
	 */
	public LatLng getEndLocation(JSONObject jsono) {
		LatLng endLocation = null;

		try {
			JSONObject location = jsono.getJSONObject("end_location");
			endLocation = new LatLng(location.getDouble("lat"),
					location.getDouble("lng"));
		} catch (JSONException jsone) {
			Log.e(LOG_TAG, "Cannot process JSON results", jsone);
		}

		return endLocation;
	}

	/**
	 * Return the copyrights contained on the JSONObject provided (which must an
	 * element of the JSONArray of type routes)
	 * 
	 * @return The copyrights contained on the JSONObject provided
	 */
	public String getCopyRights(JSONObject jsono) {
		String copyRights = null;

		try {
			copyRights = jsono.getString("copyrights");
		} catch (JSONException jsone) {
			Log.e(LOG_TAG, "Cannot process JSON results", jsone);
		}

		return copyRights;
	}

	/**
	 * Return the list of coordinate pairs defining a route, as it is contained
	 * on the JSONObject provided (which must an element of the JSONArray of
	 * type legs)
	 * 
	 * @return The list of coordinate pairs defining a route, as it is contained
	 *         on the JSONObject provided
	 */
	public ArrayList<LatLng> getDirection(JSONObject jsono) {
		ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();

		try {
			JSONArray steps = jsono.getJSONArray("steps");
			if (steps.length() > 0) {
				for (int i = 0; i < steps.length(); i++) {
					JSONObject node = steps.getJSONObject(i);
					JSONObject location = node.getJSONObject("start_location");
					listGeopoints.add(new LatLng(location.getDouble("lat"),
							location.getDouble("lng")));

					ArrayList<LatLng> arr = decodePoly(node.getJSONObject(
							"polyline").getString("points"));
					for (int j = 0; j < arr.size(); j++) {
						listGeopoints.add(new LatLng(arr.get(j).latitude, arr
								.get(j).longitude));
					}

					location = node.getJSONObject("end_location");
					listGeopoints.add(new LatLng(location.getDouble("lat"),
							location.getDouble("lng")));
				}
			}
		} catch (JSONException jsone) {
			Log.e(LOG_TAG, "Cannot process JSON results", jsone);
		}

		return listGeopoints;
	}

	/**
	 * Decode a polyline reference in an arraylist of points, each one of them
	 * defined by a coordinates pair
	 */
	private ArrayList<LatLng> decodePoly(String encoded) {
		ArrayList<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
			poly.add(position);
		}
		return poly;
	}

	/**
	 * \class SearchDirectionTask
	 * A subclass of AsyncTask that establishes HTTP
	 * connections to the Google Directions service in the background. The class
	 * definition has these generic types:
	 * URL - Specifies the URL of the connection to be established
	 * Void - Indicates that progress units are not used
	 * JSONObject - The result passed to onPostExecute()
	 */
	private class SearchDirectionTask extends AsyncTask<URL, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(URL... urls) {
			JSONObject result = null;

			HttpURLConnection conn = null;
			StringBuilder jsonResults = new StringBuilder();

			try {
				conn = (HttpURLConnection) urls[0].openConnection();
				InputStreamReader in = new InputStreamReader(
						conn.getInputStream());

				// Load the results into a StringBuilder
				int read;
				char[] buff = new char[1024];
				while ((read = in.read(buff)) != -1) {
					jsonResults.append(buff, 0, read);
				}
			} catch (IOException e) {
				Log.e(LOG_TAG, "Error connecting to Directions API", e);
				return result;
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}

			try {
				// Create a JSON object hierarchy from the results
				result = new JSONObject(jsonResults.toString());
			} catch (JSONException e) {
				Log.e(LOG_TAG, "Cannot process JSON results", e);
			}

			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				parentContext.displayRoute(result.getJSONArray("routes"));
			} catch (JSONException jsone) {
				Log.e(LOG_TAG, "Cannot process JSON results", jsone);
			}
		}
	}

	/**
	 * \interface DirectionSearchDisplayer
	 * Classes instantiating this one must
	 * implement this interface, which will enable them to be notified when the
	 * results of a Google Directions query are resolved
	 */
	public interface DirectionSearchDisplayer {
		/**
		 * Called when the results of a Google Directions query are resolved and
		 * ready
		 * 
		 * @param routes
		 *            JSONArray routes array containing the results to the
		 *            Directions query asked by the DirectionSearchDisplayer
		 *            object called
		 */
		public void displayRoute(JSONArray routes);
	}
}
