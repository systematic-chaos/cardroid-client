package uclm.esi.cardroid.search;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.SessionActivity;
import uclm.esi.cardroid.data.android.DateTimePrefs;
import uclm.esi.cardroid.data.android.TripRequest;
import uclm.esi.cardroid.data.android.User;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.util.DatePickerDialog;
import uclm.esi.cardroid.util.PlaceLocator;
import uclm.esi.cardroid.util.RouteMap;
import uclm.esi.cardroid.util.DatePickerDialog.DateTimePickerDialogListener;
import uclm.esi.cardroid.util.PlaceLocator.PlaceLocatorListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

/**
 * \class SearchActivity
 * Activity supporting the creation of new Trip instances
 * to be used in the search for matching results
 */
public class SearchActivity extends SessionActivity implements
		PlaceLocatorListener, DateTimePickerDialogListener {

	private RouteMap mRouteMap;
	private PlaceLocator mFromPlaceLocator, mToPlaceLocator;
	private DateTimePrefs mDateTime;
	private EditText mDateTimeText;
	private NumberPicker mNumberpickerSeats;

	private static final String STATE_DATETIME = "DATETIME";

	/**
	 * Extend the MyFragmentActivity.onCreate method, setting the proper layout
	 * for this activity, initializing the UI widgets and setup the action bar
	 * with this activity's title
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		setupWidgets();

		setupActionBar(R.string.search, true);
	}

	/**
	 * Initialize the UI widgets
	 */
	@Override
	protected void setupWidgets() {
		mRouteMap = (RouteMap) getSupportFragmentManager().findFragmentById(
				R.id.routeMap);

		mFromPlaceLocator = (PlaceLocator) getSupportFragmentManager()
				.findFragmentById(R.id.fromPlaceLocator);
		mToPlaceLocator = (PlaceLocator) getSupportFragmentManager()
				.findFragmentById(R.id.toPlaceLocator);

		mDateTimeText = (EditText) findViewById(R.id.editTextDate);

		// Setup NumberPicker
		mNumberpickerSeats = (NumberPicker) findViewById(R.id.numberPickerSeats);
		mNumberpickerSeats.setMinValue(getResources().getInteger(
				R.integer.seats_min));
		mNumberpickerSeats.setMaxValue(getResources().getInteger(
				R.integer.seats_max));
	}

	/**
	 * Called when the result of a reverse geocoding or geolocation query
	 * previously performed is available. Depending on which one was the
	 * PlaceLocator originating the call, set the corresponding point (start or
	 * end) in mRouteMap
	 * 
	 * @param locator
	 *            The PlaceLocator originating the call
	 */
	@Override
	public void onPlaceLocated(PlaceLocator locator) {
		if (locator == mFromPlaceLocator) {
			mRouteMap.setFromLocation(locator.getPlaceCoords());
		}
		if (locator == mToPlaceLocator) {
			mRouteMap.setToLocation(locator.getPlaceCoords());
		}
		Toast.makeText(this, locator.getPlaceName(), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(STATE_DATETIME, mDateTime);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mDateTime = savedInstanceState.getParcelable(STATE_DATETIME);
	}

	/**
	 * If a new instance of Trip can be initialized from the data provided,
	 * create and initialize it
	 */
	public void searchTrips(View v) {
		if (tripAvailable()) {
			Intent i = new Intent(SearchActivity.this,
					SearchTripResultsActivity.class);

			User me = new User().newInstance(uclm.esi.cardroid.data.ice.User
					.extractObject(_sessionController.getMyUser()));
			TripRequest trip = new TripRequest(mFromPlaceLocator.getPlace(),
					mToPlaceLocator.getPlace(), mDateTime, me,
					mNumberpickerSeats.getValue());
			trip.setDistance(mRouteMap.getDistance());
			i.putExtra(SearchTripResultsActivity.EXTRA_TRIP_REQUEST, trip);
			startActivity(i);
		} else {
			showErrorDialog();
		}
	}

	/**
	 * Check if the data provided by the user can be resolved to a valid trip
	 * route
	 * 
	 * @return Whether a valid trip route can be resolved from the data provided
	 *         by the user
	 */
	private boolean tripAvailable() {
		return mRouteMap.routeAvailable() && mDateTime != null;
	}

	/**
	 * Create an instance of the DateTimePrefsPickerDialog dialog fragment and
	 * show it
	 */
	public void showDateTimePickerDialog(View v) {
		// Create an instance of the dialog fragment and show it
		DialogFragment dialog;
		if (mDateTime == null) {
			dialog = new DateTimePrefsPickerDialog();
		} else {
			dialog = DateTimePrefsPickerDialog.newInstance(mDateTime);
		}
		dialog.show(getSupportFragmentManager(),
				DateTimePrefsPickerDialog.class.getSimpleName());
	}

	/**
	 * The dialog fragment receives a reference to this Activity through the
	 * Fragment.onAttach() callback, which it uses to call the following methods
	 * defined by the DateTimePickerDialogFragment.DateTimePickerDialogListener
	 * interface
	 */
	// / User touched the dialog's positive button
	@Override
	public void onDialogPositiveClick(DatePickerDialog dialog) {
		mDateTime = ((DateTimePrefsPickerDialog) dialog).getDateTimePrefs();
		mDateTimeText.setText(mDateTime.toString());
	}

	// / User touched the dialog's negative button
	@Override
	public void onDialogNegativeClick(DatePickerDialog dialog) {
		dialog.getDialog().cancel();
	}

	/**
	 * Show an error AlertDialog displaying a message
	 */
	private void showErrorDialog() {
		StringBuilder message = new StringBuilder();
		if (mRouteMap.getFromLocation() == null) {
			message.append("No se ha introducido un punto de origen\n");
		}
		if (mRouteMap.getToLocation() == null) {
			message.append("No se ha introducido un punto de destino\n");
		}
		if (!mRouteMap.routeAvailable()) {
			message.append("No se ha podido calcular una ruta\n");
		}
		if (mDateTime == null) {
			message.append("No se ha introducido una fecha\n");
		}
		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set the dialog characteristics
		builder.setMessage(message.toString());
		// Add the dialog's button
		builder.setNegativeButton("Volver",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		// Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public CardroidEventStormListener getCardroidEventStormListener() {
		return _eventStorm;
	}

	@Override
	public boolean replayEvents() {
		return false;
	}
}
