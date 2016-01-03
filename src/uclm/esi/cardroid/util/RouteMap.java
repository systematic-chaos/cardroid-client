package uclm.esi.cardroid.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.util.GMapV2Direction.DirectionSearchDisplayer;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * \class RouteMap
 * Fragment that displays a Google Map and provides a set of
 * methods supporting the addition of trip points (start, end and waypoints) on
 * it and the calculation and drawing of routes interconnecting these points
 */
public class RouteMap extends Fragment implements DirectionSearchDisplayer {

	private View mView;
	private GoogleMap mMap;
	private LatLng mFromCoords, mToCoords;
	private ArrayList<LatLng> mWaypoints;
	private Polyline mRoute;
	private TextView mDistanceText;
	private int mDistance;
	private ProgressBar mActivityIndicator;
	private GMapV2Direction md;

	private static final String STATE_FROMCOORDS = "FROMCOORDS";
	private static final String STATE_TOCOORDS = "TOCOORDS";
	private static final String STATE_WAYPOINTS = "WAYPOINTS";

	/**
	 * Inflate and return the View displayed by the Fragment and setup the UI
	 * widgets, including the Google Map fragment
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.route_map, container, false);
		mDistanceText = (TextView) mView.findViewById(R.id.textViewDistance);
		mActivityIndicator = (ProgressBar) mView
				.findViewById(R.id.route_progress);
		mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		setupMap();

		if (savedInstanceState != null) {
			clearMap();
			if ((mFromCoords = savedInstanceState
					.getParcelable(STATE_FROMCOORDS)) != null)
				drawFromLocation();
			if ((mToCoords = savedInstanceState.getParcelable(STATE_TOCOORDS)) != null)
				drawToLocation();
			mWaypoints = savedInstanceState
					.getParcelableArrayList(STATE_WAYPOINTS);
			calculateRoute();
		} else {
			mFromCoords = mToCoords = null;
			mWaypoints = new ArrayList<LatLng>();
		}
		return mView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(STATE_FROMCOORDS, mFromCoords);
		outState.putParcelable(STATE_TOCOORDS, mToCoords);
		outState.putParcelableArrayList(STATE_WAYPOINTS, mWaypoints);
	}

	/**
	 * Setup the Google Map fragment and customize its UI controls
	 */
	private void setupMap() {
		// Get the GoogleMap from the MapView and initialize it
		mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		UiSettings uiSettings = mMap.getUiSettings();
		uiSettings.setRotateGesturesEnabled(false);
		uiSettings.setTiltGesturesEnabled(false);
		uiSettings.setZoomControlsEnabled(false);
	}

	/**
	 * Clear map, removing all the overlay elements
	 */
	public void clearMap() {
		mFromCoords = null;
		mToCoords = null;
		mWaypoints = new ArrayList<LatLng>();
		mRoute = null;
		mMap.clear();
		mDistanceText.setText(null);
	}

	public GoogleMap getMap() {
		return mMap;
	}

	public void setOnMapLoadedCallback(OnMapLoadedCallback cb) {
		mMap.setOnMapLoadedCallback(cb);
	}

	/**
	 * @return The coordinates of the route's start point, if any (otherwise,
	 *         return null)
	 */
	public LatLng getFromLocation() {
		return mFromCoords;
	}

	/**
	 * Set the coordinates of the route's start point. If both edges of the
	 * route are initialized, calculate the route connecting them. Otherwise,
	 * set the camera position on the coordinates of the point just set
	 * 
	 * @param from
	 *            The new coordinates of the route's start point
	 */
	public void setFromLocation(LatLng from) {
		if (mFromCoords != null) {
			mMap.clear();
			if (mToCoords != null) {
				drawToLocation();
			}
		}
		mFromCoords = from;

		drawFromLocation();

		if (mToCoords == null) {
			setCameraPos(mFromCoords);
		} else {
			calculateRoute();
		}
	}

	/**
	 * @return The coordinates of the route's end point, if any (otherwise,
	 *         return null)
	 */
	public LatLng getToLocation() {
		return mToCoords;
	}

