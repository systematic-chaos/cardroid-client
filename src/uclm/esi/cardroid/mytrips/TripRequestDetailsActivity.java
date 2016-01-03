package uclm.esi.cardroid.mytrips;

import Ice.ObjectPrx;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.TripRequest;
import uclm.esi.cardroid.data.zerocice.TripRequestTypPrx;
import uclm.esi.cardroid.myprofile.FriendProfileActivity;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.newtrip.OrganizeTripActivity;

/**
 * \class TripRequestDetailsActivity
 * Activity that displays the data contained in an
 * instance of TripRequest
 */
public class TripRequestDetailsActivity extends TripDetailsActivity {
	protected TripRequestTypPrx mTripRequest;

	private static final int REQUEST_ORGANIZE_TRIP = 1;

	@Override
	protected void setupWidgets() {
		if (mTripRequest == null) {
			mTripRequest = _sessionController.getTripRequestFromId(getIntent()
					.getIntExtra(EXTRA_TRIP_ID, -1));
			mTrip = mTripRequest;
		}

		super.setupWidgets();
	}

	@Override
	protected int getContentViewId() {
		return R.layout.trip_request_details;
	}

	/**
	 * Fill the UI widgets showing mTripRequest data
	 */
	@Override
	protected void displayTripData() {
		super.displayTripData();

		mProposerText.setText(mTripRequest.getTripRequester()._toString());
		mProposerText.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TripRequestDetailsActivity.this,
						FriendProfileActivity.class);
				intent.putExtra(FriendProfileActivity.EXTRA_USER_EMAIL,
						mTripRequest.getTripRequester().getEmail());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void setupJoinButton() {
		Button organizeButton = new Button(this);
		organizeButton.setText(R.string.organize);
		LinearLayout layout = (LinearLayout) findViewById(R.id.trip_details_layout);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		layout.addView(organizeButton, lp);

		organizeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TripRequestDetailsActivity.this,
						OrganizeTripActivity.class);
				intent.putExtra(
						OrganizeTripActivity.EXTRA_PROPOSAL,
						new TripRequest()
								.newInstance(uclm.esi.cardroid.data.ice.TripRequest
										.extractObject(mTripRequest)));
				startActivityForResult(intent, REQUEST_ORGANIZE_TRIP);
			}
		});
	}

	/**
	 * Get the date and time stored in mTripRequest, and format it to a written
	 * representable format which will be stored in mDateTime
	 */
	@Override
	protected void computeDateTime() {
		mDatetime = mTripRequest.getTripDateTimePrefs()._toString();
		if (mTripRequest.hasTripReturnDate())
			mReturnDatetime = mTripRequest.getTripReturnDateTimePrefs()
					._toString();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_ORGANIZE_TRIP:
			if (resultCode == RESULT_OK) {
				QueryListener organizeTripListener = new QueryListener() {

					@Override
					public void onError() {
						Toast.makeText(TripRequestDetailsActivity.this,
								R.string.newTripFailure, Toast.LENGTH_SHORT)
								.show();
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
							Toast.makeText(TripRequestDetailsActivity.this,
									R.string.newTripSuccess, Toast.LENGTH_SHORT)
									.show();
							Intent intent = new Intent(
									TripRequestDetailsActivity.this,
									MyTripsActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
						}
					}

					@Override
					public void onDataChange(QueryModel data, boolean saved) {
					}
				};
				_sessionController
						.organizeTrip(
								organizeTripListener,
								mTripRequest,
								new uclm.esi.cardroid.data.ice.TripOffer(
										_sessionController)
										.newInstance((uclm.esi.cardroid.data.android.TripOffer) data
												.getParcelableExtra(OrganizeTripActivity.EXTRA_OFFER)));
			}
			break;
		}
	}
}
