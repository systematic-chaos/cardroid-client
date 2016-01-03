package uclm.esi.cardroid.newtrip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;

import Ice.ObjectPrx;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.SessionActivity;
import uclm.esi.cardroid.data.android.Car;
import uclm.esi.cardroid.data.android.DateTime;
import uclm.esi.cardroid.data.android.DateTimePrefs;
import uclm.esi.cardroid.data.android.TripOffer;
import uclm.esi.cardroid.data.android.TripRequest;
import uclm.esi.cardroid.data.android.User;
import uclm.esi.cardroid.data.android.Waypoint;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.CarTypPrxHelper;
import uclm.esi.cardroid.data.zerocice.Fuel;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.util.DatePickerDialog;
import uclm.esi.cardroid.util.PlaceLocator;
import uclm.esi.cardroid.util.DatePickerDialog.DateTimePickerDialogListener;
import uclm.esi.cardroid.util.PlaceLocator.PlaceLocatorListener;
import uclm.esi.cardroid.util.RouteMap;
import uclm.esi.cardroid.util.Utilities;
import uclm.esi.cardroid.util.WeekDaysButtonBar;

/**
 * \class OrganizeTripActivity
 * Activity supporting the creation of TripOffer from a given TripRequest
 */
public class OrganizeTripActivity extends SessionActivity implements
		PlaceLocatorListener, DateTimePickerDialogListener {

	private Hashtable<Integer, Integer> mTabs;
	private TextView mFromText, mToText;
	private RouteMap mRouteMap;
	private EditText mDTEditText;
	private ImageButton mDTImageButton;
	private DateTimePickerDialog mDTDialog;
	private DateTime mDateTime;

	private ArrayList<View> mTwoWayTripViews;
	private EditText mReturnDTEditText;
	private ImageButton mReturnDTImageButton;
	private DateTimePickerDialog mReturnDTDialog;
	private DateTime mReturnDateTime;
	private ArrayList<View> mRegularTripViews;
	private WeekDaysButtonBar mWeekDays;
	private TextView mWeeksTextView;

	private NumberPicker mNSeatsNumberPicker;
	private EditText mTripCharacteristicsEditText;

	private Spinner mCarSpinner;

	private EditText mPrice;
	private ArrayList<CheckBox> mAllow;

	private ArrayList<PlaceLocator> mPlaceLocators;

	private TripRequest mTripRequestProposal;

	public static final String EXTRA_PROPOSAL = "OrganizeTripActivity.PROPOSAL";
	public static final String EXTRA_OFFER = "OrganizeTripActivity.OFFER";

	private static final String STATE_DATETIME = "DATETIME";
	private static final String STATE_RETURNDATETIME = "RETURNDATETIME";

	private static final String STATE_NWAYPOINTS = "NWAYPOINTS";

	/**
	 * Extend the MyFragmentActivity.onCreate method, setting the proper layout
	 * for this activity, initializing the UI widgets and setup the action bar
	 * with this activity's title
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organize_newtripoffer);

		mTripRequestProposal = (TripRequest) getIntent().getParcelableExtra(
				EXTRA_PROPOSAL);

		setupWidgets();

		setupActionBar(R.string.offerTrip, false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mCarSpinner.getAdapter() == null) {

			// Create an ArrayAdapter to retrieve the user's cars and depict
			// them
			// as strings using a default spinner layout
			final ArrayAdapter<CarTypPrx> adapter = new ArrayAdapter<CarTypPrx>(
					this, android.R.layout.simple_spinner_item,
					android.R.id.text1, _sessionController.getMyUser()
							.getUserCars()) {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					TextView holder = null;

					if (convertView == null) {
						LayoutInflater inflater = (LayoutInflater) getContext()
								.getSystemService(
										Activity.LAYOUT_INFLATER_SERVICE);
						convertView = inflater.inflate(
								android.R.layout.simple_spinner_item, parent,
								false);
						holder = (TextView) convertView
								.findViewById(android.R.id.text1);
						convertView.setTag(holder);
					} else
						holder = (TextView) convertView.getTag();

					CarTypPrx item = getItem(position);
					if (item != null && holder != null)
						holder.setText(item._toString());

					return convertView;
				}

				@Override
				public View getDropDownView(int position, View convertView,
						ViewGroup parent) {
					return getView(position, convertView, parent);
				}
			};

			// Specify the layout to user when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// Apply the adapter to the spinner
			mCarSpinner.setAdapter(adapter);
		}
	}

	/**
	 * Initialize the UI widgets
	 */
	@Override
	protected void setupWidgets() {
		mTabs = new Hashtable<Integer, Integer>(3);
		setupBasicInformationWidgets();
		setupAdvancedInformationWidgets();
		setupTripDataWidgets();
		displayTripData();
	}

	/**
	 * Initialize the widgets displaying the basic information of the TripOffer
	 * to be created
	 */
	private void setupBasicInformationWidgets() {
		mTabs.put(R.id.textBasicInformation, R.id.layoutBasicInformation);

		mFromText = (TextView) findViewById(R.id.textViewFrom);
		mToText = (TextView) findViewById(R.id.textViewTo);
		mDTEditText = (EditText) findViewById(R.id.editTextDate);
		mDTImageButton = (ImageButton) findViewById(R.id.imageButtonDate);

		mPlaceLocators = new ArrayList<PlaceLocator>();

		mCarSpinner = (Spinner) findViewById(R.id.spinnerCar);

		mCarSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				CarTypPrx car = CarTypPrxHelper.checkedCast((ObjectPrx) parent
						.getItemAtPosition(position));
				mNSeatsNumberPicker.setMaxValue(car.getNSeats()
						- (mTripRequestProposal.getRequestedSeats() + 1));
				mNSeatsNumberPicker.setMinValue(0);
				mNSeatsNumberPicker.setEnabled(true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	/**
	 * Initialize the widgets displaying the advanced information of the
	 * TripOffer to be created
	 */
	private void setupAdvancedInformationWidgets() {
		mTabs.put(R.id.textAdvancedInformation, R.id.layoutAdvancedInformation);
		LinearLayout contentLayout = (LinearLayout) findViewById(R.id.layoutAdvancedInformation);

		if (!(mTripRequestProposal.hasReturnDateTime() || mTripRequestProposal
				.hasWeekDaysPeriodicity()))
			return;

		TextView tv;
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		int offset = 0;

		if (mTripRequestProposal.hasReturnDateTime()) {
			mTwoWayTripViews = new ArrayList<View>();
			tv = new TextView(this);
			params.gravity = Gravity.LEFT;
			tv.setLayoutParams(params);
			tv.setText(R.string.returnDate);
			tv.setTextAppearance(this, android.R.attr.textAppearanceSmall);
			mTwoWayTripViews.add(tv);
			LinearLayout ll = new LinearLayout(this);
			ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			mReturnDTEditText = new EditText(this);
			params.gravity = Gravity.BOTTOM | Gravity.LEFT;
			params.weight = 3;
			mReturnDTEditText.setLayoutParams(params);
			mReturnDTEditText.setEms(10);
			mReturnDTEditText.setFocusable(false);
			mReturnDTEditText.setFocusableInTouchMode(false);
			mReturnDTEditText
					.setRawInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
			mReturnDTImageButton = new ImageButton(this);
			params.weight = 1;
			params.gravity = Gravity.NO_GRAVITY;
			mReturnDTImageButton.setLayoutParams(params);
			mReturnDTImageButton
					.setImageResource(android.R.drawable.ic_menu_today);
			ll.addView(mReturnDTEditText);
			ll.addView(mReturnDTImageButton);
			mTwoWayTripViews.add(ll);

			offset = mTwoWayTripViews.size();
			for (int n = 0; n < offset; n++)
				contentLayout.addView(mTwoWayTripViews.get(n));

			mReturnDTImageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showDateTimePickerDialog(v);
				}
			});
		}

		if (mTripRequestProposal.hasWeekDaysPeriodicity()) {
			mRegularTripViews = new ArrayList<View>();
			tv = new TextView(this);
			tv.setText(R.string.regularTrip);
			tv.setTextAppearance(this, android.R.style.TextAppearance_Small);
			mRegularTripViews.add(tv);
			mWeeksTextView = new TextView(this);
			int periodicity;
			switch (mTripRequestProposal.getPeriodicity()) {
			case EVERYWEEK:
				periodicity = R.string.everyWeek;
				break;
			case EVENWEEKS:
				periodicity = R.string.evenWeeks;
				break;
			case ODDWEEKS:
				periodicity = R.string.oddWeeks;
				break;
			default:
				periodicity = 0;
			}
			mWeeksTextView.setText(getString(periodicity));
			mWeeksTextView.setTextAppearance(this,
					android.R.attr.textAppearanceMedium);
			mRegularTripViews.add(mWeeksTextView);

			FragmentManager fragmentManager = getSupportFragmentManager();
			mWeekDays = new WeekDaysButtonBar();
			mWeekDays.setSelectedDays(mTripRequestProposal.getWeekDays());
			fragmentManager.popBackStack();

			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.add(contentLayout.getId(), mWeekDays);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			for (int n = 0; n < mRegularTripViews.size(); n++)
				contentLayout.addView(mRegularTripViews.get(n), offset + n,
						params);
		}
	}

	/**
	 * Initialize the widgets displaying other data of the TripOffer to be
	 * created
	 */
	private void setupTripDataWidgets() {
		mTabs.put(R.id.textTripData, R.id.layoutTripData);

		mNSeatsNumberPicker = (NumberPicker) findViewById(R.id.numberPickerSeats);
		mNSeatsNumberPicker.setMinValue(0);
		mTripCharacteristicsEditText = (EditText) findViewById(R.id.editTextComments);
		mTripCharacteristicsEditText.setText(mTripRequestProposal
				.getCharacteristics());

		mPrice = (EditText) findViewById(R.id.editTextPrice);
		mAllow = new ArrayList<CheckBox>();
		mAllow.add((CheckBox) findViewById(R.id.checkBoxLuggage));
		mAllow.add((CheckBox) findViewById(R.id.checkBoxSmoke));
		mAllow.add((CheckBox) findViewById(R.id.checkBoxPets));
		mAllow.add((CheckBox) findViewById(R.id.checkBoxFood));

		mNSeatsNumberPicker.setEnabled(false);
	}

	/**
	 * Fill the UI widgets showing mTripRequestProposal data
	 */
	private void displayTripData() {
		setupMap();

		mFromText.setText(mTripRequestProposal.getFromPlace().getName());

		mToText.setText(mTripRequestProposal.getToPlace().getName());

		DateTimePrefs proposalDateTime;
		int[] date;
		int hour;
		proposalDateTime = mTripRequestProposal.getDateTime();
		date = proposalDateTime.getDate();
		switch (proposalDateTime.getTimePreferences()) {
		case AFTERNOON:
			hour = 14;
			break;
		case NIGHT:
			hour = 0;
			break;
		case MORNING:
		case ANY:
		default:
			hour = 8;
		}
		mDateTime = new DateTime(date[0], date[1], date[2], hour, 0);
		mDTEditText.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.SHORT, Locale.getDefault()).format(
				new GregorianCalendar(date[0], date[1], date[2], hour, 0)
						.getTime()));

		if (mTripRequestProposal.hasReturnDateTime()) {
			proposalDateTime = mTripRequestProposal.getReturnDateTime();
			date = proposalDateTime.getDate();
			switch (proposalDateTime.getTimePreferences()) {
			case AFTERNOON:
				hour = 14;
				break;
			case NIGHT:
				hour = 0;
				break;
			case MORNING:
			case ANY:
			default:
				hour = 8;
			}
			mReturnDateTime = new DateTime(date[0], date[1], date[2], hour, 0);
			mReturnDTEditText.setText(DateFormat.getDateTimeInstance(
					DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())
					.format(new GregorianCalendar(date[0], date[1], date[2],
							hour, 0).getTime()));
		}
	}

	/**
	 * @param car
	 *            The car to be used in the trip
	 * @param nPassengers
	 *            The number of trip passengers
	 * @param distance
	 *            The trip distance
	 * @return Guiding price for each one of the trip passengers
	 */
	private void computePrice(final Car car, final int nPassengers,
			final int distance) {
		QueryListener fuelPriceCalculationListener = new QueryListener() {

			@Override
			public void onError() {
				Hashtable<Fuel, Double> fuelPrice = new Hashtable<Fuel, Double>(
						Fuel.values().length, 1);
				TypedValue outValue = new TypedValue();
				getResources().getValue(R.dimen.unleaded_95, outValue, true);
				fuelPrice.put(Fuel.UNLEADED95, (double) outValue.getFloat());
				outValue = new TypedValue();
				getResources().getValue(R.dimen.unleaded_98, outValue, true);
				fuelPrice.put(Fuel.UNLEADED98, (double) outValue.getFloat());
				outValue = new TypedValue();
				getResources().getValue(R.dimen.diesel_a, outValue, true);
				fuelPrice.put(Fuel.DIESELA, (double) outValue.getFloat());
				outValue = new TypedValue();
				getResources().getValue(R.dimen.diesel_a_plus, outValue, true);
				fuelPrice.put(Fuel.DIESELAPLUS, (double) outValue.getFloat());
				outValue = new TypedValue();
				getResources().getValue(R.dimen.biodiesel, outValue, true);
				fuelPrice.put(Fuel.BIODIESEL, (double) outValue.getFloat());
				double price = (double) distance * fuelPrice.get(car.getFuel())
						* car.getConsumptionPerKm() / (double) nPassengers;
				mPrice.setText(String.valueOf(new BigDecimal(price).setScale(2,
						RoundingMode.HALF_UP).doubleValue()));
			}

			@Override
			public void onDataChange(double data, boolean saved) {
				if (saved) {
					double price = data * car.getConsumptionPerKm()
							/ (double) nPassengers;
					mPrice.setText(String.valueOf(new BigDecimal(price)
							.setScale(2, RoundingMode.HALF_UP).doubleValue()));
				}
			}

			@Override
			public void onDataChange(boolean data, boolean saved) {
			}

			@Override
			public void onDataChange(ObjectPrx data, boolean saved) {
			}

			@Override
			public void onDataChange(QueryModel data, boolean saved) {
			}
		};

		_sessionController.calculatePriceEstimation(
				fuelPriceCalculationListener,
				Fuel.valueOf(car.getFuel().name()), distance);
	}

	private void setupMap() {
		// Obtain the RouteMap from the SupportFragmentManager
		mRouteMap = (RouteMap) getSupportFragmentManager().findFragmentById(
				R.id.routeMap);

		mRouteMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				mRouteMap.setLocations(mTripRequestProposal.getFromPlace()
						.getCoords(), mTripRequestProposal.getToPlace()
						.getCoords());

				if (mTripRequestProposal.hasWeekDaysPeriodicity())
					mWeekDays.setEnabled(false);
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable(STATE_DATETIME, mDateTime);
		outState.putParcelable(STATE_RETURNDATETIME, mReturnDateTime);

		TableLayout waypointsTable = (TableLayout) findViewById(R.id.tableLayoutWaypoints);
		int nWaypoints = waypointsTable.getChildCount();
		waypointsTable.removeAllViews();
		outState.putInt(STATE_NWAYPOINTS, nWaypoints);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mDateTime = savedInstanceState.getParcelable(STATE_DATETIME);
		mReturnDateTime = savedInstanceState
				.getParcelable(STATE_RETURNDATETIME);

		int nWaypoints = savedInstanceState.getInt(STATE_NWAYPOINTS);
		for (int n = 0; n < nWaypoints; n++)
			addWaypoint(null);
	}

	/**
	 * If a new instance of TripOffer can be initialized from the data provided,
	 * create and initialize it
	 */
	public void newTrip(View v) {
		if (tripAvailable()) {
			User me = new User().newInstance(uclm.esi.cardroid.data.ice.User
					.extractObject(_sessionController.getMyUser()));
			Car car = new Car().newInstance(uclm.esi.cardroid.data.ice.Car
					.extractObject((CarTypPrx) mCarSpinner.getSelectedItem()));
			boolean[] allowed = new boolean[mAllow.size()];
			for (int n = 0; n < allowed.length; n++)
				allowed[n] = mAllow.get(n).isChecked();
			List<Waypoint> waypoints = new ArrayList<Waypoint>();
			for (int n = 0; n < mPlaceLocators.size(); n++)
				waypoints
						.add(new Waypoint(mPlaceLocators.get(n).getPlace(), n));
			TripOffer trip = null;
			if (!mTripRequestProposal.hasReturnDateTime()
					&& !mTripRequestProposal.hasWeekDaysPeriodicity()) {
				trip = new TripOffer(mTripRequestProposal.getFromPlace(),
						mTripRequestProposal.getToPlace(), mDateTime, me, car,
						mTripRequestProposal.getRequestedSeats()
								+ mNSeatsNumberPicker.getValue(),
						new BigDecimal(Double.parseDouble(mPrice.getText()
								.toString())).setScale(2, RoundingMode.HALF_UP)
								.doubleValue(), allowed);
			}
			if (mTripRequestProposal.hasReturnDateTime()
					&& mTripRequestProposal.hasWeekDaysPeriodicity()) {
				trip = new TripOffer(mTripRequestProposal.getFromPlace(),
						mTripRequestProposal.getToPlace(), mDateTime,
						mReturnDateTime, me, car,
						mTripRequestProposal.getRequestedSeats()
								+ mNSeatsNumberPicker.getValue(),
						new BigDecimal(Double.parseDouble(mPrice.getText()
								.toString())).setScale(2, RoundingMode.HALF_UP)
								.doubleValue(), allowed,
						mTripRequestProposal.getWeekDays(),
						mTripRequestProposal.getPeriodicity());
			}
			if (mTripRequestProposal.hasReturnDateTime()
					&& !mTripRequestProposal.hasWeekDaysPeriodicity()) {
				trip = new TripOffer(mTripRequestProposal.getFromPlace(),
						mTripRequestProposal.getToPlace(), mDateTime,
						mReturnDateTime, me, car,
						mTripRequestProposal.getRequestedSeats()
								+ mNSeatsNumberPicker.getValue(),
						new BigDecimal(Double.parseDouble(mPrice.getText()
								.toString())).setScale(2, RoundingMode.HALF_UP)
								.doubleValue(), allowed);
			}
			if (!mTripRequestProposal.hasReturnDateTime()
					&& mTripRequestProposal.hasWeekDaysPeriodicity()) {
				trip = new TripOffer(mTripRequestProposal.getFromPlace(),
						mTripRequestProposal.getToPlace(), mDateTime, me, car,
						mTripRequestProposal.getRequestedSeats()
								+ mNSeatsNumberPicker.getValue(),
						new BigDecimal(Double.parseDouble(mPrice.getText()
								.toString())).setScale(2, RoundingMode.HALF_UP)
								.doubleValue(), allowed,
						mTripRequestProposal.getWeekDays(),
						mTripRequestProposal.getPeriodicity());
			}
			trip.setDistance(mRouteMap.getDistance());
			trip.setWaypoints(waypoints.toArray(new Waypoint[0]));
			trip.addPassenger(mTripRequestProposal.getRequester(),
					mTripRequestProposal.getRequestedSeats());
			trip.setCharacteristics(mTripCharacteristicsEditText.getText()
					.toString().trim().length() > 0 ? mTripCharacteristicsEditText
					.getText().toString().trim()
					: mTripRequestProposal.getCharacteristics());
			mTripCharacteristicsEditText.setText(trip.toString());

			Intent resultIntent = new Intent();
			resultIntent.putExtra(EXTRA_OFFER, trip);
			setResult(RESULT_OK, resultIntent);
			finish();
		} else
			showErrorDialog();
	}

	/**
	 * Check whether the data provided by the user can be resolved to a valid
	 * trip route
	 * 
	 * @return Whether a valid trip route can be resolved from the data provided
	 *         by the user
	 */
	protected boolean tripAvailable() {
		boolean available = mDateTime != null;
		if (mTripRequestProposal.hasReturnDateTime()) {
			available = available && mReturnDateTime != null;
			available = available && mDateTime.compareTo(mReturnDateTime) < 0;
		}
		available = available
				&& mPrice.getText().toString().trim().length() > 0;
		available = available && mCarSpinner.getSelectedItem() != null;
		for (PlaceLocator placeLocator : mPlaceLocators)
			available = available && placeLocator.placeResolved();
		return available;
	}

	/**
	 * Show an error AlertDialog displaying a message
	 */
	private void showErrorDialog() {
		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set the dialog characteristics
		builder.setMessage(getErrorMessage());
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

	/**
	 * @return The message to be displayed in the AlertDialog created
	 */
	private String getErrorMessage() {
		StringBuilder message = new StringBuilder();
		if (mDateTime == null)
			message.append("No se ha introducido una fecha\n");
		if (mTripRequestProposal.hasReturnDateTime()) {
			if (mReturnDateTime == null)
				message.append("No se ha introducido una fecha de vuelta\n");
			else if (mDateTime.compareTo(mReturnDateTime) >= 0)
				message.append("La fecha de vuelta debe ser posterior a la fecha de ida\n");
		}
		if (mCarSpinner.getSelectedItem() == null)
			message.append("No se ha seleccionado un coche\n");
		int emptyWaypoints = 0;
		for (PlaceLocator placeLocator : mPlaceLocators)
			if (!placeLocator.placeResolved())
				emptyWaypoints++;
		if (emptyWaypoints > 0)
			message.append(emptyWaypoints
					+ " de los puntos del trayecto no han sido completados\n");
		return message.toString();
	}

	/**
	 * Add a new PlaceLocator to the trip basic data's layout. The Place this
	 * PlaceLocator will contain will be added to the mRouteMap route computed
	 * as a waypoint.
	 */
	public void addWaypoint(View v) {
		final TableLayout waypointsTable = (TableLayout) findViewById(R.id.tableLayoutWaypoints);
		final TableRow waypointRow = new TableRow(this);
		waypointRow.setId(Utilities.generateViewId());
		waypointsTable.addView(waypointRow);
		final PlaceLocator waypoint = new PlaceLocator();
		mPlaceLocators.add(mPlaceLocators.size(), waypoint);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(waypointRow.getId(), waypoint);
		fragmentTransaction.commit();

		ImageButton deleteWaypoint = new ImageButton(this);
		deleteWaypoint.setImageResource(android.R.drawable.ic_menu_delete);
		deleteWaypoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int i, index = 0;
				for (i = 0; mPlaceLocators.get(i) != waypoint; i++) {
					if (mPlaceLocators.get(i).getPlaceCoords() != null) {
						index++;
					}
				}
				if (waypoint.getPlaceCoords() != null) {
					mRouteMap.removeWaypoint(index);
				}
				mPlaceLocators.remove(i);
				waypointsTable.removeView(waypointRow);
			}
		});
		waypointRow.addView(deleteWaypoint);
	}

	/**
	 * Called when the result of a reverse geocoding or geolocation query
	 * previously performed is available. Depending on which one was the
	 * PlaceLocator originating the call, set the corresponding point (start,
	 * end or a waypoint) in mRouteMap
	 * 
	 * @param placeLocator
	 *            The PlaceLocator originating the call
	 */
	public void onPlaceLocated(PlaceLocator placeLocator) {
		if (placeLocator.hasPreviousValue()) {
			int index = 0;
			for (int i = 0; mPlaceLocators.get(i) != placeLocator; i++) {
				if (mPlaceLocators.get(i).getPlaceCoords() != null)
					index++;
			}
			mRouteMap.setWaypoint(index, placeLocator.getPlaceCoords());
		} else
			mRouteMap.addWaypoint(placeLocator.getPlaceCoords());
	}

	public void calculateRoutePrice(View v) {
		if (mCarSpinner.getSelectedItem() != null && mRouteMap.routeAvailable())
			computePrice(new Car().newInstance(uclm.esi.cardroid.data.ice.Car
					.extractObject((CarTypPrx) mCarSpinner.getSelectedItem())),
					mTripRequestProposal.getRequestedSeats()
							+ mNSeatsNumberPicker.getValue(),
					mRouteMap.getDistance());
	}

	/**
	 * Toggle the visibility (visible/gone) of the View behind the selected View
	 * 
	 * @param v
	 *            The view selected
	 */
	public void onTabToggle(View v) {
		View layout = findViewById(mTabs.get(v.getId()));
		Checkable c = (Checkable) v;
		if (c.isChecked()) {
			c.setChecked(false);
			layout.setVisibility(View.GONE);
		} else {
			c.setChecked(true);
			layout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Show the View behind the View selected and hive the View s behind the
	 * other CheckedTextView instances
	 * 
	 * @param activateTab
	 *            The View selected
	 */
	protected void switchTabInfoLayouts(View activateTab) {
		switchTabInfoLayouts(activateTab.getId());
	}

	/**
	 * Show the View behind the View selected and hide the View s behind the
	 * other CheckedTextView instances (tabs)
	 * 
	 * @param activateTabId
	 *            The id of the View selected
	 */
	private void switchTabInfoLayouts(int activateTabId) {
		Enumeration<Integer> tabs = mTabs.keys();
		int tabId, visibility;
		boolean checked;

		while (tabs.hasMoreElements()) {
			tabId = tabs.nextElement();
			if (tabId == activateTabId) {
				checked = true;
				visibility = View.VISIBLE;
			} else {
				checked = false;
				visibility = View.GONE;
			}
			((Checkable) findViewById(tabId)).setChecked(checked);
			findViewById(mTabs.get(tabId)).setVisibility(visibility);
		}
	}

	/**
	 * Create an instance of the DateTimePickerDialog dialog fragment and show
	 * it
	 */
	public void showDateTimePickerDialog(View v) {
		if (v == mDTImageButton) {
			mDTDialog = mDateTime == null ? new DateTimePickerDialog()
					: DateTimePickerDialog.newInstance(mTripRequestProposal
							.getDateTime());
			mDTDialog.show(getSupportFragmentManager(),
					DateTimePickerDialog.class.getSimpleName());
		}
		if (v == mReturnDTImageButton) {
			mReturnDTDialog = mReturnDateTime == null ? new DateTimePickerDialog()
					: DateTimePickerDialog.newInstance(mTripRequestProposal
							.getReturnDateTime());
			mReturnDTDialog.show(getSupportFragmentManager(),
					DateTimePickerDialog.class.getSimpleName());
		}
	}

	/**
	 *  User touched the dialog's positive button
	 */
	public void onDialogPositiveClick(DatePickerDialog dialog) {
		/* The dialog fragment receives a reference to this Activity through * 
		 * the Fragment.onAttach() callback, which it uses to call the       *
		 * following methods defined by the                                  *
		 * DateTimePickerDialogFragment.DateTimePickerDialogListener         * 
		 * interface                                                         */
		DateTimePickerDialog dtpDialog = (DateTimePickerDialog) dialog;
		EditText eText = null;
		if (dialog == mDTDialog) {
			mDateTime = dtpDialog.getDateTime();
			eText = mDTEditText;
		}
		if (dialog == mReturnDTDialog) {
			mReturnDateTime = dtpDialog.getDateTime();
			eText = mReturnDTEditText;
		}
		int[] date = dtpDialog.getDate();
		int[] time = dtpDialog.getTime();

		if (eText != null) {
			eText.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
					DateFormat.SHORT, Locale.getDefault()).format(
					new GregorianCalendar(date[0], date[1], date[2], time[0],
							time[1]).getTime()));
		}
	}

	/**
	 *  User touched the dialog's negative button
	 */
	public void onDialogNegativeClick(DatePickerDialog dialog) {
		dialog.getDialog().cancel();
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
