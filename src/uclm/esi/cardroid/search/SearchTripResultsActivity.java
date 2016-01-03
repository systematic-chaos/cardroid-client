package uclm.esi.cardroid.search;

import java.util.ArrayList;
import java.util.List;

import Ice.ObjectPrx;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.Trip;
import uclm.esi.cardroid.data.android.TripRequest;
import uclm.esi.cardroid.data.zerocice.TripTypPrx;
import uclm.esi.cardroid.data.zerocice.TripTypPrxHelper;
import uclm.esi.cardroid.mytrips.TripDetailsActivity;
import uclm.esi.cardroid.mytrips.TripOfferDetailsActivity;
import uclm.esi.cardroid.mytrips.TripRequestDetailsActivity;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.util.ListViewArrayActivity;

/**
 * \class SearchTripResults
 * Activity that displays the trip results matches on a
 * ListView The trips results data are loaded from a database cursor
 */
public class SearchTripResultsActivity extends
		ListViewArrayActivity<TripTypPrx> {

	public static final String EXTRA_TRIP_REQUEST = "SearchTripResultsActivity.TRIP_REQUEST";

	private TripRequest mTrip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTrip = getIntent().getParcelableExtra(EXTRA_TRIP_REQUEST);

		setupActionBar(R.string.search, false);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mTrip == null)
			mTrip = getIntent().getParcelableExtra(EXTRA_TRIP_REQUEST);
	}

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
						mAdapter = new TripListAdapter(
								SearchTripResultsActivity.this, adapterData);
						setListAdapter(mAdapter);
					} else {
						mAdapter.addAll(adapterData);
					}
					mAdapter.notifyDataSetChanged();
					mProgressBar.setVisibility(View.GONE);
					if (mAdapter.getCount() <= 0)
						setEmptyText(getString(R.string.noTripsFound));
				}
			}
		};
	}

	@Override
	protected void updateListAdapterData() {
		mProgressBar.setVisibility(View.VISIBLE);
		uclm.esi.cardroid.data.ice.TripRequest tr = new uclm.esi.cardroid.data.ice.TripRequest(
				_sessionController);
		_sessionController.searchTrips(mQueryListener, tr.newInstance(mTrip));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent i;
		TripTypPrx trip = TripTypPrxHelper.checkedCast((ObjectPrx) parent
				.getItemAtPosition(position));
		switch (trip.getTripType()) {
		case Trip.TRIP_OFFER_TYPE:
			i = new Intent(this, TripOfferDetailsActivity.class);
			break;
		case Trip.TRIP_REQUEST_TYPE:
			i = new Intent(this, TripRequestDetailsActivity.class);
			break;
		case Trip.TRIP_TYPE:
		default:
			i = new Intent(this, TripDetailsActivity.class);
		}
		i.putExtra(TripDetailsActivity.EXTRA_TRIP_ID, trip.getTripId());
		startActivity(i);
	}

	@Override
	public CardroidEventStormListener getCardroidCallbackListener() {
		return _callbackListener;
	}

	@Override
	public boolean replayEvents() {
		return false;
	}

	/**
	 * \class TripListAdapter
	 * Customized adapter for optimizing the display of the trip
	 * entries in an attractive way
	 */
	public static class TripListAdapter extends ArrayAdapter<TripTypPrx> {
		private Context _context;

		public TripListAdapter(Context context, List<TripTypPrx> items) {
			super(context, R.layout.selectable_trip_list_item, items);
			this._context = context;
		}

		// Private view holder class
		private class ViewHolder {
			public TextView fromView;
			public TextView toView;
			public TextView dateView;
			public LinearLayout returnDateLayout;
			public TextView returnDateView;
			public ImageView tripTypeView;
			public TextView nSeatsView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			TripTypPrx rowItem = getItem(position);

			LayoutInflater mInflater = (LayoutInflater) _context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.selectable_trip_list_item, parent, false);
				holder = new ViewHolder();
				holder.fromView = (TextView) convertView
						.findViewById(R.id.fromTextView);
				holder.toView = (TextView) convertView
						.findViewById(R.id.toTextView);
				holder.dateView = (TextView) convertView
						.findViewById(R.id.dateTextView);
				holder.returnDateLayout = (LinearLayout) convertView
						.findViewById(R.id.returnLinearLayout);
				holder.returnDateView = (TextView) convertView
						.findViewById(R.id.returnTextView);
				holder.tripTypeView = (ImageView) convertView
						.findViewById(R.id.tripTypeImageView);
				holder.nSeatsView = (TextView) convertView
						.findViewById(R.id.nSeatsTextView);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.fromView.setText(rowItem.getPlace1().getName());
			holder.toView.setText(rowItem.getPlace2().getName());
			holder.dateView.setText(rowItem.getTripDate()._toString());
			if (rowItem.hasTripReturnDate())
				holder.returnDateView.setText(rowItem.getTripReturnDate()
						._toString());
			else
				holder.returnDateLayout.setVisibility(View.GONE);
			switch (rowItem.getTripType()) {
			case Trip.TRIP_OFFER_TYPE:
				holder.tripTypeView.setImageDrawable(_context.getResources()
						.getDrawable(R.drawable.exclamation_icon_ldpi));
				break;
			case Trip.TRIP_REQUEST_TYPE:
				holder.tripTypeView.setImageDrawable(_context.getResources()
						.getDrawable(R.drawable.interrogation_icon_ldpi));
				break;
			case Trip.TRIP_TYPE:
			default:
			}
			holder.nSeatsView.setText("x" + rowItem.getNSeats());

			return convertView;
		}
	}
}
