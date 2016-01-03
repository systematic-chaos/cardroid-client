package uclm.esi.cardroid.newtrip;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.SessionActivity;
import uclm.esi.cardroid.data.ITrip.Periodicity;
import uclm.esi.cardroid.data.android.Date;
import uclm.esi.cardroid.data.android.Trip;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.util.DatePickerDialog;
import uclm.esi.cardroid.util.PlaceLocator;
import uclm.esi.cardroid.util.RouteMap;
import uclm.esi.cardroid.util.WeekDaysButtonBar;
import uclm.esi.cardroid.util.DatePickerDialog.DateTimePickerDialogListener;
import uclm.esi.cardroid.util.PlaceLocator.PlaceLocatorListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Checkable;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

/**
 * \class NewTrip
 * Activity supporting the creation of new trips (new instances
 * of Trip , so different subclasses of this abstract class should provide
 * support to create new instances of the different subclasses of Trip , as
 * TripOffer and TripRequest are
 */
public abstract class NewTrip extends SessionActivity implements
		PlaceLocatorListener, DateTimePickerDialogListener {

	protected Hashtable<Integer, Integer> mTabs;
	protected PlaceLocator mFromPlaceLocator, mToPlaceLocator;
	protected RouteMap mRouteMap;
	protected EditText mDTEditText;
	protected ImageButton mDTImageButton;
	protected DatePickerDialog mDTDialog;
	protected Date mDateTime;

	protected Switch mTwoWayTripSwitch;
	protected ArrayList<View> mTwoWayTripViews;
	protected EditText mReturnDTEditText;
	protected ImageButton mReturnDTImageButton;
	protected DatePickerDialog mReturnDTDialog;
	protected Date mReturnDateTime;
	protected Switch mRegularTripSwitch;
	protected ArrayList<View> mRegularTripViews;
	protected WeekDaysButtonBar mWeekDays;
	protected RadioGroup mWeeksRadioGroup;
	protected Hashtable<Integer, Periodicity> mWeeksValues;

	protected NumberPicker mNSeatsNumberPicker;
	protected EditText mTripCharacteristicsEditText;

	private static final String STATE_DATETIME = "DATETIME";
	private static final String STATE_RETURNDATETIME = "RETURNDATETIME";
	private static final String STATE_WEEKSRADIOGROUP = "WEEKSRADIOGROUP";
	private static final String STATE_WEEKDAYS = "WEEKDAYS";

	/**
	 * Initialize the UI widgets
	 */
	@Override
	protected void setupWidgets() {
		mTabs = new Hashtable<Integer, Integer>(3);
		setupBasicInformationWidgets();
		setupAdvancedInformationWidgets();
		setupTripDataWidgets();
	}

	/**
	 * Initialize the widgets displaying the basic information of the Trip to be
	 * created
	 */
	protected void setupBasicInformationWidgets() {
		mTabs.put(R.id.textBasicInformation, R.id.layoutBasicInformation);

		mFromPlaceLocator = (PlaceLocator) getSupportFragmentManager()
				.findFragmentById(R.id.fromPlaceLocator);
		mToPlaceLocator = (PlaceLocator) getSupportFragmentManager()
				.findFragmentById(R.id.toPlaceLocator);
		mRouteMap = (RouteMap) getSupportFragmentManager().findFragmentById(
				R.id.routeMap);
		mDTEditText = (EditText) findViewById(R.id.editTextDate);
		mDTImageButton = (ImageButton) findViewById(R.id.imageButtonDate);
	}

	/**
	 * Initialize the widgets displaying the advanced information of the Trip to
	 * be created
	 */
	protected void setupAdvancedInformationWidgets() {
		mTabs.put(R.id.textAdvancedInformation, R.id.layoutAdvancedInformation);

		mTwoWayTripSwitch = (Switch) findViewById(R.id.switchTwoWayTrip);
		mRegularTripSwitch = (Switch) findViewById(R.id.switchRegularTrip);
		mTwoWayTripViews = new ArrayList<View>();
		mRegularTripViews = new ArrayList<View>();
		final LinearLayout contentLayout = (LinearLayout) findViewById(R.id.layoutAdvancedInformation);

		TextView tv = new TextView(this);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.LEFT;
		tv.setLayoutParams(params);
		tv.setText(R.string.returnDate);
		tv.setTextAppearance(this, android.R.attr.textAppearanceSmall);
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mReturnDTEditText = new EditText(this);
		params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM | Gravity.LEFT;
		params.weight = 3;
		mReturnDTEditText.setLayoutParams(params);
		mReturnDTEditText.setEms(10);
		mReturnDTEditText.setFocusable(false);
		mReturnDTEditText.setFocusableInTouchMode(false);
		mReturnDTEditText
				.setRawInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
		mReturnDTImageButton = new ImageButton(this);
		params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		mReturnDTImageButton.setLayoutParams(params);
		mReturnDTImageButton.setImageResource(android.R.drawable.ic_menu_today);
		ll.addView(mReturnDTEditText);
		ll.addView(mReturnDTImageButton);
		mTwoWayTripViews.add(tv);
		mTwoWayTripViews.add(ll);

		mTwoWayTripSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							for (int n = 0; n < mTwoWayTripViews.size(); n++) {
								contentLayout.addView(mTwoWayTripViews.get(n),
										n + 1);
							}
						} else {
							contentLayout.removeViews(1,
									mTwoWayTripViews.size());
						}
					}
				});

		final FragmentManager fragmentManager = getSupportFragmentManager();
		mWeekDays = new WeekDaysButtonBar();
		final WeekDaysButtonBar weekDays = mWeekDays;
		fragmentManager.popBackStack();
		mWeeksRadioGroup = new RadioGroup(this);
		mWeeksRadioGroup.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mWeeksValues = new Hashtable<Integer, Trip.Periodicity>(
				Periodicity.values().length, 1);
		RadioButton rb = new RadioButton(this);
		rb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		rb.setText(R.string.everyWeek);
		mWeeksRadioGroup.addView(rb);
		mWeeksRadioGroup.check(rb.getId());
		mWeeksValues.put(rb.getId(), Periodicity.EVERYWEEK);
		rb = new RadioButton(this);
		rb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		rb.setText(R.string.evenWeeks);
		mWeeksRadioGroup.addView(rb);
		mWeeksValues.put(rb.getId(), Periodicity.EVENWEEKS);
		rb = new RadioButton(this);
		rb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		rb.setText(R.string.oddWeeks);
		mWeeksRadioGroup.addView(rb);
		mWeeksValues.put(rb.getId(), Periodicity.ODDWEEKS);
		mRegularTripViews.add(mWeeksRadioGroup);

		mRegularTripSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int offset = 2;
						if (mTwoWayTripSwitch.isChecked()) {
							offset += mTwoWayTripViews.size();
						}
						if (isChecked) {
							for (int n = 0; n < mRegularTripViews.size(); n++) {
								contentLayout.addView(mRegularTripViews.get(n),
										n + offset);
							}
							FragmentTransaction fragmentTransaction = fragmentManager
									.beginTransaction();
							fragmentTransaction.add(contentLayout.getId(),
									weekDays);
							fragmentTransaction.addToBackStack(null);
							fragmentTransaction.commit();
						} else {
							contentLayout.removeViews(offset,
									mRegularTripViews.size());
							fragmentManager.popBackStack();
						}
					}
				});
	}

	/**
	 * Initialize the widgets displaying other data of the Trip to be created
	 */
	protected void setupTripDataWidgets() {
		mTabs.put(R.id.textTripData, R.id.layoutTripData);

		mNSeatsNumberPicker = (NumberPicker) findViewById(R.id.numberPickerSeats);
		mNSeatsNumberPicker.setMinValue(getResources().getInteger(
				R.integer.seats_min));
		mNSeatsNumberPicker.setMaxValue(getResources().getInteger(
				R.integer.seats_max));
		mTripCharacteristicsEditText = (EditText) findViewById(R.id.editTextComments);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable(STATE_DATETIME, mDateTime);
		outState.putParcelable(STATE_RETURNDATETIME, mReturnDateTime);
		outState.putIntArray(STATE_WEEKDAYS, mWeekDays.getSelectedDayIndexes());
		outState.putString(STATE_WEEKSRADIOGROUP,
				mWeeksValues.get(mWeeksRadioGroup.getCheckedRadioButtonId())
						.name());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mDateTime = savedInstanceState.getParcelable(STATE_DATETIME);
		mReturnDateTime = savedInstanceState
				.getParcelable(STATE_RETURNDATETIME);
		mWeekDays.setSelectedDays(savedInstanceState
				.getIntArray(STATE_WEEKDAYS));

		Periodicity weeks = Periodicity.valueOf(savedInstanceState
				.getString(STATE_WEEKSRADIOGROUP));
		for (Iterator<Map.Entry<Integer, Periodicity>> iterator = mWeeksValues
				.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<Integer, Periodicity> entry = (Entry<Integer, Periodicity>) iterator
					.next();
			if (entry.getValue().equals(weeks)) {
				mWeeksRadioGroup.check(entry.getKey());
			}
		}
	}

	/**
	 * If a new instance of Trip can be initialized from the data provided,
	 * create and initialize it
	 */
	public abstract void newTrip(View v);

	/**
	 * Check whether the data provided by the user can be resolved to a valid
	 * trip route
	 * 
	 * @return Whether a valid trip route can be resolved from the data provided
	 *         by the user
	 */
	protected boolean tripAvailable() {
		boolean available = mRouteMap.routeAvailable() && mDateTime != null;
		available = available
				&& !mFromPlaceLocator.getPlace().equals(
						mToPlaceLocator.getPlace());
		if (mTwoWayTripSwitch.isChecked()) {
			available = available && mReturnDateTime != null;
			available = available && mDateTime.compareTo(mReturnDateTime) < 0;
		}
		if (mRegularTripSwitch.isChecked()) {
			available = available && mWeekDays.getSelectedDays().length > 0;
		}
		return available;
	}

	/**
	 * Show an error AlertDialog displaying a message
	 */
	protected void showErrorDialog() {
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
	protected String getErrorMessage() {
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
		if (mFromPlaceLocator.getPlace().equals(mToPlaceLocator.getPlace())) {
			message.append("Los lugares de origen y destino deben ser diferentes\n");
		}
		if (mDateTime == null) {
			message.append("No se ha introducido una fecha\n");
		}
		if (mTwoWayTripSwitch.isChecked()) {
			if (mReturnDateTime == null) {
				message.append("No se ha introducido una fecha de vuelta\n");
			} else if (mDateTime.compareTo(mReturnDateTime) >= 0) {
				message.append("La fecha de vuelta debe ser posterior a la fecha de ida\n");
			}
		}
		if (mRegularTripSwitch.isChecked()
				&& mWeekDays.getSelectedDays().length == 0) {
			message.append("No se han señalado los días de la semana en los que el viaje se realizará de forma periódica\n");
		}
		return message.toString();
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
	 * Show the View behind the View selected and hide the View s behind the
	 * other CheckedTextView instances (tabs)
	 * 
	 * @param activateTab
	 *            The View selected
	 */
	public void switchTabInfoLayouts(View activateTab) {
		switchTabInfoLayouts(activateTab.getId());
	}

	/**
	 * Show the View behind the View selected and hide the View s behind the
	 * other CheckedTextView instances (tabs)
	 * 
	 * @param activateTabId
	 *            The id of the View selected
	 */
	protected void switchTabInfoLayouts(int activateTabId) {
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
	 * Called when the result of a reverse geocoding or geolocation query
	 * previously performed is available. Depending on which one was the
	 * PlaceLocator originating the call, set the corresponding point (start or
	 * end) in mRouteMap
	 * 
	 * @param placeLocator
	 *            The PlaceLocator originating the call
	 */
	@Override
	public void onPlaceLocated(PlaceLocator placeLocator) {
		if (placeLocator == mFromPlaceLocator) {
			mRouteMap.setFromLocation(placeLocator.getPlaceCoords());
		} else {
			if (placeLocator == mToPlaceLocator) {
				mRouteMap.setToLocation(placeLocator.getPlaceCoords());
			}
		}
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
