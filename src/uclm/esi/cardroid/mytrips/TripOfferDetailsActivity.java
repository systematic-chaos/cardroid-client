package uclm.esi.cardroid.mytrips;

import Ice.ObjectPrx;
import android.content.Intent;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.zerocice.PassengerTyp;
import uclm.esi.cardroid.data.zerocice.TripOfferTypPrx;
import uclm.esi.cardroid.data.zerocice.WaypointTyp;
import uclm.esi.cardroid.myprofile.FriendProfileActivity;
import uclm.esi.cardroid.myprofile.ProfileActivity;
import uclm.esi.cardroid.mytrips.NumberPickerDialog.NumberPickerDialogListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;


/**
 * \class TripOfferDetailsActivity
 * Activity that displays the data contained in an
 * instance of TripOffer
 */
public class TripOfferDetailsActivity extends TripDetailsActivity implements
		NumberPickerDialogListener {
	private TripOfferTypPrx mTripOffer;

	private TextView mCarText, mPriceText;
	private TableLayout mWaypointsTable;
	private TableLayout mPassengersTable;

	private NumberPickerDialog mNumberPickerDialog;

	@Override
	protected void setupWidgets() {
		if (mTripOffer == null) {
			mTripOffer = _sessionController.getTripOfferFromId(getIntent()
					.getIntExtra(EXTRA_TRIP_ID, -1));
			mTrip = mTripOffer;
		}

		super.setupWidgets();
	}

	@Override
	protected int getContentViewId() {
		return R.layout.trip_offer_details;
	}

	@Override
	protected void setupBasicInformationWidgets() {
		super.setupBasicInformationWidgets();

		mWaypointsTable = (TableLayout) findViewById(R.id.tableLayoutWaypoints);
		mPassengersTable = (TableLayout) findViewById(R.id.tableLayoutPassengers);
	}

	@Override
	protected void setupTripDataWidgets() {
		super.setupTripDataWidgets();

		mCarText = (TextView) findViewById(R.id.textViewCar);
		mPriceText = (TextView) findViewById(R.id.textViewPrice);
	}

	/**
	 * Fill the UI widgets showing mTripOffer data
	 */
	@Override
	protected void displayTripData() {
		super.displayTripData();

		for (WaypointTyp waypoint : mTripOffer.getTripWaypoints()) {
			TableRow row = new TableRow(this);
			TextView waypointName = new TextView(this);
			waypointName.setText(waypoint.getWaypointPlace().getName());
			waypointName.setTextAppearance(this,
					android.R.style.TextAppearance_Medium);
			row.addView(waypointName);
			mWaypointsTable.addView(row);
		}

		mProposerText.setText(mTripOffer.getTripDriver()._toString());
		mProposerText.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TripOfferDetailsActivity.this,
						FriendProfileActivity.class);
				intent.putExtra(FriendProfileActivity.EXTRA_USER_EMAIL,
						mTripOffer.getTripDriver().getEmail());
				startActivity(intent);
			}
		});

		for (PassengerTyp passenger : mTripOffer.getTripPassengers()) {
			TableRow row = new TableRow(this);
			TextView passengerName = new TextView(this);
			passengerName.setText(passenger.getPassengerUser()._toString());
			passengerName.setTextAppearance(this,
					android.R.style.TextAppearance_Medium);
			passengerName.setTextColor(getResources().getColor(
					R.color.RoyalBlue));
			passengerName.setPaintFlags(passengerName.getPaintFlags()
					| Paint.UNDERLINE_TEXT_FLAG);
			TextView passengerSeats = new TextView(this);
			passengerSeats.setText(String.valueOf(passenger.getNSeats()));
			passengerSeats.setTextAppearance(this,
					android.R.style.TextAppearance_Medium);
			row.addView(passengerName);
			row.addView(passengerSeats);
			mPassengersTable.addView(row);
			row.setClickable(true);
			row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(TripOfferDetailsActivity.this,
							ProfileActivity.class);
					intent.putExtra(
							ProfileActivity.EXTRA_USER_EMAIL,
							mTripOffer.getTripPassengers()
									.get(mPassengersTable.indexOfChild(v))
									.getPassengerUser().getEmail());
					startActivity(intent);
				}
			});
		}

		mCarText.setText(mTripOffer.getTripCar()._toString());

		mPriceText.setText(String.valueOf(mTripOffer.getPrice()));

		String[] allow = mTripOffer.getAllowedFeatures();
		((CheckBox) findViewById(R.id.checkBoxLuggage)).setChecked(Boolean
				.valueOf(allow[0]));
		((CheckBox) findViewById(R.id.checkBoxSmoke)).setChecked(Boolean
				.valueOf(allow[1]));
		((CheckBox) findViewById(R.id.checkBoxPets)).setChecked(Boolean
				.valueOf(allow[2]));
		((CheckBox) findViewById(R.id.checkBoxFood)).setChecked(Boolean
				.valueOf(allow[3]));
	}

	@Override
	protected void setupJoinButton() {
		Button joinButton = new Button(this);
		joinButton.setText(R.string.join);
		LinearLayout layout = (LinearLayout) findViewById(R.id.trip_details_layout);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		layout.addView(joinButton, lp);

		joinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNumberPickerDialog(v);
			}
		});
	}

	/**
	 * Get the date and time stored in mTripOffer , and format it to a written
	 * representable format which will be stored in mDateTime
	 */
	@Override
	protected void computeDateTime() {
		mDatetime = mTripOffer.getTripDateTime()._toString();
		if (mTripOffer.hasTripReturnDate())
			mReturnDatetime = mTripOffer.getTripReturnDateTime()._toString();
	}

	/**
	 * Create an instance of the NumberPickerDialog dialog fragment and show it
	 */
	@Override
	public void showNumberPickerDialog(View v) {
		mNumberPickerDialog = NumberPickerDialog.newInstance(1, getResources()
				.getInteger(R.integer.seats_min), mTripOffer.getNSeats());
		mNumberPickerDialog.show(getSupportFragmentManager(),
				NumberPickerDialog.class.getSimpleName());
	}

	@Override
	public void onDialogPositiveClick(NumberPickerDialog dialog) {
		QueryListener joinTripListener = new QueryListener() {

			@Override
			public void onError() {
				Toast.makeText(TripOfferDetailsActivity.this,
						R.string.tripJoinFailure, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDataChange(boolean data, boolean saved) {
			}

			@Override
			public void onDataChange(double data, boolean saved) {
			}

			@Override
			public void onDataChange(ObjectPrx data, boolean saved) {
				if (saved) {
					mTrip = mTripOffer = (TripOfferTypPrx) data;
					displayTripData();
					Intent intent = new Intent(TripOfferDetailsActivity.this,
							MyTripsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			}

			@Override
			public void onDataChange(QueryModel data, boolean saved) {
			}
		};

		_sessionController.joinTrip(joinTripListener, mTripOffer,
				_sessionController.getMyUser(), dialog.getValue());
	}

	@Override
	public void onDialogNegativeClick(NumberPickerDialog dialog) {
		dialog.getDialog().cancel();
	}
}
