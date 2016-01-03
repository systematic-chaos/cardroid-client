package uclm.esi.cardroid.newtrip;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.IDateTimePrefs.TimePreferences;
import uclm.esi.cardroid.data.android.DateTimePrefs;
import uclm.esi.cardroid.data.android.TripRequest;
import uclm.esi.cardroid.data.android.User;
import uclm.esi.cardroid.data.zerocice.TripRequestTypPrx;
import uclm.esi.cardroid.mytrips.TripRequestDetailsActivity;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.search.DateTimePrefsPickerDialog;
import uclm.esi.cardroid.util.DatePickerDialog;
import Ice.ObjectPrx;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * \class NewTripRequestActivity 
 * Activity extending NewTrip and supporting the
 * creation of new instances of TripRequest
 */
public class NewTripRequestActivity extends NewTrip {

	protected DateTimePrefs mDateTime, mReturnDateTime;

	/**
	 * Extend the MyFragmentActivity.onCreate method, setting the proper layout
	 * for this activity, initializing the UI widgets and setup the action bar
	 * with this activity's title
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtrip_request);

		setupWidgets();

		setupActionBar(R.string.requestTrip, true);
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
	 * If a new instance of TripRequest can be initialized from the data
	 * provided, create and initialize it
	 */
	public void newTrip(View v) {
		if (tripAvailable()) {
			User me = new User().newInstance(uclm.esi.cardroid.data.ice.User
					.extractObject(_sessionController.getMyUser()));
			TripRequest trip = null;
			if (!mTwoWayTripSwitch.isChecked()
					&& !mRegularTripSwitch.isChecked()) {
				trip = new TripRequest(mFromPlaceLocator.getPlace(),
						mToPlaceLocator.getPlace(), mDateTime, me,
						mNSeatsNumberPicker.getValue());
			}
			if (mTwoWayTripSwitch.isChecked() && mRegularTripSwitch.isChecked()) {
				trip = new TripRequest(mFromPlaceLocator.getPlace(),
						mToPlaceLocator.getPlace(), mDateTime, mReturnDateTime,
						me, mNSeatsNumberPicker.getValue(),
						mWeekDays.getSelectedDays(),
						mWeeksValues.get(mWeeksRadioGroup
								.getCheckedRadioButtonId()));
			}

			if (mTwoWayTripSwitch.isChecked()
					&& !mRegularTripSwitch.isChecked()) {
				trip = new TripRequest(mFromPlaceLocator.getPlace(),
						mToPlaceLocator.getPlace(), mDateTime, mReturnDateTime,
						me, mNSeatsNumberPicker.getValue());
			}
			if (!mTwoWayTripSwitch.isChecked()
					&& mRegularTripSwitch.isChecked()) {
				trip = new TripRequest(mFromPlaceLocator.getPlace(),
						mToPlaceLocator.getPlace(), mDateTime, me,
						mNSeatsNumberPicker.getValue(),
						mWeekDays.getSelectedDays(),
						mWeeksValues.get(mWeeksRadioGroup
								.getCheckedRadioButtonId()));
			}
			trip.setDistance(mRouteMap.getDistance());
			if (mTripCharacteristicsEditText.getText().toString().trim()
					.length() > 0)
				trip.setCharacteristics(mTripCharacteristicsEditText.getText()
						.toString().trim());

			QueryListener newTripListener = new QueryListener() {

				@Override
				public void onError() {
					Toast.makeText(NewTripRequestActivity.this,
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
								.setText(((TripRequestTypPrx) data)._toString());
						Toast.makeText(NewTripRequestActivity.this,
								R.string.newTripSuccess, Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent(NewTripRequestActivity.this,
								TripRequestDetailsActivity.class);
						intent.putExtra(
								TripRequestDetailsActivity.EXTRA_TRIP_ID,
								((TripRequestTypPrx) data).getTripId());
						startActivity(intent);
						finish();
					}
				}

				@Override
				public void onDataChange(QueryModel data, boolean saved) {
				}
			};
			_sessionController.newTripRequest(newTripListener,
					new uclm.esi.cardroid.data.ice.TripRequest(
							_sessionController).newInstance(trip));
			((Button) findViewById(R.id.buttonNewTrip)).setEnabled(false);
		} else {
			showErrorDialog();
		}
	}

	/**
	 * Create an instance of the DateTimePrefsPickerDialog dialog fragment and
	 * show it
	 */
	public void showDateTimePickerDialog(View v) {
		if (v == mDTImageButton) {
			if (mDateTime == null) {
				mDTDialog = new DateTimePrefsPickerDialog();
			} else {
				mDTDialog = DateTimePrefsPickerDialog
						.newInstance((DateTimePrefs) mDateTime);
			}
			mDTDialog.show(getSupportFragmentManager(),
					DateTimePrefsPickerDialog.class.getSimpleName());
		}
		if (v == mReturnDTImageButton) {
			if (mReturnDateTime == null) {
				mReturnDTDialog = new DateTimePrefsPickerDialog();
			} else {
				mReturnDTDialog = DateTimePrefsPickerDialog
						.newInstance((DateTimePrefs) mReturnDateTime);
			}
			mReturnDTDialog.show(getSupportFragmentManager(),
					DateTimePrefsPickerDialog.class.getSimpleName());
		}
	}

	/**
	 *  User touched the dialog's positive button
	 */
	@Override
	public void onDialogPositiveClick(DatePickerDialog dialog) {
		/* The dialog fragment receives a reference to this Activity through * 
		 * the Fragment.onAttach() callback, which it uses to call the       *
		 * following methods defined by the 								 *
		 * DateTimePickerDialogFragment.DateTimePickerDialogListener		 *
		 * interface 														 */
		DateTimePrefsPickerDialog dtpDialog = (DateTimePrefsPickerDialog) dialog;
		EditText eText = null;
		if (dialog == mDTDialog) {
			super.mDateTime = mDateTime = dtpDialog.getDateTimePrefs();
			eText = mDTEditText;
		}
		if (dialog == mReturnDTDialog) {
			super.mReturnDateTime = mReturnDateTime = dtpDialog
					.getDateTimePrefs();
			eText = mReturnDTEditText;
		}
		int[] date = dtpDialog.getDate();
		TimePreferences time = dtpDialog.getTimePreferences();

		if (eText != null) {
			eText.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
					DateFormat.SHORT, Locale.getDefault()).format(
					new GregorianCalendar(date[0], date[1], date[2]).getTime()));
			eText.append("\t" + time.name());
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
