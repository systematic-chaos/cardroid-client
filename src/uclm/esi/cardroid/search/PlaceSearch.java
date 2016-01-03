package uclm.esi.cardroid.search;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;
import android.util.Log;

/**
 * \class PlaceSearch
 * Class providing features for retrieving places data from
 * the Google Places API
 */
public class PlaceSearch {

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String TYPE_NEARBY_SEARCH = "/nearbysearch";
	private static final String TYPE_DETAILS = "/details";
	private static final String OUT_JSON = "/json?";
	private static String LOG_TAG;
	private static String API_KEY;

	private static PlaceSearch ps = null;

	private PlaceSearch() {
	}

	/**
	 * Singleton pattern implementation for construction.
	 * 
	 * @param LT
	 *            Log tag
	 * @param AK
	 *            Api key
	 */
	public static PlaceSearch getInstance(final String LT, final String AK) {
		if (ps == null) {
			LOG_TAG = LT;
			API_KEY = AK;
			ps = new PlaceSearch();
		}
		return ps;
	}

	/**
	 * Perform a HTTP request to the Google Places service to get the
	 * autocomplete results to the provided input
	 * 
	 * @param input
	 *            User's hint input
	 * @param sensor
	 *            Whether the provided input was obtained via a sensor device
	 * @return JSONArray containing the autocomplete results to input
	 */
	public JSONArray autocomplete(String input, boolean sensor) {
		JSONArray result = null;
		URL url = null;

		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("input=" + URLEncoder.encode(input, "utf8"));
			sb.append("&types=(cities)");
			sb.append("&sensor=" + sensor);
			sb.append("&key=" + API_KEY);

			url = new URL(sb.toString());
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
		} catch (UnsupportedEncodingException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
		}

		SearchPlaceTask searchPlace = new SearchPlaceTask();
		searchPlace.execute(url);

		try {
			result = searchPlace.get().getJSONArray("predictions");
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "HTTP query failed");
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, "HTTP query failed");
		}

		return result;
	}

	/**
	 * Perform a HTTP request to the Google Places service to get the nearby
	 * search results to the provided location and radius
	 * 
	 * @param location
	 *            The location from which the nearby search will be placed
	 * @param radius
	 *            The distance, expressed in meters, of the nearby search scope
	 * @param sensor
	 *            Whether the provided location was obtained via a sensor device
	 * @return JSONArray containing the nearby search results
	 */
	public JSONArray nearbySearch(LatLng location, int radius, boolean sensor) {
		JSONArray result = null;
		URL url = null;

		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_NEARBY_SEARCH + OUT_JSON);
			sb.append("key=" + API_KEY);
			sb.append("&location=" + location.latitude + ","
					+ location.longitude);
			sb.append("&radius=" + radius);
			sb.append("&sensor=" + sensor);

			url = new URL(sb.toString());
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
		}

		SearchPlaceTask searchPlace = new SearchPlaceTask();
		searchPlace.execute(url);

		try {
			result = searchPlace.get().getJSONArray("results");
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "HTTP query failed");
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, "HTTP query failed");
		}

		return result;
	}

	/**
	 * Perform a HTTP request to the Google Places service to get the details
	 * about the place referenced by the reference parameter
	 * @param reference Reference to the place whose details are requested
	 * @param sensor Whether the provided reference was obtained via a sensor device
	 * @return JSONObject containing the details of the queried place
	 */
	public JSONObject details(String reference, boolean sensor) {
		JSONObject result = null;
		URL url = null;

		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS
					+ OUT_JSON);
			sb.append("key=" + API_KEY);
			sb.append("&reference=" + reference);
			sb.append("&sensor=" + sensor);

			url = new URL(sb.toString());
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
		}

		SearchPlaceTask searchPlace = new SearchPlaceTask();
		searchPlace.execute(url);

		try {
			result = searchPlace.get().getJSONObject("result");
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "HTTP query failed");
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, "HTTP query failed");
		}

		return result;
	}

	/**
	 * \class SearchPlaceTask
	 * A subclass of AsyncTask that establishes HTTP connections to the Google
	 * Places service in the background. The class definition has these generic
	 * types:
	 * URL - Specifies the URL of the connection to be established
	 * Void - Indicates that progress units are not used
	 * JSONObject - The result passed to onPostExecute()
	 */
	private class SearchPlaceTask extends AsyncTask<URL, Void, JSONObject> {

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
				Log.e(LOG_TAG, "Error connecting to Places API", e);
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
	}
}
