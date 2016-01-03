package uclm.esi.cardroid.mytrips;

import java.util.ArrayList;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.Trip;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.data.zerocice.TripTypPrxHelper;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.newtrip.NewTripOfferActivity;
import uclm.esi.cardroid.search.SearchTripResultsActivity.TripListAdapter;
import uclm.esi.cardroid.util.ListViewArrayFragment;
import Ice.ObjectPrx;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * \class MyTripsPublishedFragment
 * A fragment that displays those trips
 * published by the user
 */
public class MyTripsPublishedFragment extends ListViewArrayFragment<TripTypPrx> {

	@Override
	protected void setupListView() {

		mQueryListener = new QueryListener() {
			@Override
			public void onError() {
				mAdapter = null;
				setListAdapter(null);
				getListView().invalidateViews();
				setEmptyText(getString(R.string.retrievalError));
				mProgressBar.setVisibility(View.GONE);
			}

			@Override
			public void onDataChange(double data, boolean saved) {
			}

			@Override
			public void onDataChange(boolean data, boolean saved) {
			}

			@Override
			public void onDataChange(ObjectPrx data, boolean saved) {
			}

			@Override
			public void onDataChange(QueryModel data, boolean saved) {
				if (saved) {
					mQueryModel = data;
					ArrayList<TripTypPrx> adapterData = new ArrayList<TripTypPrx>(
							data.data.size());
					for (ObjectPrx oprx : data.data)
						adapterData.add(TripTypPrxHelper.checkedCast(oprx));
					if (mAdapter == null) {
						mAdapter = new TripListAdapter(getActivity(),
								adapterData);
						setListAdapter(mAdapter);
					} else {
						mAdapter.addAll(adapterData);
					}
					mAdapter.notifyDataSetChanged();
					mProgressBar.setVisibility(View.GONE);
					if (mAdapter.getCount() <= 0)
						setupEmptyView();
				}
			}
		};
	}

	@Override
	public void updateListAdapterData() {
		mProgressBar.setVisibility(View.VISIBLE);
		if (mSessionController != null)
			mSessionController.getUserTrips(mQueryListener,
					mSessionController.getMyUser());
	}

	/**
	 * Setup the View to be displayed by the ListView when it does not have an
	 * Adapter set or such an Adapter is empty (does not contain any elements)
	 */
	protected void setupEmptyView() {
		setEmptyText(getString(R.string.noTripsPublished));
		LinearLayout emptyLayout = (LinearLayout) getEmptyView();
		Button tripButton = new Button(getActivity());
		tripButton.setText(R.string.offerTrip);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
		emptyLayout.addView(tripButton, params);
		tripButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), NewTripOfferActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(new Intent(getActivity(),
						NewTripOfferActivity.class));
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent i;
		TripTypPrx trip = TripTypPrxHelper.checkedCast((ObjectPrx) parent
				.getItemAtPosition(position));
		switch (trip.getTripType()) {
		case Trip.TRIP_OFFER_TYPE:
			i = new Intent(getActivity(), TripOfferDetailsActivity.class);
			break;
		case Trip.TRIP_REQUEST_TYPE:
			i = new Intent(getActivity(), TripRequestDetailsActivity.class);
			break;
		case Trip.TRIP_TYPE:
		default:
			i = new Intent(getActivity(), TripDetailsActivity.class);
		}
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra(TripDetailsActivity.EXTRA_TRIP_ID, trip.getTripId());
		startActivity(i);
	}
}
