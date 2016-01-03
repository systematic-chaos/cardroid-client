package uclm.esi.cardroid.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import uclm.esi.cardroid.CardroidApp;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.Place;
import uclm.esi.cardroid.data.zerocice.PlaceTypPrx;
import uclm.esi.cardroid.data.zerocice.PlaceTypPrxHelper;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.SessionController;
import uclm.esi.cardroid.search.PlaceSearch;
import Ice.ObjectPrx;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableRow.LayoutParams;

/**
 * \class PlaceLocator
 * Fragment that allows the user to select a place and
 * retrieve its geographical coordinates This class enables this to be performed
 * on three ways: - Via an AutocompleteTextView , which launches a request to
 * the Google Places service to retrieve identifiable places from the user's
 * text hint input - Via the geolocation services, which retrieve the user's
 * geographical coordinates and launch a request to the reverse geocoding
 * services, to obtain an identifiable place from the coordinates - Via the
 * selection of a Place from the user's recent places list
 */
public class PlaceLocator extends Fragment implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	// Global constants
	/**
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private final String LOG_TAG = getClass().getSimpleName();

	// / Global variables to hold the Location Service client and the UI widgets
	private View mView;
	private LocationClient mLocationClient;
	private PlaceSearch mPlaceSearch;
	private AutoCompleteTextView mAutoComplete;
	private TextWatcher mTextChangedListener;
	private ImageButton mPlacesButton;
	private ImageButton mLocationButton;
	private ProgressBar mActivityIndicator;
	private LatLng mCoords;
	private JSONArray mPredsJsonArray;
	private boolean mHasPreviousValue;

	private static final String STATE_COORDS = "COORDS";
	private static final String STATE_HASPREVIOUSVALUE = "HASPREVIOUSVALUE";

	/**
	 * Inflate and return the View displayed by the fragment and setup the UI
	 * widgets
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.place_locator, container, false);
		mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		// Take the Google APIs key from the String resources file
		mPlaceSearch = PlaceSearch.getInstance(LOG_TAG,
				getString(R.string.api_key));

		// Setup widgets
		mActivityIndicator = (ProgressBar) mView
				.findViewById(R.id.location_progress);
		setPlacesButton();
		setLocationButton();
		setAutoCompView();

		if (savedInstanceState != null) {
			mCoords = savedInstanceState.getParcelable(STATE_COORDS);
			mHasPreviousValue = savedInstanceState.getBoolean(
					STATE_HASPREVIOUSVALUE, false);
		} else
			mHasPreviousValue = false;

		return mView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(STATE_COORDS, mCoords);
		outState.putBoolean(STATE_HASPREVIOUSVALUE, mHasPreviousValue);
	}

	/**
	 * \interface PlaceLocatorListener
	 * The activity that creates an instance of this
	 * fragment must implement this interface in order to receive event
	 * callbacks. Each method passes the Fragment in case the host needs to
	 * query it.
	 */
	public interface PlaceLocatorListener {
		public void onPlaceLocated(PlaceLocator placeLocator);
	}

	// / Use this instance of the interface to deliver action events
	protected PlaceLocatorListener mListener;

	/**
	 * @return A new Place instance, created from the coordinates and name
	 *         hosted by this PlaceLocator
	 */
	public Place getPlace() {
		LatLng coords = getPlaceCoords();
		return new Place(new LatLng(coords.latitude, coords.longitude),
				getPlaceName());
	}

	/**
	 * @return The place name hosted by this PlaceLocator
	 */
	public String getPlaceName() {
		return mAutoComplete.getText().toString();
	}

	/**
	 * @return The place coordinates hosted by this PlaceLocator
	 */
	public LatLng getPlaceCoords() {
		return mCoords;
	}

	/**
	 * @return Whether the place coordinates have a previously assigned value
	 */
	public boolean hasPreviousValue() {
		return mHasPreviousValue;
	}

	/**
	 * @return Whether the place coordinates have an assigned value
	 */
	public boolean placeResolved() {
		return mCoords != null;
	}

	/**
	 * Override the Fragment.onAttach() method to instantiate the PlaceLocator
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mLocationClient = new LocationClient(activity, PlaceLocator.this,
				PlaceLocator.this);

		// Verify that the host activity implements the callback interface
		try {
			/* Instantiate the PlaceLocatorListener so we can send events to *
			 * the host														 */
			mListener = (PlaceLocatorListener) activity;
		} catch (ClassCastException cce) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement PlaceLocatorListener");
		}
	}

	/**
	 * Check if any location provider is available
	 * 
	 * @return Whether any location provider is available
	 */
	private boolean areLocationServicesEnabled() {
		String locationProviders = Settings.Secure.getString(getActivity()
				.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		return locationProviders.length() > 0;
	}

	/**
	 * Query the location provider available to obtain the user's location and
	 * launch a reverse geocoding request from it
	 */
	public void geolocate() {
		Location currentLocation = mLocationClient.getLastLocation();

		mHasPreviousValue = mCoords != null;
		mCoords = new LatLng(currentLocation.getLatitude(),
				currentLocation.getLongitude());

		/* Reverse geocoding is long-running and synchronous. Run it on a	    *
		 * background thread. Pass the current location to the background task. *
		 * When the task finishes, onPostExecute() displays the address. First, *
		 * we must ensure that a Geocoder services is available. The activity   *
		 * indicator will be shown as long as the background task lasts.		*/
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD
				&& Geocoder.isPresent()
				&& ((MyFragmentActivity) getActivity()).isConnected()) {
			mLocationButton.setVisibility(View.GONE);
			mActivityIndicator.setVisibility(View.VISIBLE);
			(new GetAddressTask(getActivity())).execute(currentLocation, null);
		}
	}

	private void setPlacesButton() {
		mPlacesButton = (ImageButton) mView
				.findViewById(R.id.imageButtonMyplaces);

		mPlacesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SessionController sessionController = ((CardroidApp) getActivity()
						.getApplication()).getSessionController();
				if (sessionController == null)
					return;
				QueryListener userPlacesListener = new QueryListener() {

					@Override
					public void onError() {
						Toast.makeText(getActivity(), R.string.noPlacesFound,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onDataChange(boolean data, boolean saved) {
					}

					@Override
					public void onDataChange(double data, boolean saved) {
					}

					@Override
					public void onDataChange(ObjectPrx data, boolean saved) {
					}

					@Override
					public void onDataChange(QueryModel data, boolean saved) {
						if (saved) {
							ArrayList<PlaceTypPrx> adapterData = new ArrayList<PlaceTypPrx>(
									data.data.size());
							for (ObjectPrx oprx : data.data)
								adapterData.add(PlaceTypPrxHelper
										.uncheckedCast(oprx));
							/*
							 * ArrayAdapter<String> adapter = new
							 * ArrayAdapter<String>( getActivity(),
							 * R.layout.simple_dropdown_item_1line,
							 * adapterData); mAutoComplete.setAdapter(adapter);
							 */
							PlaceTypPrx homeLocation = adapterData.get(0);
							mHasPreviousValue = mCoords != null;
							mCoords = new LatLng(homeLocation.getCoords()
									.getLatitude(), homeLocation.getCoords()
									.getLongitude());

							// Set activity indicators visibility
							mActivityIndicator.setVisibility(View.GONE);
							mPlacesButton.setVisibility(View.VISIBLE);

							// Display the results of the lookup.
							mAutoComplete.setText(homeLocation.getName());
							mAutoComplete
									.removeTextChangedListener(mTextChangedListener);
							mAutoComplete.dismissDropDown();
							mAutoComplete.clearFocus();

							mListener.onPlaceLocated(PlaceLocator.this);
						} else {
							mPlacesButton.setVisibility(View.GONE);
							mActivityIndicator.setVisibility(View.VISIBLE);
						}
					}
				};
				sessionController.getUserPlaces(userPlacesListener,
						sessionController.getMyUser());
			}
		});
	}

	private void setLocationButton() {
		mLocationButton = (ImageButton) mView
				.findViewById(R.id.imageButtonMylocation);

		mLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (areLocationServicesEnabled()) {
					geolocate();
				} else {
					Toast.makeText(getActivity(),
							R.string.geolocationServicesDisabled,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void setAutoCompView() {
		mAutoComplete = (AutoCompleteTextView) mView
				.findViewById(R.id.autoCompleteTextView);

		final PlacesAutoCompleteAdapter placesAdapter = new PlacesAutoCompleteAdapter(
				getActivity(), R.layout.simple_dropdown_item_1line);
		mAutoComplete.setThreshold(getResources().getInteger(
				R.integer.completion_threshold));

		/* The TextWatcher implementation calls back the Google Places service   *
		 * to update the autoCompView's completion list, after the lapse of time *
		 * defined in R.integer.autocomplete_delay. Uses a Timer to schedule	 *
		 * calls to the service													 */
		final TextWatcher textWatcher = new TextWatcher() {

			private final int threshold = getResources().getInteger(
					R.integer.completion_threshold);
			private final int delay = getResources().getInteger(
					R.integer.autocomplete_delay);

			private final Timer timer = new Timer();

			private TimerTask lastTimer = null;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count == 1) {
					mAutoComplete.dismissDropDown();
					mAutoComplete.setAdapter(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// Cancel the last timer no matter what if text changed
				if (lastTimer != null) {
					lastTimer.cancel();
				}

				// We check against threshold twice: first to schedule the task
				if (s.length() > threshold) {
					lastTimer = new TimerTask() {

						@Override
						public void run() {
							final String placeName = mAutoComplete.getText()
									.toString();

							/* We check against threshold twice: second to see *
							 * if we should still bother calling the geocoder  *
							 * after the timeout							   */
							if (placeName.length() >= threshold) {
								
								/* Use the text as it exists when is called, *
								 * instead of the s							 */
								getActivity().runOnUiThread(new Runnable() {
									public void run() {
										
										/* Perform the operation on the *
										 * UI thread					*/
										mAutoComplete.setAdapter(placesAdapter);
										placesAdapter.getFilter().filter(
												placeName,
												new Filter.FilterListener() {

													@Override
													public void onFilterComplete(
															int count) {
														mAutoComplete
																.showDropDown();
													}
												});
									}
								});
							}
						}
					};
					
					/* Only call the Google Places service if a certain *
					 * amount of time has elapsed						*/
					timer.schedule(lastTimer, delay);
				}
			}
		};
		mAutoComplete.addTextChangedListener(textWatcher);
		mTextChangedListener = textWatcher;

		/* Add a listener that ensures that when one of the auto-fill items *
		 * is clicked, we jump right away								    */
		mAutoComplete.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				((InputMethodManager) getActivity().getSystemService(
						Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
						mAutoComplete.getWindowToken(), 0);
				mAutoComplete.removeTextChangedListener(textWatcher);
				placesAdapter.clear();
				mAutoComplete.dismissDropDown();
				mAutoComplete.clearFocus();

				/* Display on the map the information retrieved from the Google *
				 * Places autocomplete service based on the contents of the		*
				 * autoCompleteTextView that has been just updated				*/
				try {
					
					// Extract the Place descriptions from the results
					JSONObject attributions = mPlaceSearch.details(
							mPredsJsonArray.getJSONObject(position).getString(
									"reference"), false);

					/* Retrieve the coordinate longitudes from the JSON			 *
					 * object(s) received and update their corresponding values  *
					 * in the mCoords class variables Call the listener activity *
					 * to be updated according to the new values				 */
					JSONObject location = attributions
							.getJSONObject("geometry")
							.getJSONObject("location");
					mHasPreviousValue = mCoords != null;
					mCoords = new LatLng(location.getDouble("lat"), location
							.getDouble("lng"));
					mListener.onPlaceLocated(PlaceLocator.this);
				} catch (JSONException jsone) {
					Log.e(LOG_TAG, "Cannot process JSON results", jsone);
				}
			}
		});

		// Select all text if clicked when focus changed, do nothing otherwise
		mAutoComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAutoComplete.selectAll();
			}
		});
	}

	/**
	 * Launch a Google Places query from the user input to obtain a list of
	 * places the user may want to reference
	 * 
	 * @param input
	 *            The user input
	 * @return List of places name matching the user input
	 */
	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;
		mPredsJsonArray = mPlaceSearch.autocomplete(input, false);

		// Extract the Place descriptions from the results
		try {
			resultList = new ArrayList<String>(mPredsJsonArray.length());
			for (int i = 0; i < mPredsJsonArray.length(); i++) {
				resultList.add(mPredsJsonArray.getJSONObject(i).getString(
						"description"));
			}
		} catch (JSONException jsone) {
			Log.e(LOG_TAG, "Cannot process JSON results", jsone);
		}

		return resultList;
	}

	/**
	 * \class PlacesAutoCompleteAdapter
	 * ArrayAdapter to fill the drop down
	 * suggestions list behind the AutocompleteTextView for the user input with
	 * the results of a filter querying the Google Places service with the
	 * user's input
	 */
	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
			implements Filterable {
		private ArrayList<String> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						
						// Retrieve the autocomplete results
						resultList = autocomplete(constraint.toString());

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// Connect the client
		if (servicesConnected()) {
			mLocationClient.connect();
			MapsInitializer.initialize(getActivity());
		}
	}

	/**
	 * Called when the Activity is no longer visible.
	 */
	@Override
	public void onStop() {
		// Disconnecting the client invalidates it
		mLocationClient.disconnect();
		super.onStop();
	}

	/**
	 * Check whether Google Play services is available. If this is not the case,
	 * show an ErrorDialogFragment
	 * 
	 * @return Whether Google Play services is available
	 */
	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			
			// Continue
			return true;
			
			// Google Play services was not available for some reason
		} else {
			showErrorDialog(resultCode);
			return false;
		}
	}

	/**
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		// Display the connection status
		/* Toast.makeText(getActivity(), "Connected",
		 * Toast.LENGTH_SHORT).show();
		 */
	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		/*
		 * Toast.makeText(getActivity(), "Disconnected. Please re-connect.",
		 * Toast.LENGTH_SHORT).show();
		 */
	}

	/**
	 * Called by Location Services if the attempt to Location Services fails
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/* Google Play services can resolve some errors it detects. If the error *
		 * has a resolution, try sending an Intent to start a Google Play		 *
		 * services activity that can resolve error.							 */
		if (connectionResult.hasResolution()) {
			try {
				
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(getActivity(),
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				
				/* Thrown if Google Play services canceled the original *
				 * PendingIntent										*/
			} catch (IntentSender.SendIntentException e) {
				
				// Log the error
				e.printStackTrace();
			}
			
		} else {
			/* If no resolution is available, display a dialog to the user * 
			 * with the error.											   */
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	/**
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			// If the result code is Activity.RESULT_OK, try to connect again
			switch (resultCode) {
			
			case Activity.RESULT_OK:
				// Try the request again
				mLocationClient.connect();
				break;
			}
		}
	}

	/**
	 * \class GetAddressTask
	 * A subclass of AsyncTask that calls
	 * getFromLocation() in the background. The class definition has these
	 * generic types: Location - A Location object containing the current
	 * location Void - indicates that progress units are not used String - An
	 * address passed to onPostExecute()
	 */
	private class GetAddressTask extends AsyncTask<Location, Void, String> {
		private Context con;
		private Location loc;

		public GetAddressTask(Context context) {
			super();
			con = context;
		}

		/**
		 * Get a Geocoder instance, get the latitude and longitude look up the
		 * address, and return it
		 * 
		 * @param params
		 *            One or more Location objects
		 * 
		 * @return A string containing the address of the current location, or
		 *         an empty string if no address can be found, or an error
		 *         message
		 */
		@Override
		protected String doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(con, Locale.getDefault());

			// Get the current location from the input parameter list
			loc = params[0];

			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				
				// Return 1 address
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e1) {
				Log.e(LOG_TAG, "IO Exception in getFromLocation()");
				e1.printStackTrace();
				return ("IO Exception trying to get address");
				
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments "
						+ Double.toString(loc.getLatitude()) + " , "
						+ Double.toString(loc.getLongitude())
						+ " passed to address service";
				Log.e(LOG_TAG, errorString);
				e2.printStackTrace();
				return errorString;
			}

			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				
				// Get the first address
				Address address = addresses.get(0);
				
				/* Format the first line of address (if available), city, and *
				 * country name												  */
				String addressText = String.format("%s, %s",
						
				// Locality is usually a city
						address.getLocality(),
						
						// The country of the address
						address.getCountryName());
				
				// Return the text
				return addressText;
			} else {
				return "No address found";
			}
		}

		/**
		 * A method that's called once doInBackground() completes. Turn off the
		 * indeterminate activity indicator and set the text of the UI element
		 * that shows the address. Call the listener activity to be updated
		 * according to the new values
		 * 
		 * @param address
		 *            The text to be set on mAutoComplete
		 */
		@Override
		protected void onPostExecute(String address) {
			// Set activity indicators visibility
			mActivityIndicator.setVisibility(View.GONE);
			mLocationButton.setVisibility(View.VISIBLE);

			// Display the results of the lookup.
			mAutoComplete.setText(address);
			mAutoComplete.removeTextChangedListener(mTextChangedListener);
			mAutoComplete.dismissDropDown();
			mAutoComplete.clearFocus();

			mListener.onPlaceLocated(PlaceLocator.this);
		}
	}

	/**
	 * Create and show an ErrorDialogFragment displaying the details of the
	 * failed connection to Google Play Services
	 * 
	 * @param errorCode
	 *            error code returned by isGooglePlayServicesAvailable(Context)
	 *            call. If errorCode is SUCCESS then null is returned
	 */
	private void showErrorDialog(int errorCode) {
		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);

		// If Google Play services can provide an error dialog
		if (errorDialog != null) {
			
			// Create a new DialogFragment for the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();
			
			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);
			
			// Show the error dialog in the DialogFragment
			errorFragment.show(getFragmentManager(), "Location Updates");
		}
	}

	/**
	 * \class ErrorDialogFragment
	 * Define a DialogFragment that displays the
	 * error dialog
	 */
	public static class ErrorDialogFragment extends DialogFragment {
		/// Global field to contain the error dialog
		private Dialog mDialog;

		/**
		 * Default constructor. Sets the dialog field to null
		 */
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		/**
		 * @param dialog
		 *            Dialog to display
		 */
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		/**
		 * @return Dialog to return to the DialogFragment
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}
}
