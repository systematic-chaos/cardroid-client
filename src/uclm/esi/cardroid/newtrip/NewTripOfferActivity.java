package uclm.esi.cardroid.newtrip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.Car;
import uclm.esi.cardroid.data.android.DateTime;
import uclm.esi.cardroid.data.android.TripOffer;
import uclm.esi.cardroid.data.android.User;
import uclm.esi.cardroid.data.android.Waypoint;
import uclm.esi.cardroid.data.zerocice.CarTypPrx;
import uclm.esi.cardroid.data.zerocice.CarTypPrxHelper;
import uclm.esi.cardroid.data.zerocice.Fuel;
import uclm.esi.cardroid.data.zerocice.TripOfferTypPrx;
import uclm.esi.cardroid.mytrips.TripOfferDetailsActivity;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.util.DatePickerDialog;
import uclm.esi.cardroid.util.PlaceLocator;
import uclm.esi.cardroid.util.Utilities;
import Ice.ObjectPrx;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * \class NewTripOfferActivity
 * Activity extending NewTrip and supporting the
 * creation of new instances of TripOffer
 */
public class NewTripOfferActivity extends NewTrip {

	protected Spinner mCarSpinner;

	protected EditText mPrice;
	protected ArrayList<CheckBox> mAllow;

	protected ArrayList<PlaceLocator> mPlaceLocators;

	protected DateTime mDateTime, mReturnDateTime;

	private static final String STATE_NWAYPOINTS = "NWAYPOINTS";

