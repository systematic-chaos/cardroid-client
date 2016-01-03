package uclm.esi.cardroid.mytrips;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.model.LatLng;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.SessionActivity;
import uclm.esi.cardroid.data.zerocice.LatLngTyp;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.util.RouteMap;
import uclm.esi.cardroid.util.WeekDaysButtonBar;
import Ice.ObjectPrx;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * \class TripDetailsActivity
 * Activity that displays the data contained in an
 * instance of Trip
 */
public abstract class TripDetailsActivity extends SessionActivity {

	public static final String EXTRA_TRIP_ID = "tripDetails.TRIP_ID";

	protected TripTypPrx mTrip;
	protected String mDatetime, mReturnDatetime;

	protected Hashtable<Integer, Integer> mTabs;
	protected RouteMap mRouteMap;

	protected TextView mHeaderText, mFromText, mToText, mDateText,
			mProposerText, mAvailableSeatsText, mTripCharacteristicsText;

	protected ArrayList<View> mTwoWayTripViews;
	protected TextView mReturnDTTextView;
	protected ArrayList<View> mRegularTripViews;
	protected WeekDaysButtonBar mWeekDays;
	protected TextView mWeeksTextView;

	/**
	 * Extract the Trip instance from the Bundle contained in the calling Intent
	 * , and call setupWidgets to initialize the UI widgets
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewId());
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mTrip == null) {
			mTrip = _sessionController.getTripFromId(getIntent().getIntExtra(
					EXTRA_TRIP_ID, -1));

			setupWidgets();
			displayTripData();

			QueryListener listener = new QueryListener() {

				@Override
				public void onError() {
					Toast.makeText(TripDetailsActivity.this,
							R.string.tripAvailabilityFailure,
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onDataChange(boolean data, boolean saved) {
					if (saved) {
						if (!data)
							setupJoinButton();
					}
				}

				@Override
				public void onDataChange(double data, boolean saved) {
				}

				@Override
				public void onDataChange(ObjectPrx data, boolean saved) {
				}

				@Override
				public void onDataChange(QueryModel data, boolean saved) {
				}
			};

			_sessionController.userTripRegistered(listener,
					_sessionController.getMyUser(), mTrip);

			setupActionBar(mTrip._toString(), true);
		}
	}

	protected int getContentViewId() {
		return R.layout.trip_details;
	}

	/**
	 * Initialize the UI widgets
	 */
	@Override
	protected void setupWidgets() {
		mTabs = new Hashtable<Integer, Integer>(3);
		mHeaderText = (TextView) findViewById(R.id.textViewHeader);
		setupBasicInformationWidgets();
		setupAdvancedInformationWidgets();
		setupTripDataWidgets();
	}

	/**
	 * Initialize the widgets displaying the basic information of the Trip
	 */
	protected void setupBasicInformationWidgets() {
		mTabs.put(R.id.textBasicInformation, R.id.layoutBasicInformation);

		mFromText = (TextView) findViewById(R.id.textViewFrom);
		mToText = (TextView) findViewById(R.id.textViewTo);
		mRouteMap = (RouteMap) getSupportFragmentManager().findFragmentById(
				R.id.routeMap);
		mDateText = (TextView) findViewById(R.id.textViewDate);
		mProposerText = (TextView) findViewById(R.id.textViewProposer);
		mProposerText.setPaintFlags(mProposerText.getPaintFlags() 
				| Paint.UNDERLINE_TEXT_FLAG);
	}