	/**
	 * Set the coordinates of the route's end point. If both edges of the route
	 * are initialized, calculate the route connecting them. Otherwise, set the
	 * camera position on the coordinates of the point just set
	 * 
	 * @param to
	 *            The new coordinates of the route's end point
	 */
	public void setToLocation(LatLng to) {
		if (mToCoords != null) {
			mMap.clear();
			if (mFromCoords != null) {
				drawFromLocation();
			}
		}

		mToCoords = to;

		drawToLocation();

		if (mFromCoords == null) {
			setCameraPos(mToCoords);
		} else {
			calculateRoute();
		}
	}

	/**
	 * Set the coordinates of both edge-points of the route, start and end. Once
	 * both are assigned and drawn, calculate the route connecting them
	 * 
	 * @param from
	 *            The new coordinates of the route's start point
	 * @param to
	 *            The new coordinates of the route's end point
	 */
	public void setLocations(LatLng from, LatLng to) {
		setFromLocation(from);
		setToLocation(to);
	}

	/**
	 * @return The geographical distance, expressed in meters, covered by the
	 *         route currently calculated
	 */
	public int getDistance() {
		return mDistance;
	}

	/**
	 * Add a new waypoint to the route. Update the route in terms of the new
	 * addition, if the calculation of such a route is possible
	 * 
	 * @param waypoint
	 *            The waypoint coordinates to be added to the route. This
	 *            waypoint will be added in the last position of the waypoints
	 *            list, just before the end point
	 * @return true
	 */
	public boolean addWaypoint(LatLng waypoint) {
		mWaypoints.add(waypoint);
		updateRoute();
		return true;
	}

	/**
	 * Add a new waypoint at the specified position of the route's waypoints
	 * list. Update the route in terms of the new addition, if the calculation
	 * of such a route is possible
	 * 
	 * @param index
	 *            The position to insert the new waypoint in the waypoints list
	 * @param waypoint
	 *            The waypoint coordinates to be added to the route
	 * @throws IndexOutOfBoundsException
	 *             if (index < 0 || index > mWaypoints.size())
	 */
	public void addWaypoint(int index, LatLng waypoint)
			throws IndexOutOfBoundsException {
		mWaypoints.add(index, waypoint);
		updateRoute();
	}

	/**
	 * Replace the coordinates of the waypoint at the specified position of the
	 * route's waypoints list. Update the route in terms of this modification,
	 * if the calculation of such a route is possible
	 * 
	 * @param index
	 *            The position of the element in the waypoints list to be
	 *            modified
	 * @param waypoint
	 *            The waypoint coordinates to replace the value existing at the
	 *            index position of the waypoints list
	 * @return The element previously at the specified position
	 * @throws IndexOutOfBoundsException
	 *             if (index < 0 || index >= mWaypoints.size())
	 */
	public LatLng setWaypoint(int index, LatLng waypoint)
			throws IndexOutOfBoundsException {
		LatLng wp = mWaypoints.set(index, waypoint);
		updateRoute();
		return wp;
	}

	/**
	 * Remove a waypoint at the specified position of the route's waypoints
	 * list. Update the route in terms of this deletion
	 * 
	 * @param index
	 *            The index of the element in the waypoints list to be removed
	 * @return The element that was removed from the list
	 * @throws IndexOutOfBoundsException
	 *             if (index < 0 || index >= mWaypoints.size())
	 */
	public LatLng removeWaypoint(int index) throws IndexOutOfBoundsException {
		LatLng wp = mWaypoints.remove(index);
		updateRoute();
		return wp;
	}

	/**
	 * Remove the given element from the route's waypoints list, if such an
	 * element exists. If the deletion is carried out, update the route in terms
	 * of it
	 * 
	 * @param waypoint
	 *            The element to be removed from the waypoints list, if present
	 * @return If the route's waypoints list contained the specified element
	 */
	public boolean removeWaypoint(LatLng waypoint) {
		boolean remove = mWaypoints.remove(waypoint);
		updateRoute();
		return remove;
	}

	/**
	 * @return The route's waypoints list
	 */
	public ArrayList<LatLng> getWaypoints() {
		return mWaypoints;
	}