	/**
	 * Extend the MyFragmentActivity.onCreate method, setting the proper layout
	 * for this activity, initializing the UI widgets and setup the action bar
	 * with this activity's title
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtrip_offer);

		setupWidgets();

		setupActionBar(R.string.offerTrip, true);
	}

	/**
	 * After the SessionController has been instantiated an assigned to this
	 * class (via the onResume method of its SessionActivity superclass),
	 * initialize and assign the CarSpinner's adapter
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (mCarSpinner.getAdapter() == null) {

			// Create an ArrayAdapter to retrieve the user's cars and depict
			// them
			// as string using a default spinner layout
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

			// Specify the layout to use when the list of choices appears
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
		super.setupWidgets();

		mPlaceLocators = new ArrayList<PlaceLocator>();
		mPlaceLocators.add(mFromPlaceLocator);
		mPlaceLocators.add(mToPlaceLocator);
	}

	/**
	 * Initialize the widgets displaying the basic information of the TripOffer
	 * to be created
	 */
	@Override
	protected void setupBasicInformationWidgets() {
		super.setupBasicInformationWidgets();

		mCarSpinner = (Spinner) findViewById(R.id.spinnerCar);

		mCarSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				CarTypPrx car = CarTypPrxHelper.checkedCast((ObjectPrx) parent
						.getItemAtPosition(position));
				mNSeatsNumberPicker.setMaxValue(car.getNSeats() - 1);
				mNSeatsNumberPicker.setEnabled(true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
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

	/**
	 * Initialize the widgets displaying the advanced information of the
	 * TripOffer to be created
	 */
	@Override
	protected void setupAdvancedInformationWidgets() {
		super.setupAdvancedInformationWidgets();

		mReturnDTImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDateTimePickerDialog(v);
			}
		});
	}

	/**
	 * Initialize the widgets displaying other data of the TripOffer to be
	 * created
	 */
	@Override
	protected void setupTripDataWidgets() {
		super.setupTripDataWidgets();

		mPrice = (EditText) findViewById(R.id.editTextPrice);
		mAllow = new ArrayList<CheckBox>();
		mAllow.add((CheckBox) findViewById(R.id.checkBoxLuggage));
		mAllow.add((CheckBox) findViewById(R.id.checkBoxSmoke));
		mAllow.add((CheckBox) findViewById(R.id.checkBoxPets));
		mAllow.add((CheckBox) findViewById(R.id.checkBoxFood));

		mNSeatsNumberPicker.setEnabled(false);
	}

	/**
	 * If a new instance of TripOffer can be initialized from the data provided,
	 * create and initialize it
	 */
	@Override
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
			for (int n = 1; n < mPlaceLocators.size() - 1; n++)
				waypoints
						.add(new Waypoint(mPlaceLocators.get(n).getPlace(), n));
			TripOffer trip = null;
			if (!mTwoWayTripSwitch.isChecked()
					&& !mRegularTripSwitch.isChecked()) {
				trip = new TripOffer(mFromPlaceLocator.getPlace(),
						mToPlaceLocator.getPlace(), mDateTime, me, car,
						mNSeatsNumberPicker.getValue(),
						new BigDecimal(Double.parseDouble(mPrice.getText()
								.toString())).setScale(2, RoundingMode.HALF_UP)
								.doubleValue(), allowed);
			}
			if (mTwoWayTripSwitch.isChecked() && mRegularTripSwitch.isChecked()) {
				trip = new TripOffer(mFromPlaceLocator.getPlace(),
						mToPlaceLocator.getPlace(), mDateTime, mReturnDateTime,
						me, car, mNSeatsNumberPicker.getValue(),
						new BigDecimal(Double.parseDouble(mPrice.getText()
								.toString())).setScale(2, RoundingMode.HALF_UP)
								.doubleValue(), allowed,
						mWeekDays.getSelectedDays(),
						mWeeksValues.get(mWeeksRadioGroup
								.getCheckedRadioButtonId()));
			}
			if (mTwoWayTripSwitch.isChecked()
					&& !mRegularTripSwitch.isChecked()) {
				trip = new TripOffer(mFromPlaceLocator.getPlace(),
						mToPlaceLocator.getPlace(), mDateTime, mReturnDateTime,
						me, car, mNSeatsNumberPicker.getValue(),
						new BigDecimal(Double.parseDouble(mPrice.getText()
								.toString())).setScale(2, RoundingMode.HALF_UP)
								.doubleValue(), allowed);
			}
			if (!mTwoWayTripSwitch.isChecked()
					&& mRegularTripSwitch.isChecked()) {
				trip = new TripOffer(mFromPlaceLocator.getPlace(),
						mToPlaceLocator.getPlace(), mDateTime, me, car,
						mNSeatsNumberPicker.getValue(),
						new BigDecimal(Double.parseDouble(mPrice.getText()
								.toString())).setScale(2, RoundingMode.HALF_UP)
								.doubleValue(), allowed,
						mWeekDays.getSelectedDays(),
						mWeeksValues.get(mWeeksRadioGroup
								.getCheckedRadioButtonId()));
			}
			trip.setDistance(mRouteMap.getDistance());
			trip.setWaypoints(waypoints.toArray(new Waypoint[0]));
			if (mTripCharacteristicsEditText.getText().toString().trim()
					.length() > 0)
				trip.setCharacteristics(mTripCharacteristicsEditText.getText()
						.toString().trim());

			QueryListener newTripListener = new QueryListener() {

				@Override
				public void onError() {
					Toast.makeText(NewTripOfferActivity.this,
							R.string.newTripFailure, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onDataChange(double data, boolean saved) {
				}

				@Override
				public void onDataChange(boolean data, boolean saved) {
				}

				@Override
				public void onDataChange(ObjectPrx data, boolean saved) {
					if (saved) {
						mTripCharacteristicsEditText
								.setText(((TripOfferTypPrx) data)._toString());
						Toast.makeText(NewTripOfferActivity.this,
								R.string.newTripSuccess, Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent(NewTripOfferActivity.this,
								TripOfferDetailsActivity.class);
						intent.putExtra(TripOfferDetailsActivity.EXTRA_TRIP_ID,
								((TripOfferTypPrx) data).getTripId());
						startActivity(intent);
						finish();
					}
				}

				@Override
				public void onDataChange(QueryModel data, boolean saved) {
				}
			};
			_sessionController
					.newTripOffer(newTripListener,
							new uclm.esi.cardroid.data.ice.TripOffer(
									_sessionController).newInstance(trip));
			((Button) findViewById(R.id.buttonNewTrip)).setEnabled(false);
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
	@Override
	protected boolean tripAvailable() {
		boolean available = super.tripAvailable();
		available = available
				&& mPrice.getText().toString().trim().length() > 0;
		available = available && mCarSpinner.getSelectedItem() != null;
		for (PlaceLocator placeLocator : mPlaceLocators) {
			available = available && placeLocator.placeResolved();
		}
		return available;
	}

	/**
	 * @return The message to be displayed in the AlertDialog created
	 */
	@Override
	protected String getErrorMessage() {
		StringBuilder sb = new StringBuilder(super.getErrorMessage());
		if (mCarSpinner.getSelectedItem() == null) {
			sb.append("No se ha seleccionado un coche\n");
		}
		int emptyWaypoints = 0;
		for (PlaceLocator placeLocator : mPlaceLocators) {
			if (!placeLocator.placeResolved()) {
				emptyWaypoints++;
			}
		}
		if (emptyWaypoints > 0) {
			sb.append(emptyWaypoints
					+ " de los puntos del trayecto no han sido completados\n");
		}
		return sb.toString();
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
		mPlaceLocators.add(mPlaceLocators.size() - 1, waypoint);

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
				int i, index = -1;
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
	@Override
	public void onPlaceLocated(PlaceLocator placeLocator) {
		if (placeLocator == mFromPlaceLocator
				|| placeLocator == mToPlaceLocator) {
			super.onPlaceLocated(placeLocator);
		} else {
			if (placeLocator.hasPreviousValue()) {
				int index = -1;
				for (int i = 0; mPlaceLocators.get(i) != placeLocator; i++) {
					if (mPlaceLocators.get(i).getPlaceCoords() != null) {
						index++;
					}
				}
				mRouteMap.setWaypoint(index, placeLocator.getPlaceCoords());
			} else {
				mRouteMap.addWaypoint(placeLocator.getPlaceCoords());
			}
		}
	}

	public void calculateRoutePrice(View v) {
		if (mCarSpinner.getSelectedItem() != null && mRouteMap.routeAvailable())
			computePrice(new Car().newInstance(uclm.esi.cardroid.data.ice.Car
					.extractObject((CarTypPrx) mCarSpinner.getSelectedItem())),
					mNSeatsNumberPicker.getValue(), mRouteMap.getDistance());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		TableLayout waypointsTable = (TableLayout) findViewById(R.id.tableLayoutWaypoints);
		int nWaypoints = waypointsTable.getChildCount();
		waypointsTable.removeAllViews();
		outState.putInt(STATE_NWAYPOINTS, nWaypoints);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int nWaypoints = savedInstanceState.getInt(STATE_NWAYPOINTS);
		for (int n = 0; n < nWaypoints; n++) {
			addWaypoint(null);
		}
	}

	/**
	 * Create an instance of the DateTimePickerDialog dialog fragment and show
	 * it
	 */
	public void showDateTimePickerDialog(View v) {
		if (v == mDTImageButton) {
			if (mDateTime == null) {
				mDTDialog = new DateTimePickerDialog();
			} else {
				mDTDialog = DateTimePickerDialog.newInstance(mDateTime);
			}
			mDTDialog.show(getSupportFragmentManager(),
					DateTimePickerDialog.class.getSimpleName());
		}
		if (v == mReturnDTImageButton) {
			if (mReturnDateTime == null) {
				mReturnDTDialog = new DateTimePickerDialog();
			} else {
				mReturnDTDialog = DateTimePickerDialog
						.newInstance(mReturnDateTime);
			}
			mReturnDTDialog.show(getSupportFragmentManager(),
					DateTimePickerDialog.class.getSimpleName());
		}
	}

	/**
	 * User touched the dialog's positive button
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
			super.mDateTime = mDateTime = dtpDialog.getDateTime();
			eText = mDTEditText;
		}
		if (dialog == mReturnDTDialog) {
			super.mReturnDateTime = mReturnDateTime = dtpDialog.getDateTime();
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
	@Override
	public void onDialogNegativeClick(DatePickerDialog dialog) {
		dialog.getDialog().cancel();
	}
}
