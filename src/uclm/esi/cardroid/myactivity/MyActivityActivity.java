package uclm.esi.cardroid.myactivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Ice.ObjectPrx;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.android.Trip;
import uclm.esi.cardroid.data.zerocice.ActivityType;
import uclm.esi.cardroid.data.zerocice.MessageTypPrx;
import uclm.esi.cardroid.data.zerocice.PassengerTyp;
import uclm.esi.cardroid.data.zerocice.UserActivityTypPrx;
import uclm.esi.cardroid.data.zerocice.UserActivityTypPrxHelper;
import uclm.esi.cardroid.mytrips.TripDetailsActivity;
import uclm.esi.cardroid.mytrips.TripOfferDetailsActivity;
import uclm.esi.cardroid.mytrips.TripRequestDetailsActivity;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.util.ListViewArrayActivity;

/**
 * \class MyActivityActivity
 * Activity that displays the recent user's activity
 * on a ListView The user's activity is loaded from a database cursor
 */
public class MyActivityActivity extends
		ListViewArrayActivity<UserActivityTypPrx> {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupListView();

		setupActionBar(R.string.activity, true);
	}

	final private CardroidEventStormListener _mCallbackListener = new CardroidEventStormListener() {

		@Override
		public void _notify(UserActivityTypPrx action) {
			_callbackListener._notify(action);
			mAdapter.insert(action, 0);
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void message(MessageTypPrx msg) {
			_callbackListener.message(msg);
		}

		@Override
		public void inactivity() {
			_callbackListener.inactivity();
		}

		@Override
		public void error() {
			_callbackListener.error();
		}
	};

	@Override
	protected void setupListView() {
		final int elementLayout = R.layout.image_double_list_item;
		final int imageView = R.id.imageView1;
		final int textView = R.id.textView1;
		final int subtextView = R.id.textView2;

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
					ArrayList<UserActivityTypPrx> adapterData = new ArrayList<UserActivityTypPrx>(
							data.data.size());
					for (ObjectPrx oprx : data.data)
						adapterData.add(UserActivityTypPrxHelper
								.checkedCast(oprx));
					if (mAdapter == null) {
						mAdapter = new UserActivityListAdapter(
								MyActivityActivity.this, elementLayout,
								imageView, textView, subtextView, adapterData);
						setListAdapter(mAdapter);
					} else {
						mAdapter.addAll(adapterData);
					}
					mAdapter.notifyDataSetChanged();
					mProgressBar.setVisibility(View.GONE);
					if (mAdapter.getCount() <= 0)
						setEmptyText(getString(R.string.noActivityFound));
				}
			}
		};
	}

	@Override
	protected void updateListAdapterData() {
		mProgressBar.setVisibility(View.VISIBLE);
		_sessionController.getUserActivity(mQueryListener,
				_sessionController.getMyUser());
	}

	@Override
	public CardroidEventStormListener getCardroidCallbackListener() {
		return _mCallbackListener;
	}

	@Override
	public boolean replayEvents() {
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent;
		UserActivityTypPrx uActivity = UserActivityTypPrxHelper
				.checkedCast((ObjectPrx) parent.getItemAtPosition(position));
		switch (uActivity.getActivityTrip().getTripType()) {
		case Trip.TRIP_OFFER_TYPE:
			intent = new Intent(MyActivityActivity.this,
					TripOfferDetailsActivity.class);
			break;
		case Trip.TRIP_REQUEST_TYPE:
			intent = new Intent(MyActivityActivity.this,
					TripRequestDetailsActivity.class);
			break;
		case Trip.TRIP_TYPE:
		default:
			intent = new Intent(MyActivityActivity.this,
					TripDetailsActivity.class);

		}
		intent.putExtra(TripDetailsActivity.EXTRA_TRIP_ID, uActivity
				.getActivityTrip().getTripId());
		startActivity(intent);
	}

	/**
	 * \class UserActivityListAdapter
	 * Customized adapter for optimizing the display of the user activity
	 * entries in an attractive way
	 */
	public class UserActivityListAdapter extends
			ArrayAdapter<UserActivityTypPrx> {
		private Context _context;
		private int _resource, _imageProjectionView, _textProjectionView,
				_subtextProjectionView;

		public UserActivityListAdapter(Context context, int resourceId,
				int imageProjectionViewResourceId,
				int textProjectionViewResourceId,
				int subtextProjectionViewResourceId,
				List<UserActivityTypPrx> items) {
			super(context, resourceId, items);
			_context = context;
			_resource = resourceId;
			_imageProjectionView = imageProjectionViewResourceId;
			_textProjectionView = textProjectionViewResourceId;
			_subtextProjectionView = subtextProjectionViewResourceId;
		}

		// Private view holder class
		private class ViewHolder {
			public ImageView imageView;
			public TextView txtTitle;
			public TextView txtSubtitle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			UserActivityTypPrx rowItem = getItem(position);

			LayoutInflater mInflater = (LayoutInflater) _context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = mInflater.inflate(_resource, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(_imageProjectionView);
				holder.txtTitle = (TextView) convertView
						.findViewById(_textProjectionView);
				holder.txtSubtitle = (TextView) convertView
						.findViewById(_subtextProjectionView);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			byte[] avatarBytes = null;
			if (rowItem.getUserActivityType() == ActivityType.TRIPJOIN 
					|| rowItem.getUserActivityType() == ActivityType.TRIPREQUESTANSWERED) {
				List<PassengerTyp> passengers = rowItem.getActivityTrip()
						.getTripPassengers();
				avatarBytes = passengers.get(passengers.size() - 1)
						.getPassengerUser().getAvatarBytes();
			} else {
				avatarBytes = rowItem.getActivityTrip().getTripDriver()
						.getAvatarBytes();
			}
			holder.txtTitle.setText(rowItem._toString());
			holder.txtSubtitle.setText(SimpleDateFormat.getDateTimeInstance(
					SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM).format(
					new Date(rowItem.getTimeStampInMillis())));
			holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(
					avatarBytes, 0, avatarBytes.length));

			return convertView;
		}
	}
}