	/**
	 * @return List of points, where line segments are drawn between consecutive
	 *         points, which graphically displays the route drawn on the map, if
	 *         such a route exists. Otherwise, return null
	 */
	public Polyline getRoute() {
		return mRoute;
	}

	/**
	 * @return If a route connecting the start and end points, across the
	 *         waypoints, was successfully calculated
	 */
	public boolean routeAvailable() {
		return mRoute != null;
	}

	/**
	 * Place marker on the start point
	 */
	private void drawFromLocation() {
		mMap.addMarker(new MarkerOptions().position(mFromCoords).icon(
				BitmapDescriptorFactory.fromAsset("marker_a.png")));
	}

	/**
	 * Place marker on the end point
	 */
	private void drawToLocation() {
		mMap.addMarker(new MarkerOptions().position(mToCoords).icon(
				BitmapDescriptorFactory.fromAsset("marker_b.png")));
	}

	/**
	 * Set the map camera map at the specified position
	 * 
	 * @param pos
	 *            The coordinates where the camera map will be centered
	 */
	private void setCameraPos(LatLng pos) {
		mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
	}

	/**
	 * Set the position and zoom of the map camera to the specified bounds
	 * 
	 * @param bounds
	 *            The LatLngBounds to set the camera position and zoom
	 */
	private void setCameraBounds(LatLngBounds bounds) {
		mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
	}

	/**
	 * If both start and end route points are provided, calculate the route
	 * between them and draw it on the map
	 */
	private void calculateRoute() {
		if (getFromLocation() != null && getToLocation() != null) {
			mActivityIndicator.setVisibility(View.VISIBLE);

			md = new GMapV2Direction(this, getClass().getSimpleName());
			md.retrieveDirection(mFromCoords, mToCoords, mWaypoints, true,
					GMapV2Direction.MODE_DRIVING);
		}
	}

	/**
	 * Display a route on the Google Map fragment, from the data contained in
	 * JSONArray received
	 * 
	 * @param routes
	 *            Data of the route to be displayed, including route points, the
	 *            route's bounding square and the geographical distance covered
	 *            by the route
	 */
	@Override
	public void displayRoute(JSONArray routes) {
		try {
			// Set the camera to the bounding square of the route
			JSONObject directionsResponse = routes.getJSONObject(0)
					.getJSONObject("bounds");

			setCameraBounds(new LatLngBounds(new LatLng(directionsResponse
					.getJSONObject("southwest").getDouble("lat"),
					directionsResponse.getJSONObject("southwest").getDouble(
							"lng")), new LatLng(directionsResponse
					.getJSONObject("northeast").getDouble("lat"),
					directionsResponse.getJSONObject("northeast").getDouble(
							"lng"))));

			directionsResponse = routes.getJSONObject(0).getJSONArray("legs")
					.getJSONObject(0);

			// If available, retrieve the route and draw it on the map
			ArrayList<LatLng> directionPoint = md
					.getDirection(directionsResponse);
			if (directionPoint.size() > 0) {
				Toast.makeText(getActivity(), R.string.routeFound,
						Toast.LENGTH_SHORT).show();

				PolylineOptions rectLine = new PolylineOptions().width(3)
						.color(Color.RED);
				for (int i = 0; i < directionPoint.size(); i++) {
					rectLine.add(directionPoint.get(i));
				}
				mRoute = mMap.addPolyline(rectLine);
			} else {
				Toast.makeText(getActivity(), R.string.noRouteFound,
						Toast.LENGTH_SHORT).show();
				mRoute = null;
			}

			// Display the route distance
			mDistance = md.getDistanceValue(directionsResponse);
			mDistanceText.setText(md.getDistanceText(directionsResponse));
		} catch (JSONException jsone) {
			Log.e(getClass().getSimpleName(), "Cannot process JSON results",
					jsone);
		}
		mActivityIndicator.setVisibility(View.GONE);
	}

	/**
	 * If a route is available, draw it on the map (clearing it before)
	 * Otherwise, try to calculate a route
	 */
	private void updateRoute() {
		if (routeAvailable()) {
			mMap.clear();
			drawFromLocation();
			drawToLocation();
		}
		calculateRoute();
	}
}
