package uclm.esi.cardroid.mymessages;

import java.util.ArrayList;
import java.util.List;

import uclm.esi.cardroid.R;
import uclm.esi.cardroid.data.zerocice.MessageTypPrx;
import uclm.esi.cardroid.data.zerocice.UserActivityTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrx;
import uclm.esi.cardroid.data.zerocice.UserTypPrxHelper;
import uclm.esi.cardroid.network.client.CardroidEventStormListener;
import uclm.esi.cardroid.network.client.QueryModel;
import uclm.esi.cardroid.network.client.QueryController.QueryListener;
import uclm.esi.cardroid.util.ListViewArrayActivity;
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

/**
 * \class MyMessagesActivity
 * Activity that displays one entry for each user the
 * active user has had a messages conversation with. The user's messages
 * conversations entries are loaded from a database cursor.
 */
public class MyMessagesActivity extends ListViewArrayActivity<UserTypPrx> {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupActionBar(R.string.messages, true);
	}

	final private CardroidEventStormListener _mCallbackListener = new CardroidEventStormListener() {

		@Override
		public void _notify(UserActivityTypPrx action) {
			_callbackListener._notify(action);
		}

		@Override
		public void message(MessageTypPrx msg) {
			_callbackListener.message(msg);
			mAdapter.clear();
			updateListAdapterData();
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

	/**
	 * Create an asynchronous query to the database, and set a callback listener
	 * which will initialize (if necessary) and update the adapter's data upon
	 * the availability of the query results
	 */
	@Override
	protected void setupListView() {
		final int elementLayout = R.layout.image_selectable_list_item;
		final int imageView = R.id.imageView1;
		final int textView = R.id.textView1;

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
					ArrayList<UserTypPrx> adapterData = new ArrayList<UserTypPrx>(
							data.data.size());
					for (ObjectPrx oprx : data.data)
						adapterData.add(UserTypPrxHelper.checkedCast(oprx));
					if (mAdapter == null) {
						mAdapter = new UserListAdapter(MyMessagesActivity.this,
								elementLayout, imageView, textView, adapterData);
						setListAdapter(mAdapter);
					} else {
						mAdapter.addAll(adapterData);
					}
					mAdapter.notifyDataSetChanged();
					mProgressBar.setVisibility(View.GONE);
					if (mAdapter.getCount() <= 0)
						setEmptyText(getString(R.string.noMessagesFound));
				}
			}
		};
	}

	@Override
	protected void updateListAdapterData() {
		mProgressBar.setVisibility(View.VISIBLE);
		_sessionController.getMessageTalksSpeakers(mQueryListener,
				_sessionController.getMyUser());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent(MyMessagesActivity.this,
				MessageTalkActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		UserTypPrx user = UserTypPrxHelper.checkedCast((ObjectPrx) parent
				.getItemAtPosition(position));
		intent.putExtra(MessageTalkActivity.EXTRA_USER_SPEAKER, user.getEmail());
		startActivity(intent);
	}

	@Override
	public CardroidEventStormListener getCardroidCallbackListener() {
		return _mCallbackListener;
	}

	@Override
	public boolean replayEvents() {
		return false;
	}

	/**
	 * \class UserListAdapter
	 * Customized adapter for optimizing the display of the user inbox 
	 * speaker entries in an attractive and cool way
	 */
	public class UserListAdapter extends ArrayAdapter<UserTypPrx> {
		private Context _context;
		private int _resource, _imageProjectionView, _textProjectionView;

		public UserListAdapter(Context context, int resourceId,
				int imageProjectionViewResourceId,
				int textProjectionViewResourceId, List<UserTypPrx> items) {
			super(context, resourceId, items);
			_context = context;
			_resource = resourceId;
			_imageProjectionView = imageProjectionViewResourceId;
			_textProjectionView = textProjectionViewResourceId;
		}

		// Private view holder class
		private class ViewHolder {
			public ImageView imageView;
			public TextView txtTitle;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			UserTypPrx rowItem = getItem(position);

			LayoutInflater mInflater = (LayoutInflater) _context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = mInflater.inflate(_resource, parent, false);
				holder = new ViewHolder();
				holder.txtTitle = (TextView) convertView
						.findViewById(_textProjectionView);
				holder.imageView = (ImageView) convertView
						.findViewById(_imageProjectionView);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.txtTitle.setText(rowItem._toString());
			byte[] avatarBytes = rowItem.getAvatarBytes();
			holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(
					avatarBytes, 0, avatarBytes.length));

			return convertView;
		}
	}
}
