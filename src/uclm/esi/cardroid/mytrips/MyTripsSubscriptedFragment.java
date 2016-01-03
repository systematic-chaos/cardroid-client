package uclm.esi.cardroid.mytrips;

import java.util.ArrayList;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.zerocice.TripOfferTypPrx;
import uclm.esi.cardroid.data.zerocice.TripOfferTypPrxHelper;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.newtrip.NewTripRequestActivity;
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
 * \class MyTripsSubscripted
 * A fragment that displays those trips which the user
 * has subscripted herself to
 */
public class MyTripsSubscriptedFragment extends
		ListViewArrayFragment<TripTypPrx> {

	@Override
	public void setupListView() {

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
						adapterData.add(TripOfferTypPrxHelper
								.uncheckedCast(oprx));
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
			mSessionController.getPassengerTrips(mQueryListener,
					mSessionController.getMyUser());
	}

	/**
	 * Setup the View to be displayed by the ListView when it does not have an
	 * Adapter set or such an Adapter is empty (does not contain any elements)
	 */
	protected void setupEmptyView() {
		setEmptyText(getString(R.string.noTripsSubscripted));
		LinearLayout emptyLayout = (LinearLayout) getEmptyView();
		Button tripButton = new Button(getActivity());
		tripButton.setText(R.string.requestTrip);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
		emptyLayout.addView(tripButton, params);
		tripButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),
						NewTripRequestActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent i = new Intent(getActivity(), TripOfferDetailsActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		TripOfferTypPrx tOffer = TripOfferTypPrxHelper
				.uncheckedCast((ObjectPrx) parent.getItemAtPosition(position));
		i.putExtra(TripOfferDetailsActivity.EXTRA_TRIP_ID, tOffer.getTripId());
		startActivity(i);
	}
}