	/**
	 * Initialize the widgets displaying the advanced information of the Trip
	 */
	protected void setupAdvancedInformationWidgets() {
		mTabs.put(R.id.textAdvancedInformation, R.id.layoutAdvancedInformation);
		LinearLayout contentLayout = (LinearLayout) findViewById(R.id.layoutAdvancedInformation);

		if (!(mTrip.hasTripReturnDate() || mTrip.hasWeekDaysPeriodicity()))
			return;

		TextView tv;
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.LEFT;
		int offset = 0;

		if (mTrip.hasTripReturnDate()) {
			computeDateTime();
			mTwoWayTripViews = new ArrayList<View>();
			tv = new TextView(this);
			tv.setText(R.string.returnDate);
			tv.setTextAppearance(this, android.R.style.TextAppearance_Small);
			mTwoWayTripViews.add(tv);
			tv = new TextView(this);
			tv.setText(mReturnDatetime);
			tv.setTextAppearance(this, android.R.style.TextAppearance_Medium);
			mTwoWayTripViews.add(tv);

			offset = mTwoWayTripViews.size();
			for (int n = 0; n < offset; n++)
				contentLayout.addView(mTwoWayTripViews.get(n), params);
		}

		if (mTrip.hasWeekDaysPeriodicity()) {
			mRegularTripViews = new ArrayList<View>();
			tv = new TextView(this);
			tv.setText(R.string.regularTrip);
			tv.setTextAppearance(this, android.R.style.TextAppearance_Small);
			mRegularTripViews.add(tv);
			mWeeksTextView = new TextView(this);
			int periodicity;
			switch (mTrip.getTripPeriodicity()) {
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
					android.R.style.TextAppearance_Medium);
			mRegularTripViews.add(mWeeksTextView);

			FragmentManager fragmentManager = getSupportFragmentManager();
			mWeekDays = new WeekDaysButtonBar();
			String[] wd = mTrip.getTripWeekDays();
			char[] weekDays = new char[wd.length];
			for (int n = 0; n < wd.length; n++)
				weekDays[n] = wd[n].charAt(0);
			mWeekDays.setSelectedDays(weekDays);
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
	 * Initialize the widgets displaying other data of the Trip
	 */
	protected void setupTripDataWidgets() {
		mTabs.put(R.id.textTripData, R.id.layoutTripData);

		mAvailableSeatsText = (TextView) findViewById(R.id.textViewSeats);
		mTripCharacteristicsText = (TextView) findViewById(R.id.textViewComments);
	}

	/**
	 * Fill the UI widgets showing mTrip data
	 */
	protected void displayTripData() {
		if (mDatetime == null)
			computeDateTime();

		setupMap();

		StringBuilder header = new StringBuilder();
		header.append(mTrip.getPlace1().getName() + " - ");
		header.append(mTrip.getPlace2().getName());
		header.append("\t" + mDatetime);
		mHeaderText.setText(header);

		mFromText.setText(mTrip.getPlace1().getName());

		mToText.setText(mTrip.getPlace2().getName());

		mDateText.setText(mDatetime);

		mAvailableSeatsText.setText(String.valueOf(mTrip.getNSeats()));

		mTripCharacteristicsText.setText(mTrip.getCharacteristics());
	}

	protected void setupMap() {
		// Obtain the RouteMap from the SupportFragmentManager
		mRouteMap = (RouteMap) getSupportFragmentManager().findFragmentById(
				R.id.routeMap);

		mRouteMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				LatLngTyp fromCoords = mTrip.getPlace1().getCoords();
				LatLngTyp toCoords = mTrip.getPlace2().getCoords();
				mRouteMap.setLocations(
						new LatLng(fromCoords.getLatitude(), fromCoords
								.getLongitude()),
						new LatLng(toCoords.getLatitude(), toCoords
								.getLongitude()));

				if (mTrip.hasWeekDaysPeriodicity())
					mWeekDays.setEnabled(false);
			}
		});
	}

	protected abstract void setupJoinButton();

	/**
	 * Get the date and time stored in mTrip , and format it to a written
	 * representable format which will be stored in mDatetime
	 */
	protected void computeDateTime() {
		mDatetime = mTrip.getTripDate()._toString();
		if (mTrip.hasTripReturnDate())
			mReturnDatetime = mTrip.getTripReturnDate()._toString();
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
	public void switchInfoLayouts(View activateTab) {
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

	@Override
	public CardroidEventStormListener getCardroidEventStormListener() {
		return _eventStorm;
	}

	@Override
	public boolean replayEvents() {
		return false;
	}
}
